package rest;

import static utils.ServerUtils.NULL_VAL;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.ChartModel;
import models.SerieModel;

import com.google.common.primitives.Doubles;

public class AbstractChart {
	private double meanDouble(List<Double> array){
		double sum=0;
		for (Double val : array) {
			sum+=val;
		}
		return (double)sum/array.size();
	}
	private long meanLong(List<Long> array){
		double sum=0;
		for (Long val : array) {
			sum+=val;
		}
		return (long)sum/array.size();
	}
	public Response response(ChartModel chart, boolean expires){
		Response.ResponseBuilder response = Response.ok(chart).type(MediaType.APPLICATION_JSON);
		if (expires) response.expires(new Date(2*System.currentTimeMillis()-chart.getDate().getTime()));
		return response.build();
	}

	public double[] meanListY(List<List<Double>> list){
		double result[] = new double[list.size()];
		int pos=0;
		for (List<Double> values: list) {
			result[pos] = meanDouble(values);
			pos++;
		}
		return result;
	}
	public long[] meanListX(List<List<Long>> list){
		long result[] = new long[list.size()];
		int pos=0;
		for (List<Long> values: list) {
			result[pos] = meanLong(values);
			pos++;
		}
		return result;
	}

	public long[] meanListForTwo(List<List<Long>> list){
		//		double result[] = new double[list.size()*2];
		//		ArrayList<Double> result=new ArrayList<>();
		int pos=0;
		long result[];

		result = new long[list.size()*4];
		for (List<Long> values: list) {
			long initial=values.get(0);
			long last=values.get(values.size()-1);
			long portion= (last-initial)/4;
			result[pos] = initial;
			pos++;

			result[pos]=initial+portion;
			pos++;
			result[pos]=last-portion;
			pos++;

			result[pos] = last;
			pos++;
		}
		return result;
	}

	public List<Integer> getMatches(int y, int m,int w, int d, long[] xValues) throws ParseException{
		List<Integer> result=new ArrayList<Integer>();
		Calendar calendar= Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, y);

