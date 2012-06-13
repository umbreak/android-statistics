package com.chart.restclient;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

import com.chart.pojos.BaseChartModel;
import com.chart.pojos.CategoryModel;
import com.chart.pojos.ChartModel;
import com.chart.pojos.CommentModel;
import com.chart.pojos.SerieModel;
/**
 * Singleton that perform the HTTP connection with the Server and creates the correct JSON structure (using REST).
 * Uses the Spring Framework for Android.
 * Obtain the response and Serialize (with gson internally) automatically to the desired Object.
 * Used by the IntentServer (PUTServer and GETServer)*/

public enum Processor {
	i;
//	private static final String url="http://134.109.4.10:8080/TU-Charts-Server/rest/";
	private static final String url="http://192.168.137.1:8080/TU-Charts-Server/rest/";

	//Date received from the server to make the hash

	//Header: Eetac_token sent to the server
	private static String token;

	public static File cache_path;
	public static final String TAG="RestClient";
	private RestTemplate restTemplate;
	private HttpEntity<?> requestEntity;
	private HttpHeaders requestHeaders;
	private Processor(){
		restTemplate = new RestTemplate();
		requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));
		requestHeaders.setAcceptEncoding(Collections.singletonList(ContentCodingType.GZIP));
		requestEntity = new HttpEntity<Object>(requestHeaders);
//		restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

	}
	//Reading input stream, saving to file and returning the bitmap. From: http://evgeny-goldin.com/blog/category/spring/
//	private static final RequestCallback ACCEPT_CALLBACK =
//			new RequestCallback()
//	{
//		public void doWithRequest ( ClientHttpRequest request ) throws IOException
//		{
//			request.getHeaders().setAccept(Collections.singletonList(new MediaType("application","json")));
//			request.getHeaders().setAcceptEncoding(Collections.singletonList(ContentCodingType.GZIP));
//		}
//	};

	//	public String postUser(User user){
	//		try{
	//			
	//			requestHeaders.set("Eetac_challenge", UUID.randomUUID().toString());
	//			requestEntity = new HttpEntity<Object>(requestHeaders);
	//			ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/date/", HttpMethod.GET, requestEntity, String.class);
	//			String challenge_date=responseEntity.getBody();
	//			System.out.println("Date="+challenge_date);
	//			
	//			
	//			String hashed_password=ToolKit.i.getHash(user.password);
	//			token=ToolKit.i.getHash(hashed_password+challenge_date);
	//			System.out.println("token="+token);
	//			user.password=token;
	//
	//			HttpEntity<User> commentEntity = new HttpEntity<User>(user, requestHeaders);
	//			responseEntity = restTemplate.exchange(url + "/user/", HttpMethod.POST, commentEntity, String.class);
	//			String response=responseEntity.getBody();
	//
	//			requestHeaders.remove("Eetac_challenge");
	//			requestHeaders.set("Eetac_token", token);
	//			requestEntity = new HttpEntity<Object>(requestHeaders);
	//			return response;
	//
	//		}catch(HttpClientErrorException e){
	//			if (e.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE)){
	//				Log.d(TAG, e.toString());
	//				return "-1";
	//			}
	//			return "-3";
	//		}catch(Exception e){
	//			Log.d(TAG, e.toString());
	//			return "-2";
	//		}		
	//	}
	//CATEGORIES ------------------------------------------>

	public CategoryModel[] getCategories(){
		try{
			ResponseEntity<CategoryModel[]> responseEntity = restTemplate.exchange(url + "categories", HttpMethod.GET, requestEntity, CategoryModel[].class);
			System.out.println("category array=" + Arrays.toString(responseEntity.getBody()));
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			Log.d(TAG, e.toString());
			return null;
		}	
	}

	public CategoryModel getCategory(int category_id){
		try{
			ResponseEntity<CategoryModel> responseEntity = restTemplate.exchange(url + "categories/"+category_id, HttpMethod.GET, requestEntity, CategoryModel.class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			Log.d(TAG, e.toString());
			return null;
		}	
	}

	//CHARTS ------------------------------------------>

	public BaseChartModel[] getCharts(){
		try{
			ResponseEntity<BaseChartModel[]> responseEntity = restTemplate.exchange(url + "charts", HttpMethod.GET, requestEntity, BaseChartModel[].class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			Log.d(TAG, e.toString());
			return null;
		}	
	}
	//sort types: name, popular
	public BaseChartModel[] getCharts(String sort){
		try{
			ResponseEntity<BaseChartModel[]> responseEntity = restTemplate.exchange(url + "charts/sort?="+sort, HttpMethod.GET, requestEntity, BaseChartModel[].class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			Log.d(TAG, e.toString());
			return null;
		}	
	}
	public BaseChartModel[] getNewCharts(){
		try{
			ResponseEntity<BaseChartModel[]> responseEntity = restTemplate.exchange(url + "charts/new", HttpMethod.GET, requestEntity, BaseChartModel[].class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			Log.d(TAG, e.toString());
			return null;
		}	
	}
	public ChartModel getChart(int chart_id){
		try{
			ResponseEntity<ChartModel> responseEntity = restTemplate.exchange(url + "charts/"+chart_id, HttpMethod.GET, requestEntity, ChartModel.class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			Log.d(TAG, e.toString());
			return null;
		}	
	}
	public BaseChartModel getShortChart(int chart_id){
		try{
			ResponseEntity<BaseChartModel> responseEntity = restTemplate.exchange(url + "charts/"+chart_id, HttpMethod.GET, requestEntity, BaseChartModel.class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			Log.d(TAG, e.toString());
			return null;
		}	
	}
	public SerieModel[] getChartSeries(int chart_id){
		try{
			ResponseEntity<SerieModel[]> responseEntity = restTemplate.exchange(url + "charts/"+chart_id + "/series", HttpMethod.GET, requestEntity, SerieModel[].class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			Log.d(TAG, e.toString());
			return null;
		}	
	}

	//COMMENTS ------------------------------------------>

	public CommentModel[] getChartComments(int chart_id){
		try{
			ResponseEntity<CommentModel[]> responseEntity = restTemplate.exchange(url + "charts/"+chart_id + "/comments", HttpMethod.GET, requestEntity, CommentModel[].class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			Log.d(TAG, e.toString());
			return null;
		}	
	}

	public Integer putComment(int chart_id, CommentModel c){
		try{
			HttpEntity<CommentModel> commentEntity = new HttpEntity<CommentModel>(c, requestHeaders);
			ResponseEntity<Integer> responseEntity = restTemplate.exchange(url + "charts/"+chart_id+"/comments", HttpMethod.PUT, commentEntity, Integer.class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			Log.d(TAG, e.toString());
			return 0;
		}	
	}
	public boolean deleteComment(int chart_id, int comment_id){
		try{
			restTemplate.exchange(url + "charts/"+chart_id+"/comments/"+ comment_id, HttpMethod.DELETE, requestEntity, String.class);
			return true;

		}catch(HttpClientErrorException e){
			Log.d(TAG, e.toString());
			return false;
		}	
	}

//	public static void setUrl(String url) {
//		Processor.url = "http://" + url + ":8080/TU-Charts-Server/rest/";
//	}	
}