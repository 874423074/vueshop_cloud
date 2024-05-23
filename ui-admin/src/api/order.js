import request from '@/utils/request'

export function GetList(data) {
	return request({
		url: '/o/admin/order/list',
		method: 'get',
		params: data
	})
}

export function GetInfo(id) {
	return request({
		url: '/o/admin/order/info/' + id,
		method: 'get'
	})
}
export function GetDeliveryInfo(id) {
	return request({
		url: '/o/admin/order/deliveryInfo',
		method: 'get',
		params: {
			id: id
		}
	})
}

export function CloseOrder(data) {
	return request({
		url: '/o/admin/order/close',
		method: 'post',
		data: data
	})
}

export function ShipOrder(data) {
	return request({
		url: '/o/admin/order/ship',
		method: 'post',
		data: data
	})
}