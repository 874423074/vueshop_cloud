<script setup>
const props = defineProps({
  products: Object,
})

</script>

<template>
  <div class="product_wrap">
    <van-grid :column-num="2" :gutter="8">
      <van-grid-item v-for="item in props.products" @click="$router.push(`/product/${item.id}`)">
        <van-image :src="item.image"/>
        <div class="product_title van-multi-ellipsis--l2">{{item.name}}</div>
        <div class="product_price">¥{{item.price}}</div>
      </van-grid-item>
    </van-grid>
  </div>
</template>

<style scoped>
  .product_wrap {
    margin-top: 8px;
  }

  .product_title {
    font-size: 14px;
    line-height: 30px;
    font-weight: 500;
    width: 100%;
  }

  .product_price {
    font-size: 20px;
    color: rgb(217, 61, 61);
    width: 100%;
    padding: 10px 0;
  }
</style>
