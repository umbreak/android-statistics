package utils;

import jabx.model.ChartModel;
import jabx.model.SerieModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.google.common.base.CharMatcher;
import com.google.common.primitives.Doubles;

public enum ServerUtils {
	i;
	private SimpleDateFormat dateFormat;
	public static double NULL_VAL=1E+54;

	private ServerUtils(){
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}


	public ChartModel fromCSV(String name, String description, File file){
		try {
			Scanner scanner = new Scanner(file);
			try{
				scanner.nextLine();

				setDateFormat(scanner.nextLine().split(";")[0]);
				scanner.close();
			}catch (NullPointerException e){
				e.printStackTrace();
			}
			boolean someNullElems=false;
			scanner = new Scanner(file);
			if (scanner.hasNext()){
				String titles[]=scanner.nextLine().split(";");


				ArrayList<Double>[] yVal = (ArrayList<Double>[])new ArrayList[titles.length-1];
				List<Double> xVal = new ArrayList<>();

				for (int j = 0; j < yVal.length; j++) 
					yVal[j] = new ArrayList<Double>();

				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					line=line.replace(",", ".");
					if (!line.isEmpty()){
						String values[]=line.split(";");
						if (values.length > 1){
							//The first element is the Xval
							try {
								xVal.add((double)dateFormat.parse(values[0]).getTime());
							} catch (ParseException e) {
								e.printStackTrace();
							}     
							for (int j=1; j < values.length; j++)
								try{
									yVal[(j-1)].add(Double.valueOf(values[j]));
								}catch(NumberFormatException e){
									someNullElems=true;
									yVal[(j-1)].add(null);
								}
						}
					}
				}

				Set<SerieModel> lines = new LinkedHashSet<SerieModel>();

				for (int i=0; i< yVal.length; i++){
					if (someNullElems){
						double double_array[]=toPrimitive(yVal[i]);
						int[] minmax=getMaxMin(yVal[i]);
						lines.add(new SerieModel(titles[i+1], double_array,minmax));
					}else{
						double double_array[]=Doubles.toArray(yVal[i]);
						lines.add(new SerieModel(titles[i+1], double_array));
					}
				}


				scanner.close();
				file.delete();
				
				Calendar cal=Calendar.getInstance();
				cal.setTimeInMillis(xVal.get(0).longValue());
				int firstYear=cal.get(Calendar.YEAR);
				cal.setTimeInMillis(xVal.get(xVal.size()-1).longValue());
				int lastYear=cal.get(Calendar.YEAR);
				if (firstYear== lastYear) lastYear=0;
				ChartModel chart = new ChartModel(name, description, Doubles.toArray(xVal), lines, firstYear,lastYear);
				chart.setxLegend(titles[0]);
				return chart;

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	private void setDateFormat(String date){
		String delimiter=".";
		if (date.contains("."))
			delimiter=".";
		else if (date.contains("/"))
			delimiter="/";
		int numColons=CharMatcher.is(':').countIn(date);
		if (numColons == 1)
			dateFormat=new SimpleDateFormat("dd" + delimiter + "MM" + delimiter + "yyyy HH:mm");
		else if (numColons == 2)
			dateFormat=new SimpleDateFormat("dd" + delimiter + "MM" + delimiter + "yyyy HH:mm:ss");
		else if (numColons == 3)
			dateFormat=new SimpleDateFormat("dd" + delimiter + "MM" + delimiter + "yyyy HH:mm:ss:SS");
		else
			if (date.contains(" "))
				dateFormat=new SimpleDateFormat("dd" + delimiter + "MM" + delimiter + "yyyy HH");
			else		
				dateFormat=new SimpleDateFormat("dd" + delimiter + "MM" + delimiter + "yyyy");
	}
	private double[] toPrimitive(List<Double> list){
		double[] result= new double[list.size()];
		int pos=0;
		for (Double value : list) {
			if (value == null) result[pos]=NULL_VAL;
			else result[pos]=value;
			pos++;
		}
		return result;
	}
	private int[] getMaxMin(List<Double> list){
		Double max,min;
		max=min=list.get(0).doubleValue();
		for (Double value : list) {
			if (value !=null){
				if (min> value) min=value;
				if (max<value) max=value;
			}
		}
		return new int[]{min.intValue(),max.intValue()};
	}


}
