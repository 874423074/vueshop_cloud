<script setup>

	import {ref, reactive} from 'vue'
	import {useRouter} from "vue-router"
	import {GetCartList, GetTotal, DeleteCart, UpdateQuantity} from '^/cart.js'

	const router = useRouter()

	const data = reactive({
		products: [],
		checked: [],
		checkAll: true,
		total: 0,
		isEdit: false
	})

	const initData = () => {
		GetCartList().then(res => {
			data.products = res.data

			data.checked = data.products.map(e => e.id)
			data.checkAll = true

			getTotal()
		})
	}
	initData()

	const getTotal = () => {
		GetTotal(data.checked).then(res => {
			data.total = res.data.total
		})
	}
	const changeChecked = () => {
		const isAll = data.checked.length === data.products.length && data.checked.length > 0
		data.checkAll = isAll

		getTotal()
	}

	const checkboxGroup = ref(null)
	const checkAll = () => {
		let hasChecked = data.checked.length > 0 && data.checked.length < data.products.length
		if (hasChecked) {
			checkboxGroup.value.toggleAll(true);
		} else {
			checkboxGroup.value.toggleAll()
		}

		getTotal()
	}

	let checkBak = []
	const mangeCart = () => {
		data.isEdit = !data.isEdit

		if (data.isEdit) {
			checkBak = [...data.checked];
			data.checked = [];

			data.total = 0;
		} else {
			data.checked = [...checkBak]

			getTotal()
		}
	}

	import {showConfirmDialog} from 'vant'

	const handleSubmit = () => {
		if (data.isEdit) {
			// 删除
			showConfirmDialog({
				message: '确认删除所选商品吗？',
			}).then(() => {
				DeleteCart(data.checked).then(res => {
					initData()
				})
			});
		} else {
			router.push({
				name: 'OrderPreview',
				query: {
					cartIds: data.checked
				}
			})
		}
	}

	const changeQuantity = (value, detail) => {
		if (value == '' || value == null) {
			return
		}
		UpdateQuantity(detail.name, value).then(res => {
			getTotal()
		}).catch(e => {
			initData()
		})
	}

</script>

<template>
	<van-nav-bar title="购物车" :right-text="data.isEdit? '完成' : '管理'" @click-right="mangeCart"/>

	<div class="tab-cart">
		<van-checkbox-group class="card-goods" ref="checkboxGroup" v-model="data.checked" @change="changeChecked">
			<div class="card-goods__item" v-for="item in data.products"><!---->

				<van-checkbox :key="item.id" :name="item.id"></van-checkbox>

				<van-card
						:title="item.productName"
						:price="item.price"
						:thumb="item.productImage"
						@click-thumb="$router.push('/product/' + item.productId)"
				>
					<template #tags>
						<van-tag color="#c8c9cc" text-color="white" style="margin: 6px 6px 0 0;" v-for="it in item.sku.split(';')">
							{{it}}
						</van-tag>
					</template>
					<template #num>
						<van-stepper integer v-model="item.quantity" :name="item.id" @change="changeQuantity"/>
					</template>
				</van-card>
			</div>
		</van-checkbox-group>

		<van-empty v-if="data.products.length === 0" description="购物车空空如也，快去逛逛"/>
		<van-submit-bar
				style="margin-bottom: 51px;"
				v-if="data.products.length > 0"
				:price="data.total * 100"
				:buttonText="data.isEdit? '删除所选' : '提交订单'"
				:disabled="data.checked.length === 0"
				@submit="handleSubmit"
		>
			<van-checkbox style="padding: 0 10px;" v-model="data.checkAll" @click="checkAll">全选</van-checkbox>
		</van-submit-bar>
	</div>
</template>

<style lang="scss" scoped>

	.card-goods {
		margin: 8px;

		.card-goods__item {
			display: flex;
			align-items: center;
			margin-bottom: 10px;
			background-color: #fff;
		}
	}

</style>