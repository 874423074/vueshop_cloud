<script setup>

	import {defineEmits, defineProps, ref, toRefs} from 'vue'
	import {ElMessage} from "element-plus"
	import {ShipOrder} from '^/order'


	const props = defineProps({
		showDialog: Boolean,
		order: Object
	})
	const {showDialog, order} = toRefs(props)

	const shipForm = ref({})
	const init = (id) => {
		shipForm.value.id = id
	}
	defineExpose({init})

	const emits = defineEmits(['closeHandle'])
	const closeDialog = (hasChange) => {
		emits('closeHandle', hasChange)
	}

	const submitForm = () => {
		ShipOrder(shipForm.value).then(res => {
			ElMessage({type: 'success', message: '操作成功！'})
			closeDialog(true)
		})
	}

</script>
<template>
	<el-drawer
			title="订单发货"
			v-model="showDialog"
			width="600px"
			:before-close="closeDialog"

	>
		<el-descriptions title="基本信息" border direction="vertical">
			<el-descriptions-item align="center" label="订单编号">{{ order.sn }}</el-descriptions-item>
			<el-descriptions-item align="center" label="用户账号">{{ order.username }}</el-descriptions-item>
		</el-descriptions>

		<el-descriptions title="收货人信息" border style="margin-top: 20px;" direction="vertical">
			<el-descriptions-item align="center" label="收货人">{{ order.receiverName || '-' }}
			</el-descriptions-item>
			<el-descriptions-item align="center" label="手机号">{{ order.receiverTel || '-' }}
			</el-descriptions-item>
			<el-descriptions-item align="center" label="邮编">{{ order.receiverPostalCode || '-' }}
			</el-descriptions-item>
			<el-descriptions-item align="center" label="收货地址">{{ order.receiverAddress || '-' }}
			</el-descriptions-item>
		</el-descriptions>

		<el-form :model="shipForm" ref="ruleForm" label-width="100px" style="margin-top: 20px;">
			<el-input v-model="shipForm.id" type="hidden"></el-input>

			<el-form-item label="配送方式" prop="remark" label-width="100px">
				<el-select v-model="shipForm.deliveryCompany" placeholder="请选择物流公司" :teleported='false'>
					<el-option key="SF" label="顺丰速运" value="顺丰速运"/>
					<el-option key="ZTO" label="中通快递" value="中通快递"/>
					<el-option key="YTO" label="圆通速递" value="圆通速递"/>
					<el-option key="YD" label="韵达速递" value="韵达速递"/>
				</el-select>
			</el-form-item>
			<el-form-item label="物流单号" prop="deliverySn" label-width="100px">
				<el-input v-model="shipForm.deliverySn" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item>
				<el-button type="primary" @click="submitForm">立即发货</el-button>
				<el-button @click="closeDialog">关闭</el-button>
			</el-form-item>
		</el-form>
	</el-drawer>
</template>