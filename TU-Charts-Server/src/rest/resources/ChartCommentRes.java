package rest.resources;

import hibernate.db.DB_Process;
import jabx.model.CommentModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import javax.xml.bind.JAXBElement;

import com.google.common.collect.Ordering;


public class ChartCommentRes {
	@Context UriInfo uriInfo;
	@Context Request request;
	private int id;
	private String user_email;
	public ChartCommentRes(UriInfo uriInfo, Request request, int id, String user_email) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id=id;
		this.user_email=user_email;
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public ArrayList<CommentModel> getComments() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		try{
			ArrayList<CommentModel> result= new ArrayList<CommentModel>(DB_Process.i.getChart(id).getComments());
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
			return DB_Process.i.getComment(id, comment_id);
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public CommentModel putComment(CommentModel c) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		try{
			//			CommentModel c=comment.getValue();
			c.setDate(new Date());
			if (user_email != null)
				DB_Process.i.setComment(id, c,user_email );
			else
				DB_Process.i.setComment(id, c,"Didac@tu-chemnitz.de" );
			return c;
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public String delComment(@PathParam("id") int comment_id) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		try{
			DB_Process.i.delComment(comment_id);
			return String.valueOf(comment_id);
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
}
