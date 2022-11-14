package com.es.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.es.model.Student;
import com.es.service.ElasticService2;

@Service
public class ElasticService2Empl implements ElasticService2 {
	
	@Autowired
	RestHighLevelClient client;
	
	@Override
	public Map<String, Object> addStudent(String index, Student student) {
		IndexRequest request = new IndexRequest(index);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("id", student.getId());
		map.put("name", student.getName());
		map.put("Date", student.getTime());
	
		request.source(map, XContentType.JSON);

		try {

			IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
			System.out.println(indexResponse);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

}
