import request from '@/utils/request.js'

export function GetCategory(id, current, sort) {
	return request({
		url: '/p/app/category/' + id,
		method: 'get',
		params: {
			current: current,
			sort: sort
		}
	})
}

export function GetProduct(id) {
	return request({
		url: '/p/app/product/' + id,
		method: 'get'
	})
}