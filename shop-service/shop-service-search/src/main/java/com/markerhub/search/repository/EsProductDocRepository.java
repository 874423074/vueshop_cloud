package com.markerhub.search.repository;

import com.markerhub.search.document.EsProductDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsProductDocRepository extends ElasticsearchRepository<EsProductDoc, Long> {
}
