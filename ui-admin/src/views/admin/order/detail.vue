<script setup>

	import {GetInfo} from '^/order'
	import {ref} from 'vue'

	import {useRoute, useRouter} from 'vue-router'

	const route = useRoute()
	const router = useRouter()

	const order = ref({})

	const orderId = route.query.id
	const initData = () => {
		GetInfo(orderId).then(res => {
			order.value = res.data
		})
	}
	initData()

	import Delivery from './delivery.vue'
	const deliveryDialog = ref(false)
	const deliveryRef = ref(null)
	const deliveryOrder = () => {
		deliveryDialog.value = true
		deliveryRef.value.init(orderId)
	}

	const closeHandle = (hasChange) => {
		deliveryDialog.value = false

		if (hasChange) {
			initData()
		}
	}

</script>

<template>

	<el-card shadow="never" style="width: 1000px; margin: 20px auto;">

		<template #header>
			<div class="card-header">
				<h3>当前订单状态：{{order.orderStatusStr}}</h3>
				<div>
					<el-button class="button" text v-if="order.deliverySn" @click="deliveryOrder">查看物流</el-button>
					<el-button class="button" text>备注订单</el-button>
					<el-button @click="$router.go(-1)">关闭返回</el-button>
				</div>
			</div>
		</template>

		<el-descriptions title="基本信息" border direction="vertical" column="4">
			<el-descriptions-item align="center" label="订单编号">{{ order.sn }}</el-descriptions-item>
			<el-descriptions-item align="center" label="用户账号">{{ order.username }}</el-descriptions-item>
			<el-descriptions-item align="center" label="创建时间">{{ order.created }}</el-descriptions-item>
			<el-descriptions-item align="center" label="支付时间">{{ order.payTime || '-' }}</el-descriptions-item>

			<el-descriptions-item align="center" label="配送方式">{{ order.deliveryCompany || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="物流单号">{{ order.deliverySn || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="发货时间">{{ order.deliveryTime || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="确认收货时间">{{ order.receiveTime || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="备注">
				{{ order.note || '-' }}
			</el-descriptions-item>
		</el-descriptions>


		<el-descriptions title="收货人信息" border style="margin-top: 20px;" direction="vertical" column="4">
			<el-descriptions-item align="center" label="收货人">{{ order.receiverName || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="手机号">{{ order.receiverTel || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="邮编">{{ order.receiverPostalCode || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="收货地址">{{ order.receiverAddress || '-' }}</el-descriptions-item>
		</el-descriptions>

		<el-descriptions title="费用信息" border style="margin-top: 20px;" direction="vertical">

			<el-descriptions-item align="center" label="商品总金额">￥{{ order.totalAmount || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="运费">{{ order.freight || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="折扣">-</el-descriptions-item>

		</el-descriptions>

		<h4>商品信息</h4>
		<el-table :data="order.orderItems" border style="margin-top: 20px; width: 100%; text-align: center;">
			<el-table-column align="center" label="商品图片" width="180">
				<template #default="scope">
					<el-image style="width: 80px; height: 80px" :src="scope.row.productImage" fit="fill"/>
				</template>
			</el-table-column>
			<el-table-column align="center" prop="productName" label="商品名称" width="380"/>
			<el-table-column align="center" prop="productSn" label="商品编号" width="180"/>
			<el-table-column align="center" prop="price" label="价格"/>
			<el-table-column align="center" prop="quantity" label="数量"/>
			<el-table-column align="center" prop="sku" label="规格"/>
		</el-table>

		<h4>操作信息</h4>
		<el-table data="" border style="margin-top: 20px; width: 100%; text-align: center;">
			<el-table-column align="center" prop="productName" label="管理员" width="380"/>
			<el-table-column align="center" prop="productSn" label="操作时间" width="180"/>
			<el-table-column align="center" prop="price" label="订单状态"/>
			<el-table-column align="center" prop="quantity" label="付款状态"/>
			<el-table-column align="center" prop="sku" label="发货状态"/>
			<el-table-column align="center" prop="sku" label="备注"/>
		</el-table>

		<div class="card-footer">
			<el-button @click="$router.go(-1)">关闭返回</el-button>
		</div>

	</el-card>

	<Delivery ref="deliveryRef" :showDialog="deliveryDialog" :order="order"  @closeHandle="closeHandle"></Delivery>

</template>

<style lang="scss" scoped>
	.card-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.card-footer {
		text-align: center;
		margin: 20px auto;
	}
</style>
