package com.chart.restclient;

import static com.chart.AppUtils.NULL_VAL;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.util.Log;

import com.chart.AppUtils;
import com.chart.pojos.BaseChartModel;
import com.chart.pojos.CategoryModel;
import com.chart.pojos.ChartModel;
import com.chart.pojos.CommentModel;
import com.chart.pojos.SerieModel;
import com.chart.pojos.UserModel;
/**
 * Singleton that perform the HTTP connection with the Server and creates the correct JSON structure (using REST).
 * Uses the Spring Framework for Android.
 * Obtain the response and Serialize (with gson internally) automatically to the desired Object.
 * Used by the IntentServer (PUTServer and GETServer)*/

public enum Processor {
	i;
		private static final String url="http://134.109.4.10:8080/TU-Charts-Server/rest/";
//	private static final String url="http://192.168.137.1:8080/TU-Charts-Server/rest/";
	public Context context;
	public UserModel myUser;

	//Date received from the server to make the hash

	//Header: Eetac_token sent to the server
	private static String token;

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

	//LOGIN ----------------------------------------------->
	public int postUser(UserModel user){
		try{
			myUser=user;
			requestHeaders.set("TU_challenge", UUID.randomUUID().toString());
			requestEntity = new HttpEntity<Object>(requestHeaders);
			ResponseEntity<String> responseEntity = restTemplate.exchange(url + "date/", HttpMethod.GET, requestEntity, String.class);
			String challenge_date=responseEntity.getBody();
			if (NULL_VAL == null)
				NULL_VAL = restTemplate.exchange(url + "charts/null", HttpMethod.GET, requestEntity, Double.class).getBody();


			String hashed_password=AppUtils.i.getHash(user.password);
			token=AppUtils.i.getHash(hashed_password+challenge_date);
			System.out.println("token="+token);
			user.password=token;

			HttpEntity<UserModel> commentEntity = new HttpEntity<UserModel>(user, requestHeaders);
			restTemplate.postForLocation(url + "users/login", commentEntity);

			requestHeaders.remove("TU_challenge");
			requestHeaders.set("chemnitz_token", token);
			requestEntity = new HttpEntity<Object>(requestHeaders);
			return 1;

		}catch(HttpClientErrorException e){
			if (e.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE)){
				Log.d(TAG, e.toString());
				return -1;
			}
			return -3;
		}catch(Exception e){
			Log.d(TAG, e.toString());
			return -2;
		}		
	}
	//CATEGORIES ------------------------------------------>

	public CategoryModel[] getCategories(){
		try{
			ResponseEntity<CategoryModel[]> responseEntity = restTemplate.exchange(url + "categories", HttpMethod.GET, requestEntity, CategoryModel[].class);
			System.out.println("category array=" + Arrays.toString(responseEntity.getBody()));
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return getCategories();
			}
			Log.d(TAG, e.toString());
			return null;
		}	
	}

	public CategoryModel getCategory(int category_id){
		try{
			ResponseEntity<CategoryModel> responseEntity = restTemplate.exchange(url + "categories/"+category_id, HttpMethod.GET, requestEntity, CategoryModel.class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return getCategory(category_id);
			}
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
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return getCharts();
			}
			Log.d(TAG, e.toString());
			return null;
		}	
	}
	//sort types: name, popular
	public BaseChartModel[] getCharts(String sort){
		try{
			ResponseEntity<BaseChartModel[]> responseEntity = restTemplate.exchange(url + "charts?sort="+sort, HttpMethod.GET, requestEntity, BaseChartModel[].class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return getCharts(sort);
			}
			Log.d(TAG, e.toString());
			return null;
		}
	}
	public BaseChartModel[] getConcreteCharts(String concrete){
		try{
			ResponseEntity<BaseChartModel[]> responseEntity = restTemplate.exchange(url + "charts?concrete="+concrete, HttpMethod.GET, requestEntity, BaseChartModel[].class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return getConcreteCharts(concrete);
			}
			Log.d(TAG, e.toString());
			return null;
		}	
	}
	public BaseChartModel[] getNewCharts(){
		try{
			ResponseEntity<BaseChartModel[]> responseEntity = restTemplate.exchange(url + "charts/new", HttpMethod.GET, requestEntity, BaseChartModel[].class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return getNewCharts();
			}
			Log.d(TAG, e.toString());
			return null;
		}		
	}

	public BaseChartModel[] getCharts(int category_id){
		try{
			ResponseEntity<BaseChartModel[]> responseEntity = restTemplate.exchange(url + "categories/" + category_id + "/charts", HttpMethod.GET, requestEntity, BaseChartModel[].class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return getCharts(category_id);
			}
			Log.d(TAG, e.toString());
			return null;
		}
	}
	public synchronized ChartModel getChart(String path){
		try{
			ResponseEntity<ChartModel> responseEntity = restTemplate.exchange(url + path,HttpMethod.GET, requestEntity, ChartModel.class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return getChart(path);
			}
			Log.d(TAG, e.toString());
			return null;
		}
	}
	public BaseChartModel getShortChart(int chart_id){
		try{
			ResponseEntity<BaseChartModel> responseEntity = restTemplate.exchange(url + "charts/"+chart_id, HttpMethod.GET, requestEntity, BaseChartModel.class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return getShortChart(chart_id);
			}
			Log.d(TAG, e.toString());
			return null;
		}	
	}
	public SerieModel[] getChartSeries(int chart_id){
		try{
			ResponseEntity<SerieModel[]> responseEntity = restTemplate.exchange(url + "charts/"+chart_id + "/series", HttpMethod.GET, requestEntity, SerieModel[].class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return getChartSeries(chart_id);
			}
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
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return getChartComments(chart_id);
			}
			Log.d(TAG, e.toString());
			return null;
		}	
	}

	public CommentModel putComment(int chart_id, CommentModel c){
		try{
			HttpEntity<CommentModel> commentEntity = new HttpEntity<CommentModel>(c, requestHeaders);
			ResponseEntity<CommentModel> responseEntity = restTemplate.exchange(url + "charts/"+chart_id+"/comments", HttpMethod.PUT, commentEntity, CommentModel.class);
			return responseEntity.getBody();

		}catch(HttpClientErrorException e){
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return putComment(chart_id,c);
			}
			Log.d(TAG, e.toString());
			return null;
		}
	}
	public boolean deleteComment(int chart_id, int comment_id){
		try{
			restTemplate.exchange(url + "charts/"+chart_id+"/comments/"+ comment_id, HttpMethod.DELETE, requestEntity, String.class);
			return true;

		}catch(HttpClientErrorException e){
			if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
				Log.d(TAG, "Re-logging");
				if (postUser(myUser) > 0) return deleteComment(chart_id,comment_id);
			}
			Log.d(TAG, e.toString());
			return false;
		}
	}

	//	public static void setUrl(String url) {
	//		Processor.url = "http://" + url + ":8080/TU-Charts-Server/rest/";
	//	}	
}