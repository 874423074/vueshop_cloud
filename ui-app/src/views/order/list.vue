<script setup>

	import {ref, reactive} from 'vue'
	import {useRouter, useRoute} from "vue-router"
	import {ListOrder} from '^/order.js'
	import OrderBtns from '~/OrderBtns.vue'

	const router = useRouter()
	const route = useRoute()

	const data = reactive({
		tabs: [
			{title: '全部', status: ""},
			{title: '待付款', status: "0"},
			{title: '待发货', status: "1"},
			{title: '待收货', status: "2"},
			{title: '退款中', status: "5"},
		],
		activeTab: '',

		orders: [],
		status: '',

		current: 1,
		loading: false,
		finished: false
	})

	data.status = data.activeTab = route.query.status

	const changeTab = (status) => {
		data.activeTab = status
		data.status = status
		data.current = 1
		data.orders = []
		data.finished = false

		router.replace("/order/list?status=" + status)
	}

	const initData = () => {
		ListOrder(data.status, data.current).then(res => {
			data.orders.push(...res.data.records)

			data.loading = false

			if (res.data.total === data.orders.length) {
				data.finished = true
			}

			data.current++
		})
	}
	// initData()

</script>

<template>

	<van-nav-bar title="订单管理" left-text="返回" left-arrow @click-left="$router.go(-1)"/>

	<van-tabs :active="data.activeTab" :before-change="changeTab">
		<van-tab :title="item.title" :name="item.status" v-for="item in data.tabs">

			<van-list
					v-model:loading="data.loading"
					:finished="data.finished"
					finished-text="没有更多了"
					@load="initData"
			>
				<div style="background-color: white;" v-for="order in data.orders">

					<van-cell-group style="margin-top: 8px;" border @click.native="$router.push('/order/detail?id=' + order.id)">
						<van-cell :title="'订单: ' + order.sn" :value="order.orderStatusStr"></van-cell>

						<van-card
								v-for="item in order.orderItems"
								:title="item.productName"
								:price="item.price"
								:num="item.quantity"
								:thumb="item.productImage"
						>
							<template #tags>
								<van-tag color="#c8c9cc" text-color="white" style="margin: 6px 6px 0 0;"
								         v-for="it in item.sku.split(';')">
									{{it}}
								</van-tag>
							</template>
						</van-card>

					</van-cell-group>

					<div class="total">合计: ￥{{order.totalAmount}}（含运费￥{{order.freight}}）</div>
					<div slot="footer">
						<OrderBtns :order="order"></OrderBtns>
					</div>
				</div>

			</van-list>
		</van-tab>
	</van-tabs>
</template>

<style lang="scss" scoped>
	.total {
		font-size: 14px;
		text-align: right;
		padding: 10px;
	}
</style>
