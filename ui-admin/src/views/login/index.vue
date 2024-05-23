<script setup>

	import {ref, reactive} from 'vue'

	import {useStore} from 'vuex'
	import {useRouter} from "vue-router"

	const store = useStore()
	const router = useRouter()

	const loginRules = ref({
		username: [
			{required: true, message: '请输入用户名', trigger: 'blur'}
		],
		password: [
			{required: true, message: '请输入密码', trigger: 'blur'}
		],
	})

	const loginForm = reactive({
		username: 'admin',
		password: '888888'
	})
	const formRef = ref(null)
	const handleLogin = () => {
		formRef.value.validate(async (valid) => {
			if (valid) {
				store.dispatch('login', loginForm).then(res => {
					router.push('/')
				})
			}
		})
	}

</script>

<template>
	<div class="login-container">
		<el-form ref="formRef" class="login-form" auto-complete="on" :rules="loginRules" :model="loginForm">

			<div class="title-container">
				<h3 class="title">VueShop后台管理系统</h3>
			</div>

			<el-form-item prop="username">
				<el-input
						v-model="loginForm.username"
						placeholder="Username"
						type="text"
				/>
			</el-form-item>

			<el-form-item prop="password">
				<el-input
						v-model="loginForm.password"
						type="password"
						placeholder="Password"
				/>
			</el-form-item>

			<el-button type="primary" style="width:100%;margin-bottom:30px;" @click.prevent="handleLogin">Login</el-button>

			<div class="tips">
				<span>生活就像海洋，只有意志坚强的人才能到达彼岸！</span><br>
				<span>原创公众号：MarkerHub，网站：<a href="https://javamake.com" target="_blank">https://javamake.com</a></span>
			</div>

		</el-form>
	</div>
</template>

<style lang="scss">
	$bg: #283443;
	$light_gray: #fff;
	$cursor: #fff;
	@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
		.login-container .el-input input {
			color: $cursor;
		}
	}

	/* reset element-ui css */
	.login-container {
		.el-input {
			display: inline-block;
			height: 47px;
			/*width: 85%;*/

			input {
				background: transparent;
				border: 0px;
				-webkit-appearance: none;
				border-radius: 0px;
				padding: 12px 5px 12px 15px;
				color: $light_gray;
				height: 47px;
				caret-color: $cursor;

				&:-webkit-autofill {
					box-shadow: 0 0 0px 1000px $bg inset !important;
					-webkit-text-fill-color: $cursor !important;
				}
			}
		}

		.el-form-item {
			border: 1px solid rgba(255, 255, 255, 0.1);
			background: rgba(0, 0, 0, 0.1);
			border-radius: 5px;
			color: #454545;
		}
	}
</style>

<style lang="scss" scoped>
	$bg: #2d3a4b;
	$dark_gray: #889aa4;
	$light_gray: #eee;
	.login-container {
		min-height: 100%;
		width: 100%;
		background-color: $bg;
		overflow: hidden;

		.login-form {
			position: relative;
			width: 520px;
			max-width: 100%;
			padding: 160px 35px 0;
			margin: 0 auto;
			overflow: hidden;
		}

		.tips {
			font-size: 14px;
			color: #fff;
			margin-bottom: 10px;

			span {
				&:first-of-type {
					margin-right: 16px;
				}
			}
		}

		.svg-container {
			padding: 6px 5px 6px 15px;
			line-height: 1;
			color: $dark_gray;
			width: 30px;
		}

		.title-container {
			position: relative;

			.title {
				font-size: 26px;
				color: $light_gray;
				margin: 0px auto 40px auto;
				text-align: center;
				font-weight: bold;
			}
		}
	}
</style>
