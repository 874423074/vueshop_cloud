import request from '@/utils/request'

export function GetList(data) {
	return request({
		url: '/p/admin/product/list',
		method: 'get',
		params: data
	})
}

export function GetInfo(id) {
	return request({
		url: '/p/admin/product/info/' + id,
		method: 'get'
	})
}

export function Delete(ids) {
	return request({
		url: '/p/admin/product/delete',
		method: 'post',
		data: ids
	})
}

export function Add(data) {
	return request({
		url: '/p/admin/product/save',
		method: 'post',
		data: data
	})
}

export function GetSku(id) {
	return request({
		url: '/p/admin/sku/product/' + id,
		method: 'get'
	})
}

export function AddSku(data) {
	return request({
		url: '/p/admin/sku/save',
		method: 'post',
		data: data
	})
}

export function UpdateState(data) {
	return request({
		url: '/p/admin/product/updateState',
		method: 'post',
		params: data
	})
}
