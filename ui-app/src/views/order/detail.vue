<script setup name="OrderDetail">
	import {ref} from 'vue'
	import {useRouter, useRoute} from "vue-router"
	import {OrderDetail} from '^/order.js'
	import OrderBtns from '~/OrderBtns.vue'

	const router = useRouter()
	const route = useRoute()

	const id = route.query.id
	const order = ref({})

	const initData = () => {
		OrderDetail(id).then(res => {
			order.value = res.data
		})
	}
	initData()

	import { showImagePreview } from 'vant'
	const previewImg = (index) => {
		showImagePreview({
			images: order.value.appRefund.images.split(';'),
			startPosition: index
		});
	}

</script>

<template>
	<van-nav-bar title="订单详情" left-text="返回" left-arrow @click-left="$router.go(-1)"/>

	<van-cell>
		<template #title>
			<h3>状态：{{order.orderStatusStr}}</h3>
		</template>
	</van-cell>

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

	<van-cell-group style="margin-top: 8px;">
		<van-cell icon="location-o">
			<template #title>
				<span>收货地址：</span>
				<span>{{order.receiverName}} </span>&nbsp
				<span>{{order.receiverTel}}</span>
			</template>
			<template #label>
				<span style="color: #999999">{{order.receiverAddress}}</span>
			</template>
		</van-cell>
	</van-cell-group>

	<van-cell-group style="margin-top: 8px;">
		<van-cell title="订单号" :value="order.sn"/>
		<van-cell title="总金额">
			<div slot="value">
				<span style="color: red; font-weight: bold; font-size: 18px;">￥{{ order.totalAmount }}</span>
			</div>
		</van-cell>
		<van-cell title="运费" :value="order.freight"/>
		<van-cell title="创建时间" :value="order.created"/>
		<van-cell v-if="order.payTime" title="支付时间" :value="order.payTime"/>
		<van-cell v-if="order.deliveryTime" title="发货时间" :value="order.deliveryTime"/>
		<van-cell v-if="order.receiveTime" title="收货时间" :value="order.receiveTime"/>
	</van-cell-group>

	<van-cell-group v-if="order.deliverySn" style="margin-top: 8px;">
		<van-cell title="物流公司" :value="order.deliveryCompany"/>
		<van-cell title="物流单号" :value="order.deliverySn"/>
	</van-cell-group>
	<van-cell-group style="margin-top: 8px;">
		<van-cell title="备注" :value="order.note"/>
	</van-cell-group>

	<van-cell-group v-if="order.appRefund" style="margin-top: 8px;">
		<van-cell title="退货单号" :value="order.appRefund.sn"/>
		<van-cell title="退款金额" :value="'￥' + order.appRefund.refundAmount"/>
		<van-cell title="退款原因" :value="order.appRefund.reason"/>
		<van-cell title="退货方式" :value="order.appRefund.method"/>
		<van-cell title="申请退款时间" :value="order.appRefund.created"/>
		<van-cell title="完成退款时间" :value="order.appRefund.returnTime" v-if="order.appRefund.returnTime"/>
		<van-cell v-if="order.appRefund.images">
			<van-image
					v-for="(img, index) in order.appRefund.images.split(';')"
					width="100"
					height="100"
					style="margin: 5px"
					:src="img"
					@click="previewImg(index)"
			/>
		</van-cell>
	</van-cell-group>

	<!-- 订单动作 -->
	<van-cell-group style="margin-top: 8px;">
		<van-cell>
			<OrderBtns :order="order"></OrderBtns>
		</van-cell>
	</van-cell-group>

</template>

<style scoped lang="scss">
	.order_btn {
		text-align: right;
		padding: 8px;

		.van-button {
			margin-left: 10px;
		}
	}
</style>