import request from '@/utils/request.js'

export function GetHome() {
	return request({
		url: '/p/app/home',
		method: 'get'
	})
}

export function Search(kw, current) {
	return request({
		url: '/c/app/search',
		method: 'get',
		params: {
			kw: kw,
			current: current
		}
	})
}