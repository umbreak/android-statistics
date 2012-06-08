//package com.chart.restclient;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Collections;
//
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.http.ContentCodingType;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.client.ClientHttpRequest;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RequestCallback;
//import org.springframework.web.client.RestTemplate;
//import org.w3c.dom.Comment;
//
//import com.chart.pojos.CategoryModel;
//
//import android.graphics.Bitmap;
//import android.graphics.Picture;
//import android.util.Log;
///**
// * Singleton that perform the HTTP connection with the Server and creates the correct JSON structure (using REST).
// * Uses the Spring Framework for Android.
// * Obtain the response and Serialize (with gson internally) automatically to the desired Object.
// * Used by the IntentServer (PUTServer and GETServer)*/
//
//public enum Processor {
//	i;
//	//	private static final String url="http://147.83.7.85:8080/DXAT_Server/rest";
//	private static String url;
//	//Date received from the server to make the hash
//
//	//Header: Eetac_token sent to the server
//	private static String token;
//
//	public static File cache_path;
//	public static final String TAG="RestClient";
//	private RestTemplate restTemplate;
//	private HttpEntity<?> requestEntity;
//	private HttpHeaders requestHeaders;
//	private Processor(){
//		restTemplate = new RestTemplate();
//		requestHeaders = new HttpHeaders();
//		requestHeaders.setAccept(Collections.singletonList(new MediaType("application","json")));
//		requestHeaders.setAcceptEncoding(Collections.singletonList(ContentCodingType.GZIP));
//		requestEntity = new HttpEntity<Object>(requestHeaders);
//	}
//	//Reading input stream, saving to file and returning the bitmap. From: http://evgeny-goldin.com/blog/category/spring/
//	private static final RequestCallback ACCEPT_CALLBACK =
//			new RequestCallback()
//	{
//		public void doWithRequest ( ClientHttpRequest request ) throws IOException
//		{
//			request.getHeaders().setAccept(Collections.singletonList(new MediaType("application","json")));
//			request.getHeaders().setAcceptEncoding(Collections.singletonList(ContentCodingType.GZIP));
//		}
//	};
//
////	public String postUser(User user){
////		try{
////			
////			requestHeaders.set("Eetac_challenge", UUID.randomUUID().toString());
////			requestEntity = new HttpEntity<Object>(requestHeaders);
////			ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/date/", HttpMethod.GET, requestEntity, String.class);
////			String challenge_date=responseEntity.getBody();
////			System.out.println("Date="+challenge_date);
////			
////			
////			String hashed_password=ToolKit.i.getHash(user.password);
////			token=ToolKit.i.getHash(hashed_password+challenge_date);
////			System.out.println("token="+token);
////			user.password=token;
////
////			HttpEntity<User> commentEntity = new HttpEntity<User>(user, requestHeaders);
////			responseEntity = restTemplate.exchange(url + "/user/", HttpMethod.POST, commentEntity, String.class);
////			String response=responseEntity.getBody();
////
////			requestHeaders.remove("Eetac_challenge");
////			requestHeaders.set("Eetac_token", token);
////			requestEntity = new HttpEntity<Object>(requestHeaders);
////			return response;
////
////		}catch(HttpClientErrorException e){
////			if (e.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE)){
////				Log.d(TAG, e.toString());
////				return "-1";
////			}
////			return "-3";
////		}catch(Exception e){
////			Log.d(TAG, e.toString());
////			return "-2";
////		}		
////	}
//
//	public CategoryModel[] getCategories(){
//		try{
//			ResponseEntity<ShortSite[]> responseEntity = restTemplate.exchange(url + "/site", HttpMethod.GET, requestEntity, ShortSite[].class);
//			return responseEntity.getBody();
//
//		}catch(HttpClientErrorException e){
//			Log.d(TAG, e.toString());
//			return null;
//		}	
//	}
//
//	public Site getSite(String site){
//		try{
//			ResponseEntity<Site> responseEntity = restTemplate.exchange(url + "/site/"+site, HttpMethod.GET, requestEntity, Site.class);
//			return responseEntity.getBody();
//
//		}catch(HttpClientErrorException e){
//			Log.d(TAG, e.toString());
//			return null;
//		}	
//	}
//	public ShortSite getShortSite(String site){
//		try{
//			ResponseEntity<ShortSite> responseEntity = restTemplate.exchange(url + "/site/"+site + "?type=short", HttpMethod.GET, requestEntity, ShortSite.class);
//			return responseEntity.getBody();
//
//		}catch(HttpClientErrorException e){
//			Log.d(TAG, e.toString());
//			return null;
//		}	
//	}
//	public Comment[] getSiteComments(String site){
//		try{
//			ResponseEntity<Comment[]> responseEntity = restTemplate.exchange(url + "/site/"+site+"/comment/", HttpMethod.GET, requestEntity, Comment[].class);
//			return responseEntity.getBody();
//
//		}catch(HttpClientErrorException e){
//			Log.d(TAG, e.toString());
//			return null;
//		}
//	}
//	public Comment[] getPictureComments(String site, String picture){
//		try{
//			ResponseEntity<Comment[]> responseEntity = restTemplate.exchange(url + "/site/"+site+"/picture/"+ picture + "/comment/", HttpMethod.GET, requestEntity, Comment[].class);
//			return responseEntity.getBody();
//
//		}catch(HttpClientErrorException e){
//			Log.d(TAG, e.toString());
//			return null;
//		}
//	}
//	public Comment putComment(String site, Comment c){
//		try{
//			HttpEntity<Comment> commentEntity = new HttpEntity<Comment>(c, requestHeaders);
//			ResponseEntity<Comment> responseEntity = restTemplate.exchange(url + "/site/"+site+"/comment/", HttpMethod.PUT, commentEntity, Comment.class);
//			return responseEntity.getBody();
//
//		}catch(HttpClientErrorException e){
//			Log.d(TAG, e.toString());
//			return null;
//		}	
//	}
//	
//	public Comment putComment(String site,String picture, Comment c){
//		try{
//			HttpEntity<Comment> commentEntity = new HttpEntity<Comment>(c, requestHeaders);
//			ResponseEntity<Comment> responseEntity = restTemplate.exchange(url + "/site/"+site+"/picture/"+picture+"/comment/", HttpMethod.PUT, commentEntity, Comment.class);
//			return responseEntity.getBody();
//
//		}catch(HttpClientErrorException e){
//			Log.d(TAG, e.toString());
//			return null;
//		}	
//	}
//
//	public int putCheckin(String site, Checkin checkin){
//		try{
//			HttpEntity<Checkin> checkinEntity = new HttpEntity<Checkin>(checkin, requestHeaders);
//			//			restTemplate.put(url + "/site/"+site+"/checkin/", checkinEntity);
//			ResponseEntity<Integer> responseEntity = restTemplate.exchange(url + "/site/"+site+"/checkin/", HttpMethod.PUT, checkinEntity, Integer.class);
//			return responseEntity.getBody();
//
//		}catch(HttpClientErrorException e){
//			Log.d(TAG, e.toString());
//			if (e.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE))
//				return -2;
//			else
//				return -3;
//		}	
//	}
//
//	public boolean deleteCheckin(String site, int checkin_id){
//		try{
//			//			HttpEntity<Checkin_PJ> checkinEntity = new HttpEntity<Checkin_PJ>(checkin, requestHeaders);
//			//			restTemplate.put(url + "/site/"+site+"/checkin/", commentEntity);
//			restTemplate.exchange(url + "/site/"+site+"/checkin/"+ checkin_id, HttpMethod.DELETE, requestEntity, String.class);
////			restTemplate.delete(url + "/site/"+site+"/checkin/" + checkin_id);
//			return true;
//			//			ResponseEntity<Integer> responseEntity = restTemplate.exchange(url + "/user/", HttpMethod.DELETE, commentEntity, Integer.class);
//			//			return responseEntity.getBody();
//
//		}catch(HttpClientErrorException e){
//			Log.d(TAG, e.toString());
//			return false;
//		}	
//	}
//	public Bitmap getImage(String path){
//		try{
//			return restTemplate.execute(url_image + "/" + path, HttpMethod.GET, ACCEPT_CALLBACK, new BitmapResponseExtractor(new File(cache_path,path.substring(path.lastIndexOf('/') + 1))));
//			//			ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url_image + "/site/"+path, HttpMethod.GET, requestEntity, byte[].class);
//			//			return responseEntity.getBody();
//
//		}catch(HttpClientErrorException e){
//			Log.d(TAG, e.toString());
//			return null;
//		}
//	}
//	public Picture postImage(String site, String file_url){
//		try{
//			MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
//			parts.add("file", new FileSystemResource(new File(file_url)));
//
//			HttpEntity<MultiValueMap<String, Object>> imageEntity = new HttpEntity<MultiValueMap<String, Object>>(parts, requestHeaders);
//			ResponseEntity<Picture> responseEntity = restTemplate.exchange(url+"/site/"+site+"/picture/", HttpMethod.POST, imageEntity, Picture.class);
//			return responseEntity.getBody();
//
//
//		}catch(HttpClientErrorException e){
//			Log.d(TAG, e.toString());
//			return null;
//		}	
//	}
//
//
//	public static void setUrl(String url) {
//		//		Processor.url="http://192.168.1.4:8080/DXAT_Server/rest";
//		Processor.url = "http://" + url + ":8080/TU-Charts-Server/rest/";
//	}	
//}