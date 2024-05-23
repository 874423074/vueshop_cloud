<script setup>

	import {ref, reactive} from 'vue'
	import {useRouter, useRoute} from "vue-router"
	import {ListAddress} from '^/address.js'

	const router = useRouter()
	const route = useRoute()

	const data = reactive({
		addresses: [],
		chosenAddressId: 0
	})

	const initData = () => {
		ListAddress().then(res => {
			data.addresses = res.data
		})

		data.chosenAddressId = ~~route.query.addressId
	}
	initData()

	const handleAdd = () => {
		router.push({
			name: 'EditAddress',
			query: {...route.query}
		})
	}

	const handleEdit = (item, index) => {
		router.push({
			name: 'EditAddress',
			query: {...route.query, id: item.id}
		})
	}

	const handleSelect = (item, index) => {

		if (Object.keys(route.query).length === 0) {
			// 地址管理
			return
		}

		router.push({
			name: 'OrderPreview',
			query: {...route.query, addressId: item.id}
		})
	}
</script>

<template>
	<van-nav-bar title="选择地址" left-text="返回" left-arrow @click-left="$router.go(-1)"/>

	<van-address-list
			v-model="data.chosenAddressId"
			:list="data.addresses"
			default-tag-text="默认"
			@add="handleAdd"
			@edit="handleEdit"
			@select="handleSelect"
	/>

	<van-empty v-if="data.addresses.length===0" description="还没地址，请新添加"/>

</template>

<style scoped lang="scss">
	.my-swipe .van-swipe-item {
	}

	.product_item {
		display: block;
	}
</style>
