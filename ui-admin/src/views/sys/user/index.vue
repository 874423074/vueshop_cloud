<script setup>
	import {ref, reactive} from 'vue'
	import {GetList, Delete, ResetPwd} from '^/user'
	import {ElMessage, ElMessageBox} from "element-plus";

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

	import Role from './role.vue'
	const roleDialog = ref(false)
	const roleRef = ref(null)
	const roleHandle = (id) => {
		roleDialog.value = true
		roleRef.value.init(id)
	}

	const closeHandle = (hasChange) => {
		editDialog.value = false
		roleDialog.value = false

		if (hasChange) {
			initData()
		}
	}

	const repassHandle = (id, username) => {
		ElMessageBox.confirm(
				'将重置用户【' + username + '】的密码，是否继续？', '警告',
				{confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning',}
		).then(() => {
			ResetPwd(id).then(res => {
				ElMessage({message: '操作成功', type: 'success'})
			})
		})
	}

</script>


<template>
	<div>
		<el-form :inline="true" ref="formRef">

			<el-form-item>
				<el-button type="primary" @click="editHandle(null)">新增</el-button>
			</el-form-item>
			<el-form-item>
				<el-button type="danger" @click="delHandle(null)" :disabled="delBtnStatus">批量删除</el-button>
			</el-form-item>
			<el-form-item>
				<el-input placeholder="名称" clearable></el-input>
			</el-form-item>
			<el-form-item>
				<el-button @click="initData()">搜索</el-button>
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
			<el-table-column prop="username" label="用户名" width="120"></el-table-column>

			<el-table-column prop="code" label="角色名称">
				<template #default="scope">
					<el-tag size="small" type="info" v-for="item in scope.row.roles">{{item.name}}</el-tag>
				</template>

			</el-table-column>

			<el-table-column prop="email" label="邮箱"></el-table-column>
			<el-table-column prop="phone" label="手机号"></el-table-column>
			<el-table-column prop="status" label="状态">
				<template #default="scope">
					<el-tag size="small" v-if="scope.row.status === 0" type="success">正常</el-tag>
					<el-tag size="small" v-else-if="scope.row.status === 1" type="danger">禁用</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="created" label="创建时间" width="200"></el-table-column>

			<el-table-column prop="icon" label="操作" width="260">
				<template #default="scope">
					<el-button type="text" @click="roleHandle(scope.row.id)">分配角色</el-button>
					<el-divider direction="vertical"></el-divider>
					<el-button type="text" @click="repassHandle(scope.row.id, scope.row.username)">重置密码</el-button>
					<el-divider direction="vertical"></el-divider>
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

	</div>

	<Edit ref="editRef" :showDialog="editDialog" @closeHandle="closeHandle"></Edit>
	<Role ref="roleRef" :showDialog="roleDialog" @closeHandle="closeHandle"></Role>

</template>

