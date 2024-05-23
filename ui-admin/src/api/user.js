import request from '@/utils/request'

export function GetList(data) {
	return request({
		url: '/s/sys/user/list',
		method: 'get',
		params: data
	})
}

export function GetInfo(id) {
	return request({
		url: '/s/sys/user/info/' + id,
		method: 'get'
	})
}

export function Delete(ids) {
	return request({
		url: '/s/sys/user/delete',
		method: 'post',
		data: ids
	})
}

export function Add(data) {
	return request({
		url: '/s/sys/user/save',
		method: 'post',
		data: data
	})
}

export function Role(id, roleIds) {
	return request({
		url: '/s/sys/user/role/' + id,
		method: 'post',
		data: roleIds
	})
}

export function ResetPwd(id) {
	return request({
		url: '/s/sys/user/resetPwd?id=' + id,
		method: 'post'
	})
}


