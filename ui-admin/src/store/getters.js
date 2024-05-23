const getters = {
	token: state => state.account.token,
	userInfo: state => state.account.userInfo,
	menus: state => state.account.menus,
	auths: state => state.account.auths,
}
export default getters