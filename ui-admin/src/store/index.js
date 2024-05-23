import {createStore} from "vuex"
import getters from './getters'

const files = import.meta.globEager('./modules/*.js')

const modules = Object.entries(files).reduce((modules, [path, mod]) => {
	const moduleName = path.replace(/^\.\/modules\/(.*)\.\w+$/, '$1')
	modules[moduleName] = mod.default
	return modules
}, {})

export default createStore({
	modules,
	getters
})