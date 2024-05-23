<script setup>

	import {ref, toRefs, defineProps} from 'vue'
	import {ConfirmOrder, DeleteOrder, CancelOrder, PayOrder} from '^/order.js'
	import {showConfirmDialog, showNotify} from "vant";

	const props = defineProps({
		order: Object,
	})
	const {order} = toRefs(props)

	const handleDelete = () => {
		showConfirmDialog({
			message: '确认删除该订单吗？',
		}).then(() => {
			DeleteOrder(order.value.id).then(res => {
				showNotify({type: 'primary', message: '删除成功'})

				order.value.orderStatus = -1
				order.value.orderStatusStr = '已删除'
			})
		})
	}

	const handleCancel = () => {
		showConfirmDialog({
			message: '确认取消该订单吗？',
		}).then(() => {
			CancelOrder(order.value.id).then(res => {
				showNotify({type: 'primary', message: '取消成功'})

				order.value.orderStatus = 4
				order.value.orderStatusStr = '已取消'
			})
		})
	}

	const payForm = ref(null)
	const handlePay = () => {
		PayOrder(order.value.sn).then(res => {
			payForm.value.innerHTML = res.data
			payForm.value.children[0].submit()
		})
	}

	const handleConfirm = () => {
		showConfirmDialog({
			message: '确认已到收到货了吗？',
		}).then(() => {
			ConfirmOrder(order.value.id).then(res => {
				showNotify({type: 'primary', message: '收货成功'})

				order.value.orderStatus = 3
				order.value.orderStatusStr = '待评论'
			})
		})
	}
</script>

<template>
	<div class="order_btn">
		<van-button size="small" @click="handleDelete" v-if="[3,4,6,7].includes(order.orderStatus)">删除订单</van-button>
		<van-button size="small" @click="$router.push('/order/refund?id=' + order.id)"
		            v-if="[1,2,3].includes(order.orderStatus)">申请退款
		</van-button>
		<van-button size="small" @click="handleCancel" v-if="[0].includes(order.orderStatus)">取消订单</van-button>
		<van-button size="small" @click="handlePay" type="primary" v-if="[0].includes(order.orderStatus)">马上支付</van-button>
		<van-button size="small" @click="$router.push('/order/deliveryInfo?id=' + order.id)"
		            v-if="[2,3].includes(order.orderStatus)">查看物流
		</van-button>
		<van-button size="small" @click="handleConfirm" type="primary" v-if="[2].includes(order.orderStatus)">确认收货
		</van-button>
	</div>

	<div ref="payForm"></div>
</template>

<style lang="scss" scoped>
	.order_btn {
		text-align: right;
		padding: 8px;

		.van-button {
			margin-left: 10px;
		}
	}
</style>
