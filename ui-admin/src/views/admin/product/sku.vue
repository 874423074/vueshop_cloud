<template>
	<el-card shadow="never">
		<span>商品：{{data.product.sn}} - {{ data.product.name }} </span>
		<el-divider direction="vertical"></el-divider>
		<el-button type="text" @click="$router.push('/admin/product/edit?id=' + data.product.id)">编辑</el-button>
	</el-card>
	<el-card shadow="never" style="width: 800px; margin: 20px auto;">

		<p style="margin:24px 0 10px 0">规格属性（注意：选中或取消都会重置【价格/库存】信息）</p>

		<el-checkbox-group v-model="data.selectSpecValues" @change="changeSpec">
			<el-checkbox v-for="item in data.specs" :label="item.name" />
		</el-checkbox-group>

		<div class="goods-spec">
			<span>商品规格</span>
			<el-link type="primary" @click="addPrivateSpec" class="goods-spec-add">添加其他规格</el-link>
		</div>

		<div class="goods-container" v-for="(attr, index) in privateGoodsItem" :key="index">
			<div class="goods-content">
				<div class="goods-content-box">
					<div class="goods-content-left">
						<el-form label-width="80px" style="width:400px">
							<el-form-item label="规格名">
								<el-input v-model="attr.privateSpecName" placeholder="请输入规格名"></el-input>
							</el-form-item>
							<el-form-item label="规格值" >
								<el-tag size="large" v-for="tag in attr.dynamicTags" :key="tag" closable
								        :disable-transitions="false"
								        @close="handleClose(tag, attr)">
									{{ tag }}
								</el-tag>
								<el-input
										class="input-new-tag"
										v-if="attr.inputVisible"
										v-model="attr.inputValue"
										:ref="`saveTagInput${index}`"
										@keyup.enter.native="handleInputConfirm(attr.inputValue, attr)"
										@blur="handleInputConfirm(attr.inputValue, attr)"
								>
								</el-input>
								<el-button v-else class="button-new-tag" size="small" @click="showInput(attr, index)">+
									添加
								</el-button>
							</el-form-item>
						</el-form>
					</div>
					<div class="goods-content-right">
						<el-link type="danger" @click="delPrivateSpec(index)">删除规格</el-link>
					</div>
				</div>
			</div>
		</div>

		<template v-if="tableColumnList.tableBodyList.length > 0">
		<p style="margin:24px 0 10px 0">价格 / 库存</p>
		<el-table ref="multipleTable" :data="tableColumnList.tableBodyList" stripe tooltip-effect="dark" style="width: 100%;margin-top:1%;">
			<el-table-column :property="item.prop" v-for="item in tableColumnList.tableHeaderList" :key="item.prop" align="center" fixed="left">
				<template #header>
					<span>{{item.propName}}</span>
				</template>
				<template #default="scope">
					<span>{{ scope.row[scope.column.property] }}</span>
				</template>
			</el-table-column>
			<el-table-column align="center" width="160">
				<template #header>
					<span>价格</span>
				</template>
				<template #default="scope">
					<el-input size="large" type="text" placeholder="保留两位小数点" v-model="scope.row.price"></el-input>
				</template>
			</el-table-column>
			<el-table-column align="center" width="150">
				<template #header>
					<span>库存</span>
				</template>
				<template #default="scope">
					<el-input size="large" placeholder="请输入整数" v-model="scope.row.stock"></el-input>
				</template>
			</el-table-column>

		</el-table>
		</template>
		<div style="margin: 20px auto; text-align: center;">
			<el-button @click="$router.go(-1)">关闭返回</el-button>
			<el-button type="primary" @click="submitSku">立即保存</el-button>
		</div>
	</el-card>
</template>


