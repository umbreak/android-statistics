package hibernate.db;

import jabx.model.ChartModel;
import jabx.model.SerieModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.google.common.base.CharMatcher;
import com.google.common.primitives.Doubles;

public enum ServerUtils {
	i;
	private SimpleDateFormat dateFormat;
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
							for (int j=1; j < values.length; j++){
								yVal[(j-1)].add(Double.valueOf(values[j]));

							}
						}
					}
				}

				Set<SerieModel> lines = new LinkedHashSet<SerieModel>();
//				max=(double)(yVal[0].get(0));
//				min=max;
//				for (int i=0; i< yVal.length; i++){
//					double double_array[]=Doubles.toArray(yVal[i]);
//					double new_min=Doubles.min(double_array);
//					double new_max=Doubles.max(double_array);
//					if (min > new_min) min=new_min;
//					if (max < new_max) max=new_max;
//					lines.add(new SerieModel(titles[i+1], double_array));
//				}
				
				for (int i=0; i< yVal.length; i++){
					double double_array[]=Doubles.toArray(yVal[i]);
					lines.add(new SerieModel(titles[i+1], double_array));
				}
				//				for (int j = 0; j < yVal.length; j++) 
				//					System.out.println("Y values="+ yVal[j]);
				//				System.out.println("max=" + max + " min="+min);

				scanner.close();
				file.delete();

				ChartModel chart = new ChartModel(name, description, Doubles.toArray(xVal), lines);
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
	
//	private static synchronized double[] toPrimitiveDouble(List<Double> data){
//		double[] tempArray = new double[data.size()];
//		int i = 0;
//		for (Double d : data) {
//			tempArray[i] = (double) d;
//			//			System.out.println(tempArray[i]);
//			//			if (min > tempArray[i]){
//			//				min=tempArray[i];
//			//			}
//			//			else if (max < tempArray[i])
//			//				max=tempArray[i];
//			i++;
//		}
//		double new_min=Doubles.min(tempArray);
//		double new_max=Doubles.max(tempArray);
//		if (min > new_min) min=new_min;
//		else if (max < new_max) max=new_max;
//		return tempArray;
//	}
}
