import request from '@/utils/request.js'

export function ListAddress() {
	return request({
		url: '/o/app/address/list',
		method: 'get'
	})
}

export function GetAddress(id) {
	return request({
		url: '/o/app/address/info/' + id,
		method: 'get'
	})
}

export function AddAddress(data) {
	return request({
		url: '/o/app/address/save',
		method: 'post',
		data
	})
}

export function DeleteAddress(id) {
	return request({
		url: '/o/app/address/delete',
		method: 'post',
		params: {
			id: id
		}
	})
}