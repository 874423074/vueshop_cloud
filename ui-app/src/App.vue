<script setup>
  import { ref } from 'vue'

  const active = ref(0);

</script>

<template>

  <div style="margin-bottom: 60px">
    <router-view></router-view>
  </div>

  <van-tabbar v-model="active" route v-show="$route.meta.showTabBar">
    <van-tabbar-item to="/" icon="wap-home">首页</van-tabbar-item>
    <van-tabbar-item to="/cart" icon="shopping-cart">购物车</van-tabbar-item>
    <van-tabbar-item to="/mine" icon="manager">我的</van-tabbar-item>
  </van-tabbar>
</template>

<style lang="scss" src="@/assets/scss/global.scss"></style>
<style>
  :root {
    --van-font-bold: 500
  }
</style>

