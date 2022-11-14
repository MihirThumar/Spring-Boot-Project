package com.es.config;

import javax.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


public class ElasticConfiguration implements DisposableBean{

	private RestHighLevelClient client;
	

	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public RestHighLevelClient readClient() {
		
		RestClientBuilder builder = RestClient.builder(new HttpHost("localhost",9200,"http")).setHttpClientConfigCallback(
					httpClientBuilder -> httpClientBuilder
					.setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build()));
		
		builder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(1000)
					.setSocketTimeout(6000)
					.setConnectionRequestTimeout(0));
		
		client = new RestHighLevelClient(builder);
		return client;
		
	}
	
	@PreDestroy
	public void destroy() throws Exception {
		client.close();
	}

}
