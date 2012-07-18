package rest.resources;

import hibernate.db.DB_Process;
import jabx.model.BaseChartModel;
import jabx.model.CategoryModel;
import jabx.model.UserTokenTime;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import utils.AuthManager;

import com.google.common.collect.Ordering;
@Path("/categories")
public class CategoriesResource {
	@Context UriInfo uriInfo;
	@Context Request request;
	@Context ServletContext context;
	@HeaderParam("chemnitz_token") @DefaultValue("") String token;
	private String user_email;
	
	private void checkLogin(){
		try{
			UserTokenTime user_token_time=AuthManager.i.getToken_table().get(token);
			user_email=user_token_time.getEmail();
			user_token_time.updateLastAction();
		}catch (Exception e){
//			throw new WebApplicationException(Response.Status.FORBIDDEN);
			System.out.println("NOT LOGGED");
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<CategoryModel> getCategories() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		try{
			checkLogin();
			return new ArrayList<CategoryModel>(DB_Process.i.getCategories());
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/charts")
	public ArrayList<BaseChartModel> getCategoryCharts(@PathParam("id") int id) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		try{
			checkLogin();
			return new ArrayList<BaseChartModel> (Ordering.from(DB_Process.i.getDate_comparator()).sortedCopy(DB_Process.i.getCategoryCharts(id)));
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public CategoryModel getCategory(@PathParam("id") int id) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		try{
			checkLogin();
			return DB_Process.i.getCategory(id);
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

}
