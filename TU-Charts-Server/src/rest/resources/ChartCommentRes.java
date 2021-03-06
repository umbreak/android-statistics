package rest.resources;

import hibernate.db.DB_Process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import models.ChartModel;
import models.CommentModel;


public class ChartCommentRes {
	@Context UriInfo uriInfo;
	@Context Request request;
	private String user_email;
	private ChartModel chart;
	public ChartCommentRes(UriInfo uriInfo, Request request, ChartModel chart, String user_email) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.chart=chart;
		this.user_email=user_email;
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public ArrayList<CommentModel> getComments() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		try{
			ArrayList<CommentModel> result= new ArrayList<CommentModel>(chart.getComments());
			Collections.sort(result);
			return result;
			//			return new ArrayList<CommentModel> (Ordering.from(DB_Process.i.getDate_comparator()).sortedCopy(DB_Process.getChart(id).getComments()));
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public CommentModel getComment(@PathParam("id") int comment_id) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		try{
			return DB_Process.i.getComment(chart.getId(), comment_id);
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public CommentModel putComment(CommentModel c) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		try{
			c.setDate(new Date());
			if (user_email != null)
				return DB_Process.i.setComment(chart.getId(), c,user_email );
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response delComment(@PathParam("id") int comment_id) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		try{
			DB_Process.i.delComment(comment_id);
			return Response.ok().build();
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
}
