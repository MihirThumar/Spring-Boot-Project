package com.es.config;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class EsConfig {

	private RestHighLevelClient client;

	public RestClientBuilder restClientBuilder() {
		RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
		try {
			builder.setRequestConfigCallback(httpClientBuilder -> httpClientBuilder.setSocketTimeout(6000));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder;
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public RestHighLevelClient readClient() {
		RestClientBuilder builder = RestClient.builder(new HttpHost("localhost",9200,"http"));
		
		builder.setRequestConfigCallback(
				
				requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(1000).setSocketTimeout(6000).setConnectionRequestTimeout(0));
		
		return new RestHighLevelClient(builder);
	}
	
	public void destroy() throws IOException {
		client.close();
	}
	
}
