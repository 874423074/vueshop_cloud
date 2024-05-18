package com.markerhub.upload.service;

import cn.hutool.core.img.Img;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.lang.UUID;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AppUploadService {

	@Value("${cos.secretId}")
	private String secretId;

	@Value("${cos.secretKey}")
	private String secretKey;

	@Value("${cos.region}")
	private String region;

	@Value("${cos.bucketName}")
	private String bucketName ;
	/**
	 *
	 * @param pics 需要上传的图片
	 * @param scale 缩小比例
	 * @param quality 压缩比例
	 * @return
	 * @throws IOException
	 */
	public List<String> upload(MultipartFile[] pics, float scale, double quality) throws IOException {

		// 检查图片是否符合要求
		if (pics == null || pics.length == 0) {
			return new ArrayList<>();
		}

		List<String> paths = new ArrayList<>();
		for (MultipartFile pic : pics) {
			String type = FileTypeUtil.getType(pic.getInputStream());
			Assert.isTrue(!pic.isEmpty(), "图片错误");
			Assert.isTrue(Arrays.asList("png", "jpg", "jpeg").contains(type.toLowerCase()), "不支持图片类型");

			// 压缩图片
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Img.from(pic.getInputStream())
					.scale(scale)
					.setQuality(quality)
					.write(outputStream)
			;

			// 上传到云存储
			String key = UUID.randomUUID().toString();
			String path = "image/" + key + "." + type;
			PutObjectRequest putObjectRequest = new PutObjectRequest(this.bucketName, path, new ByteArrayInputStream(outputStream.toByteArray()), null);
			getClient().putObject(putObjectRequest);

			// 组合获取图片访问链接
			String picPath = "https://" + this.bucketName + ".cos." + this.region + ".myqcloud.com/" + path;
			paths.add(picPath);
		}

		return paths;
	}

	private COSClient getClient() {

		String secretId = this.secretId;
		String secretKey = this.secretKey;
		COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

		// ClientConfig 中包含了后续请求 COS 的客户端设置：
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setRegion(new Region(this.region));
		clientConfig.setHttpProtocol(HttpProtocol.https);

		// 设置 socket 读取超时，默认 30s
		clientConfig.setSocketTimeout(30*1000);
		// 设置建立连接超时，默认 30s
		clientConfig.setConnectionTimeout(30*1000);

		// 生成 cos 客户端。
		return new COSClient(cred, clientConfig);
	}
}
