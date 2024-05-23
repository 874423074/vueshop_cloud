<script setup>
	import ProductGrid from '~/ProductGrid.vue'

	import {ref, reactive} from 'vue'
	import {GetCategory} from '^/product.js'

	import {useRoute} from 'vue-router'
	const route = useRoute()

	const data = reactive({
		tabs: [
			{title: '默认排序', nav: 0},
			{title: '最新上市', nav: 1},
			{title: '销量最多', nav: 2},
		],

		category: {},
		products: [],

		activeTab: 0,
		current: 1,

		loading: false,
		finished: false
	})

	const categoryId = route.params.categoryId
	const initData = () => {
		GetCategory(categoryId, data.current, data.activeTab).then(res => {
			data.products.push(...res.data.pageData.records)
			data.category = res.data.category

			data.loading = false

			if (res.data.pageData.total === data.products.length) {
				data.finished = true
			}

			data.current ++

		})
	}
	// initData()

	const changeTab = (name) => {
		data.activeTab = name
		data.current = 1
		data.products = []

		data.finished = false

		// initData()
	}

</script>

<template>
	<van-nav-bar :title="data.category.name" left-text="返回" left-arrow @click-left="$router.back()"/>

	<van-tabs :active="data.activeTab" :before-change="changeTab">
		<van-tab :title="item.title" :name="item.nav" v-for="item in data.tabs">

			<van-list
					v-model:loading="data.loading"
					:finished="data.finished"
					finished-text="没有更多了"
					@load="initData"
			>
				<!--商品列表-->
				<ProductGrid :products="data.products"></ProductGrid>

			</van-list>
		</van-tab>
	</van-tabs>
</template>
