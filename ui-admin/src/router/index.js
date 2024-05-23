import {createRouter, createWebHashHistory} from 'vue-router'

const routes = [
	{
		path: '/',
		name: 'Layout',
		component: () => import(`../components/Layout.vue`),
		redirect: '/home',
		children: [
			{
				path: '/home',
				name: 'Home',
				component: () => import(`../views/home.vue`),
			}
		]
	},
	{
		path: '/*',
		redirect: '/',
	},
	{
		path: '/login',
		name: 'Login',
		component: () => import(`../views/login/index.vue`),
	},
]

const router = createRouter({
	history: createWebHashHistory(),
	routes
})
export default router