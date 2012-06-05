package hibernate.db;



import jabx.model.CategoryModel;
import jabx.model.ChartModel;
import jabx.model.SerieModel;
import jabx.model.UserModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;


public class SessionFactoryHibernate {

	private static SessionFactoryHibernate instance;
	private SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;


	public static SessionFactoryHibernate getSingleton(){
		if (instance==null){
			instance = new SessionFactoryHibernate();
		}

		return instance;
	}

	private SessionFactoryHibernate(){
		Configuration configuration = new Configuration();
		configuration.configure();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);		
		LoadDefaultData();

	}

	private void LoadDefaultData(){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		int id_charts, id_categories, id_user;
		List<ChartModel> charts = new ArrayList<>();
		List<CategoryModel> categories = new ArrayList<>();
		List<UserModel> users = new ArrayList<>();

		//5 CHARTS -------------------------------------
		String name[] = {"Chart 1","TCP Comparision","Salaries 1","Chart 2", "Backbone Throughtput"};
		String description[] = {"Test of an idnosnds dsindios ffnieuehtur trui trubguitg euribguieif",
				"Difference in performance between TCP Westwood and TCP Reno in Wifi environments",
				"Minimum salaries in the EU state Countries",
				"Simulation of an fjfdnjfd fdnfieo feifoefmoir iriojfiore firejfioreoif reifoierjfi iforiejif",
		"Show the Backbone traffic on the NETCATt"};
		//Chart with 10 values (xAxis = DATE)
		Calendar c=Calendar.getInstance();
		String[] xValues=new String[9];
		c.set(2012, 9, 1);
		xValues[0]=c.getTime().toString();
		c.set(2012, 9, 8);
		xValues[0]=c.getTime().toString();
		c.set(2012, 9, 15);
		xValues[1]=c.getTime().toString();
		c.set(2012, 9, 22);
		xValues[2]=c.getTime().toString();
		c.set(2012, 9, 23);
		xValues[3]=c.getTime().toString();
		c.set(2012, 9, 25);
		xValues[4]=c.getTime().toString();
		c.set(2012, 9, 27);
		xValues[5]=c.getTime().toString();
		c.set(2012, 9, 28);
		xValues[6]=c.getTime().toString();
		c.set(2012, 9, 29);
		xValues[7]=c.getTime().toString();
		c.set(2012, 9, 30);
		xValues[8]=c.getTime().toString();

		//2 Lines with 10 values (yAxis = int)
		double yValues0[] = new double[10];
		double yValues1[] = new double[10];


		for (int i=0; i < 10; i++){
			yValues0[i] = getRandom(6, 20);
			yValues1[i] = getRandom(4, 15);
		}
		Set<SerieModel> lines = new HashSet<SerieModel>();
		lines.add(new SerieModel("Line 0", yValues0));
		lines.add(new SerieModel("Line 1", yValues1));

		//Creation of the 5 Charts
		for (id_charts=0; id_charts< 5; id_charts++)
			charts.add(new ChartModel(name[id_charts], description[id_charts], xValues, lines));

		//8 CATEGORIES -------------------------------------
		String categories_name[] = {"Electronics","Physics","Mechanics","Internet","Nanotechnology","Signal Processing","Economics","Multimedia"};
		String categories_description[] = {"Electronics category","Physics category","Mechanics category","Internet category","Nanotechnology category","Signal Processing category","Economics category","Multimedia category"};

		for (id_categories=0; id_categories < 8; id_categories++)
			categories.add(new CategoryModel(categories_name[id_categories], categories_description[id_categories]));

		//4 USERS -------------------------------------
		String users_name[] = {"Didac","Thomas","Paco","TU-Chemnitz"};

		for (id_user=0; id_user < 4; id_user++)
			users.add(new UserModel(users_name[id_user], users_name[id_user], users_name[id_user]+ "@tu-chemnitz.de"));



		//Associate Categories & Charts & Users
		//		charts.get(0).setCategory(categories.get(2)); //Chart 1 ->Physics
		//		charts.get(1).setCategory(categories.get(4)); //TCP Comparision ->Internet
		//		charts.get(2).setCategory(categories.get(7)); //Salaries 1 ->Economy
		//		charts.get(3).setCategory(categories.get(1)); //Chart 2 ->Electronics
		//		charts.get(4).setCategory(categories.get(4)); //Backbone Throughtput ->Internet

		categories.get(1).addChart(charts.get(0));
		categories.get(3).addChart(charts.get(1));
		categories.get(3).addChart(charts.get(4));
		categories.get(6).addChart(charts.get(2));
		categories.get(0).addChart(charts.get(3));

		users.get(0).setChart(charts.get(0));
		users.get(0).setChart(charts.get(1));
		users.get(0).setChart(charts.get(2));
		users.get(0).setChart(charts.get(3));
		users.get(1).setChart(charts.get(4));


		for (SerieModel serie: lines) 
			session.save(serie);

		for (int i = 0; i < id_user; i++) 
			session.save(users.get(i));

		for (int i = 0; i < id_charts; i++) 
			session.save(charts.get(i));


		for (int i = 0; i < id_categories; i++) 
			session.save(categories.get(i));

		session.getTransaction().commit();
		session.clear();

	}
	private Double getRandom(int min, int max){
		return Double.valueOf((min+ (int)(Math.random()*((max-min)+1))));
	}
	public Session getSession(){  //Devuelve una session nueva para cada instancia
		return sessionFactory.getCurrentSession();
	}
	public Session getNewSession(){  //Devuelve una session nueva para cada instancia
		return sessionFactory.openSession();
	}


}
