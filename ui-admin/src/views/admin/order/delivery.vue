<script setup>

	import {defineEmits, defineProps, ref, toRefs} from 'vue'
	import {GetDeliveryInfo} from '^/order'


	const props = defineProps({
		showDialog: Boolean,
		order: Object
	})
	const {showDialog, order} = toRefs(props)

	const deliveries = ref([])
	const init = (id) => {
		GetDeliveryInfo(id).then(res => {
			deliveries.value = res.data
		})
	}
	defineExpose({init})

	const emits = defineEmits(['closeHandle'])
	const closeDialog = (hasChange) => {
		emits('closeHandle', hasChange)
	}


</script>
<template>
	<el-drawer
			title="物流信息"
			v-model="showDialog"
			width="600px"
			:before-close="closeDialog"
	>
		<el-descriptions border direction="vertical">
			<el-descriptions-item align="center" label="配送方式">{{ order.deliveryCompany || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="物流单号">{{ order.deliverySn || '-' }}</el-descriptions-item>
			<el-descriptions-item align="center" label="发货时间">{{ order.deliveryTime || '-' }}</el-descriptions-item>
		</el-descriptions>

		<el-timeline style="margin-top: 20px;">
			<el-timeline-item v-for="item in deliveries" :timestamp="item.acceptTime">
				{{ item.acceptStation }} | {{item.remark || ''}}
			</el-timeline-item>
		</el-timeline>
	</el-drawer>
</template>
