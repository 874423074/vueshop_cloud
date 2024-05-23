<script setup>

	import {ref, reactive} from 'vue'
	import {useStore} from 'vuex'
	import {useRoute, useRouter} from 'vue-router'

	const store = useStore()
	const route = useRoute()
	const router = useRouter()

	const loginForm = reactive({
		username: 'test',
		password: '888888'
	})

	const redirect = route.query.redirect
	const handleLogin = () => {
		store.dispatch('login', loginForm).then(res => {
			if (redirect) {
				router.replace(redirect)
			} else {
				router.replace('/mine')
			}
		})
	}
</script>

<template>
	<van-nav-bar
			title="登录"
			left-text="返回"
			left-arrow
			@click-left="$router.go(-1)"
	/>

	<h3 id="title">VueShop</h3>

	<van-form @submit="handleLogin">
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
		</van-cell-group>
		<div style="margin: 16px;">
			<van-button block type="primary" native-type="submit">
				提交
			</van-button>
		</div>
		<div style="margin: 16px"><router-link to="/register" style="color: #1989FA"> >去注册 </router-link> </div>
	</van-form>

</template>

<style scoped>
	#title {
		padding: 100px 0 50px 0;
		text-align: center;
		color: #666666;
	}
</style>
