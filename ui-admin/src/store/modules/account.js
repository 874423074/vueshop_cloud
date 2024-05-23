import {Login, GetUserInfo} from '^/login'

const state = {
	token: localStorage.getItem('satoken') || '',
	userInfo: JSON.parse(localStorage.getItem('userInfo') || null) || {},
	menus: JSON.parse(localStorage.getItem('menus') || null) || [],
	auths: localStorage.getItem('auths') || null,
}
const mutations = {
	SET_TOKEN: (state, token) => {
		state.token = token
		localStorage.setItem("satoken", token)
	},
	CLEAR_TOKEN: (state) => {
		state.token = ''
		state.userInfo = ''

		localStorage.clear()
	},
	SET_INFO: (state, data) => {
		state.userInfo = data.userInfo
		state.menus = data.menus
		state.auths = data.auths

		localStorage.setItem("userInfo", JSON.stringify(data.userInfo))
		localStorage.setItem("menus", JSON.stringify(data.menus))
		localStorage.setItem("auths", data.auths)
	},

}
const actions = {
	login({commit}, loginForm){
		return new Promise(((resolve, reject) => {
			Login(loginForm).then(res => {
				commit('SET_TOKEN', res.data.token)
				commit('SET_USER_INFO', res.data.userInfo)
				resolve()
			}).catch(err => {
				reject(err)
			})

		}))
	},

	logout({commit}) {
		commit('CLEAR_TOKEN')
	},

	getUserInfo({commit}){
		return new Promise(((resolve, reject) => {
			GetUserInfo().then(res => {
				commit('SET_INFO', res.data)
				resolve()
			}).catch(err => {
				reject(err)
			})
		}))
	},



}

export default {
	namespace: true,
	state,
	mutations,
	actions
}
