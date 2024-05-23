<script setup name="Mine">

	import {ref} from "vue"
	import {useRouter} from "vue-router"
	import {useStore} from 'vuex'
	import {showConfirmDialog} from 'vant'
	import {CountOrder} from '^/order.js'

	const router = useRouter()
	const store = useStore()

	const userInfo = ref(store.getters.userInfo)
	const logout = () => {
		showConfirmDialog({
			message: '确认退出登录吗？',
		}).then(() => {
			store.dispatch('logout').then(res => {
				router.push('/')
			})
		})
	}

	const count = ref({})
	const initData = () => {
		CountOrder().then(res => {
			count.value = res.data
		})
	}
	initData()

	let showMarkerHub = ref(false)

</script>

<template>
	<div>
		<!--  -->
		<div class="mine_header" @click="logout">
			<div class="mine_header_img">
				<img :src="userInfo.avatar">
			</div>
			<div>
				<div class="mine_header_number ">{{userInfo.username}}</div>
				<div class="mine_header_text">欢迎你，购物愉快！</div>
			</div>
		</div>

		<div class="mine_order_wrap">
			<van-cell title="我的订单" is-link value="全部订单" to="/order/list"/>
			<van-grid clickable :gutter="0" :border="false">
				<van-grid-item icon="pending-payment" text="待付款" to="/order/list?status=0"
				               :badge="count.unPay === 0 ? '' : count.unPay"/>
				<van-grid-item icon="debit-pay" text="待发货" to="/order/list?status=1"
				               :badge="count.unDeli === 0 ? '' : count.unDeli"/>
				<van-grid-item icon="logistics" text="待收货" to="/order/list?status=2"
				               :badge="count.unRecv === 0 ? '' : count.unRecv"/>
				<van-grid-item icon="chat-o" text="待评论" to="/comment/list?status=0"
				               :badge="count.unComment === 0 ? '' : count.unComment"/>
				<van-grid-item icon="refund-o" text="退款中" to="/order/list?status=5"
				               :badge="count.unRefund === 0 ? '' : count.unRefund"/>
			</van-grid>
		</div>

		<div>
			<van-cell title="地址管理" is-link to="/address/list"/>
			<van-cell title="关注公众号" is-link @click="showMarkerHub = true"/>
		</div>
		<van-overlay :show="showMarkerHub" @click="showMarkerHub = false">
			<div class="wrapper">
				<img src="@/assets/MarkerHub.jpg">
			</div>
		</van-overlay>
	</div>

</template>

<style>

	.flex {
		display: flex;
	}

	.mine_header {
		display: flex;
		background-color: #fff;
		padding: 20px;
		margin-bottom: 10px;
	}

	.mine_header_img {
		width: 50px;
		height: 50px;
		border-radius: 25px;
		background-color: #1baeae;
		margin-right: 16px;
	}

	.mine_header_img img {
		width: 100%;
		border-radius: 25px;
	}

	.mine_header_number {
		font-size: 18px;
		text-align: left;
		line-height: 30px;
	}

	.mine_header_text {
		/*font-size: 15px;*/
		text-align: left;
		color: #969799;
	}

	/*  */
	.mine_order_wrap {
		background-color: white;
		margin-bottom: 10px;
	}

	/*  */
	.van-cell__title span {
		font-size: 15px;
		font-weight: 500;
	}

	.wrapper {
		display: flex;
		align-items: center;
		justify-content: center;
		height: 100%;
	}

	.wrapper img {
		width: 220px;
	}

</style>
