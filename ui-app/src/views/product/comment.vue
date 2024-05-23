<script setup  name="Comment">

	import {ref, reactive} from 'vue'
	import {useRouter, useRoute} from 'vue-router'
	import {ProductComment} from '@/api/comment'

	const router = useRouter()
	const route = useRoute()

	const data = reactive({
		comments: [],

		finished: false,
		loading: false,
		current: 1,
		total: 0
	})

	const productId = route.query.id
	const initData = () => {
		data.loading = true

		ProductComment(productId, data.current).then(res => {
			data.comments.push(...res.data.records)

			data.loading = false
			if (data.comments.length === res.data.total) {
				data.finished = true
			}
			data.current++
		})
	}

	import { showImagePreview } from 'vant'
	const previewImg = (comment, index) => {
		showImagePreview({
			images: comment.images.split(';'),
			startPosition: index
		});
	}
</script>

<template>
	<van-nav-bar title="商品评价" left-text="返回" left-arrow @click-left="$router.back()"/>

	<van-list
			v-model:loading="data.loading"
			:finished="data.finished"
			finished-text="没有更多了"
			@load="initData"
	>
		<div class="comment_wrap" v-for="(item,index) in data.comments" :key="index">
			<div class="comment_top">
				<div class="comment_top_left">
					<div class="comment_avatar">
						<img :src="item.userAvatar" alt="">
					</div>
					<div class="comment_person">
						<div class="comment_name">{{item.username}}</div>
						<div class="comment_date">{{item.created}}</div>
					</div>
				</div>
			</div>
			<p class="comment_content">{{item.content}}</p>
			<van-grid :border="false" :column-num="3" v-if="item.images">
				<van-grid-item v-for="(img, index) in item.images.split(';')">
						<van-image
								width="100"
								height="100"
								style="margin: 5px"
								:src="img"
								@click="previewImg(item, index)"
						/>
				</van-grid-item>
			</van-grid>
		</div>
	</van-list>

</template>



<style>
	.tag_wrap {
		padding: 10px 20px;
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		background-color: #fff;
		margin-bottom: 10px;
	}

	.tag {
		margin-right: 10px;
		padding: 6px 10px;
	}

	/*  */
	.comment_wrap {
		background-color: #fff;
		padding: 0 16px;
		margin: 10px 0;
		border-bottom: 1px solid #f7f7f7;
	}

	.comment_top {
		display: flex;
		justify-content: space-between;
		padding: 10px 0;
	}

	.comment_top_left {
		display: flex;
		justify-content: space-between;
	}

	.comment_avatar {
		width: 50px;
		height: 50px;
		border-radius: 25px;
		margin-right: 16px;
	}

	.comment_avatar img {
		width: 100%;
		height: 100%;
		border-radius: 25px;
	}

	.comment_name {
		line-height: 28px;
	}

	.comment_date {
		color: #969799;
	}

	.comment_content {
		margin: 5px 0;
	}
</style>
