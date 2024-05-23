<script setup>
	import {ref, reactive} from 'vue'
	import {useRouter, useRoute} from "vue-router"
	import {PayCheck} from '^/order.js'

	const router = useRouter()
	const route = useRoute()

	const data = reactive({
		result: '结果查询中...',
		image: 'network'
	})

	const orderSn = route.query.out_trade_no
	const initData = () => {
		PayCheck(orderSn).then(res => {

			if (res.data === 'success') {
				data.result = '订单支付成功'
				data.image = 'default'
			} else {
				data.result = res.data
				data.image = 'error'
			}
		})
	}
	initData()

</script>

<template>
	<van-nav-bar title="支付结果"/>

	<van-empty image-size="180" :image="data.image">
		<template #description>
			<span class="description">{{data.result}}</span>
		</template>
		<van-button plain type="primary" @click="$router.push({name: 'Mine'})">查看订单列表</van-button>
	</van-empty>
</template>

<style lang="scss" scoped>
	.description {
		font-size: 16px;
		font-weight: bold;
		color: #323233;
	}
</style>