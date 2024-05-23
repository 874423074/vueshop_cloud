<script setup>

	import {ref, reactive} from 'vue'
	import {useStore} from 'vuex'
	import {useRoute, useRouter} from 'vue-router'
	import {Register, GetCaptcha} from "^/login"
	import { showDialog } from 'vant'

	const store = useStore()
	const route = useRoute()
	const router = useRouter()

	const loginForm = reactive({
		username: 'test',
		password: '888888',
		uuid: '',
		code: ''
	})

	const handleRegister = () => {
		Register(loginForm).then(res => {
			showDialog({
				message: '注册成功，请去登录',
			}).then(() => {
				router.replace('/login')
			});
		})
	}

	const base64Img = ref('')
	const changeImg = () => {
		GetCaptcha().then(res => {
			base64Img.value = res.data.base64Img
			loginForm.uuid = res.data.uuid
		})
	}
	changeImg()

</script>

<template>
	<van-nav-bar
			title="登录"
			left-text="返回"
			left-arrow
			@click-left="$router.go(-1)"
	/>

	<h3 id="title">VueShop</h3>

	<van-form @submit="handleRegister">
		<van-cell-group inset>
			<van-field
					v-model="loginForm.username"
					name="用户名"
					label="用户名"
					placeholder="用户名"
					:rules="[{ required: true, message: '请填写用户名' }]"
			/>
			<van-field
					v-model="loginForm.password"
					type="password"
					name="密码"
					label="密码"
					placeholder="密码"
					:rules="[{ required: true, message: '请填写密码' }]"
			/>
			<van-field
					v-model="loginForm.code"
					name="验证码"
					label="验证码"
					placeholder="验证码"
					:rules="[{ required: true, message: '请填写验证码' }]"
			>
				<template #button>
					<van-image width="100px" height="25px" :src="base64Img" @click="changeImg"></van-image>
				</template>

			</van-field>
		</van-cell-group>
		<div style="margin: 16px;">
			<van-button block type="primary" native-type="submit">
				提交
			</van-button>
		</div>
	</van-form>

</template>

<style scoped>
	#title {
		padding: 100px 0 50px 0;
		text-align: center;
		color: #666666;
	}
</style>
