<script setup>
	import {reactive, toRefs, ref, nextTick, defineProps, defineEmits} from 'vue'
	import {GetInfo, Add} from '^/spec'
	import {ElMessage} from "element-plus"

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
			{required: true, message: '请输入规格', trigger: 'blur'}
		]
	})

	let dynamicTags = ref([])
	const editForm = ref({})
	const init = (id) => {

		dynamicTags.value = []
		if (id) {
			GetInfo(id).then(res => {
				editForm.value = res.data
				dynamicTags.value.push(...res.data.options.split(';'))
			})
		} else {
			editForm.value = {
				sortOrder: 1
			}
		}

	}
	defineExpose({init})

	const ruleForm = ref(null)
	const submitForm = () => {
		ruleForm.value.validate((vaild) => {
			if (vaild) {
				editForm.value.options = dynamicTags.value.join(';')
				Add(editForm.value).then(res => {
					ElMessage({type: 'success', message: '操作成功！'})
					closeDialog(true)
				})
			}
		})
	}

	const inputValue = ref('')
	const inputVisible = ref(false)
	const InputRef = ref()


	const showInput = () => {
		inputVisible.value = true
		nextTick(() => {
			InputRef.value.input.focus()
		})
	}

	const handleInputConfirm = () => {
		if (inputValue.value) {
			dynamicTags.value.push(inputValue.value)
		}
		inputVisible.value = false
		inputValue.value = ''
	}

	const handleClose = (tag) => {
		dynamicTags.value.splice(dynamicTags.value.indexOf(tag), 1)
	}

</script>

<template>
	<!--新增对话框-->
	<el-drawer
			title="编辑"
			v-model="showDialog"
			:before-close="closeDialog"
			width="600px"
	>

		<el-form :model="editForm" :rules="rules" ref="ruleForm" label-width="100px"
		         class="demo-editForm">
			<el-input v-model="editForm.id" autocomplete="off" type="hidden"></el-input>

			<el-form-item label="规格" prop="name" label-width="100px">
				<el-input v-model="editForm.name" autocomplete="off"></el-input>
			</el-form-item>

			<el-form-item label="可选值" label-width="100px">

				<el-tag
						v-for="tag in dynamicTags"
						:key="tag"
						class="mx-1"
						closable
						:disable-transitions="false"
						@close="handleClose(tag)"
						style="margin-right: 5px;"
				>
					{{ tag }}
				</el-tag>

				<el-input
						v-if="inputVisible"
						ref="InputRef"
						v-model="inputValue"
						class="ml-1 w-20"
						size="small"
						@keyup.enter="handleInputConfirm"
						@blur="handleInputConfirm"
				/>
				<el-button v-else class="button-new-tag ml-1" size="small" @click="showInput">
					添加
				</el-button>

			</el-form-item>
			<el-form-item label="排序" prop="sortOrder" label-width="100px">
				<el-input-number v-model="editForm.sortOrder" :min="1" :max="99"/>
			</el-form-item>

			<el-form-item>
				<el-button type="primary" @click="submitForm">立即创建</el-button>
				<el-button @click="closeDialog">关闭</el-button>
			</el-form-item>
		</el-form>

	</el-drawer>
</template>

<style lang="scss" scoped>
	.avatar-uploader .avatar {
		width: 50px;
		height: 50px;
		display: block;
	}

	.el-icon.avatar-uploader-icon {
		border: 1px dashed var(--el-border-color);
		border-radius: 6px;

		width: 50px;
		height: 50px;
		text-align: center;
		background-color: var(--el-fill-color-lighter);
	}
</style>

