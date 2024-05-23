<script setup>

	import {GetList, Delete, UpdateState} from '^/product'
	import {ref, reactive} from 'vue'
	import {ElMessageBox, ElMessage} from 'element-plus'

	const viewData = reactive({
		pageData: [],
		page: {
			current: 1,
			size: 10
		},
		total: 0,

		searchForm: {},
		categories: []
	})

	const initData = () => {
		const params = {
			...viewData.searchForm,
			...viewData.page
		}
		GetList(params).then(res => {
			viewData.pageData = res.data.pageData.records
			viewData.total = res.data.pageData.total

			viewData.categories = res.data.categories
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

	const updateState = (id, state, status) => {
		UpdateState({id: id, [state]: status})
	}


</script>

<template>
	<div>
		<el-form :inline="true" ref="formRef" :model="viewData.searchForm">

			<el-form-item>
				<el-button type="primary" @click="$router.push('/admin/product/edit')">新增</el-button>
			</el-form-item>
			<el-form-item>
				<el-button type="danger" @click="delHandle(null)" :disabled="delBtnStatus">批量删除</el-button>
			</el-form-item>
			<el-form-item>
				<el-input v-model="viewData.searchForm.name" placeholder="名称" clearable></el-input>
			</el-form-item>
			<el-form-item>
				<el-select v-model="viewData.searchForm.categoryId" placeholder="请选择分类">
					<el-option label="全部" value=""></el-option>
					<el-option
							v-for="item in viewData.categories"
							:label="item.name"
							:key="item.id"
							:value="item.id">
					</el-option>
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
				@selection-change="handleSelectionChange">

			<el-table-column type="selection" width="45"></el-table-column>
			<el-table-column prop="id" label="ID" width="50"></el-table-column>
			<el-table-column prop="name" label="名称"></el-table-column>
			<el-table-column prop="sn" label="编码" width="180"></el-table-column>
			<el-table-column label="图片" show-overflow-tooltip width="70">
				<template #default="scope">
					<el-avatar shape="square" :src="scope.row.image"></el-avatar>
				</template>
			</el-table-column>
			<el-table-column prop="categoryName" label="分类" show-overflow-tooltip  width="60"></el-table-column>
			<el-table-column prop="price" label="价格" show-overflow-tooltip width="70"></el-table-column>
			<el-table-column prop="sale" label="销量" show-overflow-tooltip width="50"></el-table-column>
			<el-table-column prop="stock" label="库存" show-overflow-tooltip width="50"></el-table-column>
			<el-table-column prop="icon" label="快速操作" width="230">
				<template #default="scope">
					<div>
						在售：<el-switch v-model="scope.row.isOnSale" @change="updateState(scope.row.id, 'isOnSale', scope.row.isOnSale)"></el-switch>
						<el-divider direction="vertical"></el-divider>
						新产品：<el-switch v-model="scope.row.isNew"  @change="updateState(scope.row.id, 'isNew', scope.row.isNew)"></el-switch>
					</div>
					<div>
						热门：<el-switch v-model="scope.row.isHot"  @change="updateState(scope.row.id, 'isHot', scope.row.isHot)"></el-switch>
						<el-divider direction="vertical"></el-divider>
						置顶：<el-switch v-model="scope.row.isTop"  @change="updateState(scope.row.id, 'isTop', scope.row.isTop)"></el-switch>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="icon" label="操作" width="150">

				<template #default="scope">
					<el-button type="text" @click="$router.push('/admin/product/edit?id=' + scope.row.id)">编辑</el-button>
					<el-divider direction="vertical"></el-divider>
					<el-button type="text" @click="$router.push('/admin/product/sku?id=' + scope.row.id)">规格</el-button>
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
	</div>
</template>
