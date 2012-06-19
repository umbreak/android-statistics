package rest.resources;

import hibernate.db.DB_Process;
import jabx.model.BaseChartModel;
import jabx.model.CategoryModel;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletContext;
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

import com.google.common.collect.Ordering;
@Path("/categories")
public class CategoriesResource {
	@Context UriInfo uriInfo;
	@Context Request request;
	@Context ServletContext context;
	@HeaderParam("chemnitz_token") @DefaultValue("") String token;
	private int user_id;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<CategoryModel> getCategories(){
		try{
			return new ArrayList<CategoryModel>(DB_Process.getCategories());
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/charts")
	public ArrayList<BaseChartModel> getCategoryCharts(@PathParam("id") int id){
		try{
			return new ArrayList<BaseChartModel> (Ordering.from(DB_Process.i.getDate_comparator()).sortedCopy(DB_Process.getCategoryCharts(id)));
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public CategoryModel getCategory(@PathParam("id") int id){
		try{
			return DB_Process.getCategory(id);
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

}
