<script setup>
	import {reactive, ref, toRefs, defineProps, defineEmits, defineExpose} from 'vue'
	import {GetInfo, Add} from '^/menu'
	import {ElMessage} from 'element-plus'

	const props = defineProps({
		showDialog: Boolean,
		tableData: Object
	})
	const {showDialog, tableData} = toRefs(props)

	const emits = defineEmits(['closeHandle'])
	const closeDialog = (hasChange) => {
		emits('closeHandle', hasChange)
	}

	const editForm = ref({})
	const init = (id) => {
		if (id) {
			GetInfo(id).then(res => {
				editForm.value = res.data
			})
		} else {
			editForm.value = {}
		}
	}
	defineExpose({init})

	const ruleForm = ref()
	const submitForm = () => {
		ruleForm.value.validate((valid) => {
			if (valid) {
				Add(editForm.value).then(res => {
					ElMessage({message: '操作成功', type: 'success'})
					closeDialog(true)
				})
			}
		})
	}

	const rules = reactive({
		parentId: [
			{required: true, message: '请选择上级菜单', trigger: 'blur'}
		],
		title: [
			{required: true, message: '请输入名称', trigger: 'blur'}
		],
		perms: [
			{required: true, message: '请输入权限编码', trigger: 'blur'}
		],
		type: [
			{required: true, message: '请选择状态', trigger: 'blur'}
		],
		orderNum: [
			{required: true, message: '请填入排序号', trigger: 'blur'}
		],
		status: [
			{required: true, message: '请选择状态', trigger: 'blur'}
		]
	})

</script>

<template>
	<!--新增对话框-->
	<el-drawer
			title="新增/编辑"
			width="600px"
			v-model="showDialog"
			:before-close="closeDialog"
	>

		<el-form :model="editForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-editForm">
			<el-input v-model="editForm.id" autocomplete="off" type="hidden"></el-input>

			<el-form-item label="上级菜单" prop="parentId">
				<el-select v-model="editForm.parentId" placeholder="请选择上级菜单" :teleported="false">

					<el-option label="顶级菜单" :value="0"></el-option>

					<template v-for="item in tableData">
						<el-option :label="item.title" :value="item.id"></el-option>
						<template v-for="it in item.children">
							<el-option :label="it.title" :value="it.id">
								<span>{{ '- ' + it.title}}</span>
							</el-option>
						</template>
					</template>

				</el-select>
			</el-form-item>

			<el-form-item label="菜单名称" prop="name" label-width="100px">
				<el-input v-model="editForm.title" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item label="权限编码" prop="perms" label-width="100px">
				<el-input v-model="editForm.perms" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item label="图标" prop="icon" label-width="100px">
				<el-input v-model="editForm.icon" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="菜单URL" prop="path" label-width="100px">
				<el-input v-model="editForm.path" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item label="菜单组件" prop="component" label-width="100px">
				<el-input v-model="editForm.component" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item label="类型" prop="type" label-width="100px">
				<el-radio-group v-model="editForm.type">
					<el-radio :label=0>目录</el-radio>
					<el-radio :label=1>菜单</el-radio>
					<el-radio :label=2>按钮</el-radio>
				</el-radio-group>
			</el-form-item>

			<el-form-item label="状态" prop="status" label-width="100px">
				<el-radio-group v-model="editForm.status">
					<el-radio :label=0>正常</el-radio>
					<el-radio :label=1>禁用</el-radio>
				</el-radio-group>
			</el-form-item>

			<el-form-item label="排序号" prop="sortOrder" label-width="100px">
				<el-input-number v-model="editForm.sortOrder" :min="1" label="排序号">1</el-input-number>
			</el-form-item>

			<el-form-item>
				<el-button type="primary" @click="submitForm">立即创建</el-button>
				<el-button @click="closeDialog">关闭</el-button>
			</el-form-item>
		</el-form>

	</el-drawer>
</template>
