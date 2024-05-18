package com.markerhub.search.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.search.document.EsProductDoc;

public interface AppSearchService {
	int initEsData();

	Page<EsProductDoc> search(String kw, int current, int size);
}
