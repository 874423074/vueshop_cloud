<script setup>

	import {GetInfo, AuditRefund} from '^/refund'
	import {ref, reactive} from 'vue'

	import {useRoute, useRouter} from 'vue-router'
	import {ElMessage, ElMessageBox} from "element-plus";

	const route = useRoute()
	const router = useRouter()

	const refund = ref({})
	const order = ref({})
	const operator = ref({})

	const id = route.query.id
	const initData = () => {
		GetInfo(id).then(res => {
			refund.value = res.data.refund
			operator.value = res.data.operator
			order.value = res.data.order
		})
	}
	initData()

	const form = ref({})

	const handleSubmit = (status) => {
		ElMessageBox.confirm('确认进行此操作吗?', '警告',
				{confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning',}
		).then(() => {
			AuditRefund({id: id, refundStatus: status, ...form.value}).then(res => {
				ElMessage({message: '操作成功', type: 'success'})
				initData()
			})
		})
	}

</script>

<template>

	<el-card shadow="never" style="width: 1000px; margin: 20px auto;">

		<template #header>
			<div class="card-header">
				<h3>当前订单状态：{{refund.refundStatusStr}}</h3>
				<div>
					<el-button @click="$router.go(-1)">关闭返回</el-button>
				</div>
			</div>
		</template>

		<h4>退货商品（合计：￥{{order.totalAmount}}）</h4>
		<el-table :data="order.orderItems" border style="margin-top: 20px; width: 100%; text-align: center;">
			<el-table-column align="center" label="商品图片" width="120">
				<template #default="scope">
					<el-image style="width: 80px; height: 80px" :src="scope.row.productImage" fit="fill"/>
				</template>
			</el-table-column>
			<el-table-column align="center" prop="productName" label="商品名称" width="300"/>
			<el-table-column align="center" prop="productSn" label="商品编号" width="180"/>
			<el-table-column align="center" prop="price" label="价格"/>
			<el-table-column align="center" prop="quantity" label="数量"/>
			<el-table-column align="center" prop="sku" label="规格"/>
		</el-table>

		<el-descriptions title="收货人信息" border style="margin-top: 20px;" direction="vertical" column="4">
			<el-descriptions-item align="center" label="收货人">{{ order.receiverName || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="手机号">{{ order.receiverTel || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="邮编">{{ order.receiverPostalCode || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="收货地址">{{ order.receiverAddress || '-' }}</el-descriptions-item>
		</el-descriptions>

		<el-descriptions title="物流信息" border style="margin-top: 20px;" direction="vertical" column="4">
			<el-descriptions-item align="center" label="物流公司">{{ order.deliveryCompany || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="物流单号司">{{ order.deliverySn || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="发货时间">{{ order.deliveryTime || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="收货时间">{{ order.receiveTime || '-' }}</el-descriptions-item>

		</el-descriptions>

		<el-descriptions title="退货单信息" border style="margin-top: 20px;" column="1">
			<el-descriptions-item align="center" label="订单号">{{ refund.orderSn || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="退货单号">{{ refund.sn || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="申请时间">{{ refund.created || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="退货方式">{{ refund.method || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="退货原因">{{ refund.reason || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="问题描述">{{ refund.description || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="凭证图片">
				<el-image v-if="refund.images" v-for="item in refund.images.split(';')"
				          style="width: 80px; height: 80px" :src="item" fit="fill"/>
			</el-descriptions-item>
		</el-descriptions>

		<el-descriptions v-if="refund.refundStatus === 0" title="请处理" border style="margin-top: 20px;" column="1">
			<el-descriptions-item align="center" label="订单总金额">￥{{ order.totalAmount }}</el-descriptions-item>
			<el-descriptions-item align="center" label="确认退款金额">
				<el-input v-model="form.refundAmount" placeholder="请沟通确认退款金额"></el-input>
			</el-descriptions-item>
			<el-descriptions-item align="center" label="处理备注">
				<el-input v-model="form.operateRemark" type="textarea"></el-input>
			</el-descriptions-item>
		</el-descriptions>

		<el-descriptions v-if="refund.refundStatus === 1" title="请处理" border style="margin-top: 20px;" column="1">
			<el-descriptions-item align="center" label="收货备注">
				<el-input v-model="form.receiptRemark" type="textarea"></el-input>
			</el-descriptions-item>
		</el-descriptions>

		<el-descriptions v-if="refund.operatorId" title="退货操作管理员" border style="margin-top: 20px;" column="1">
			<el-descriptions-item align="center" label="操作管理员">{{ operator || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="操作备注">{{ refund.operateRemark || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="操作时间">{{ refund.operateTime || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="收货备注">{{ refund.receiptRemark || '-' }}</el-descriptions-item>
		</el-descriptions>

		<div class="card-footer">
			<el-button v-if="refund.refundStatus === 0" type="primary" @click="handleSubmit(1)">确认退货</el-button>
			<el-button v-if="refund.refundStatus === 0" type="danger" @click="handleSubmit(3)">拒绝退货</el-button>
			<el-button v-if="refund.refundStatus === 1" type="success" @click="handleSubmit(2)">确认收货并退款</el-button>
			<el-button @click="$router.go(-1)">关闭返回</el-button>
		</div>

	</el-card>

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
