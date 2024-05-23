<script setup name="CommentInfo">

	import {ref} from 'vue'
	import {useRoute} from "vue-router"
	import {CommentDetail} from '@/api/comment.js'

	const route = useRoute()
	const comment = ref({})
	const commentId = route.query.id

	const initData = () => {
		CommentDetail(commentId).then(res => {
			console.log(res.data)
			comment.value = res.data
		})
	}
	initData()

	import { showImagePreview } from 'vant'
	const previewImg = (index) => {
		showImagePreview({
			images: comment.value.images.split(';'),
			startPosition: index
		});
	}

</script>

<template>
	<van-nav-bar title="评论详情" left-text="返回" left-arrow @click-left="$router.go(-1)"/>

	<van-cell-group style="margin: 20px 0;">
		<van-cell title="评分">
			<van-rate v-model="comment.score" readonly/>
		</van-cell>
		<van-cell title="评论内容" :value="comment.content"/>
		<van-cell v-if="comment.images">
			<van-image
					v-for="(img, index) in comment.images.split(';')"
					width="100"
					height="100"
					style="margin: 5px"
					:src="img"
					@click="previewImg(index)"
			/>
		</van-cell>
	</van-cell-group>

</template>