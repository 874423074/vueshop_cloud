<script setup>

	import {GetList, CloseOrder} from '^/order'
	import {ref, reactive} from 'vue'
	import {ElMessageBox, ElMessage} from 'element-plus'

	const viewData = reactive({
		searchForm: {},
		pageData: [],
		page: {
			current: 1,
			size: 10
		},
		total: 0,

		orderStatus: null,
		orderStatusList: [],
	})

	const initData = () => {
		const params = {
			...viewData.searchForm,
			...viewData.page
		}

		GetList(params).then(res => {
			viewData.pageData = res.data.pageData.records
			viewData.total = res.data.pageData.total
			viewData.orderStatusList = res.data.orderStatusList
			console.log(viewData.orderStatus)
		})
	}
	initData()

	const handleChangeCurrent = (val) => {
		viewData.page.current = val
		initData()
	}

	const closeOrder = (id) => {
		ElMessageBox.prompt('确认关闭该订单吗？', '警告', {
			confirmButtonText: '确认',
			cancelButtonText: '取消',
			type: 'warning'
		}).then(({value}) => {
			CloseOrder({id: id, adminNote: `${value}`}).then(res => {
				ElMessage({type: 'success', message: '操作成功！'})
				initData()
			})
		})
	}

	import Ship from './ship.vue'

	const shipDialog = ref(false)
	const shipRef = ref(null)
	const order = ref({})
	const shipOrder = (row) => {
		shipDialog.value = true
		shipRef.value.init(row.id)
		order.value = row
	}

	import Delivery from './delivery.vue'
	const deliveryDialog = ref(false)
	const deliveryRef = ref(null)
	const deliveryOrder = (row) => {
		deliveryDialog.value = true
		deliveryRef.value.init(row.id)
		order.value = row
	}

	const closeHandle = (hasChange) => {
		shipDialog.value = false
		deliveryDialog.value = false

		if (hasChange) {
			initData()
		}
	}

</script>
<template>
	<div>
		<el-form :inline="true" ref="formRef" :model="viewData.searchForm">

			<el-form-item>
				<el-input v-model="viewData.searchForm.sn" placeholder="订单号" clearable></el-input>
			</el-form-item>
			<el-form-item>
				<el-input v-model="viewData.searchForm.username" placeholder="用户名" clearable></el-input>
			</el-form-item>
			<el-form-item>

				<el-select v-model="viewData.searchForm.orderStatus" class="m-2" placeholder="请选择订单状态" clearable>
					<el-option
							v-for="item in viewData.orderStatusList"
							:key="item.key"
							:value="item.key"
							:label="item.value"
					/>
				</el-select>
			</el-form-item>


			<el-form-item>
				<el-button @click="initData">搜索</el-button>
			</el-form-item>

		</el-form>

		<el-table
				ref="multipleTable"
				:data="viewData.pageData"
				tooltip-effect="dark"
				style="width: 100%"
				border
				stripe
		>

			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column prop="id" label="ID" width="80"></el-table-column>
			<el-table-column prop="sn" label="订单号" width="180"></el-table-column>
			<el-table-column prop="username" label="用户名" show-overflow-tooltip width="120"></el-table-column>
			<el-table-column prop="totalAmount" label="订单金额" show-overflow-tooltip width="100"></el-table-column>
			<el-table-column prop="orderStatusStr" label="订单状态" show-overflow-tooltip width="100"></el-table-column>
			<el-table-column prop="created" label="创建时间" show-overflow-tooltip width="180"></el-table-column>
			<el-table-column prop="payTime" label="支付时间" show-overflow-tooltip width="180"></el-table-column>
			<el-table-column prop="note" label="备注" show-overflow-tooltip></el-table-column>
			<el-table-column prop="icon" label="操作">

				<template #default="scope">
					<el-button type="text" @click="$router.push('/admin/order/detail?id=' + scope.row.id)">查看订单</el-button>
					<template v-if="scope.row.orderStatus === 0">
						<el-divider direction="vertical"></el-divider>
						<el-button type="text" @click="closeOrder(scope.row.id)">关闭订单</el-button>
					</template>
					<template v-if="scope.row.orderStatus === 1">
						<el-divider direction="vertical"></el-divider>
						<el-button type="text" @click="shipOrder(scope.row)">订单发货</el-button>
					</template>
					<template v-if="scope.row.deliverySn">
						<el-divider direction="vertical"></el-divider>
						<el-button type="text"  @click="deliveryOrder(scope.row)">查看物流</el-button>
					</template>
				</template>
			</el-table-column>
		</el-table>

		<el-pagination background layout="total, prev, pager, next"
		               :total="viewData.total"
		               :page-size="viewData.page.size"
		               @current-change="handleChangeCurrent"
		               style="margin-top: 15px"/>

	</div>

	<Ship ref="shipRef" :showDialog="shipDialog" :order="order"  @closeHandle="closeHandle"></Ship>
	<Delivery ref="deliveryRef" :showDialog="deliveryDialog" :order="order"  @closeHandle="closeHandle"></Delivery>
</template>