		if (d != 0){
			calendar.set(Calendar.MONTH, m-1);
			calendar.set(Calendar.DAY_OF_MONTH, d);
			long firstVal=calendar.getTimeInMillis();

			calendar.set(Calendar.DAY_OF_MONTH, d+1);
			long lastVal=calendar.getTimeInMillis()-1;

			for (int i = 0; i < xValues.length; i++)
				if (xValues[i] >= firstVal && xValues[i] <= lastVal)
					result.add(i);	

			return result;

		}else if (w != 0){
			calendar.set(Calendar.MONTH, m-1);
			
			if (calendar.get(Calendar.WEEK_OF_MONTH) == 0){
				w=w-1;
				if (w != 0){
					calendar.set(Calendar.WEEK_OF_MONTH, w);
					calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
				}
			}else
				if (w != 1){
					calendar.set(Calendar.WEEK_OF_MONTH, w);
					calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
				}
			System.out.println("WEEK="+w);
			long firstVal=calendar.getTimeInMillis();

			calendar.clear();
			calendar.set(Calendar.YEAR, y);
			if (w== 5)
				calendar.set(Calendar.MONTH, m);
			else{
				calendar.set(Calendar.MONTH, m-1);
				calendar.set(Calendar.WEEK_OF_MONTH, w+1);
				calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

			}

			long lastVal=calendar.getTimeInMillis()-1;
			for (int i = 0; i < xValues.length; i++)
				if (xValues[i] >= firstVal && xValues[i] <= lastVal)
					result.add(i);
//			System.out.println("First=" + new Date(firstVal) + " Last=" + new Date(lastVal));
			return result;

		}else if (m != 0){
			calendar.set(Calendar.MONTH, m-1);
			long firstVal=calendar.getTimeInMillis();

			calendar.set(Calendar.MONTH, m);
			long lastVal=calendar.getTimeInMillis()-1;

			for (int i = 0; i < xValues.length; i++)
				if (xValues[i] >= firstVal && xValues[i] <= lastVal)
					result.add(i);

			return result;

		}else{
			long firstVal=calendar.getTimeInMillis();
			calendar.set(Calendar.YEAR, y+1);
			long lastVal=calendar.getTimeInMillis()-1;
			for (int i = 0; i < xValues.length; i++)
				if (xValues[i] >= firstVal && xValues[i] <= lastVal)
					result.add(i);
			return result;
		}
	}

	//Check if the chart is static, if new content can be added. If the request is the month= 3 and the chart bunck of data
	//actually finish in month=3 it means that probaby in some days more data will be added to the chart. In this case, the data of this month will change
	//and we have to warn to the client about that (the client may be using a cache)
	public boolean isDynamic(int year, int month, int week, int day, long[] xValues){
		if (year== 0) return true;
		Calendar actual= Calendar.getInstance();
		actual.setTimeInMillis(xValues[xValues.length-1]);
		Calendar requested= Calendar.getInstance();
		requested.clear();
		requested.set(Calendar.YEAR, year);
		requested.set(Calendar.MONTH, month-1);
		if (day > 0)
			requested.set(Calendar.DAY_OF_MONTH, day);
		else if (week > 0)
			requested.set(Calendar.WEEK_OF_MONTH, week-1);

		if (day >0)
			actual.add(Calendar.DAY_OF_MONTH, -1);
		else if (week > 0){
			actual.add(Calendar.WEEK_OF_MONTH, -1);
			actual.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		}
		else if (month > 0)
			actual.add(Calendar.MONTH, -1);
		else if (year> 0)
			actual.add(Calendar.YEAR, -1);
		return (requested.after(actual));
	}

	private int find(List<Double> array, double value) {
		for(int i=0; i<array.size(); i++) 
			if(array.get(i) == value)
				return i;
		return -1;
	}
	public ArrayList<Integer> getSelectedPositions(Set<Double> uniqueDoubles, List<Double> list){
		ArrayList<Integer> result= new ArrayList<>();
		for (Double value : uniqueDoubles) {
			result.add(find(list, value));
		}
		return result;
	}
	public void modifYval(List<Integer> xValues, Set<SerieModel> y_results_tmp){

		//Modif Y Values
		for (SerieModel serieModel : y_results_tmp) {
			double[] data=new double[xValues.size()];
			for (int i=0; i<xValues.size();i++)
				data[i]=serieModel.getYvalues()[xValues.get(i)];
			serieModel.setYvalues(data);
		}
	}
	public long[] modifXval(List<Integer> xValues, long [] x_results_tmp){
		long[] x_results=new long[xValues.size()];

		//Modif X Values
		for (int i=0; i< xValues.size(); i++)
			x_results[i]=x_results_tmp[xValues.get(i)];

		return x_results;
	}
	public static List<Double> deleteDuplicates(List<Double> list){
		List<Double> result = new ArrayList<>();
		int rep=0;
		for (Double value : list) {
			if (!result.contains(value)) result.add(value);
			else{
				result.add(NULL_VAL);
				rep++;
			}
		}
		return result;
	}
	public double[] setMaxMinList(List<List<Double>> groupYval){
		//		int pos=0;
		final int pixelElems=groupYval.get(0).size();
		ArrayList<Double> result = new ArrayList<Double>();
		for (List<Double> list : groupYval) {

			double[] array_list=Doubles.toArray(list);
			ArrayList<Double> newElems= new ArrayList<>();
			newElems.add(array_list[0]);
			newElems.add(array_list[array_list.length-1]);			
			Double max = Doubles.min(array_list);
			Double min = Doubles.max(array_list);
			if (pixelElems > 3){
				if (newElems.contains(max))
					newElems.add(1,NULL_VAL);
				else
					newElems.add(1,max);
				if (newElems.contains(min))
					newElems.add(1,NULL_VAL);
				else
					newElems.add(1,min);
			}

			result.addAll(newElems);
		}
		return Doubles.toArray(result);
	}

}
