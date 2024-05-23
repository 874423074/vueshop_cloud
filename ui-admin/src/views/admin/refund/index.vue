<script setup>

	import {GetList} from '^/refund'
	import {ref, reactive} from 'vue'

	const viewData = reactive({
		searchForm: {},
		pageData: [],
		page: {
			current: 1,
			size: 10
		},
		total: 0,

		refundStatusList: [],
	})

	const initData = () => {
		const params = {
			...viewData.searchForm,
			...viewData.page
		}

		GetList(params).then(res => {
			viewData.pageData = res.data.pageData.records
			viewData.total = res.data.pageData.total
			viewData.refundStatusList = res.data.refundStatusList
		})
	}
	initData()

	const handleChangeCurrent = (val) => {
		viewData.page.current = val
		initData()
	}
</script>

<template>
	<div>
		<el-form :inline="true" ref="formRef" :model="viewData.searchForm">

			<el-form-item>
				<el-input v-model="viewData.searchForm.sn" placeholder="退货单号" clearable></el-input>
			</el-form-item>
			<el-form-item>
				<el-input v-model="viewData.searchForm.orderSn" placeholder="订单号" clearable></el-input>
			</el-form-item>
			<el-form-item>
				<el-select v-model="viewData.searchForm.refundStatus" class="m-2" placeholder="请选择退货状态" clearable>
					<el-option
							v-for="item in viewData.refundStatusList"
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
			<el-table-column prop="id" label="ID" width="45"></el-table-column>
			<el-table-column prop="sn" label="退货单" width="180"></el-table-column>
			<el-table-column prop="orderSn" label="订单号" width="180"></el-table-column>
			<el-table-column prop="username" label="用户名" show-overflow-tooltip width="120"></el-table-column>
			<el-table-column prop="refundAmount" label="退款金额" show-overflow-tooltip width="100"></el-table-column>
			<el-table-column prop="method" label="退货方式" show-overflow-tooltip width="100"></el-table-column>
			<el-table-column prop="reason" label="退货原因" show-overflow-tooltip width="200"></el-table-column>
			<el-table-column prop="refundStatusStr" label="状态" show-overflow-tooltip width="100"></el-table-column>
			<el-table-column prop="created" label="创建时间" show-overflow-tooltip width="180"></el-table-column>
			<el-table-column prop="operateTime" label="处理时间" show-overflow-tooltip width="180"></el-table-column>
			<el-table-column prop="icon" label="操作" width="100"  fixed="right">
				<template #default="scope">
					<el-button type="text" @click="$router.push('/admin/refund/detail?id=' + scope.row.id)">查看退货单</el-button>
				</template>
			</el-table-column>
		</el-table>

		<el-pagination background layout="total, prev, pager, next"
		               :total="viewData.total"
		               :page-size="viewData.page.size"
		               @current-change="handleChangeCurrent"
		               style="margin-top: 15px"/>
	</div>
</template>