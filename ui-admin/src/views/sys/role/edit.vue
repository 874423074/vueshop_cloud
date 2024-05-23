<script setup>
	import {reactive, toRefs, ref, defineProps, defineEmits, defineExpose} from 'vue'
	import {GetInfo, Add} from '^/role'
	import {ElMessage} from "element-plus"

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
		name: [
			{required: true, message: '请输入角色名称', trigger: 'blur'}
		],
		code: [
			{required: true, message: '请输入唯一编码', trigger: 'blur'}
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

			<el-form-item label="角色名称" prop="name" label-width="100px">
				<el-input v-model="editForm.name" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item label="唯一编码" prop="code" label-width="100px">
				<el-input v-model="editForm.code" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item label="描述" prop="remark" label-width="100px">
				<el-input v-model="editForm.remark" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item>
				<el-button type="primary" @click="submitForm">立即创建</el-button>
				<el-button @click="emits('closeHandle')">关闭</el-button>
			</el-form-item>
		</el-form>

	</el-drawer>
</template>
