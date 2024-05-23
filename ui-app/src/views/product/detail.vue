<script setup>

	import Sku from '~/Sku.vue'
	import {ref, reactive} from 'vue'
	import {GetProduct} from '^/product.js'
	import {GetCartCount} from '^/cart.js'
	import {useRoute} from 'vue-router'

	const route = useRoute()

	const data = reactive({
		product: {},
		pics: [],

		cartCount: '',

		showSku: false,
		handle: ''
	})

	const productId = route.params.productId
	const initData = () => {
		GetProduct(productId).then(res => {
			data.product = res.data

			if (data.product.pics) {
				data.pics = data.product.pics.split(';')
			}
		})
	}
	initData()

	const getCartCount = () => {
		GetCartCount().then(res => {
			data.cartCount = res.data
		})
	}
	getCartCount()

	const handleAction = action => {
		data.handle = action
		data.showSku = true
	}
	const closeSkuShow = () => {
		data.showSku = false
		getCartCount()
	}

</script>

<template>
	<van-nav-bar :title="data.product.name" left-text="返回" left-arrow @click-left="$router.back()"/>

	<!--轮播图-->
	<van-swipe class="my-swipe" :autoplay="3000" indicator-color="#39a9ed">
		<van-swipe-item v-for="item in data.pics">
			<img :src="item">
		</van-swipe-item>
	</van-swipe>

	<div>
		<div class="pd20">

			<!--价格、产品名称、摘要-->
			<div class="flex between">
				<div class="product_price">￥ {{data.product.price}}</div>
				<div class="product_sale">销量 {{data.product.sale}}</div>
			</div>
			<div class="product_title">{{data.product.name}}</div>
		</div>

		<!--评价-->
		<van-cell class="product_text_wrap" :title="'用户评价（ ' + data.product.comments + ' ）'" is-link :to="'/product/comment?id=' + data.product.id"/>

		<!--详情-->
		<div class="product_img_wrap">
			<v-md-editor :model-value="data.product.detail" mode="preview"></v-md-editor>
		</div>

		<!--加入购物车-->
		<van-action-bar>
			<van-action-bar-icon icon="chat-o" text="客服" color="#ee0a24"/>
			<van-action-bar-icon icon="cart-o" text="购物车" :badge="data.cartCount" @click="$router.push('/cart')"/>
			<van-action-bar-button type="warning" text="加入购物车" @click="handleAction('cart')"/>
			<van-action-bar-button type="danger" text="立即购买" @click="handleAction('order')"/>
		</van-action-bar>

	</div>

	<van-action-sheet v-model:show="data.showSku" title="规格选择">
		<Sku :product="data.product" :handle="data.handle" @closeSkuShow="closeSkuShow"></Sku>
	</van-action-sheet>

</template>

<style lang="scss" scoped>
	.container {
		background-color: #f1f1f1;
	}

	.my-swipe .van-swipe-item {
		color: #fff;
		font-size: 20px;
		height: 360px;
		text-align: center;
		width: 100%;
	}

	.my-swipe .van-swipe-item img {
		width: 100%;
		height: 100%;
	}

	.pd20 {
		padding: 20px 20px 10px 20px;
		background-color: #fff;
	}

	.flex {
		display: flex;
	}

	.between {
		justify-content: space-between;
	}

	.product_price {
		color: rgb(234, 19, 19);
		font-weight: bold;
		font-size: 21px;
	}

	.product_sale {
		font-size: 14px;
		color: #888;
	}

	.product_title {
		text-align: left;
		font-size: 16px;
		font-weight: 500;
		padding: 5px 0;
	}

	.product_text_wrap {
		padding: 15px 20px;
		margin: 10px 0;
		color: #444444;
		background-color: #fff;
	}

	.product_text_wrap span {
		font-size: 14px;
		color: #444444;
		padding-right: 10px;
	}

	/* 推荐 */
	.product_recommand {
		background-color: #fff;
		padding: 10px;
	}

	.product_recommand_title {
		font-size: 16px;
		font-weight: 500;
		text-align: left;
		padding: 2px 0;
	}

	.product_recommand_list {
		display: flex;
		text-align: left;
	}

	.product_recommand_card {
		width: 33%;
	}

	.product_recommand_img {
		margin: 12px 3px;
		height: 110px;
		border-radius: 10px;
		background-color: rgb(110, 98, 198);
	}

	.product_recommand_img img {
		width: 100%;
		height: 100%;
	}

	.product_recommand_text {
		font-size: 15px;
		font-weight: 500;
	}

	.product_recommand_price {
		color: rgb(234, 19, 19);
		font-size: 18px;
		padding: 5px 0;
		font-weight: 500;
	}

	.price {
		font-size: 16px;
		font-weight: 500;
	}

	/* 详情图片 */
	.product_img_wrap {
		margin-bottom: 20px;
		color: white;
	}

	.van-nav-bar__text {
		font-size: 15px;
		font-weight: 500;
	}

	/*  */
	.van-field__control,
	.van-button__text,
	.van-tab__text--ellipsis {
		font-size: 16px;
	}

	.van-coupon-list__field {
		margin-top: 10px;
	}
</style>
