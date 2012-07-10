package hibernate.db;

import jabx.model.ChartModel;
import jabx.model.SerieModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.google.common.primitives.Doubles;
import com.mchange.v1.util.ArrayUtils;

public enum ServerUtils {
	i;
	private static double max;
	private static double min;
	private final SimpleDateFormat dataFormat;
	private ServerUtils(){
		dataFormat=new SimpleDateFormat("dd.MM.yyyy HH:mm");
	}
	
	public SimpleDateFormat getDataFormat() {
		return dataFormat;
	}

	public static ChartModel fromCSV(String name, String description, File file){
		try {
			Scanner scanner = new Scanner(file);
			if (scanner.hasNext()){
				String titles[]=scanner.nextLine().split(";");


				ArrayList<Double>[] yVal = (ArrayList<Double>[])new ArrayList[titles.length-1];
				List<String> xVal = new ArrayList<>();

				for (int j = 0; j < yVal.length; j++) 
					yVal[j] = new ArrayList<Double>();

				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					line=line.replace(",", ".");
					if (!line.isEmpty()){
						String values[]=line.split(";");
						if (values.length > 1){
							//The first element is the Xval
							xVal.add(values[0]);     
							for (int j=1; j < values.length; j++){
								yVal[(j-1)].add(Double.valueOf(values[j]));

							}
						}
					}
				}

				Set<SerieModel> lines = new HashSet<SerieModel>();
				max=(double)(yVal[0].get(0));
				min=max;
				for (int i=0; i< yVal.length; i++){
					double double_array[]=Doubles.toArray(yVal[i]);
					double new_min=Doubles.min(double_array);
					double new_max=Doubles.max(double_array);
					if (min > new_min) min=new_min;
					if (max < new_max) max=new_max;
					lines.add(new SerieModel(titles[i+1], double_array));
				}
				//				for (int j = 0; j < yVal.length; j++) 
				//					System.out.println("Y values="+ yVal[j]);
				//				System.out.println("max=" + max + " min="+min);

				scanner.close();
				file.delete();

				ChartModel chart = new ChartModel(name, description, xVal.toArray(new String[xVal.size()]), lines, (int)max,(int)min);
				chart.setxLegend(titles[0]);
				return chart;

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
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
