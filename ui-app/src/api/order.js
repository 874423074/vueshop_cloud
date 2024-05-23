import request from '@/utils/request.js'

export function OrderPreview(data) {
	return request({
		url: '/o/app/order/preview',
		method: 'post',
		data
	})
}

export function CreateOrder(data) {
	return request({
		url: '/o/app/order/create',
		method: 'post',
		data
	})
}

export function PayOrder(orderSn) {
	return request({
		url: '/o/app/order/pay',
		method: 'post',
		params: {
			orderSn: orderSn
		}
	})
}

export function PayCheck(orderSn) {
	return request({
		url: '/o/app/pay/check',
		method: 'post',
		params: {
			orderSn: orderSn
		}
	})
}

export function CountOrder() {
	return request({
		url: '/o/app/order/count',
		method: 'get'
	})
}
export function ListOrder(status, current) {
	return request({
		url: '/o/app/order/list',
		method: 'get',
		params: {
			status: status,
			current: current
		}
	})
}

export function OrderDetail(id) {
	return request({
		url: '/o/app/order/detail/' + id,
		method: 'get'
	})
}


export function DeleteOrder(id) {
	return request({
		url: '/o/app/order/delete',
		method: 'post',
		params: {
			id: id
		}
	})
}

export function RefundOrder(id) {
	return request({
		url: '/o/app/refund/order',
		method: 'get',
		params: {
			orderId: id
		}
	})
}

export function CancelOrder(id) {
	return request({
		url: '/o/app/order/cancel',
		method: 'post',
		params: {
			id: id
		}
	})
}

export function OrderDelivery(id) {
	return request({
		url: '/o/app/order/deliveryInfo',
		method: 'get',
		params: {
			id: id
		}
	})
}

export function ConfirmOrder(id) {
	return request({
		url: '/o/app/order/confirm',
		method: 'post',
		params: {
			id: id
		}
	})
}

export function RefundData(id) {
	return request({
		url: '/o/app/refund/order',
		method: 'get',
		params: {
			orderId: id
		}
	})
}

export function RefundApply(data) {
	return request({
		url: '/o/app/refund/apply',
		method: 'post',
		processData: false,
		contentType: false,
		data
	})
}




