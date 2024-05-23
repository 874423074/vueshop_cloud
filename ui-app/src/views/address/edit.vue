<script setup name="CommentEdit">

	import {ref, reactive} from 'vue'
	import {useRouter, useRoute} from "vue-router"
	import {GetAddress, DeleteAddress, AddAddress} from '^/address.js'
	import {areaList} from '@vant/area-data'
	import {showConfirmDialog, showNotify} from 'vant'


	const router = useRouter()
	const route = useRoute()

	const address = ref({})

	const addressId = route.query.id
	const initData = () => {
		if (addressId) {
			GetAddress(addressId).then(res => {
				address.value = res.data
			})
		}
	}
	initData()

	const handleSave = (info) => {
		AddAddress(info).then(res => {
			showNotify({type: 'primary', message: '操作成功'})

			router.back()
		})
	}

	const handleDelete = () => {
		showConfirmDialog({
			message: '确认删除该地址吗？',
		}).then(() => {
			DeleteAddress(addressId).then(res => {
				showNotify({type: 'primary', message: '删除成功'})

				router.back()
			})
		})
	}

</script>

<template>
	<van-nav-bar title="编辑地址" left-text="返回" left-arrow @click-left="$router.back()"/>

	<van-address-edit
			:address-info="address"
			:area-list="areaList"
			show-postal
			show-delete
			show-set-default
			show-search-result
			:area-columns-placeholder="['请选择', '请选择', '请选择']"
			@save="handleSave"
			@delete="handleDelete"
	/>

</template>