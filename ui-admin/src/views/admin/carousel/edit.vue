<script setup>
	import Upload from '~/Upload.vue'
	import {reactive, toRefs, ref, defineProps, defineEmits} from 'vue'
	import {GetInfo, Add} from '^/carousel'
	import {ElMessage} from "element-plus"

	const props = defineProps({
		showDialog: Boolean
	})
	const {showDialog} = toRefs(props)

	const emits = defineEmits(['closeHandle'])
	const closeDialog = (hasChange) => {
		emits('closeHandle', hasChange)
	}

	const rules = reactive({
		name: [
			{required: true, message: '请输入广告名称', trigger: 'blur'}
		],
		position: [
			{required: true, message: '请选择广告位置', trigger: 'change'}
		]
	})
	const positions = ref([
		{label: '轮播图', value: 'carousel'},
		{label: '右侧', value: 'side'},
		{label: '底部', value: 'foot'},
	])

	const editForm = ref({})
	const pics = ref([])

	const init = (id) => {
		pics.value = []

		if (id) {
			GetInfo(id).then(res => {
				editForm.value = res.data
				pics.value.push({url: res.data.image})

			})
		} else {
			editForm.value = {
				sortOrder: 1,
				isShow: true
			}
		}

	}
	defineExpose({init})

	const ruleForm = ref(null)
	const submitForm = () => {
		ruleForm.value.validate((vaild) => {
			if (pics.value.length === 0) {
				ElMessage.error('请上传图片')
				return
			}
			editForm.value.image = pics.value[0].url
			if (vaild) {
				Add(editForm.value).then(res => {
					ElMessage({type: 'success', message: '操作成功！'})
					closeDialog(true)
				})
			}
		})
	}
</script>

<template>
	<!--新增对话框-->
	<el-drawer
			title="编辑"
			v-model="showDialog"
			width="600px"
			:before-close="closeDialog"
	>

		<el-form :model="editForm" :rules="rules" ref="ruleForm" label-width="100px"
		         class="demo-editForm">
			<el-input v-model="editForm.id" autocomplete="off" type="hidden"></el-input>

			<el-form-item label="名称" prop="name" label-width="100px">
				<el-input v-model="editForm.name" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item label="图片" prop="code" label-width="100px">
				<Upload :pics="pics" :limit="1"></Upload>

			</el-form-item>
			<el-form-item label="链接" prop="url" label-width="100px">
				<el-input v-model="editForm.url" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item label="位置" prop="position" label-width="100px">
				<el-select v-model="editForm.position"  :teleported='false'>
					<el-option v-for="item in positions" :label="item.label" :value="item.value" />
				</el-select>
			</el-form-item>
			<el-form-item label="是否展示" prop="isShow" label-width="100px">
				<el-switch v-model="editForm.isShow" />
			</el-form-item>
			<el-form-item label="排序" prop="sortOrder" label-width="100px">
				<el-input-number v-model="editForm.sortOrder" :min="1" :max="99" />
			</el-form-item>

			<el-form-item>
				<el-button type="primary" @click="submitForm">立即创建</el-button>
				<el-button @click="closeDialog">关闭</el-button>
			</el-form-item>
		</el-form>

	</el-drawer>
</template>

<style lang="scss" scoped>
	.avatar-uploader .avatar {
		width: 50px;
		height: 50px;
		display: block;
	}

	.el-icon.avatar-uploader-icon {
		border: 1px dashed var(--el-border-color);
		border-radius: 6px;

		width: 50px;
		height: 50px;
		text-align: center;
		background-color: var(--el-fill-color-lighter);
	}
</style>

