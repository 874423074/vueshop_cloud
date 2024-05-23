<script setup>

	import {ref, reactive, toRefs, defineProps, defineEmits} from 'vue'
	import bwPowerSet from '@/utils/power-set.js'
	import { showSuccessToast, showFailToast } from 'vant'
	import {AddCart} from '^/cart.js'

	import {useRouter} from 'vue-router'
	const router = useRouter()

	const props = defineProps({
		product: Object,
		handle: String
	})
	const {product, handle} = toRefs(props)

	const data = reactive({
		price: 0,
		quantity: 1
	})

	// 规格重组，status控制规格状态
	let specs = ref([...product.value.specifications])
	const skuStocks = product.value.skuStocks
	console.log(specs.value)

	specs.value = specs.value.map(item => {
		return {
			spec: item.spec,
			values: item.value.split(';').map(it => {
				return {
					value: it,
					status: 0
				}
			})
		}
	})
	console.log(specs.value)

	const clickSpec = (it, values) => {

		if (it.status == 2) {
			// 禁用状态则跳过
			return

		} else if (it.status == 1) {
			// 如果是选中则改为取消选中

			it.status = 0
		} else {
			// 未选中改为选中，兄弟节点改为未选中
			values.forEach(e => e.status = 0)
			it.status = 1
		}

		// 更新其他规格节点的状态
		updateStatus()

		// 展示规格组合的最新价格
		showPrice()

	}

	const updateStatus = () => {
		// 获取用户选中了那些规格组合
		const selectedArr = getSelectedArr()

		console.log("selectedArr")
		console.log(selectedArr)

		specs.value.forEach((item, index) => {

			// 复制一份已选择规格
			const _selectedArr = [...selectedArr]

			item.values.forEach(it => {
				if (it.status === 1) {
					return
				} else {
					_selectedArr[index] = it.value

					// 比如：['55英寸', 'K9', '']
					console.log("_selectedArr")
					console.log(_selectedArr)
				}

				// 去掉空的，然后用;号连起来
				const key = _selectedArr.filter(v => v).join(";")
				const pathDict = createPathDict()
				// 去字典中判断是否存在
				if (pathDict.indexOf(key) < 0) {
					it.status = 2
				} else {
					it.status = 0
				}

			})

		})
	}

	const getSelectedArr = () => {
		return specs.value.map(item => {
			const spec = item.values.find(it => it.status === 1)
			return spec ? spec.value : ''
		})
	}


	// 生成组合字典
	const createPathDict = () => {

		const pathDict = []

		skuStocks.forEach(skuStock => {

			if (skuStock.stock == 0) {
				return
			}

			// 规格组合的幂集
			const powerSet = bwPowerSet(skuStock.sku.split(';'))
			console.log(skuStock.sku)
			console.log(powerSet)

			powerSet.forEach(item => {
				if (item.length === 0) return

				const path = item.join(";")

				pathDict.push(path)
			})
		})

		console.log("pathDict")
		console.log(pathDict)

		return pathDict
	}
	createPathDict()

	const showPrice = () => {
		// 获取用户选中了那些规格组合
		const selectedArr = getSelectedArr()
		const skuStock = skuStocks.find(item => item.sku == selectedArr.join(';'))
		if (skuStock) {
			data.price = skuStock.price
		} else {
			data.price = product.value.price
		}
	}

	showPrice()
	updateStatus()

	const emits = defineEmits(['closeSkuShow'])
	const handleSubmit = () =>{

		const selectedArr = getSelectedArr().filter(v => v)
		if (specs.value.length !== selectedArr.length) {
			showFailToast('请选择规格')
			return
		}
		const skuStock = skuStocks.find(item => item.sku == selectedArr.join(';'))
		const skuId = skuStock.id

		console.log("skuId")
		console.log(skuId)

		if (handle.value === 'cart') {
			AddCart({
				productId: product.value.id,
				skuId: skuId,
				quantity: data.quantity
			}).then(res => {
				showSuccessToast({
					message:'已加入购物车',
					duration: 1000,
					onClose: function () {
						emits('closeSkuShow')
					}
				})
			})
		} else {
			router.push({
				name: 'OrderPreview',
				query: {
					productId: product.value.id,
					skuId: skuId,
					quantity: data.quantity
				}
			})
		}
	}

</script>

<template>
	<div class="spec">

		<div>
			<van-image width="100" height="100" :src="product.image"/>
			<span class="price">￥ {{data.price}}</span>
			<span>请选择规格</span>
		</div>

		<div v-for="item in specs"><!---->
			<div class='title'>{{item.spec}}</div>
			<div class='spec-items'>

				<div class='spec-item' v-for="it in item.values"><!---->
					<span v-if="it.status == 0" @click="clickSpec(it, item.values)" class="">{{it.value}}</span>
					<span v-if="it.status == 1" @click="clickSpec(it, item.values)" class="active">{{it.value}}</span>
					<span v-if="it.status == 2" class="disabled">{{it.value}}</span>
				</div>
			</div>
		</div>

		<div>
			<div class='title'>数量</div>
			<van-stepper v-model="data.quantity"/>
		</div>

		<div style="margin: 16px;">
			<van-button round block type="primary" native-type="submit" @click="handleSubmit">确定</van-button>
		</div>

	</div>

</template>

<style scoped>
	.spec {
		font-size: 14px;
		padding: 16px;
	}

	.spec .price {
		font-size: 21px;
		font-weight: bold;
		color: rgb(234, 19, 19);
		margin: 0 10px;
	}

	.title {
		line-height: 40px;
	}

	.spec-item {
		display: inline-block;
		margin-right: 10px;
	}

	.spec-item span {
		border: 1px solid #eee;
		cursor: pointer;
		padding: 5px 10px;
	}

	.spec-item .active {
		border: 1px solid red;
		background-color: red;
		color: #fff;
	}

	.spec-item .disabled {
		color: #c0c4cc;
		cursor: not-allowed;
		background-image: none;
		background-color: #fff;
		border-color: #ebeef5;
	}
</style>
