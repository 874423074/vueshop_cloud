import request from '@/utils/request'

export function GetList(data) {
	return request({
		url: '/o/admin/refund/list',
		method: 'get',
		params: data
	})
}

export function GetInfo(id) {
	return request({
		url: '/o/admin/refund/info/' + id,
		method: 'get'
	})
}

export function AuditRefund(data) {
	return request({
		url: '/o/admin/refund/audit',
		method: 'post',
		data: data
	})
}