<script setup>
	import ProductGrid from '~/ProductGrid.vue'

	import {ref, reactive} from 'vue'
	import {Search} from '^/index.js'

	import {useRoute} from 'vue-router'
	const route = useRoute()

	const data = reactive({
		keyword: '',
		products: [],
		histories: [],

		current: 1,
		loading: false,
		finished: false
	})

	const initData = () => {
		if (!data.keyword.trim()) {
			data.loading = false
			data.keyword = ''
			return
		}

		Search(data.keyword, data.current).then(res => {
			data.products.push(...res.data.records)

			data.loading = false

			if (res.data.total === data.products.length) {
				data.finished = true
			}

			data.current ++
		});
	}

	const handleSearch = () => {
		data.current = 1
		data.products = []
		data.finished = false

		setHistory(data.keyword)

		initData()
	}

	const setHistory = (word) => {
		if (word.trim() && data.histories.indexOf(word.trim()) < 0) {
			data.histories.unshift(word.trim())
			localStorage.setItem("keywords", data.histories.join("|"))
		}
	}
	const clearHistory = () => {
		data.histories = []
		localStorage.setItem("keywords", "")
	}
	const clickHistory = (word) => {
		data.keyword = word
		handleSearch()
	}

	data.histories = (localStorage.getItem("keywords") || "").split('|')

</script>

<template>
	<van-nav-bar title="商品搜索" left-text="返回" left-arrow @click-left="$router.back()"/>

	<div class="item_search">
		<form action="/search">
			<van-search placeholder="请输入商品名称" v-model="data.keyword" @search="handleSearch" autofocus/>
		</form>
		<div class="item_search_content">
			<div class="item_search_text clearfix">
				<div class="float-l">历史搜索</div>
				<div class="float-r" @click="clearHistory">清空历史记录</div>
			</div>

			<div class="item_search_history">
				<van-tag color="#c8c9cc" text-color="white" v-for="item in data.histories" @click="clickHistory(item)">
					{{item}}
				</van-tag>
			</div>
		</div>

		<!--商品列表-->
		<van-list
				v-model:loading="data.loading"
				:finished="data.finished"
				finished-text="没有更多了"
				@load="initData"
		>
			<!--商品列表-->
			<ProductGrid :products="data.products"></ProductGrid>

		</van-list>

		<div v-show="!data.products">
			<van-empty description="抱歉,没有找到符合条件商品"/>
		</div>

	</div>
</template>

<style lang="scss" scoped>
	.item_search_content {
		padding: 15px 10px;
		background-color: #fff;
	}

	.item_search_text {
		margin-bottom: 10px;
	}

	.item_search_history > span {
		margin-right: 10px;
	}

	.product_wrap {
		margin-top: 8px;
	}

	.product_title {
		font-size: 14px;
		line-height: 30px;
		font-weight: 500;
		width: 100%;
	}

	.product_price {
		font-size: 20px;
		color: rgb(217, 61, 61);
		width: 100%;
		padding: 10px 0;
	}
</style>
