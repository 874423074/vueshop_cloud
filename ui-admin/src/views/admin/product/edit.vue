<script setup>

	import Upload from '~/Upload.vue'

	import {reactive, ref} from 'vue'
	import {GetInfo, Add} from '^/product'
	import {GetList as GetCategories} from '^/category'
	import {useRoute, useRouter} from 'vue-router'

	import {ElMessage} from "element-plus"

	const route = useRoute()
	const router = useRouter()

	const editForm = ref({})
	const categories = ref([])

	const data = reactive({
		pics: []
	})

	const productId = route.query.id
	const initData = () => {
		if (productId) {
			GetInfo(productId).then(res => {
				editForm.value = res.data
				if (!editForm.value.detail) {
					editForm.value.detail = ''
				}
				if (res.data.pics) {
					res.data.pics.split(';').forEach(pic => {
						data.pics.push({
							url: pic
						})
					})
				}
			})
		} else {
			editForm.value = {
				detail: '',
				isOnSale: true,
				isHot: false,
				isNew: false,
				isTop: false,
				cost: 0,
				stock: 999,
				sortOrder: 1,
			}
		}
		GetCategories().then(res => {
			categories.value = res.data.records
		})
	}

	initData()

	const ruleForm = ref(null)
	const submitForm = () => {
		if (data.pics.length === 0) {
			ElMessage.error('至少上传一张图片')
			return
		}
		editForm.value.pics = data.pics.map(item => item.url).join(';')
		editForm.value.image = data.pics[0].url

		ruleForm.value.validate((vaild) => {
			if (vaild) {
				Add(editForm.value).then(res => {
					ElMessage({type: 'success', message: '操作成功！'})
					router.back()
				})
			}
		})
	}

	const rules = reactive({
		name: [
			{required: true, message: '请输入分类名称', trigger: 'blur'}
		]
	})

</script>
<template>
	<el-card shadow="never" style="width: 800px; margin: 0 auto;">

		<el-form :model="editForm" :rules="rules" ref="ruleForm" label-width="100px"
		         class="demo-editForm">
			<el-input v-model="editForm.id" autocomplete="off" type="hidden"></el-input>

			<el-form-item label="商品分类" label-width="100px">
				<el-select v-model="editForm.categoryId">
					<el-option v-for="item in categories" :label="item.name" :value="item.id"/>
				</el-select>
			</el-form-item>

			<el-form-item label="商品名称" prop="name" label-width="100px">
				<el-input v-model="editForm.name" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item label="商品图片" prop="code" label-width="100px">
				<Upload :pics="data.pics"></Upload>
			</el-form-item>

			<el-form-item label="关键字" prop="keywords" label-width="100px">
				<el-input v-model="editForm.keywords" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="价格" prop="price" label-width="100px">
				<el-input v-model="editForm.price" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="成本价格" prop="cost" label-width="100px">
				<el-input v-model="editForm.cost" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="库存" prop="stock" label-width="100px">
				<el-input v-model="editForm.stock" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="展示" prop="stock" label-width="100px">
				在售：
				<el-switch v-model="editForm.isOnSale"></el-switch>
				<el-divider direction="vertical"></el-divider>
				新产品：
				<el-switch v-model="editForm.isNew"></el-switch>
				<el-divider direction="vertical"></el-divider>
				热门：
				<el-switch v-model="editForm.isHot"></el-switch>
				<el-divider direction="vertical"></el-divider>
				置顶：
				<el-switch v-model="editForm.isTop"></el-switch>
			</el-form-item>
			<el-form-item label="排序" prop="sortOrder" label-width="100px">
				<el-input-number v-model="editForm.sortOrder" :min="1" :max="10"/>
			</el-form-item>
			<el-form-item label="商品详情" prop="detail" label-width="100px">
				<v-md-editor v-model="editForm.detail" height="400px"></v-md-editor>
			</el-form-item>

			<el-form-item>
				<el-button @click="$router.go(-1)">关闭返回</el-button>
				<el-button type="primary" @click="submitForm">立即创建</el-button>
			</el-form-item>
		</el-form>
	</el-card>
</template>

<style lang="scss" scoped>
	.v-md-editor {
		border: 1px solid var(--el-border-color);
		box-shadow: none;
	}
</style>

