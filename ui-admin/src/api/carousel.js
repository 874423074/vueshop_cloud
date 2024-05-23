import request from '@/utils/request'

export function GetList(data) {
	return request({
		url: '/p/admin/ad/list',
		method: 'get',
		params: data
	})
}

export function GetInfo(id) {
	return request({
		url: '/p/admin/ad/info/' + id,
		method: 'get'
	})
}

export function Delete(ids) {
	return request({
		url: '/p/admin/ad/delete',
		method: 'post',
		data: ids
	})
}

export function Add(data) {
	return request({
		url: '/p/admin/ad/save',
		method: 'post',
		data: data
	})
}