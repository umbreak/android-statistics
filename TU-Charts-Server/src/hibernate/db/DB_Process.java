package hibernate.db;

import jabx.model.BaseChartModel;
import jabx.model.CategoryModel;
import jabx.model.ChartModel;
import jabx.model.SerieModel;
import jabx.model.UserModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

public enum DB_Process {
	i;
	private static Session session;
	private DB_Process(){

	}

	//SITE
	public static List <BaseChartModel> getCharts() {
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		List<BaseChartModel> charts = session.createQuery("FROM jabx.model.BaseChartModel as BaseChartModel").list();
		//		Collections.sort(sites);
		//crit.setMaxResults(50);
		trans.commit();
		return charts;
	}
	public static ChartModel getChart(int chart_id){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		ChartModel chart = (ChartModel) session.get(ChartModel.class, chart_id);		
		trans.commit();
		return chart; 		
	}
	public static void setChart(ChartModel chart){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();	
		for (SerieModel serie: chart.getyValues()) 
			session.save(serie);
		session.save(chart);
		session.update(chart.getCategory());
		//FALTA AÑADIR EL USUARIO	
		//		session.update(chart.getUser());
		trans.commit();		
	}
	
	public static void delChart(int chart_id){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		ChartModel chart = (ChartModel) session.get(ChartModel.class, chart_id);
//		UserModel user=chart.getUser();
//		CategoryModel category = chart.getCategory();
		for (SerieModel serie: chart.getyValues()) 
			session.delete(serie);		
		session.delete(chart);
		session.update(chart.getUser());
		session.update(chart.getCategory());
		trans.commit();		
	}


	public static List <CategoryModel> getCategories() {
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		List<CategoryModel> categories = session.createQuery("FROM jabx.model.CategoryModel as CategoryModel").list();
		//		Collections.sort(sites);
		//crit.setMaxResults(50);
		trans.commit();
		return categories;
	}


	public static CategoryModel getCategory(int category_id){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		CategoryModel category = (CategoryModel) session.get(CategoryModel.class, category_id);		
		trans.commit();
		return category; 		
	}

	public static CategoryModel getCategory(String name){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		CategoryModel category=(CategoryModel)session.createQuery("from jabx.model.CategoryModel as category where category.name =:name").setParameter("name", name).uniqueResult();

		trans.commit();
		return category; 		
	}
	
	public static void setCategory(CategoryModel category){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();	
		session.save(category);
		trans.commit();		
	}
	
	public static void delCategory(int category_id){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		CategoryModel category = (CategoryModel) session.get(CategoryModel.class, category_id);	
		session.delete(category);
		trans.commit();		
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

				ArrayList<Double> a= new ArrayList<Double>();
				System.out.println("X values="+ xVal);
				Set<SerieModel> lines = new HashSet<SerieModel>();
				for (int i=0; i< yVal.length; i++){
					lines.add(new SerieModel(titles[i+1], toPrimitiveDouble(yVal[i])));
				}
				for (int j = 0; j < yVal.length; j++) 
					System.out.println("Y values="+ yVal[j]);


				scanner.close();
				ChartModel chart = new ChartModel(name, description, xVal.toArray(new String[xVal.size()]), lines);
				chart.setxLegend(titles[0]);
				return chart;

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static double[] toPrimitiveDouble(List<Double> data){
		double[] tempArray = new double[data.size()];
		int i = 0;
		for (Double d : data) {
			tempArray[i] = (double) d;
			i++;
		}
		return tempArray;
	}


}
