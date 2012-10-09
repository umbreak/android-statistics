package rest.resources;
//Response status: http://jersey.java.net/nonav/apidocs/1.10/jersey/javax/ws/rs/core/Response.Status.html
import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import utils.AuthManager;


@Path("/date")
public class ChallengeResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getDate(@DefaultValue("") @HeaderParam("TU_challenge") String challenge_user) {  
		if (!challenge_user.isEmpty()){
			String uniqueDate=AuthManager.getUniqueDate();
			AuthManager.i.getChallenge_table().put(challenge_user, uniqueDate);
			return uniqueDate;
		}else{
			throw new WebApplicationException(Response.Status.FORBIDDEN);
		}
	}			
}