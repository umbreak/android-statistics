package rest.resources;

import static utils.ServerUtils.TYPE_AVERAGE;
import static utils.ServerUtils.TYPE_DISPERSION;
import static utils.ServerUtils.TYPE_DUPLICATES;
import static utils.ServerUtils.TYPE_DUPLICATES_2;
import static utils.ServerUtils.TYPE_ORIGINAL;
import hibernate.db.DB_Process;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import models.ChartModel;
import models.SerieModel;

import rest.AbstractChart;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;

public class ChartRes extends AbstractChart{
	@Context UriInfo uriInfo;
	@Context Request request;



	private ChartModel chart;
	private String user_email;
	public ChartRes(UriInfo uriInfo, Request request, ChartModel chart, String user_email) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.chart = chart;
		this.user_email=user_email;
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getChart(@DefaultValue("0") @QueryParam("x") int x, @DefaultValue("0") @QueryParam("y") int y, @DefaultValue("0") @QueryParam("month") int month,@DefaultValue("0") @QueryParam("week") int week , @QueryParam("day") int day, @QueryParam("year") int year, @DefaultValue("0") @QueryParam("type") int type) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException, ParseException {
		try{
			boolean expires=isDynamic(year, month, week, day, chart.getxValues());
			//Return the date requested
			if (month != 0 || day != 0 || year!=0 || week !=0){
				List<Integer> matches=getMatches(year, month, week, day, chart.getxValues());
				if (matches.size()==0)
					return null;
				modifYval(matches,chart.getyValues());
				chart.setxValues(modifXval(matches, chart.getxValues()));
			}
			
			if ((x == 0 && y == 0) || type == TYPE_ORIGINAL)
				return response(chart, expires);
			
			//Num of elements inside each of the groups The division is rounded up
			//			int sizeGroup=(chart.getxValues().length + x -1) / x;

			int sizeGroup = (int)Math.ceil((float)chart.getxValues().length/x);
			if (type == TYPE_DUPLICATES) sizeGroup*=2;

			if (sizeGroup < 2)
				return response(chart, expires);
			//Impossible to apply the reduction algorithm if there's more then one Y serie
			if (type == TYPE_DUPLICATES && chart.getyValues().size() == 1)
				type=TYPE_DUPLICATES_2;

			if (type == TYPE_DISPERSION && sizeGroup < 5)
				return response(chart, expires);

			//			int[] repetitions=new int[((chart.getxValues().length + numXgroups -1) / numXgroups)];
			List<List<Integer>> matchesMatrix= new ArrayList<>();
			//Choosing values from the Y axis (average)
			for (SerieModel serie : chart.getyValues()) {
				//Splitting the Y values in groups of Yval/android_screen_height

				List<List<Double>> groupYval=Lists.partition(Doubles.asList(serie.getYvalues()), sizeGroup);
				//				System.out.println("number of sub-lists=" + groupYval.size());
				//				System.out.println("Sublist size=" + groupYval.get(0).size());	
				//Choosing the average value of each group
				if (type == TYPE_DUPLICATES_2){
					double result[]=new double[0];
					for (List<Double> list : groupYval) {
						Set<Double> uniqueDoubles= Sets.newLinkedHashSet(list);
						//						System.out.println("Duplicates in the array=" + (list.size() - uniqueDoubles.size()));
						matchesMatrix.add(getSelectedPositions(uniqueDoubles, list));
						result=Doubles.concat(result,Doubles.toArray(uniqueDoubles));
					}
					serie.setYvalues(result);
				}
				else if (type == TYPE_DUPLICATES){
					ArrayList<Double> result=new ArrayList<>();
					for (List<Double> list : groupYval) {
						List<Double> uniqueDoubles= deleteDuplicates(list);
						result.addAll(uniqueDoubles);
					}
					serie.setYvalues(Doubles.toArray(result));
				}else if (type == TYPE_DISPERSION){
					serie.setYvalues(setMaxMinList(groupYval));
				}
				else
					serie.setYvalues(meanListY(groupYval));

			}

			//Choosing values from the X axis (average)
			List<List<Long>> groupXval = Lists.partition(Longs.asList(chart.getxValues()), sizeGroup);
			if (type==TYPE_DUPLICATES_2){
				long result[]=new long[0];
				int pos=0;
				for (List<Long> list: groupXval) {
					List<Integer> listMatches=matchesMatrix.get(pos);
					long[] concat=new long[listMatches.size()];
					for (int i = 0; i < concat.length; i++) 
						concat[i]=list.get(listMatches.get(i));
					result=Longs.concat(result,concat);
					pos++;
				}
				chart.setxValues(result);
			}else if (type==TYPE_DISPERSION){
				chart.setxValues(meanListForTwo(groupXval));
			}
			else if (type ==TYPE_AVERAGE)
				chart.setxValues(meanListX(groupXval));

			return response(chart, expires);

		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@GET
	@Path("series")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<SerieModel> getYValues() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		try{
			return chart.getyValues();
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@Path("comments")
	public ChartCommentRes getComments(){
		return new ChartCommentRes(uriInfo, request, chart, user_email);
	}


}
