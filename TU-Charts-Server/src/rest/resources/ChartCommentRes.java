package rest.resources;

import hibernate.db.DB_Process;
import jabx.model.CommentModel;

import java.util.Date;
import java.util.Set;

import javax.ws.rs.Consumes;
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


public class ChartCommentRes {
	@Context UriInfo uriInfo;
	@Context Request request;
	private int id;
	public ChartCommentRes(UriInfo uriInfo, Request request, int id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id=id;
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Set<CommentModel> getComments() {
		try{
			return DB_Process.getChart(id).getComments();
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public CommentModel getComment(@PathParam("id") int comment_id){
		try{
			return DB_Process.getComment(id, comment_id);
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public CommentModel putComment(JAXBElement<CommentModel> comment) {
		CommentModel c=comment.getValue();
		c.setDate(new Date());
		DB_Process.setComment(c);
		return c;
	}
}
