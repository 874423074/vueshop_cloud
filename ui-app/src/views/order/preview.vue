<script setup>
	import {ref, reactive} from 'vue'
	import {useRouter, useRoute} from "vue-router"
	import {OrderPreview, CreateOrder, PayOrder} from '^/order.js'

	const router = useRouter()
	const route = useRoute()

	const data = reactive({
		address: {},
		cartItems: [],
		freight: 0,
		total: 0,

		loadingBtn: false,
		note: '',
		payWay: 'ali'
	})

	const getParam = () => {
		const cartIds = route.query.cartIds
		const ids = []

		if (cartIds) {
			if (cartIds instanceof Array) {
				ids.push(...cartIds)
			} else {
				ids.push(cartIds)
			}
		}

		let addressId = route.query.addressId
		if (!addressId) {
			// 默认收货地址
			addressId = data.address.id
		}

		return {
			// 购物车订单预览
			cartIds: ids,

			// 立即购买预览
			productId: route.query.productId,
			skuId: route.query.skuId,
			quantity: route.query.quantity,

			addressId: addressId
		};
	}

	const initData = () => {
		OrderPreview(getParam()).then(res => {

			data.address = res.data.address
			data.cartItems = res.data.cartItems
			data.freight = res.data.freight
			data.total = res.data.total
		})
	}
	initData()

	const toAddressList = () => {
		// const {addressId, ...temp} = route.query
		router.push({
			name: 'ListAddress',
			query: route.query
		})
	}

	const payForm = ref(null)
	const handleSubmit = () => {
		data.loadingBtn = true

		let param = getParam()

		CreateOrder({
			note: data.note,
			payWay: data.payWay,
			...param
		}).then(res => {
			// 得到订单编号，马上发起支付
			PayOrder(res.data).then(res2 => {
				payForm.value.innerHTML = res2.data
				payForm.value.children[0].submit()
			})
		})
	}

</script>

<template>

	<van-nav-bar title="订单确认" left-text="返回" left-arrow @click-left="$router.go(-1)"/>

	<div class="order">
		<van-cell-group>
			<van-cell isLink icon="location-o" @click="toAddressList">

				<!--没有收货地址-->
				<template #title v-if="!data.address">
					<span>收货地址：</span>
					<span style="color: #999999">请填写收货地址！</span>
				</template>

				<!--有收货地址-->
				<template #title v-if="data.address">
					<span>收货地址：</span>
					<span>{{data.address.name}}</span>&nbsp
					<span>{{data.address.tel}}</span>
				</template>
				<template #label v-if="data.address">
					<span style="color: #999999">{{data.address.address}}</span>
				</template>

			</van-cell>
		</van-cell-group>

		<van-card
				v-for="item in data.cartItems"
				:title="item.productName"
				:price="item.price"
				:num="item.quantity"
				:thumb="item.productImage"
		>
			<template #tags>
				<van-tag color="#c8c9cc" text-color="white" style="margin: 6px 6px 0 0;" v-for="it in item.sku.split(';')">
					{{it}}
				</van-tag>
			</template>
		</van-card>

		<van-cell-group style="margin-top: 8px;">
			<van-cell title="运费">
				<span class="red">￥{{data.freight}}</span>
			</van-cell>
			<van-field placeholder="请输入备注" label="订单备注" v-model="data.note"></van-field>
		</van-cell-group>

		<van-cell-group style="margin-top: 8px;">
			<van-radio-group v-model="data.payWay">
				<van-cell>
					<van-radio name="ali">
						<van-icon name="alipay"/>
						支付宝
					</van-radio>
				</van-cell>
				<van-cell>
					<van-radio name="wx" disabled>
						<van-icon name="wechat"/>
						微信支付
					</van-radio>
				</van-cell>
			</van-radio-group>
		</van-cell-group>

		<van-submit-bar
				:price="data.total * 100"
				buttonText="提交订单"
				:disabled="!data.address"
				:loading="data.loadingBtn"
				@submit="handleSubmit"
		/>
	</div>

	<div ref="payForm"></div>

</template>

<style scoped lang="scss">
</style>
