package rest.resources;

import hibernate.db.DB_Process;
import jabx.model.UserModel;
import jabx.model.UserModelPass;
import jabx.model.UserTokenTime;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import utils.AuthManager;
import utils.HashUtils;

@Path("/users")
public class UsersResource {
	@Context UriInfo uriInfo;
	@Context Request request;
	@Context ServletContext context;
	@HeaderParam("chemnitz_token") @DefaultValue("") String token;
	private String user_email;

	public UsersResource() {
		super();
	}	

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response checkLogin(UserModelPass user, @DefaultValue("") @HeaderParam("TU_challenge") String challenge_user) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
	
		if (challenge_user.isEmpty())
			throw new WebApplicationException(Response.Status.FORBIDDEN);
		System.out.println("user pass=" + user.getPassword());
		System.out.println("user email=" + user.getEmail());

		UserModel realUser=DB_Process.i.getUser(user.getEmail());
		if (realUser !=null){
			String challenge_value=AuthManager.i.getChallenge_table().get(challenge_user);

			if (HashUtils.i.getHash((realUser.getPassword()+challenge_value)).equals(user.getPassword())){
				AuthManager.i.getChallenge_table().remove(challenge_user);
				AuthManager.i.getToken_table().put(user.getPassword(), new UserTokenTime(realUser.getEmail()));
				System.out.println("Adding token=" + user.getPassword() + " to user="+ realUser.getEmail());

				//				System.out.println("All token table=" + AuthManager.i.getToken_table());

				return Response.ok().build();
			}else throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);

		}else{
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserModel> getUsers() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {  
		try{
			return DB_Process.i.getUsers();
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public String puUser(UserModelPass user) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {  
		if (DB_Process.i.getUser(user.getEmail()) == null){
			DB_Process.i.setUser(new UserModel(user));
			String token=HashUtils.i.getHash(AuthManager.getUniqueDate()+user.getPassword());
			AuthManager.i.getToken_table().put(token,  new UserTokenTime(user.getEmail()));
			return token;
		}else throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
	}
}
