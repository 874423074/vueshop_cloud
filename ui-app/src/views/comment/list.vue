<script setup name="CommentList">

	import {ref, reactive} from 'vue'
	import {ListComment, PostComment} from '@/api/comment.js'
	import {useRouter, useRoute} from "vue-router"
	import OrderBtns from '~/orderBtns.vue'

	const router = useRouter()
	const route = useRoute()

	const data = reactive({
		tabs: [
			{name: '0', title: '未评价'},
			{name: '1', title: '已评价'}
		],

		finished: false,
		loading: false,

		orderItems: [],
		activeTab: 0,
		status: 0,
		current: 1
	})

	data.status = data.activeTab = route.query.status
	const initData = () => {

		data.loading = true
		ListComment(data.status, data.current).then(res => {

			data.orderItems.push(...res.data.records)
			data.current = res.data.current

			data.loading = false
			if (data.orderItems.length === res.data.total) {
				data.finished = true
			}
			data.current++
		})
	}

	const changeTab = (index) => {

		data.status = index
		data.activeTab = index
		data.current = 1
		data.orderItems = []
		data.finished = false

		router.replace(`/comment/list?status=` + index);
	}

</script>

<template>
	<van-nav-bar title="商品评论" left-text="返回" left-arrow @click-left="$router.go(-1)"/>

	<van-tabs :active="data.activeTab" :before-change="changeTab">
		<van-tab v-for="tab in data.tabs" :title="tab.title" :name="tab.name">

			<van-list
					:loading="data.loading"
					:finished="data.finished"
					finished-text="没有更多了"
					@load="initData"
			>

				<div style="background-color: white;" v-for="item in data.orderItems">

					<van-cell-group style="margin-top: 8px;" border>

						<van-card
								:title="item.productName"
								:thumb="item.productImage"
								@click-thumb="$router.push('/product/' + item.productId)"
								:price="item.price"
								:num="item.quantity"
						>
							<template #tags v-if="item.sku">
								<van-tag color="#c8c9cc" text-color="white" style="margin: 6px 6px 0 0;"
								         v-for="spec in item.sku.split(';')">
									{{spec}}
								</van-tag>
							</template>

							<template #footer>
								<van-button v-if="!item.commentId" type="primary" size="small"  @click.stop="$router.push('/comment/edit?orderItemId=' + item.id)">立即评论</van-button>
								<van-button v-if="item.commentId" type="primary" size="small"  @click.stop="$router.push('/comment/info?id=' + item.commentId)">查看评论</van-button>
							</template>
						</van-card>

					</van-cell-group>
				</div>

			</van-list>

		</van-tab>
	</van-tabs>
</template>

<style lang="scss" scoped>
	.total {
		font-size: 14px;
		text-align: right;
		padding: 10px;
	}

	.footer_btn {
		text-align: right;
		padding: 8px;

		.van-button {
			margin-left: 10px;
		}
	}
</style>
