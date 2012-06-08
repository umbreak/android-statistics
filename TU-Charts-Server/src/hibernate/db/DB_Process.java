package hibernate.db;

import jabx.model.BaseChartModel;
import jabx.model.CategoryModel;
import jabx.model.ChartModel;
import jabx.model.CommentModel;
import jabx.model.SerieModel;
import jabx.model.UserModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public enum DB_Process {
	i;
	private static Session session;
	private DB_Process(){

	}

	//CHART ------------------------------------------>
	public static List <BaseChartModel> getCharts(int max) {
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		Query q= session.createQuery("FROM jabx.model.BaseChartModel as BaseChartModel");
		q.setMaxResults(max);
		List<BaseChartModel> charts = q.list();
		//		Collections.sort(sites);
		//crit.setMaxResults(50);
		trans.commit();
		return charts;
	}

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



	//COMMENT ------------------------------------------>

	public static CommentModel getComment(int chart_id, int comment_id){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		CommentModel comment=(CommentModel)session.createQuery("jabx.model.CommentModel as comment where comment.chart.id =:chart_id and comment.id =:comment_id").setParameter("chart_id", chart_id).setParameter("comment_id", comment_id).uniqueResult();
		trans.commit();
		return comment;
	}

	public static void setComment(CommentModel comment){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();	

		session.save(comment);
		session.update(comment.getChart());
		session.update(comment.getUser());
		trans.commit();		
	}



	//CATEGORY ------------------------------------------>

	public static List <CategoryModel> getCategories() {
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		List<CategoryModel> categories = session.createQuery("FROM jabx.model.CategoryModel as CategoryModel").list();
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


	//USER ------------------------------------------>

	public static List <UserModel> getUsers() {
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		List<UserModel> users = session.createQuery("FROM jabx.model.UserModel as UserModel").list();
		trans.commit();
		return users;
	}
	public static UserModel getUser(String email){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		UserModel user = (UserModel) session.get(UserModel.class, email);		
		trans.commit();
		return user; 		
	}
	public static boolean setUser(UserModel user){
		try{
			session=SessionFactoryHibernate.getSingleton().getSession();
			final Transaction trans = session.beginTransaction();	
			session.save(user);
			trans.commit();
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static void delUser(String email){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		UserModel user = (UserModel) session.get(UserModel.class, email);	
		session.delete(user);
		trans.commit();		
	}




}
