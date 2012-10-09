package rest.resources;

import hibernate.db.DB_Process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import rest.resources.ChartsResource.PredicateDate;

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
@Path("/categories")
public class CategoriesResource {
	@Context UriInfo uriInfo;
	@Context Request request;
	@Context ServletContext context;
	@HeaderParam("chemnitz_token") @DefaultValue("") String token;
	private String user_email;
	private UserModel user;

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
	public ArrayList<CategoryModel> getCategories() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		try{
			checkLogin();
			List<CategoryModel> categories= DB_Process.i.getCategories();
			Collection<CategoryModel> allowedCat= Collections2.filter(categories, new PredicateCategories(user.getCategories_denied()));
			ArrayList<CategoryModel> listCats=new ArrayList<>(allowedCat);
			return listCats;
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
			if (user.getCategories_denied() != null)
				if (Ints.contains(user.getCategories_denied(), id))
					throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
			List<BaseChartModel> charts= DB_Process.i.getCategoryCharts(id);
			Collection<BaseChartModel> allowedCharts= Collections2.filter(charts, new PredicateChart(user.getCharts_denied()));
			return new ArrayList<BaseChartModel> (Ordering.from(DB_Process.i.getDate_comparator()).sortedCopy(allowedCharts));
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

	static class PredicateCategories implements Predicate<CategoryModel>{
		private int categories_denied[];
		public PredicateCategories(int categories_denied[]) {
			super();
			this.categories_denied=categories_denied;
		}
		@Override
		public boolean apply(@Nullable CategoryModel category) {
			if (categories_denied == null) return true;
			return !Ints.contains(categories_denied, category.getId());
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

}
