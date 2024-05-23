<script setup>

	import {GetList, Delete} from '^/carousel'
	import {ref, reactive} from 'vue'
	import {ElMessageBox, ElMessage} from 'element-plus'

	const viewData = reactive({
		pageData: [],
		page: {
			current: 1,
			size: 10
		},
		total: 0,

		searchForm: {}
	})

	const initData = () => {
		const params = {
			...viewData.searchForm,
			...viewData.page
		}
		GetList(params).then(res => {
			viewData.pageData = res.data.records
			viewData.total = res.data.total
		})
	}
	initData()

	const handleChangeCurrent = (val) => {
		viewData.page.current = val
		initData()
	}

	const delBtnStatus = ref(true)
	let selectedIds = []
	const handleSelectionChange = (val) => {
		delBtnStatus.value = val.length === 0
		selectedIds = val.map(item => item.id)
	}

	const delHandle = (id) => {

		let ids = [];

		if (id) {
			ids.push(id)
		} else {
			// 批量删除
			ids.push(...selectedIds)
		}

		ElMessageBox.confirm(
				'确认删除该记录吗?', '警告',
				{confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning',}
		).then(() => {
			Delete(ids).then(res => {
				ElMessage({message: '操作成功', type: 'success'})
				initData()
			})
		})
	}

	import Edit from './edit.vue'

	const editDialog = ref(false)
	const editRef = ref(null)
	const editHandle = (id) => {
		editDialog.value = true
		editRef.value.init(id)
	}

	const closeHandle = (hasChange) => {
		editDialog.value = false

		if (hasChange) {
			initData()
		}
	}

</script>

<template>
	<div>
		<el-form :inline="true" ref="formRef" :model="viewData.searchForm">

			<el-form-item>
				<el-button type="primary" @click="editHandle(null)">新增</el-button>
			</el-form-item>
			<el-form-item>
				<el-button type="danger" @click="delHandle(null)" :disabled="delBtlStatus">批量删除</el-button>
			</el-form-item>
			<el-form-item>
				<el-input v-model="viewData.searchForm.name" placeholder="名称" clearable></el-input>
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
				@selection-change="handleSelectionChange">

			<el-table-column type="selection" width="55"></el-table-column>
			<el-table-column prop="name" label="名称" width="320"></el-table-column>
			<el-table-column label="图片" show-overflow-tooltip>
				<template #default="scope">
					<el-image :src="scope.row.image"/>
				</template>
			</el-table-column>
			<el-table-column prop="position" label="位置" show-overflow-tooltip></el-table-column>
			<el-table-column prop="url" label="链接" show-overflow-tooltip></el-table-column>
			<el-table-column prop="isShow" label="是否展示" show-overflow-tooltip></el-table-column>
			<el-table-column prop="sortOrder" label="排序" show-overflow-tooltip></el-table-column>
			<el-table-column prop="icon" label="操作">

				<template #default="scope">
					<el-button type="text" @click="editHandle(scope.row.id)">编辑</el-button>
					<el-divider direction="vertical"></el-divider>
					<el-button type="text" @click="delHandle(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>

		<el-pagination background layout="total, prev, pager, next"
		               :total="viewData.total"
		               :page-size="viewData.page.size"
		               @current-change="handleChangeCurrent"
		               style="margin-top: 15px"/>

		<Edit ref="editRef" :showDialog="editDialog" @closeHandle="closeHandle"></Edit>

	</div>
</template>