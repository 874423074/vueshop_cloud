<script setup>
	import {reactive, toRefs, ref, defineProps, defineEmits, defineExpose} from 'vue'
	import {ElMessage} from "element-plus"
	import {GetInfo, Add} from '^/user'


	const props = defineProps({
		showDialog: Boolean
	})
	const {showDialog} = toRefs(props)

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
		username: [
			{required: true, message: '请输入用户名称', trigger: 'blur'}
		],
		email: [
			{required: true, message: '请输入邮箱', trigger: 'blur'}
		],
		status: [
			{required: true, message: '请选择状态', trigger: 'blur'}
		]
	})
</script>

<template>
	<!--新增对话框-->
	<el-drawer
			title="编辑"
			width="600px"
			v-model="showDialog"
			:before-close="closeDialog"
	>

		<el-form :model="editForm" :rules="rules" ref="ruleForm" label-width="100px"
		         class="demo-editForm">
			<el-input v-model="editForm.id" autocomplete="off" type="hidden"></el-input>

			<el-form-item label="用户名" prop="username" label-width="100px">
				<el-input v-model="editForm.username" autocomplete="off"></el-input>
				<el-alert
						title="不填密码则初始密码为888888"
						:closable="false"
						type="info"
						style="line-height: 12px;"
				></el-alert>
			</el-form-item>
			<el-form-item label="密码" prop="password" label-width="100px">
				<el-input v-model="editForm.password" autocomplete="off" type="password"></el-input>
			</el-form-item>
			<el-form-item label="邮箱" prop="email" label-width="100px">
				<el-input v-model="editForm.email" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="手机号" prop="phone" label-width="100px">
				<el-input v-model="editForm.phone" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item label="状态" prop="status" label-width="100px">
				<el-radio-group v-model="editForm.status">
					<el-radio :label="1">禁用</el-radio>
					<el-radio :label="0">正常</el-radio>
				</el-radio-group>
			</el-form-item>

			<el-form-item>
				<el-button type="primary" @click="submitForm">立即创建</el-button>
				<el-button @click="closeDialog">关闭</el-button>
			</el-form-item>
		</el-form>

	</el-drawer>
</template>

