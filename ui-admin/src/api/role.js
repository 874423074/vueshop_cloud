import request from '@/utils/request'

export function GetList(data) {
	return request({
		url: '/s/sys/role/list',
		method: 'get',
		params: data
	})
}

export function GetInfo(id) {
	return request({
		url: '/s/sys/role/info/' + id,
		method: 'get'
	})
}

export function Delete(ids) {
	return request({
		url: '/s/sys/role/delete',
		method: 'post',
		data: ids
	})
}

export function Add(data) {
	return request({
		url: '/s/sys/role/save',
		method: 'post',
		data: data
	})
}

export function Perm(id, menuIds) {
	return request({
		url: '/s/sys/role/perm/' + id,
		method: 'post',
		data: menuIds
	})
}

