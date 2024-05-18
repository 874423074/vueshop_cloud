package com.markerhub.search.mq;

import cn.hutool.core.bean.BeanUtil;
import com.markerhub.product.entity.AppProduct;
import com.markerhub.product.feign.AppProductClient;
import com.markerhub.rabbit.lang.SearchRabbitConst;
import com.markerhub.search.document.EsProductDoc;
import com.markerhub.search.repository.EsProductDocRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class SearchConsumer {

	@Resource
	AppProductClient appProductClient;

	@Resource
	EsProductDocRepository esProductDocRepository;

	@RabbitListener(queues = SearchRabbitConst.PRODUCT_SEARCH_CREATE_QUEUE)
	public void createListener(Long productId) {
		AppProduct appProduct = appProductClient.getById(productId).getData();

		if (appProduct == null || !appProduct.getIsOnSale()) {
			esProductDocRepository.deleteById(productId);
		}

		EsProductDoc productDoc = BeanUtil.copyProperties(appProduct, EsProductDoc.class);

		esProductDocRepository.save(productDoc);
		log.info("正在更新商品{}数据", productId);
	}

	@RabbitListener(queues = SearchRabbitConst.PRODUCT_SEARCH_DELETE_QUEUE)
	public void deleteListener(List<Long> productIds) {
		esProductDocRepository.deleteAllById(productIds);
	}

}
