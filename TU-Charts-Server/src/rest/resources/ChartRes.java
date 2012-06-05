package rest.resources;

import jabx.model.ChartModel;
import jabx.model.SerieModel;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import rest.tables.ModelTables;

public class ChartRes {
	@Context UriInfo uriInfo;
	@Context Request request;
	private int id;
	public ChartRes(UriInfo uriInfo, Request request, int id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public ChartModel getChart() {
		try{
			return ModelTables.i.getCharts().get(id);
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@GET
	@Path("line")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<SerieModel> getValues(){
		try{
			return ModelTables.i.getCharts().get(id).getyValues();
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
	
	@Path("comments")
	public ChartCommentRes getComments(){
		return new ChartCommentRes(uriInfo, request, id);
	}

}
