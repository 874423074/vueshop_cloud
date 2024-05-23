<script setup>
	import {reactive, toRefs, ref, defineEmits, defineExpose} from 'vue'
	import {ElMessage} from "element-plus"
	import {GetList} from '^/role'
	import {GetInfo, Role} from '^/user'

	const props = defineProps({
		showDialog: {type: Boolean}
	})
	const {showDialog} = toRefs(props)

	const emits = defineEmits(['closeHandle'])
	const closeDialog = (hasChange) => {
		emits('closeHandle', hasChange)
	}

	const treeData = ref([])
	const treeRef = ref(null)
	let userId = null
	const init = (id) => {

		userId = id

		// 获取菜单列表
		GetList().then(res => {
			treeData.value = res.data.records

			// 回显已勾选的菜单id
			GetInfo(id).then(res2 => {
				const roleIds = res2.data.roles.map(e => e.id)
				console.log("roleIds" + roleIds)
				console.log(roleIds)
				treeRef.value.setCheckedKeys(roleIds)
			})
		})

	}
	defineExpose({init})

	const submitHandle = () => {
		const roleIds = treeRef.value.getCheckedKeys()
		Role(userId, roleIds).then(res => {
			ElMessage({message: '操作成功', type: 'success'})
			closeDialog(true)
		})
	}

</script>

<template>
	<!--新增对话框-->
	<el-drawer
			title="分配角色"
			width="600px"
			v-model="showDialog"
			:before-close="closeDialog"
	>

		<el-form>
			<el-form-item>
				<el-tree
						:data="treeData"
						show-checkbox
						ref="treeRef"
						:check-strictly=true
						node-key="id"
						:default-expand-all=true
						:props="{children: 'children',label: 'name'}">
				</el-tree>
			</el-form-item>

			<el-form-item>
				<el-button type="primary" @click="submitHandle">立即创建</el-button>
				<el-button @click="closeDialog">关闭</el-button>
			</el-form-item>
		</el-form>

	</el-drawer>
</template>

