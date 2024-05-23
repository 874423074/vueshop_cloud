import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

import Components from 'unplugin-vue-components/vite';
import {VantResolver} from 'unplugin-vue-components/resolvers';

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [vue(),
		Components({
			resolvers: [VantResolver()],
		})
	],
	resolve: {
		alias: {
			"@": path.resolve(__dirname, "src"),
			"~": path.resolve(__dirname, "src/components"),
			"^": path.resolve(__dirname, "src/api")
		}
	}
})
