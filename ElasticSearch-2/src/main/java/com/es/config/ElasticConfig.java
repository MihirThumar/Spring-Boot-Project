package com.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;


public class ElasticConfig {

	public static RestHighLevelClient levelClient() {
		
		RestHighLevelClient client = null;
		
		try {
			
			RestClientBuilder builder = RestClient.builder(new HttpHost("localhost",9200,"http"));
			
			client = new RestHighLevelClient(builder);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return client;
		
	}
	
}
