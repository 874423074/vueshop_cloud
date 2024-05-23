<script setup>
	import {defineEmits, defineExpose, ref, toRefs} from 'vue'
	import {ElMessage} from "element-plus"
	import {GetMenuTree} from '^/menu'
	import {GetInfo, Perm} from '^/role'


	const props = defineProps({
		showDialog: {type: Boolean}
	})
	const {showDialog} = toRefs(props)

	const emits = defineEmits(['closeHandle'])
	const closeDialog = (hasChange) => {
		emits('closeHandle', hasChange)
	}

	const permForm = ref({})
	const treeData = ref([])
	const treeRef = ref(null)
	let roleId = null
	const init = (id) => {

		roleId = id

		// 获取菜单列表
		GetMenuTree().then(res => {
			treeData.value = res.data

			// 回显已勾选的菜单id
			GetInfo(id).then(res2 => {
				treeRef.value.setCheckedKeys(res2.data.menuIds)
			})
		})

	}
	defineExpose({init})

	const submitHandle = () => {
		const menuIds = treeRef.value.getCheckedKeys()
		Perm(roleId, menuIds).then(res => {
			ElMessage({message: '操作成功', type: 'success'})
			closeDialog(true)
		})
	}


</script>

<template>
	<el-drawer
			title="分配权限"
			v-model="showDialog"
			:before-close="closeDialog"
			width="600px">

		<el-form :model="permForm">
			<el-form-item>
				<el-tree
						:data="treeData"
						show-checkbox
						ref="treeRef"
						:default-expand-all=true
						node-key="id"
						:check-strictly=true
						:props="{children: 'children',label: 'title'}">
				</el-tree>
			</el-form-item>

			<el-form-item>
				<el-button @click="closeDialog">关 闭</el-button>
				<el-button type="primary" @click="submitHandle">确 定</el-button>
			</el-form-item>
		</el-form>
	</el-drawer>
</template>
