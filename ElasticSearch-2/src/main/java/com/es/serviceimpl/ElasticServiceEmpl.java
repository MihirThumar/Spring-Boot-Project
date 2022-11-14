package com.es.serviceimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.ParsedMin;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.es.model.Doctor;
import com.es.service.ElasticService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings({ "deprecation", "unused" })
@Service
public class ElasticServiceEmpl implements ElasticService {

	@Autowired
	RestHighLevelClient client;

	@Autowired
	ObjectMapper mapper;

//	create index
	@Override
	public Map<String, Object> addDoctor(String index, Doctor doc, HttpSession httpSession) {

		IndexRequest request = new IndexRequest("doctor");

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("gender", doc.getGender());
		map.put("name", doc.getName());
		map.put("adreesh", doc.getAdreesh());
		map.put("age", doc.getAge());

		httpSession.setAttribute("response", map);
		request.source(map, XContentType.JSON);

		try {

			IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
			System.out.println(indexResponse);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

//	delete index
	@Override
	public boolean deleteIndex(String index) {

		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
		try {
			client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

//	Get Doctor
	@Override
	public SearchResponse getDoctor(String index, String id, HttpSession httpSession) throws IOException {

		Map<String, Object> map = new HashMap<String, Object>();

		SearchRequest requestAll = new SearchRequest(index);

		MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(matchAllQueryBuilder);

		requestAll.source(sourceBuilder);

		SearchResponse searchResponseForAll = client.search(requestAll, RequestOptions.DEFAULT);

		List<Object> list = new ArrayList<>();

		for (SearchHit s : searchResponseForAll.getHits().getHits()) {
			list.add(s.getSourceAsMap());
		}

		httpSession.setAttribute("response", list);
		map.put("response", list);
		map.put("code", 200);
		map.put("message", "Got response from elastic search's database successfully...");
		map.put("status", true);
		return searchResponseForAll;
	}

//	Get Doctor by id
	@Override
	public Doctor getDoctorById(String index, String id, HttpSession httpSession) {

		SearchRequest searchRequest = new SearchRequest(index);

		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("_id", id);
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(matchQueryBuilder);
		searchRequest.source(sourceBuilder);

		List<Map<?, ?>> list = new ArrayList<Map<?, ?>>();

		try {

			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

			for (SearchHit s : searchResponse.getHits().getHits()) {
				list.add(s.getSourceAsMap());
			}
			httpSession.setAttribute("doctorWithId", list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Doctor> matchSearch(HttpSession httpSession) {

		MultiSearchRequest request = new MultiSearchRequest();

		SearchRequest searchRequest = new SearchRequest("rahul");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("name", "Rahul")));
		searchRequest.source(searchSourceBuilder);

		request.add(searchRequest);

		try {
			MultiSearchResponse response = client.msearch(request, RequestOptions.DEFAULT);
			MultiSearchResponse.Item firstResponse = response.getResponses()[0];
			SearchResponse searchResponse = firstResponse.getResponse();
			MultiSearchResponse.Item secondResponse = response.getResponses()[0];
			searchResponse = secondResponse.getResponse();

			List<Object> list = new ArrayList<>();

			for (SearchHit s : searchResponse.getHits().getHits()) {
				list.add(s.getSourceAsMap());
			}
			httpSession.setAttribute("boolQueryResult", list);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

//	Delete by id
	@Override
	public boolean deleteDoctorById(String index, String id) {
		DeleteRequest deleteRequest = new DeleteRequest(index, id);

		try {
			DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

//	Update Doctor
	@SuppressWarnings("deprecation")
	@Override
	public UpdateResponse updateDoctorDetails(String index, String id, Doctor doctor, HttpSession httpSession)
			throws IOException {
		// String writeValueAsString = mapper.writeValueAsString(doctor);
		// UpdateRequest updateRequest = new
		// UpdateRequest(index,id).doc(writeValueAsString,XContentType.JSON);
		// updateRequest.doc(writeValueAsString,XContentType.JSON);

		return client.update(new UpdateRequest(index, id).doc(mapper.writeValueAsString(doctor), XContentType.JSON),
				RequestOptions.DEFAULT);
	}

//							Aggregation
	@SuppressWarnings("deprecation")
	@Override
	public ResponseEntity<?> aggregationFuction(String caseValue, String index) {

		Map<String, Object> map = new HashMap<>();

		if (caseValue.equalsIgnoreCase("sum") || caseValue.equalsIgnoreCase("avg") || caseValue.equalsIgnoreCase("min")
				|| caseValue.equalsIgnoreCase("max")) {

			try {
				switch (caseValue.toLowerCase()) {
				case "sum":
					// SearchRequest requestSum = new SearchRequest(index);
					// SearchSourceBuilder builderSum = new SearchSourceBuilder();
					// SumAggregationBuilder aggregationSum =
					// AggregationBuilders.sum("aggSum").field("age");
					// builderSum.aggregation(aggregationSum);
					// requestSum.source(builderSum);
					// SearchResponse searchResponseSum = client.search(requestSum,
					// RequestOptions.DEFAULT);
					// Aggregations aggregationsSum = searchResponseSum.getAggregations();
					// ParsedSum psum = aggregationsSum.get("aggSum");

					ParsedSum psum = client.search(
							new SearchRequest(index).source(new SearchSourceBuilder()
									.aggregation(AggregationBuilders.sum("aggSum").field("age"))),
							RequestOptions.DEFAULT).getAggregations().get("aggSum");

					map.put("sum", psum.getValue());

					break;

				case "avg":
//					SearchRequest requestAvg = new SearchRequest(index);
//					SearchSourceBuilder builderAvg = new SearchSourceBuilder();
//					AvgAggregationBuilder aggregationAvg = AggregationBuilders.avg("aggAvg").field("age");
//					builderAvg.aggregation(aggregationAvg);
//					requestAvg.source(builderAvg);
//					SearchResponse searchResponseAvg = client.search(requestAvg, RequestOptions.DEFAULT);
//					Aggregations aggregationsAvg = searchResponseAvg.getAggregations();
//					ParsedAvg pavg = aggregationsAvg.get("aggAvg");

					ParsedAvg pavg = client.search(
							new SearchRequest(index).source(new SearchSourceBuilder()
									.aggregation(AggregationBuilders.avg("aggAvg").field("age"))),
							RequestOptions.DEFAULT).getAggregations().get("aggAvg");

					map.put("avg", pavg.getValue());
					break;

				case "min":
					SearchRequest requestMin = new SearchRequest(index);
					SearchSourceBuilder builderMin = new SearchSourceBuilder();
					MinAggregationBuilder min = AggregationBuilders.min("aggForMin").field("age");
					builderMin.aggregation(min);
					requestMin.source(builderMin);
					SearchResponse response = client.search(requestMin, RequestOptions.DEFAULT);
					Aggregations aggregations = response.getAggregations();
					ParsedMin pamin = aggregations.get("aggForMin");

//					ParsedMin pamin = client.search(new SearchRequest(index).source(new SearchSourceBuilder().aggregation
//							(AggregationBuilders.min("aggForMin").field("age"))),RequestOptions.DEFAULT).getAggregations().get("aggForMin");

					map.put("min", pamin.getValue());
					break;

				case "max":
//					MaxAggregationBuilder max = AggregationBuilders.max("aggForMax").field("age");
//					SearchSourceBuilder builderMax = new SearchSourceBuilder();
//					SearchRequest requestMax = new SearchRequest(index);
//					builderMax.aggregation(max);
//					requestMax.source(builderMax);
					// SearchResponse searchResponseMax = client.search(requestMax,
					// RequestOptions.DEFAULT);
					// Aggregations aggregationsMax = searchResponseMax.getAggregations();
					// ParsedMax pmax = aggregationsMax.get("aggForMax");

					ParsedMax pmax = client.search(
							new SearchRequest(index).source(new SearchSourceBuilder()
									.aggregation(AggregationBuilders.max("aggForMax").field("age"))),
							RequestOptions.DEFAULT).getAggregations().get("aggForMax");

					map.put("max", pmax.getValue());
					break;

				default:
					System.err.println("Wrong Keyword");
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			map.put("error", "Please enter keyword from 'sum','avg','min','max' ");
		}
		return ResponseEntity.ok(map);
	}

//	cardinalityFucn
	@SuppressWarnings("deprecation")
	@Override
	public ResponseEntity<?> cardinlityFunc() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		SearchSourceBuilder builderCar = new SearchSourceBuilder();
		CardinalityAggregationBuilder aggregationCar = AggregationBuilders.cardinality("aggCar").field("Age");
		
		SearchRequest requestCar = new SearchRequest("doctor");
		requestCar.source(builderCar);
		
		builderCar.aggregation(aggregationCar);
		requestCar.source(builderCar);
		
		try {
			
			SearchResponse responseCar = client.search(requestCar, RequestOptions.DEFAULT);
			Aggregations agg = responseCar.getAggregations();
			Cardinality pc = agg.get("aggCar");
			map.put("cardinality", pc.getValue());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(map);
	}

	@Override
	public Object rangedQuery(Integer age,String index) throws IOException {
	
	SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("age").gte(age).lte(30);
	searchSourceBuilder.query(rangeQueryBuilder);
	
//	BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
//			.should(QueryBuilders	.matchQuery("age", age))
//			.should(QueryBuilders	.matchQuery("age", age));
	
	SearchRequest searchRequest = new SearchRequest(index);
	searchRequest.source(searchSourceBuilder);
	@SuppressWarnings("deprecation")
	SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	return searchResponse;
	
	}

}
