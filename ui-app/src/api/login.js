import request from '@/utils/request.js'

export function Login(data) {
	return request({
		url: '/a/app/login',
		method: 'post',
		data
	})
}

export function GetCaptcha() {
	return request({
		url: '/a/app/captcha',
		method: 'get'
	})
}

export function Register(data) {
	return request({
		url: '/a/app/register',
		method: 'post',
		data
	})
}