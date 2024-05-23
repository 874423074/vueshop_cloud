import request from '@/utils/request'

export function ListComment(status) {
	return request({
		url: '/o/app/comment/list',
		method: 'get',
		params: {
			status: status
		}
	})
}
export function CommentDetail(id) {
	return request({
		url: '/o/app/comment/info',
		method: 'get',
		params: {
			id: id
		}
	})
}

export function ProductComment(productId, current) {
	return request({
		url: '/o/app/comment/product',
		method: 'get',
		params: {
			productId: productId,
			current: current
		}
	})
}

export function PostComment(data) {
	return request({
		url: '/o/app/comment/post',
		method: 'post',
		processData:false,
		contentType:false,
		data
	})
}
