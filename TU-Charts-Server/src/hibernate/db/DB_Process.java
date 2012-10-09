package hibernate.db;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import models.BaseChartModel;
import models.CategoryModel;
import models.ChartModel;
import models.CommentModel;
import models.SerieModel;
import models.UserModel;

public enum DB_Process {
	i;
	private final Comparator<BaseChartModel> date_comparator;
	private final Comparator<BaseChartModel> name_comparator;
	private final Comparator<BaseChartModel> popularity_comparator;
	@Resource public final EntityManagerFactory factory;
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
		factory=Persistence.createEntityManagerFactory("manager1");

		date_comparator = new Comparator<BaseChartModel>() {
			@Override
			public int compare(BaseChartModel o1, BaseChartModel o2) {
				return o2.getDate().compareTo(o1.getDate());
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
	public List <BaseChartModel> getCharts(int max) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		//utx.begin();
		Query q= em.createQuery("SELECT new BaseChartModel (c.id, c.name, c.description, c.xLegend, c.yLegend, c.votes, c.firstYear, c.lastYear, c.date, c.category) FROM models.BaseChartModel c");
		q.setMaxResults(max);
		List<BaseChartModel> charts = q.getResultList();
		em.close();
		return charts;
	}

	public List <BaseChartModel> getCharts() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		List<BaseChartModel> charts = em.createQuery("SELECT new BaseChartModel (c.id, c.name, c.description, c.xLegend, c.yLegend, c.votes, c.firstYear, c.lastYear, c.date, c.category) FROM models.BaseChartModel c").getResultList();
		em.close();
		return charts;
	}
	public ChartModel getChart(int chart_id) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		//utx.begin();
		ChartModel chart = em.find(ChartModel.class, chart_id);	
		em.close();
		return chart;
	}
	public void uploadChart(int chart_id, ChartModel newChart)throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		ChartModel chart = em.find(ChartModel.class, chart_id);

		em.getTransaction().begin();
		chart.updateChart(newChart);
		
		for (SerieModel series : chart.getyValues())
			series=em.merge(series);
		
		chart=em.merge(chart);
		em.flush();
		em.getTransaction().commit();
		em.close();
	}
	
	public BaseChartModel getBaseChart(int chart_id) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		//utx.begin();
		BaseChartModel chart;
		try{
			chart = (BaseChartModel)em.createQuery("SELECT new BaseChartModel (c.id, c.name, c.description, c.xLegend, c.yLegend, c.votes, c.firstYear, c.lastYear, c.date, c.category) FROM models.BaseChartModel c WHERE c.id =:chart_id").setParameter("chart_id", chart_id).getSingleResult();
		}catch (RuntimeException e){ return null; }
		em.close();
		return chart;
	}

	public void setChart(ChartModel chart) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		//utx.begin();
		CategoryModel category=em.find(CategoryModel.class, chart.getCategory().getId());
		//		CategoryModel category=(CategoryModel)em.createQuery("models.CategoryModel as category WHERE category.name =:name").setParameter("name", chart.getCategory().getName()).getSingleResult();
		category.addChart(chart);
		em.getTransaction().begin();
		for (SerieModel serie: chart.getyValues()) 
			em.persist(serie);

		em.persist(chart);
		em.refresh(category);
		em.getTransaction().commit();

		//FALTA AÑADIR EL USUARIO	
		//		session.update(chart.getUser());
		em.close();

	}

	public void delChart(int chart_id)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {

		EntityManager em=factory.createEntityManager();
		em.getTransaction().begin();
		ChartModel chart = (ChartModel) em.find(ChartModel.class, chart_id);
		UserModel user= chart.getUser();
		CategoryModel category = chart.getCategory();
		//		UserModel user=chart.getUser();
		//		CategoryModel category = chart.getCategory();
		for (SerieModel serie: chart.getyValues())
			em.remove(serie);		
		em.remove(chart);
		try
		{
			em.refresh(user);
		}catch (Exception e){}

		em.refresh(category);
		em.getTransaction().commit();
		em.close();

	}



	//COMMENT ------------------------------------------>

	public CommentModel getComment(int chart_id, int comment_id)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		CommentModel comment=(CommentModel)em.createQuery("models.CommentModel as comment WHERE comment.chart.id =:chart_id and comment.id =:comment_id").setParameter("chart_id", chart_id).setParameter("comment_id", comment_id).getSingleResult();
		em.close();
		return comment;
	}

	public CommentModel setComment(int chart_id, CommentModel comment, String email)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();

		ChartModel chart = (ChartModel) em.find(ChartModel.class, chart_id);
		UserModel user = (UserModel) em.find(UserModel.class, email);

		comment.setChart(chart);
		comment.setUser(user);
		em.getTransaction().begin();
		comment=em.merge(comment);

		em.refresh(chart);
		em.refresh(user);
		em.getTransaction().commit();
		em.close();
		return comment;

	}


	public void delComment(int comment_id)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		CommentModel comment=(CommentModel)em.find(CommentModel.class, comment_id);
		ChartModel chart= comment.getChart();
		UserModel user= comment.getUser();
		em.getTransaction().begin();
		em.remove(comment);
		em.refresh(chart);
		em.refresh(user);
		em.getTransaction().commit();
		em.close();

	}

	//CATEGORY ------------------------------------------>
	public List <CategoryModel> getCategories()  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();

		//utx.begin();
		List<CategoryModel> categories = em.createQuery("FROM models.CategoryModel as CategoryModel").getResultList();
		Collections.sort(categories);
		em.close();
		return categories;

	}


	public CategoryModel getCategory(int category_id)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		//utx.begin();
		CategoryModel category = (CategoryModel) em.find(CategoryModel.class, category_id);	
		em.close();
		return category; 	
	}

	public CategoryModel getCategoryCharts(String name)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		CategoryModel category=(CategoryModel)em.createQuery("models.CategoryModel as category WHERE category.name =:name").setParameter("name", name).getSingleResult();
		return category; 		
	}

	public List <BaseChartModel> getCategoryCharts(int category_id)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		List<BaseChartModel> charts = em.createQuery("SELECT new BaseChartModel (c.id, c.name, c.description, c.xLegend, c.yLegend, c.votes, c.firstYear, c.lastYear, c.date, c.category) FROM models.BaseChartModel c WHERE c.category.id =:category_id ").setParameter("category_id", category_id).getResultList();
		em.close();
		return charts; 		
	}


	public CategoryModel getCategory(String name)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		CategoryModel category=(CategoryModel)em.createQuery("from models.CategoryModel as category where category.name =:name").setParameter("name", name).getSingleResult();
		em.close();
		return category; 
	}

	public void setCategory(CategoryModel category)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		em.getTransaction().begin();
		em.persist(category);
		em.getTransaction().commit();
		em.close();

	}

	public void delCategory(int category_id)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		CategoryModel category = (CategoryModel) em.find(CategoryModel.class, category_id);	
		em.getTransaction().begin();
		em.remove(category);
		em.getTransaction().commit();
		em.close();

	}


	//USER ------------------------------------------>

	public List <UserModel> getUsers()  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		List<UserModel> users = em.createQuery("FROM models.UserModel as UserModel").getResultList();
		em.close();
		return users;
	}
	public UserModel getUser(String email)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		UserModel user = (UserModel) em.find(UserModel.class, email);		
		em.close();
		return user; 	

	}

	public boolean setUser(UserModel user)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
		em.close();
		return true;
	}

	public void delUser(String email)  throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		EntityManager em=factory.createEntityManager();
		UserModel user = (UserModel) em.find(UserModel.class, email);	
		em.getTransaction().begin();
		em.remove(user);
		em.getTransaction().commit();
		em.close();

	}



}
