package hibernate.db;

import jabx.model.UserModel;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import utils.HashUtils;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class ClientTester {
	public static void main(String[] args) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		//		Comment comment= new Comment("Me cago en dios");
		UserModel didac=new UserModel();
//		didac.setId(2);
		
		//		comment.setUser(didac);
		//		service.path("rest").path("site/1/comment/").accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).put(Comment.class,comment);

		//		Checkin checkin = new Checkin();
		//		
		//		
		//		checkin.setUser(adrien);
		//		service.path("rest").path("site/1/checkin/").accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).put(Checkin.class,checkin);
		//		
		//		checkin.setUser(didac);
		//		service.path("rest").path("site/2/checkin/2").queryParam("user_id", "2").accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).delete();

		String uuid=UUID.randomUUID().toString();
		String date=(service.path("rest").path("date").header("TU_challenge", uuid).accept(MediaType.APPLICATION_JSON_TYPE).get(String.class));
		System.out.println("Date received from server to challenge="+date);
		didac.setEmail("Didac@tu-chemnitz.de");
//		String hashed_password=HashUtils.i.getHash("Didac");
		
		String token=HashUtils.i.getHash("Didac"+date);
		System.out.println(token);
		didac.setPassword(token);
		service.path("rest").path("users/login").header("TU_challenge", uuid).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).post(didac);
//		System.out.println( service.path("rest").path("site").header("Eetac_token", token).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).get(String.class));
//		System.out.println( service.path("rest").path("site/4").header("Eetac_token", token).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).get(String.class));
//		System.out.println( service.path("rest").path("site/4/comment").header("Eetac_token", token).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).get(String.class));
// 		System.out.println( service.path("rest").path("site/4/picture/32/comment").header("Eetac_token", token).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).get(String.class));
// 	
		
		
		
//		List<Integer> integers= new ArrayList<Integer>();
// 		integers.add(1);
// 		integers.add(2);
// 		
// 		service.path("rest").path("site").header("Eetac_token", token).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).post(Integer.class,integers);

		
		
		
 		// 		service.path("rest").path("site/1/comment/1").header("Eetac_token", token).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).delete();
//		System.out.println( service.path("rest").path("site/3").header("Eetac_token", token).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).get(String.class));
//		System.out.println( service.path("rest").path("site/4").header("Eetac_token", token).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).get(String.class));
//		System.out.println( service.path("rest").path("site/5").header("Eetac_token", token).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).get(String.class));
//		System.out.println( service.path("rest").path("site/6").header("Eetac_token", token).accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).get(String.class));


	}
	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/TU-Charts-Server").build();
	}
}
