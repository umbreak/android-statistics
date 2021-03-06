package rest.resources;

import static utils.ServerUtils.NULL_VAL;
import hibernate.db.DB_Process;
import static utils.ServerUtils.MINIMUM_NEW_DATE;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;
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

import rest.resources.CategoriesResource.PredicateCategories;

import models.BaseChartModel;
import models.CategoryModel;
import models.ChartModel;
import models.UserModel;
import models.UserTokenTime;

import utils.AuthManager;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

@Path("/charts")
public class ChartsResource {
	@Context UriInfo uriInfo;
	@Context Request request;
	@Context ServletContext context;
	@HeaderParam("chemnitz_token") @DefaultValue("") String token;
	private String user_email;	
	private UserModel user;

	public ChartsResource() {
		super();
	}

	private void checkLogin(){
		try{
			UserTokenTime user_token_time=AuthManager.i.getToken_table().get(token);
			user_email=user_token_time.getEmail();
			user_token_time.updateLastAction();
			user= DB_Process.i.getUser(user_email);
		}catch (Exception e){
			throw new WebApplicationException(Response.Status.FORBIDDEN);
			//			System.out.println("NOT LOGGED");
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
			List<BaseChartModel> charts=DB_Process.i.getCharts();
			Collection<BaseChartModel>	allowedCharts=Collections2.filter(charts, new PredicateChart(user.getCharts_denied()));
			allowedCharts=Collections2.filter(allowedCharts, new PredicateChartCat(user.getCategories_denied()));


			if (sort.equalsIgnoreCase("name"))
				return new ArrayList<BaseChartModel> (Ordering.from(DB_Process.i.getName_comparator()).sortedCopy(allowedCharts));
			else if (sort.equalsIgnoreCase("popular"))
				return new ArrayList<BaseChartModel> (Ordering.from(DB_Process.i.getPopularity_comparator()).sortedCopy(allowedCharts));
			else
				return new ArrayList<BaseChartModel> (Ordering.from(DB_Process.i.getDate_comparator()).sortedCopy(allowedCharts));
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
			boolean found=false;
			int i=0;
			Collection<BaseChartModel> dateCharts=null;

			List<BaseChartModel> charts = DB_Process.i.getCharts();

			Calendar cal=Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());	
			while (i < 3 && !found){
				cal.add(Calendar.DAY_OF_MONTH, MINIMUM_NEW_DATE);
				dateCharts= Collections2.filter(charts, new PredicateDate(cal.getTime()));
				if (dateCharts.size() != 0 )found=true;
				i++;
			}
			dateCharts=Collections2.filter(dateCharts, new PredicateChart(user.getCharts_denied()));
			return new ArrayList<>(Ordering.from(DB_Process.i.getDate_comparator()).sortedCopy(dateCharts));


			//			return new ArrayList<BaseChartModel> (Iterables.get(Iterables.partition
			//					(Ordering.from(DB_Process.i.getDate_comparator()).sortedCopy(charts), 5),0));

		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/null")
	public Double getDoubles(){
		return NULL_VAL;
	}

	@Path("{chart}")
	public ChartRes getChart(@PathParam("chart") int id) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		checkLogin();
		ChartModel chart = DB_Process.i.getChart(id);
		if (user.getCharts_denied() != null)
			if (Ints.contains(user.getCharts_denied(), id))
				throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);

		if (user.getCategories_denied() != null)
			if (Ints.contains(user.getCategories_denied(), chart.getCategory().getId()))
				throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);

		return new ChartRes(uriInfo, request, chart, user_email);
	}

	static class PredicateDate implements Predicate<BaseChartModel>{
		private Date date;
		public PredicateDate(Date date) {
			super();
			this.date=date;
		}
		@Override
		public boolean apply(@Nullable BaseChartModel chart) {
			return chart.getDate().after(date);
		}
	}
	static class PredicateChart implements Predicate<BaseChartModel>{
		private int chart_denied[];
		public PredicateChart(int chart_denied[]) {
			super();
			this.chart_denied=chart_denied;
		}
		@Override
		public boolean apply(@Nullable BaseChartModel chart) {
			if (chart_denied == null) return true;

			return !Ints.contains(chart_denied, chart.getId());
		}
	}
	static class PredicateChartCat implements Predicate<BaseChartModel>{
		private int categories_denied[];
		public PredicateChartCat(int categories_denied[]) {
			super();
			this.categories_denied=categories_denied;
		}
		@Override
		public boolean apply(@Nullable BaseChartModel chart) {
			if (categories_denied == null) return true;

			return !Ints.contains(categories_denied, chart.getCategory().getId());
		}
	}


}
