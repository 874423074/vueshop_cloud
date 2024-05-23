import request from '@/utils/request'

export function GetMenuTree() {
	return request({
		url: '/s/sys/menu/list',
		method: 'get'
	})
}

export function GetInfo(id) {
	return request({
		url: '/s/sys/menu/info/' + id,
		method: 'get'
	})
}

export function Delete(id) {
	return request({
		url: '/s/sys/menu/delete?id=' + id,
		method: 'post'
	})
}

export function Add(data) {
	return request({
		url: '/s/sys/menu/save',
		method: 'post',
		data: data
	})
}

