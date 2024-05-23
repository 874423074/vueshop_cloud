<script setup>
	import {reactive, toRefs, ref, defineProps, defineEmits} from 'vue'
	import {GetInfo, Add} from '^/category'
	import {ElMessage} from "element-plus"
	import Upload from '~/Upload.vue'

	const props = defineProps({
		showDialog: Boolean
	})
	const {showDialog} = toRefs(props)

	const emits = defineEmits(['closeHandle'])
	const closeDialog = (hasChange) => {
		emits('closeHandle', hasChange)
	}

	const rules = reactive({
		name: [
			{required: true, message: '请输入分类名称', trigger: 'blur'}
		]
	})

	const editForm = ref({})
	const pics = ref([])
	const init = (id) => {
		pics.value = []
		if (id) {
			GetInfo(id).then(res => {
				editForm.value = res.data
				pics.value.push({
					name: '',
					url: res.data.iconUrl
				})
			})
		} else {
			editForm.value = {
				sortOrder: 1,
				isShow: true
			}
		}

	}
	defineExpose({init})

	const ruleForm = ref(null)
	const submitForm = () => {
		if (pics.value.length === 0) {
			ElMessage.error("请上传图标")
		} else {
			editForm.value.iconUrl = pics.value[0].url
			ruleForm.value.validate((vaild) => {
				if (vaild) {
					Add(editForm.value).then(res => {
						ElMessage({type: 'success', message: '操作成功！'})
						closeDialog(true)
					})
				}
			})
		}
	}
</script>
<template>
	<!--新增对话框-->
	<el-drawer
			title="编辑"
			v-model="showDialog"
			width="600px"
			:before-close="closeDialog"
	>

		<el-form :model="editForm" :rules="rules" ref="ruleForm" label-width="100px"
		         class="demo-editForm">
			<el-input v-model="editForm.id" autocomplete="off" type="hidden"></el-input>

			<el-form-item label="名称" prop="name" label-width="100px">
				<el-input v-model="editForm.name" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item label="图标" prop="iconUrl" label-width="100px">

				<Upload :pics="pics" :limit="1"></Upload>

			</el-form-item>

			<el-form-item label="描述" prop="remark" label-width="100px">
				<el-input v-model="editForm.remark" autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="是否展示" prop="isShow" label-width="100px">
				<el-switch v-model="editForm.isShow"/>
			</el-form-item>
			<el-form-item label="排序" prop="sortOrder" label-width="100px">
				<el-input-number v-model="editForm.sortOrder" :min="1"/>
			</el-form-item>

			<el-form-item>
				<el-button type="primary" @click="submitForm">立即创建</el-button>
				<el-button @click="closeDialog">关闭</el-button>
			</el-form-item>
		</el-form>

	</el-drawer>
</template>

