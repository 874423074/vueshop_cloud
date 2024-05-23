import request from '@/utils/request.js'

export function GetCartCount() {
	return request({
		url: '/t/app/cart/count',
		method: 'get'
	})
}

export function AddCart(data) {
	return request({
		url: '/t/app/cart/add',
		method: 'post',
		data
	})
}

export function GetCartList() {
	return request({
		url: '/t/app/cart/list',
		method: 'get'
	})
}

export function GetTotal(data) {
	return request({
		url: '/t/app/cart/total',
		method: 'post',
		data
	})
}

export function DeleteCart(data) {
	return request({
		url: '/t/app/cart/delete',
		method: 'post',
		data
	})
}

export function UpdateQuantity(id, quantity) {
	return request({
		url: '/t/app/cart/updateQuantity',
		method: 'post',
		params: {
			id: id,
			quantity: quantity
		}
	})
}