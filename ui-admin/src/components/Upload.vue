<script setup>
	import {defineProps, toRefs, reactive, ref} from "vue"

	import {useStore} from 'vuex'
	import {ElMessage} from "element-plus"

	const store = useStore()

	const props = defineProps({
		pics: Array,
		limit: {type: Number, default: 9}
	})
	const {pics, limit} = toRefs(props)

	const viewData = reactive({
		action: import.meta.env.VITE_APP_BASE_API + "/p/admin/upload",
		headers: {
			Authorization: store.getters.token
		},
		dialogVisible: false,
		dialogImageUrl: ''
	})

	const handleSuccess = (res, uploadFile, uploadFiles) => {
		uploadFile.url = res.data[0]
	}

	const handlePreview = (uploadFile) => {
		viewData.dialogImageUrl = uploadFile.url
		viewData.dialogVisible = true
	}
	const handleExceed = (files, uploadFiles) => {
		ElMessage({type: 'error', message: '超出数量或大小限制！'})
	}

</script>

<template>
	<el-upload
			v-model:file-list="pics"
			:action=viewData.action
			:headers=viewData.headers
			name="pics"
			multiple
			list-type="picture-card"
			:on-preview="handlePreview"
			:on-success="handleSuccess"
			:on-exceed="handleExceed"
			:limit="limit"
	>
		<el-icon>
			<Plus/>
		</el-icon>
	</el-upload>

	<el-dialog v-model="viewData.dialogVisible">
		<img w-full :src="viewData.dialogImageUrl" alt="Preview Image"/>
	</el-dialog>
</template>

<style scoped>

</style>