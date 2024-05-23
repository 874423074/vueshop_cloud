import {createRouter, createWebHashHistory} from 'vue-router'

const routes = [
	{
		path: '/',
		name: 'Index',
		component: () => import('../views/index.vue'),
		meta: {
			showTabBar: true
		}
	},
	{
		path: '/login',
		name: 'Login',
		component: () => import('../views/account/login.vue')
	},
	{
		path: '/register',
		name: 'Register',
		component: () => import('../views/account/register.vue')
	},
	{
		path: '/category/:categoryId',
		name: 'Category',
		component: () => import('../views/product/category.vue')
	},
	{
		path: '/product/:productId',
		name: 'Product',
		component: () => import('../views/product/detail.vue')
	},
	{
		path: '/product/comment',
		name: 'ProductComment',
		component: () => import('../views/product/comment.vue')
	},
	{
		path: '/search',
		name: 'Search',
		component: () => import('../views/search.vue')
	},
	{
		path: '/order/preview',
		name: 'OrderPreview',
		component: () => import('../views/search.vue')
	},
	{
		path: '/cart',
		name: 'Cart',
		component: () => import('../views/member/cart.vue'),
		meta: {
			showTabBar: true
		}
	},
	{
		path: '/order/preview',
		name: 'OrderPreview',
		component: () => import('../views/order/preview.vue')
	},
	{
		path: '/order/payRes',
		name: 'OrderPay',
		component: () => import('../views/order/payRes.vue')
	},
	{
		path: '/order/list',
		name: 'OrderList',
		component: () => import('../views/order/list.vue')
	},
	{
		path: '/order/detail',
		name: 'OrderDetail',
		component: () => import('../views/order/detail.vue')
	},
	{
		path: '/order/deliveryInfo',
		name: 'OrderDeliveryInfo',
		component: () => import('../views/order/delivery.vue')
	},
	{
		path: '/order/refund',
		name: 'OrderRefund',
		component: () => import('../views/order/refund.vue')
	},

	{
		path: '/address/list',
		name: 'ListAddress',
		component: () => import('../views/address/list.vue')
	},
	{
		path: '/address/edit',
		name: 'EditAddress',
		component: () => import('../views/address/edit.vue')
	},
	{
		path: '/mine',
		name: 'Mine',
		component: () => import('../views/member/mine.vue'),
		meta: {
			showTabBar: true
		}
	},
	{
		path: '/comment/list',
		name: 'CommentList',
		component: () => import(`../views/comment/list.vue`),
	},
	{
		path: '/comment/edit',
		name: 'CommentEdit',
		component: () => import(`../views/comment/edit.vue`),
	},
	{
		path: '/comment/info',
		name: 'CommentInfo',
		component: () => import(`../views/comment/info.vue`),
	},
	{
		path: '/*',
		redirect: '/',
	},
]

const router = createRouter({
	history: createWebHashHistory(),
	routes
})
export default router