import {Login} from '^/login'

const state = {
	token: localStorage.getItem('satoken-user') || '',
	userInfo: JSON.parse(localStorage.getItem('userInfo') || null) || {}
}
const mutations = {
	SET_TOKEN: (state, token) => {
		state.token = token
		localStorage.setItem("satoken-user", token)
	},
	CLEAR_TOKEN: (state) => {
		state.token = ''
		state.userInfo = ''

		localStorage.clear()
	},
	SET_USER_INFO: (state, userInfo) => {
		state.userInfo = userInfo
		localStorage.setItem("userInfo", JSON.stringify(userInfo))
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
	}

}

export default {
	namespace: true,
	state,
	mutations,
	actions
}
