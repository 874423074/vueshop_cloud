package com.markerhub.search.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.product.entity.AppProduct;
import com.markerhub.product.feign.AppProductClient;
import com.markerhub.search.document.EsProductDoc;
import com.markerhub.search.repository.EsProductDocRepository;
import com.markerhub.search.service.AppSearchService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppSearchServiceImpl implements AppSearchService {

	@Resource
	AppProductClient appProductClient;

	@Resource
	EsProductDocRepository esProductDocRepository;

	@Resource
	ElasticsearchRestTemplate elasticsearchRestTemplate;

	@Override
	public int initEsData() {

		esProductDocRepository.deleteAll();

		List<AppProduct> appProducts = appProductClient.listOnSales().getData();
		List<EsProductDoc> productDocs = BeanUtil.copyToList(appProducts, EsProductDoc.class);

		Iterable<EsProductDoc> iterable = esProductDocRepository.saveAll(productDocs);

		return ListUtil.toList(iterable).size();
	}

	@Override
	public Page<EsProductDoc> search(String kw, int current, int size) {

		Criteria criteria = new Criteria("name").matches(kw)
				.or(new Criteria("keywords").matches(kw));

		Query query = new CriteriaQuery(criteria).setPageable(PageRequest.of(current - 1, size));

		SearchHits<EsProductDoc> searchHits = elasticsearchRestTemplate.search(query, EsProductDoc.class);
		List<EsProductDoc> productDocs = searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());

		Page<EsProductDoc> page = new Page<>(current, size, searchHits.getTotalHits());
		page.setRecords(productDocs);

		return page;
	}
}
