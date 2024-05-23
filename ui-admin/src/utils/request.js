import axios from "axios"
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {ElMessage} from 'element-plus'

import store from '@/store'
import router from '@/router'

const service = axios.create({
	baseURL: import.meta.env.VITE_APP_BASE_API,
	timeout: 5000
})

service.interceptors.request.use((config) => {

	// 前置处理

	NProgress.start()

	if (store.getters.token) {
		config.headers['satoken'] = store.getters.token
	}

	return config
}, (error) => {
	return Promise.reject(error)
})

service.interceptors.response.use((response) => {

	// 后置处理
	NProgress.done()

	const res = response.data

	if (res.code === 200) {
		return res
	} else {
		ElMessage.error(res.msg)
		return Promise.reject(new Error(res.msg))
	}

}, (error) => {
	// 异常处理
	NProgress.done()

	console.log(error)
	console.log(error.response)
	const {status, data} = error.response
	if (status === 401) {
		store.dispatch('logout')

		router.push({path: '/login'})
	} else {
		error.response && ElMessage.error(data.msg)
	}

	return Promise.reject(error);
})

export default service