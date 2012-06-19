package hibernate.db;



import jabx.model.CategoryModel;
import jabx.model.ChartModel;
import jabx.model.SerieModel;
import jabx.model.UserModel;

import java.text.SimpleDateFormat;
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
//		LoadDefaultData();

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
		SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		final int NUM_VALUES=50;
		int month=2;
		String[] xValues=new String[NUM_VALUES];
		
		int day[]=new int[]{1,5,8,14,22};
		int num_day=0;
		for (int i=0; i < NUM_VALUES; i++){
			c.set(2012,month, day[num_day]);
			xValues[i]=format1.format(c.getTime());
			if (num_day == 4){
				num_day=0;
				month++;
			}else
				num_day++;
		}

		//2 Lines with 50 values (yAxis = int)
		double yValues0[] = new double[50];
		double yValues1[] = new double[50];

		double yValues2[] = new double[50];
		double yValues3[] = new double[50];
		double yValues4[] = new double[50];

		double yValues5[] = new double[50];

		double yValues6[] = new double[50];

		double yValues7[] = new double[50];
		double yValues8[] = new double[50];

		for (int i=0; i < 50; i++){
			yValues0[i] = getRandom(6, 20);
			yValues1[i] = getRandom(4, 15);
			yValues2[i] = getRandom(5, 18);
			yValues3[i] = getRandom(6, 22);
			yValues4[i] = getRandom(3, 10);
			yValues5[i] = getRandom(6, 12);
			yValues6[i] = getRandom(4, 21);
			yValues7[i] = getRandom(5, 20);
			yValues8[i] = getRandom(4, 17);
		}
		Set<SerieModel> lines[] = (Set<SerieModel>[])new HashSet[5];
		for (int i = 0; i < lines.length; i++) {
			lines[i]= new HashSet<SerieModel>();
		}
		lines[0].add(new SerieModel("Line 0", yValues0));
		lines[0].add(new SerieModel("Line 1", yValues1));

		lines[1].add(new SerieModel("Line 2", yValues2));
		lines[1].add(new SerieModel("Line 3", yValues3));
		lines[1].add(new SerieModel("Line 4", yValues4));

		lines[2].add(new SerieModel("Line 5", yValues5));

		lines[3].add(new SerieModel("Line 6", yValues6));

		lines[4].add(new SerieModel("Line 7", yValues7));
		lines[4].add(new SerieModel("Line 8", yValues8));


		//Creation of the 5 Charts
		for (id_charts=0; id_charts< 5; id_charts++)
			charts.add(new ChartModel(name[id_charts], description[id_charts], xValues, lines[id_charts], 22,4));

		//8 CATEGORIES -------------------------------------
		String categories_name[] = {"Electronics","Physics","Mechanics","Internet","Nanotechnology","Signal Processing","Economics","Multimedia"};
		String categories_description[] = {"Electronics category","Physics category","Mechanics category","Internet category","Nanotechnology category","Signal Processing category","Economics category","Multimedia category"};

		for (id_categories=0; id_categories < 8; id_categories++)
			categories.add(new CategoryModel(categories_name[id_categories], categories_description[id_categories]));

		//4 USERS -------------------------------------
		String users_name[] = {"Didac","Thomas","Paco","TU-Chemnitz"};
		
		int []categoies_denied= new int[]{1,2};
		int []charts_denied= new int[]{1,6,3};

		
		for (id_user=0; id_user < 4; id_user++)
			users.add(new UserModel(users_name[id_user], users_name[id_user], users_name[id_user]+ "@tu-chemnitz.de", categoies_denied,charts_denied));



		//Associate Categories & Charts & Users
				charts.get(0).setCategory(categories.get(2)); //Chart 1 ->Physics
				charts.get(1).setCategory(categories.get(4)); //TCP Comparision ->Internet
				charts.get(2).setCategory(categories.get(7)); //Salaries 1 ->Economy
				charts.get(3).setCategory(categories.get(1)); //Chart 2 ->Electronics
				charts.get(4).setCategory(categories.get(4)); //Backbone Throughtput ->Internet

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


		for (int i=0; i < lines.length; i++)
			for (SerieModel serie: lines[i]) 
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
