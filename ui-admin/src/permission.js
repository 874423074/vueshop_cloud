import router from '@/router'
import store from '@/store'

const whiteList = ['Login', 'Lock']

// 是否已经动态添加路由
let hasRoute = false

router.beforeEach(async (to, from, next) => {

	// 白名单
	if (whiteList.includes(to.name)) {
		next()
	}

	// 权限校验，是否已经登录
	if (!store.getters.token) {
		next('/login')
	} else {

		// 获取用户信息userInfo
		let userInfo = store.getters.userInfo
		if (!userInfo || JSON.stringify(userInfo) === '{}') {
			await store.dispatch('getUserInfo')
		}

		// 根据菜单动态生成路由
		console.log(store.getters.menus)

		if (!hasRoute) {
			// 动态添加路由
			addRouter(store.getters.menus)

			hasRoute = true

			next({...to, replace: true})
		}

		next();
	}
})

let newRoutes = router.options.routes
const addRouter = (menus) => {
	menus.forEach(menu => {
		// menu转成router
		let route = menuToRoute(menu)
		if (route) {
			// router添加到Layout的children
			router.addRoute('Layout', route)
			newRoutes[0].children.push(route)
		}

		if (menu.children) {
			// 递归
			addRouter(menu.children)
		}
	})
}

// vite动态获取组件的方法
const modules = import.meta.glob('./views/*/*/*.vue')

// 导航转路由
const menuToRoute = (menu) => {

	if (!menu.component) {
		return null
	}

	let route = {
		path: menu.path,
		name: menu.name,
		meta: {
			icon: menu.icon,
			title: menu.title
		}
	}

	route.component = modules['./views/' + menu.component]
	return route
}
