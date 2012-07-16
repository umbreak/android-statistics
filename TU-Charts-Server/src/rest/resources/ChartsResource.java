package rest.resources;

import hibernate.db.DB_Process;
import jabx.model.BaseChartModel;
import jabx.model.ChartModel;
import jabx.model.UserTokenTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import rest.tables.AuthManager;

import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

@Path("/charts")
public class ChartsResource {
	@Context UriInfo uriInfo;
	@Context Request request;
	@Context ServletContext context;
	@HeaderParam("chemnitz_token") @DefaultValue("") String token;
	private String user_email;	

	public ChartsResource() {
		super();
	}
	
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
	public ArrayList<BaseChartModel> getCharts(@DefaultValue("date") @QueryParam("sort") String sort, @DefaultValue("") @QueryParam("concrete") String concrete) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		try{
			checkLogin();
			if (!concrete.isEmpty()){
				ArrayList<BaseChartModel> result= new ArrayList<>();
				String chart_ids[]=concrete.split(";");
				for (String id : chart_ids){ 
					BaseChartModel chart=DB_Process.i.getBaseChart(Integer.parseInt(id));
					if (chart!=null)
						result.add(chart);
				}
				return result;
			}
			if (sort.equalsIgnoreCase("name"))
				return new ArrayList<BaseChartModel> (Ordering.from(DB_Process.i.getName_comparator()).sortedCopy(DB_Process.i.getCharts()));
			else if (sort.equalsIgnoreCase("popular"))
				return new ArrayList<BaseChartModel> (Ordering.from(DB_Process.i.getPopularity_comparator()).sortedCopy(DB_Process.i.getCharts()));
			else
				return new ArrayList<BaseChartModel> (Ordering.from(DB_Process.i.getDate_comparator()).sortedCopy(DB_Process.i.getCharts()));
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@GET
	@Path("new")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<BaseChartModel> getNewCharts() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		try{
			checkLogin();

			ArrayList<BaseChartModel> charts=new ArrayList<BaseChartModel>(DB_Process.i.getCharts());
			//			Collections.sort(charts,date_comparator);


			return new ArrayList<BaseChartModel> (Iterables.get(Iterables.partition
					(Ordering.from(DB_Process.i.getDate_comparator()).sortedCopy(charts), 5),0));

		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@Path("{chart}")
	public ChartRes getChart(@PathParam("chart") int id){
		checkLogin();
		return new ChartRes(uriInfo, request, id, user_email);
	}


}