<script setup>
	import {ref, computed, watch, reactive, nextTick} from 'vue'
	import {useRoute, useRouter} from 'vue-router'
	import {GetSku, AddSku} from '@/api/product.js'
	import {ElMessage, ElMessageBox} from "element-plus";

	const route = useRoute()
	const router = useRouter()

	const data = reactive({
		selectSpecValues: [],
		specs: [],
		skus: [],
		product: {},
	})

	const tableColumnList = ref({
		tableHeaderList: [],
		tableBodyList: []
	})
	const oldSpecValues = ref([])
	const privateGoodsItem = ref([])

	const initData = async () => {
		const id = route.query.id
		await GetSku(id).then(res => {

			data.product = res.data.product

			data.specs.push(...res.data.specs)
			data.selectSpecValues.push(...res.data.selectSpecValues)

			res.data.specValues.forEach(item => {
				privateGoodsItem.value.push({
					privateSpecName: item.spec,
					dynamicTags: [...item.value.split(';')],
					inputVisible: false,
					inputValue: ''
				})
			})

			// 初始化 旧商品规格
			oldSpecValues.value.push(...privateGoodsItem.value)

			data.skus.push(...res.data.skuStocks)
		})
	}
	initData()

	const changeSpec = (value) => {
		data.specs.forEach(item => {
			let exist = privateGoodsItem.value.some(e => e.privateSpecName == item.name)
			if (value.indexOf(item.name) > -1){
				if (!exist) {
					privateGoodsItem.value.push({
						privateSpecName: item.name,
						dynamicTags: [...item.options.split(';')],
						inputVisible: false,
						inputValue: ''
					})
				}
			} else {
				if (exist) {
					const newArrays = privateGoodsItem.value.filter(e => e.privateSpecName != item.name);
					privateGoodsItem.value = []
					privateGoodsItem.value.push(...newArrays)
				}

			}
		})
	}

	// 添加规格
	const addPrivateSpec = (index) => {
		privateGoodsItem.value.push({
			privateSpecName: '',
			dynamicTags: [],
			inputVisible: false,
			inputValue: ''
		})
	}
	const delPrivateSpec = (index) => {
		data.selectSpecValues.splice(data.selectSpecValues.indexOf(privateGoodsItem.value[index].privateSpecName), 1)
		privateGoodsItem.value.splice(index, 1)
	}
	const handleInputConfirm = (val, attr) => {
		if (val) {
			attr.dynamicTags.push(val)
		}
		attr.inputVisible = false
		attr.inputValue = ''
	}
	const handleClose = (tag, item) => {
		item.dynamicTags.splice(item.dynamicTags.indexOf(tag), 1)
	}
	const showInput = (attr, index) => {
		attr.inputVisible = true
	}
	// 笛卡尔积算法
	const cartesianProductOf = (...args) => {
		return args.reduce(
			(total, current) => {
				let ret = []
				total.forEach((a) => {
					current.forEach((b) => {
						ret.push(a.concat([b]))
					})
				})
				return ret
			},
			[[]]
		)
	}

	const calculateAttribute = computed(() => {

		// 初始化
		let obj = {}
		privateGoodsItem.value.forEach((item) => {
			// 判断有没有输入规格名
			if (item.privateSpecName) {
				//规格名:规格值     //'颜色':'尺寸'
				obj[item.privateSpecName] = item.dynamicTags
			}
		})
		return obj
	})

	watch(calculateAttribute, (newVal, oldVal) => {

		let cloneNewVal = JSON.parse(JSON.stringify(newVal))
		let attrName = [] //规格名数组
		let attrValue = [] //规格值数组
		for (let key in cloneNewVal) {
			attrName.push(key)
			attrValue.push(cloneNewVal[key])
		}

		// 表格内容数据（笛卡尔积算法）
		let finalArr = cartesianProductOf(...attrValue)

		let tableObj = {
			tableBodyList: [],
			tableHeaderList: []
		}

		// 表格内容
		tableObj.tableBodyList = finalArr.map((item) => {
			let obj = {
				id: '',
				productId: data.product.id,
				price: data.product.price,
				stock: 0,
				sku: '',
			}
			for (let i = 0; i < item.length; i++) {
				obj[attrName[i]] = item[i]
			}
			obj['sku'] = item.join(";")

			data.skus.forEach(item => {
				if (item.sku == obj['sku']) {
					obj['price'] = item.price
					obj['stock'] = item.stock
					obj['id'] = item.id
				}
			})

			return obj
		})
		tableColumnList.value.tableBodyList = tableObj.tableBodyList //表格内容数据

		// 表头
		let skuTableArr = Object.keys(newVal)
		tableObj.tableHeaderList = skuTableArr.map((item) => {
			return {
				prop: item,
				propName: item
			}
		})
		tableColumnList.value.tableHeaderList = tableObj.tableHeaderList // 表头
	}, {
		deep:true
	})

	const submitSku = () => {

		let specValues = privateGoodsItem.value.map(e => {
			return {
				productId: data.product.id,
				spec: e.privateSpecName,
				value: e.dynamicTags.join(";")
			}
		})

		ElMessageBox.confirm('确认提交SKU吗？', '警告', {
				confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning',
			}
		).then(() => {
			AddSku({
				skuStocks: tableColumnList.value.tableBodyList,
				specValues: specValues,
				productId: data.product.id
			}).then(res => {
				ElMessage({type: 'success', message: '操作成功！'})
			})
		})
	}

</script>
<style lang="scss" scoped>
	.goods-spec {
		display: flex;
		justify-content: space-between;
		margin:24px 0 10px 0
	}

	.goods-spec .goods-spec-add {
		margin-right: 15px;
	}

	.goods-container .button-new-tag {
		height: 32px;
		line-height: 30px;
		padding-top: 0;
		padding-bottom: 0;
	}

	.goods-container .input-new-tag {
		width: 90px;
		margin-right: 10px;
		vertical-align: bottom;
	}

	.goods-container .el-tag {
		margin-right: 10px;
	}

	.goods-container .goods-content {
		margin-bottom: 10px;
		padding: 14px;
		border: 1px solid #ebeef5;
		border-radius: 4px;
		background-color: #fcfcfc;
	}

	.goods-content .goods-content-box {
		display: flex;
		align-items: center;
	}

	.goods-content-box .goods-content-left {
		flex: 1;
	}
</style>

