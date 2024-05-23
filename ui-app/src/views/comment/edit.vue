<script setup>

	import {ref} from 'vue'
	import {PostComment} from '@/api/comment.js'
	import {useRoute, useRouter} from "vue-router"
	import {showNotify} from "vant";

	const route = useRoute()
	const router = useRouter()

	const comment = ref({
		score: 5,
		orderItemId: null,
		content: "",
		images: []
	})

	comment.value.orderItemId = route.query.orderItemId
	const submitHandle = () => {
		let formData = new FormData();
		for (const key in comment.value) {
			formData.append(key, comment.value[key])
		}

		if (comment.value.images.length > 0) {
			for (let index = 0; index < comment.value.images.length; index++) {
				const pic = comment.value.images[index]
				//element.file取的是File文件对象
				formData.append("pics", pic.file);
			}
		}

		PostComment(formData).then(res => {
			showNotify({type: 'primary', message: '评论成功'})

			router.replace('/comment/info?id=' + res.data)
		})
	}

</script>
<template>
	<van-nav-bar title="商品评论" left-text="返回" left-arrow @click-left="$router.go(-1)"/>

	<van-form @submit="submitHandle" style="margin-top: 20px;">
		<van-cell-group inset>
			<van-field name="rate" label="评分">
				<template #input>
					<van-rate v-model="comment.score"/>
				</template>
			</van-field>
			<van-field
					name="content"
					v-model="comment.content"
					rows="3"
					autosize
					label="评论"
					type="textarea"
					maxlength="50"
					placeholder="请输入评论内容"
					show-word-limit
			/>
			<van-field name="pics" label="图片上传">
				<template #input>
					<van-uploader v-model="comment.images" multiple :max-count="4"/>
				</template>
			</van-field>
		</van-cell-group>
		<div style="margin: 16px;">
			<van-button round block type="primary" native-type="submit">
				提交
			</van-button>
		</div>
	</van-form>

</template>