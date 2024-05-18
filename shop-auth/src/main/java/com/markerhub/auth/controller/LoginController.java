package com.markerhub.auth.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Producer;
import com.markerhub.satoken.utils.StpUserUtil;
import com.markerhub.user.dto.LoginDto;
import com.markerhub.user.dto.RegisterDto;
import com.markerhub.core.lang.Result;
import com.markerhub.user.dto.UserInfoDto;
import com.markerhub.user.entity.AppUser;
import com.markerhub.auth.service.AppUserService;
import com.markerhub.redis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/app")
public class LoginController {

	@Resource
	AppUserService appUserService;

	@Resource
	Producer producer;

	@Resource
	RedisUtil redisUtil;

	@PostMapping("/login")
	public Result login(@Validated @RequestBody LoginDto loginDto) {

		// 校验用户名密码
		AppUser appUser = appUserService.login(loginDto);

		StpUserUtil.login(appUser.getId());

		// 生成token
		String token = StpUserUtil.getTokenValue();
		return Result.success(MapUtil.builder()
				.put("token", token)
				.put("userInfo", BeanUtil.toBean(appUser, UserInfoDto.class))
				.build()
		);
	}

	@GetMapping("/captcha")
	public Result getCaptcha() throws IOException {

		// 生成uuid和验证码
		String uuid = UUID.randomUUID().toString();
		String code = producer.createText();

		log.info("uuid - code - {}，{}", uuid, code);

		// 生成base64编码的图片验证码
		BufferedImage image = producer.createImage(code);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, "jpeg", outputStream);

		BASE64Encoder encoder = new BASE64Encoder();
		String base64Img = "data:image/jpeg;base64," + encoder.encode(outputStream.toByteArray());

		// 把uuid和验证码存储到redis
		redisUtil.set(uuid, code, 120);

		// 返回uuid和base64图片
		return Result.success(MapUtil.builder()
				.put("uuid", uuid)
				.put("base64Img", base64Img)
				.build());
	}

	@PostMapping("/register")
	public Result register(@Validated @RequestBody RegisterDto form) {

		// 验证验证码是否正确
		Assert.isTrue(form.getCode().equals(redisUtil.get(form.getUuid()).toString()), "验证码不正确");

		// 判断用户名是否已存在
		long count = appUserService.count(new QueryWrapper<AppUser>().eq("username", form.getUsername()));
		if (count > 0) {
			return Result.fail("用户名已存在");
		}

		// 进行注册
		AppUser user = new AppUser();
		user.setUsername(form.getUsername());
		user.setPassword(SecureUtil.md5(form.getPassword()));
		user.setAvatar("https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/zhuawaba/5a9f48118166308daba8b6da7e466aab.jpg");
		user.setCreated(LocalDateTime.now());
		appUserService.save(user);

		return Result.success();
	}
}
