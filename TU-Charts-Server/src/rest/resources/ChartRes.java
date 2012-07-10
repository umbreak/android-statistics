package rest.resources;

import hibernate.db.DB_Process;
import hibernate.db.ServerUtils;
import jabx.model.ChartModel;
import jabx.model.SerieModel;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;

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
	public ChartModel getChart(@DefaultValue("0") @QueryParam("x") int x, @DefaultValue("0") @QueryParam("y") int y, @DefaultValue("0") @QueryParam("month") int month) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException, ParseException {
		try{
			ChartModel chart=DB_Process.i.getChart(id);
			if (month != 0){
				List<Integer> matches=getMatches(month, chart.getxValues());
				if (matches.size()==0)
					return null;
				modifYval(matches,chart.getyValues());
				chart.setxValues(modifXval(matches, chart.getxValues()));
			}
			if (x == 0 && y == 0)
				return chart;
			int numXgroups=(chart.getxValues().length + x -1) / x;
			//			int numYgroups=(series.get(0).getYvalues().length + y -1) / y;

			if (numXgroups < 2)
				return chart;

			//Choosing values from the Y axis (average)
			for (SerieModel serie : chart.getyValues()) {
				//Splitting the Y values in groups of Yval/android_screen_height
				List<List<Double>> groupYval=Lists.partition(Doubles.asList(serie.getYvalues()), numXgroups);
				System.out.println("number of sub-lists=" + groupYval.size());
				System.out.println("Sublist size=" + groupYval.get(0).size());				
				//Choosing the average value of each group
				serie.setYvalues(meanList(groupYval));
			}

			//Choosing values from the X axis (average)
			if (!isNum(chart.getxValues()[0])){
				Date xDate[]=toDate(chart.getxValues());
				List<List<Date>> groupXval=Lists.partition(Lists.newArrayList(xDate), numXgroups);
				System.out.println("number of sub-dates=" + groupXval.size());
				System.out.println("SubDates size=" + groupXval.get(0).size());
				//				for (Date d : groupXval.get(0))
				//					System.out.println(d);
				chart.setxValues(toString(meanList(groupXval, Date.class)));
				//				System.out.println("get average of the first subgroup=" + chart.getxValues()[0]);

			}
			return chart;

		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@GET
	@Path("series")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<SerieModel> getValues() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		try{
			return DB_Process.i.getChart(id).getyValues();
		}catch(NullPointerException e){
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@Path("comments")
	public ChartCommentRes getComments(){
		return new ChartCommentRes(uriInfo, request, id);
	}

	//	private double mean(double[] serie){
	//		double sum=0.0;
	//		for (Double val : serie)
	//			sum += val;
	//		return sum/serie.length;
	//	}
	//	private double[] meanSerie(List<List<Double>> serie_group){
	//		double[] result= new double[serie_group.size()];
	//		int pos=0;
	//		for (List<Double> : serie_group) {
	//			result[pos]=mean(values);
	//			pos++;
	//		}
	//		System.out.println("Mean Serie=" + Arrays.toString(result));
	//
	//		return result;
	//	}

	private boolean isNum(String s) {
		try {Double.parseDouble(s);}
		catch (NumberFormatException nfe) {return false;}
		return true;
	}
	private Date[] toDate(String[] values){
		Date result[]=new Date[values.length];
		try {
			for (int i = 0; i < values.length; i++)
				result[i]=ServerUtils.i.getDataFormat().parse(values[i]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	private String[] toString(Date[] values){
		String result[]=new String[values.length];
		for (int i = 0; i < values.length; i++)
			result[i]=ServerUtils.i.getDataFormat().format(values[i]);
		return result;
	}

	//	private Date mean(Date[] array){
	//		long sum=0;
	//		for (Date date : array) {
	//			sum+=date.getTime();
	//		}
	//		return new Date(sum/array.length);
	//	}
	//	private Date[] meanDate(List<Date> date_group){
	//		Date[] result= new Date[date_group.size()];
	//		int pos=0;
	//		for (Date[] values : date_group) {
	//			result[pos]=mean(values);
	//			pos++;
	//		}
	//		System.out.println("Mean Date=" + Arrays.toString(result));
	//		return result;
	//	}
	private <T> T mean(List<T> array, Class<T> clazz){
		double sum=0;

		for (T val : array) {
			if (clazz == Date.class)
				sum+=((Date)val).getTime();
			else
				sum += ((Double)val);
		}
		try {
			if (clazz == Date.class)
				return clazz.getConstructor(long.class).newInstance((long)sum/array.size());
			else
				return clazz.getConstructor(double.class).newInstance((double)sum/array.size());

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException |InvocationTargetException | NoSuchMethodException | SecurityException e) 
		{e.printStackTrace(); return null;} 
	}

	private <T> T[] meanList(List<List<T>> list, Class<T> clazz){
		T[] result=(T[])Array.newInstance(clazz,list.size());
		int pos=0;
		for (List<T> values : list) {
			result[pos]=mean(values, clazz);
			pos++;
		}
		return result;
	}
	private double[] meanList(List<List<Double>> list){
		double result[] = new double[list.size()];
		int pos=0;
		for (List<Double> values: list) {
			result[pos] = mean(values,Double.class);
			pos++;
		}
		return result;
	}
	private List<Integer> getMatches(int m, String[] xValues) throws ParseException{
		List<Integer> result=new ArrayList<Integer>();
		String month=(m < 10 ? "0" : "") + m;
		for (int i = 0; i < xValues.length; i++)
			if (xValues[i].substring(3, 5).equals(month))	

				result.add(i);	

		return result;
	}
	private void modifYval(List<Integer> xValues, Set<SerieModel> y_results_tmp){

		//Modif Y Values
		for (SerieModel serieModel : y_results_tmp) {
			double[] data=new double[xValues.size()];
			for (int i=0; i<xValues.size();i++)
				data[i]=serieModel.getYvalues()[xValues.get(i)];
			serieModel.setYvalues(data);
		}
	}
	private String[] modifXval(List<Integer> xValues, String [] x_results_tmp){
		String[] x_results=new String[xValues.size()];

		//Modif X Values
		for (int i=0; i< xValues.size(); i++)
			x_results[i]=x_results_tmp[xValues.get(i)];

		return x_results;
	}

}
