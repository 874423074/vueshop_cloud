import request from '@/utils/request'

export function GetList(data) {
	return request({
		url: '/p/admin/specification/list',
		method: 'get',
		params: data
	})
}

export function GetInfo(id) {
	return request({
		url: '/p/admin/specification/info/' + id,
		method: 'get'
	})
}

export function Delete(ids) {
	return request({
		url: '/p/admin/specification/delete',
		method: 'post',
		data: ids
	})
}

export function Add(data) {
	return request({
		url: '/p/admin/specification/save',
		method: 'post',
		data: data
	})
}