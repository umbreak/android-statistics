package rest.resources;

import hibernate.db.DB_Process;
import jabx.model.BaseChartModel;
import jabx.model.ChartModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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

import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

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
			return new ArrayList<BaseChartModel>(DB_Process.getCharts());
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@GET
	@Path("new")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<BaseChartModel> getNewCharts(){
		try{
			Comparator<BaseChartModel> date_comparator = new Comparator<BaseChartModel>() {
				@Override
				public int compare(BaseChartModel o1, BaseChartModel o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			};

			ArrayList<BaseChartModel> charts=new ArrayList<BaseChartModel>(DB_Process.getCharts());
//			Collections.sort(charts,date_comparator);
			
			
			return new ArrayList<BaseChartModel> (Iterables.get(Iterables.partition
					(Ordering.from(date_comparator).reverse().sortedCopy(charts), 5),0));
			
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@Path("{chart}")
	public ChartRes getChart(@PathParam("chart") int id){
		return new ChartRes(uriInfo, request, id);
	}


}
