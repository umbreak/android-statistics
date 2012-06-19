package hibernate.db;

import jabx.model.BaseChartModel;
import jabx.model.CategoryModel;
import jabx.model.ChartModel;
import jabx.model.CommentModel;
import jabx.model.SerieModel;
import jabx.model.UserModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public enum DB_Process {
	i;
	private static Session session;
	private final Comparator<BaseChartModel> date_comparator;
	private final Comparator<BaseChartModel> name_comparator;
	private final Comparator<BaseChartModel> popularity_comparator;
	public Comparator<BaseChartModel> getDate_comparator() {
		return date_comparator;
	}

	public Comparator<BaseChartModel> getName_comparator() {
		return name_comparator;
	}

	public Comparator<BaseChartModel> getPopularity_comparator() {
		return popularity_comparator;
	}

	private DB_Process(){
		date_comparator = new Comparator<BaseChartModel>() {
			@Override
			public int compare(BaseChartModel o1, BaseChartModel o2) {
				return o2.getId() - o1.getId();
			}
		};
		popularity_comparator = new Comparator<BaseChartModel>() {
			@Override
			public int compare(BaseChartModel o1, BaseChartModel o2) {
				return o2.getVotes() - o1.getVotes();
			}
		};
		name_comparator = new Comparator<BaseChartModel>() {
			@Override
			public int compare(BaseChartModel o1, BaseChartModel o2) {
				return o1.getName().compareTo(o2.getName());
			}
		};

	}

	//CHART ------------------------------------------>
	public static List <BaseChartModel> getCharts(int max) {
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		Criteria crit = session.createCriteria(BaseChartModel.class);
		Query q= session.createQuery("SELECT new BaseChartModel (c.id, c.name, c.description, c.xLegend, c.yLegend, c.votes, c.type, c.date, c.category) FROM jabx.model.BaseChartModel c");
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
		BaseChartModel c= new BaseChartModel("name", "description");
		List<BaseChartModel> charts = session.createQuery("SELECT new BaseChartModel (c.id, c.name, c.description, c.xLegend, c.yLegend, c.votes, c.type, c.date, c.category) FROM jabx.model.BaseChartModel c").list();
		//		Criteria crit = session.createCriteria(BaseChartModel.class);
//		crit.add(Restrictions.eq("base.class", BaseChartModel.class));
//		List<BaseChartModel> charts =crit.list();
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
		CommentModel comment=(CommentModel)session.createQuery("jabx.model.CommentModel as comment WHERE comment.chart.id =:chart_id and comment.id =:comment_id").setParameter("chart_id", chart_id).setParameter("comment_id", comment_id).uniqueResult();
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
	public static void delComment(int comment_id){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();	
		CommentModel comment=(CommentModel)session.get(CommentModel.class, comment_id);
		session.delete(comment);
		session.update(comment.getChart());
		session.update(comment.getUser());
		trans.commit();		
	}



	//CATEGORY ------------------------------------------>

	public static List <CategoryModel> getCategories() {
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		List<CategoryModel> categories = session.createQuery("FROM jabx.model.CategoryModel as CategoryModel").list();
		Collections.sort(categories);
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
	public static List <BaseChartModel> getCategoryCharts(int category_id){
		session=SessionFactoryHibernate.getSingleton().getSession();
		final Transaction trans = session.beginTransaction();
		List<BaseChartModel> charts = session.createQuery("SELECT new BaseChartModel (c.id, c.name, c.description, c.xLegend, c.yLegend, c.votes, c.type, c.date, c.category) FROM jabx.model.BaseChartModel c WHERE c.category.id =:category_id ").setParameter("category_id", category_id).list();
		trans.commit();
		return charts; 		
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
