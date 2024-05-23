<script setup name="OrderDelivery">
	import {ref, reactive} from 'vue'
	import {useRoute} from "vue-router"
	import {OrderDelivery} from '^/order.js'

	const route = useRoute()

	const orderId = route.query.id

	const data = reactive({
		deliveryCompany: '',
		deliverySn: '',
		deliveryInfo: []
	})

	const initData = () => {
		OrderDelivery(orderId).then(res => {
			data.deliveryCompany = res.data.deliveryCompany
			data.deliverySn = res.data.deliverySn
			data.deliveryInfo = res.data.deliveryInfo
		})
	}
	initData()

</script>

<template>
	<van-nav-bar title="物流信息" left-text="返回" left-arrow @click-left="$router.go(-1)"/>

	<van-cell-group style="margin: 20px 0;">
		<van-cell title="物流公司" :value="data.deliveryCompany"/>
		<van-cell title="物流单号" :value="data.deliverySn"/>
	</van-cell-group>

	<van-steps direction="vertical" :active="0">
		<van-step v-for="item in data.deliveryInfo">
			<strong>{{item.acceptStation}} - {{item.remark}}</strong>
			<p>{{item.acceptTime}}</p>
		</van-step>
	</van-steps>

</template>