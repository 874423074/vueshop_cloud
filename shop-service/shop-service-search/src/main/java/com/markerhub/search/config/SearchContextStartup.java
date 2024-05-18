package com.markerhub.search.config;

import com.markerhub.search.service.AppSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class SearchContextStartup implements ApplicationRunner {

	@Resource
	AppSearchService appSearchService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		int count = appSearchService.initEsData();
		log.info("已初始化es数据{}条", count);
	}
}
