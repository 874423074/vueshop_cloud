<script setup>

	import {GetHome} from '^/index'
	import {ref, reactive} from 'vue'
	import ProductGrid from '~/ProductGrid.vue'

	const data = reactive({
		categories: [],
		carousels: [],
		products: []
	})

	const initData = () => {
		GetHome().then(res => {
			data.categories = res.data.categories
			data.carousels = res.data.carousels
			data.products = res.data.products

		})
	}
	initData()


</script>

<template>
	<div class="container">

		<div class="white_bg">
			<!--搜索-->
			<van-search placeholder="请输入搜索关键词" disabled @click="$router.push('/search')"/>

			<!--轮播图-->
			<van-swipe class="my-swipe" :autoplay="3000" indicator-color="white">
				<van-swipe-item v-for="item in data.carousels">
					<router-link :to="item.url"><img :src="item.image"></router-link>
				</van-swipe-item>
			</van-swipe>

			<div class="index_info_wrap"><span class="index_info">公众号:MarkerHub</span><span
					class="index_info">·自由品牌</span><span class="index_info">·30天无忧退货</span></div>

			<!-- 分类 -->
			<div class="categary_wrap">
				<div class="categary_status">
					<div class="categary_status_item" v-for="item in data.categories" @click="$router.push(`/category/${item.id}`)">
						<div class="categary_status_img"><img :src="item.iconUrl" alt=""></div>
						<div class="categary_status_text">{{item.name}}</div>
					</div>
				</div>
			</div>
		</div>

		<!--商品列表-->
		<ProductGrid :products="data.products"></ProductGrid>


	</div>
</template>

<style scoped>
	.container {
		/*background-color: #f1f1f1;*/
		font-size: 14px;
	}

	.white_bg {
		background-color: #fff;
	}

	/* search */
	.search_wrap {
		width: 96%;
		margin: 0px auto 20px;
		height: 40px;
		border-radius: 16px;
		background-color: rgb(193, 188, 236);
	}

	/* swipe */
	.swipe_wrap {
		width: 90%;
		margin: auto;
	}

	.my-swipe .van-swipe-item {
		color: #fff;
		/*border-radius: 16px;*/
		font-size: 20px;
		height: 170px;
		text-align: center;
		background-color: rgb(110, 98, 198);
		/*border-radius: 10px;*/
	}

	.my-swipe .van-swipe-item img {
		width: 100%;
		height: 170px;
		/*border-radius: 10px;*/
	}

	.flex {
		display: flex;
		flex-direction: row;
	}

	.index_info_wrap {
		padding: 16px 20px;
	}

	.index_info {
		font-size: 14px;
		padding: 10px 6px;
		color: #666666;
	}

	/*  */
	.index_noteice_img {
		width: 90%;
		height: 80px;
		border-radius: 40px;
		margin: auto;
		background-color: rgb(110, 98, 198);
	}

	.index_noteice_img img {
		width: 100%;
		height: 100%;
		border-radius: 40px;
	}

	/*  */
	.categary_wrap {
		background-color: white;
		padding-bottom: 20px;
	}

	.categary {
		padding: 16px 20px;
		display: flex;
		border-bottom: 1px solid #f9f9f9;
		justify-content: space-between;
	}

	.categary_text {
		font-size: 16px;
	}

	.categary_all {
		font-size: 16px;
		color: #888888;
	}

	.categary_status {
		width: 100%;
		display: flex;
		display: flex;
		flex-wrap: wrap;
	}

	.categary_status_item {
		width: 20%;
		padding: 10px 0;
		box-sizing: border-box;
	}

	.categary_status_img {
		width: 35px;
		height: 35px;
		margin: auto;
	}

	.categary_status_img img {
		width: 100%;
		height: 100%;
	}

	.categary_status_text {
		font-size: 14px;
		padding: 10px;
		text-align: center;
	}

	.van-field__control {
		font-size: 16px;
	}


</style>
