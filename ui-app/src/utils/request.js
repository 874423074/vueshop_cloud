import axios from "axios"
import {showLoadingToast, closeToast, showFailToast, showConfirmDialog} from 'vant'
import 'vant/es/toast/style'
import 'vant/es/dialog/style'
import 'vant/es/notify/style'
import 'vant/es/image-preview/style'

import store from '@/store'
import router from "@/router";

const service = axios.create({
	baseURL: import.meta.env.VITE_APP_BASE_API,
	timeout: 5000
})

service.interceptors.request.use((config) => {

	// 前置处理

	// 加载中
	showLoadingToast({
		message: '加载中...',
		forbidClick: true,
		duration: 0
	});

	if (store.getters.token) {
		config.headers['satoken-user'] = store.getters.token
	}

	return config
}, (error => {
	return Promise.reject(error)
}))

service.interceptors.response.use((response) => {

	// 结束加载中
	closeToast()

	// 后置处理
	const res = response.data
	if (res.code != 200) {
		showFailToast(res.msg)
		return Promise.reject(new Error(res.msg))
	} else {
		return res
	}


}, (error => {
	// 异常处理
	closeToast()

	let {status, data} = error.response
	if (status === 401) {
		store.dispatch('logout')

		showConfirmDialog({
			message: '请登录后操作',
			confirmButtonText: '去登录'
		}).then(() => {
			// on confirm
			router.push({
				path: '/login',
				query: {
					redirect: router.currentRoute.value.fullPath
				}
			})
		})
	} else {
		showFailToast(data.msg)
	}


	return Promise.reject(error);
}))

export default service