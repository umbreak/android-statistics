package utils;

import jabx.model.UserTokenTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

public enum AuthManager {
	i;
	private Map<String, UserTokenTime> token_table;
	private Map <String, String> challenge_table;
	private final static Random rand= new Random();
	private final static SimpleDateFormat uniqueDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
	private AuthManager(){
		token_table= new HashMap<String, UserTokenTime>();
		challenge_table= new HashMap<String, String>();
		token_table.put("Test_token", new UserTokenTime("Test@tu-chemnitz.de"));

		// Predicate
		final Predicate < UserTokenTime > removeDifference = new Predicate < UserTokenTime > (){
			public boolean apply(UserTokenTime arg0) {
				if (arg0.getEmail().equals("Test@tu-chemnitz.de"))
					arg0.updateLastAction();
				//900.000 millisec == 15min
				return ((System.currentTimeMillis() - arg0.getLastAction()) < 900000);
			}
		};

		token_table= Maps.filterValues(token_table, removeDifference);


		//		TimerTask timerTask = new TimerTask() 
		//		{ 
		//			public void run()  
		//			{ 
		//				token_table.get("Test_token").updateLastAction();
		//				//Each 20min the Timer checks the difference between the actual time and the lasAction time.
		//				//If it's greater than 15min, the session token is deleted.
		//				token_table= Maps.filterValues(token_table, removeDifference);
		//			}
		//		}; 
		//
		//		// Aquí se pone en marcha el timer cada segundo. 
		//		Timer timer = new Timer(); 
		//		// Dentro de 0 milisegundos avísame cada 20min
		//		timer.scheduleAtFixedRate(timerTask, 0, 1200000);
	}
	public Map<String, UserTokenTime> getToken_table() {
		return token_table;
	}
	public Map<String, String> getChallenge_table() {
		return challenge_table;
	}

	public static String getUniqueDate(){
		return uniqueDateFormat.format(new Date()) + rand.nextInt(99 - 10 + 1) + 10;
	}
	public void setToken_table(Map<String, UserTokenTime> token_table) {
		this.token_table = token_table;
	}



}
