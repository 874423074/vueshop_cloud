<script setup>

	import {useStore} from 'vuex'
	import {useRoute, useRouter} from 'vue-router'
	import {ref, computed} from 'vue'

	const store = useStore()
	const route = useRoute()
	const router = useRouter()

	import {
		Document,
		Menu as IconMenu,
		Location,
		Setting,
		Fold,
		Expand
	} from '@element-plus/icons-vue'

	const menus = store.getters.menus
	const userInfo = store.getters.userInfo

	// 修正：route.name -> route.path
	const defaultActive = computed(() => route.path)

	const logout = () => {
		store.dispatch('logout').then( res => {
			router.replace('/login')
		})
	}

</script>

<template>
	<el-container>
		<el-aside class="el-aside-open">
			<el-menu
					active-text-color="#ffd04b"
					background-color="#545c64"
					class="el-menu-vertical-demo"
					:default-active="defaultActive"
					text-color="#fff"
					style="height: 100%"
					router
			>
				<el-menu-item index="/home">
					<el-icon>
						<icon-menu/>
					</el-icon>
					<span>首页</span>
				</el-menu-item>

				<el-sub-menu :index="menu.name" v-for="menu in menus">
					<template #title>
						<el-icon>
							<location/>
						</el-icon>
						<span>{{menu.title}}</span>
					</template>

					<el-menu-item-group v-for="item in menu.children">
						<el-menu-item :index="item.path">{{item.title}}</el-menu-item>
					</el-menu-item-group>

				</el-sub-menu>
			</el-menu>
		</el-aside>

		<el-container>
			<el-header>
				<div class="navBar">
					<h4>VueShop商城管理系统</h4>

					<div class="navBar-right">
						<el-dropdown>
					    <span class="el-dropdown-link">
					      <el-avatar shape="square" :size="40"
					                 :src="userInfo.avatar"></el-avatar>
					    </span>
							<template #dropdown>
								<el-dropdown-menu>
									<el-dropdown-item @click="logout">退出</el-dropdown-item>
								</el-dropdown-menu>
							</template>
						</el-dropdown>
					</div>

				</div>
			</el-header>

			<el-main style="padding: 10px;">
				<router-view/>

			</el-main>
		</el-container>
	</el-container>

</template>

<style lang="scss" scoped>

	.el-container {
		width: 100%;
		height: 100%;
	}

	.el-aside-open {
		width: 200px;
	}

	.container {
		width: calc(100% - 210px);
		height: 100%;

		position: fixed;
		top: 0;
		right: 0;
		z-index: 9;
		transition: all 0.28s;

		&.hidderContainer {
			width: calc(100% - 67px);
		}
	}

	::v-deep .el-header {
		padding: 0;
	}

	.navBar {
		width: 100%;
		height: 60px;
		overflow: hidden;
		background-color: #fff;
		box-shadow: 0 1px 1px rgba(0, 21, 41, 0.08);
		padding: 0 16px;
		display: flex;
		align-items: center;
		box-sizing: border-box;
		position: relative;
	}

	.navBar {
		.navBar-right {
			flex: 1;
			display: flex;
			align-items: center;
			justify-content: flex-end;

			::v-deep .navBar-item {
				display: inline-block;
				margin-left: 18px;
				font-size: 22px;
				color: #5a5e66;
				box-sizing: border-box;
				cursor: pointer;
			}
		}
	}

</style>
