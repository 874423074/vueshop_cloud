<script setup>
	import {ref, reactive} from 'vue'
	import {GetMenuTree, Delete} from '^/menu'
	import {ElMessageBox, ElMessage} from 'element-plus'

	const viewData = reactive({
		tableData: []
	})

	const initData = () => {
		GetMenuTree().then(res => {
			viewData.tableData = res.data
		})
	}
	initData()

	const delHandle = (id) => {
		ElMessageBox.confirm(
				'确认删除该菜单吗?', '警告',
				{confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning',}
		).then(() => {
			Delete(id).then(res => {
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
		<el-form :inline="true">
			<el-form-item>
				<el-button type="primary" @click="editHandle(null)">新增</el-button>
			</el-form-item>
		</el-form>

		<el-table
				:data="viewData.tableData"
				style="width: 100%;margin-bottom: 20px;"
				row-key="id"
				border
				stripe
				default-expand-all>

			<el-table-column prop="title" label="名称" sortable width="180"></el-table-column>
			<el-table-column prop="perms" label="权限编码" sortable width="180"></el-table-column>
			<el-table-column prop="icon" label="图标"></el-table-column>
			<el-table-column prop="type" label="类型" width="70">
				<template #default="scope">
					<el-tag size="small" v-if="scope.row.type === 0">目录</el-tag>
					<el-tag size="small" type="success" v-if="scope.row.type === 1">菜单</el-tag>
					<el-tag size="small" type="info" v-if="scope.row.type === 2">按钮</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="path" label="菜单URL"></el-table-column>
			<el-table-column prop="component" label="菜单组件" width="220"></el-table-column>
			<el-table-column prop="sortOrder" label="排序号" width="70"></el-table-column>
			<el-table-column prop="status" label="状态" width="70">
				<template #default="scope">
					<el-tag size="small" type="success" v-if="scope.row.status === 0">正常</el-tag>
					<el-tag size="small" type="danger" v-if="scope.row.status === 1">禁用</el-tag>
				</template>
			</el-table-column>
			<el-table-column label="操作">
				<template #default="scope">
					<el-button type="text" @click="editHandle(scope.row.id)">编辑</el-button>
					<el-divider direction="vertical"></el-divider>
					<el-button type="text" @click="delHandle(scope.row.id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>

	</div>

	<Edit ref="editRef" :showDialog="editDialog" :tableData="viewData.tableData" @closeHandle="closeHandle"></Edit>

</template>