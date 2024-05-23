import request from '@/utils/request'

export function Login(data) {
	return request({
		url: '/s/sys/login',
		method: 'post',
		data
	})
}

export function GetUserInfo() {
	return request({
		url: '/s/sys/userInfo',
		method: 'get'
	})
}