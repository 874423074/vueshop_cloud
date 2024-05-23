> 视频讲解版本
> 
> 沙箱地址：
https://open.alipay.com/develop/sandbox/account
pqjpcm3551@sandbox.com
> 
VueShop-Cloud
├─shop-auth                        认证中心
├─shop-common                      公共模块
│  ├─shop-common-core              核心公共模块
│  ├─shop-common-feign             openFeign配置模块
│  ├─shop-common-mybatis           mybatis plus配置模块
│  ├─shop-common-rabbit            Rabbit配置模块
│  ├─shop-common-redis             缓存配置模块
│  ├─shop-common-satoken           权限安全配置模块
│  └─shop-common-upload            图片上传模块
├─shop-gateway                     服务网关
├─shop-service                     子服务模块
│  ├─shop-service-cartItem         购物车模块
│  ├─shop-service-order            订单与支付模块
│  ├─shop-service-product          商品模块
│  ├─shop-service-search           搜索模块
│  └─shop-service-system           权限模块
└─shop-service-api                 内部api模块
   ├─shop-service-cartItem-api     购物车模块内部接口
   ├─shop-service-order-api        订单模块内部接口
   ├─shop-service-product-api      商品模块内部接口
   ├─shop-service-system-api       权限模块内部接口
   └─shop-service-user-api         用户模块内部接口