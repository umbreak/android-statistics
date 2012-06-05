package rest.resources;

import hibernate.db.SessionFactoryHibernate;
import jabx.model.BaseChartModel;
import jabx.model.ChartModel;
import java.util.ArrayList;
import java.util.HashSet;
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


import rest.tables.ModelTables;

@Path("/charts")
public class ChartsResource {
	@Context UriInfo uriInfo;
	@Context Request request;
	@Context ServletContext context;
	@HeaderParam("chemnitz_token") @DefaultValue("") String token;
	private int user_id;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<BaseChartModel> getCharts(){
		try{
			return new ArrayList<BaseChartModel>(ModelTables.i.getCharts().values());
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@GET
	@Path("new")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<ChartModel> getNewCharts(){
		try{
			Set<ChartModel> newCharts= new HashSet<>();
			newCharts.add(ModelTables.i.getCharts().get(0));
			newCharts.add(ModelTables.i.getCharts().get(2));
			return newCharts;
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@Path("{chart}")
	public ChartRes getChart(@PathParam("chart") int id){
		return new ChartRes(uriInfo, request, id);
	}


}
