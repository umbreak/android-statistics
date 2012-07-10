package com.chart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AppUtils {
	i;
	public SimpleDateFormat date_format;

	public List<String> type_charts;
	
	//SharedPreferences
	public static final String ID="userid";
	public static final String UPDATE_ALWAYS="checkbox";
	public static final String USER="username";
	public static final String PASS="password";
	public static final String SERVER_IP="server_ip";
	
	//Scale factors (4:1, 2:1, 1:1, 1:2, 1:4)
	public static final int SCALE_4_1=1;
	public static final int SCALE_2_1=2;
	public static final int SCALE_1=3;
	public static final int SCALE_1_2=4;
	public static final int SCALE_1_4=5;
	
	//LOADERS
	public static final int LOADER_CATEGORIES=0;
	public static final int LOADER_LIST_CHARTS=1;
	public static final int LOADER_CHART=2;
	public static final int LOADER_LIST_COMMENTS=3;
	public static final int LOADER_ADD_COMMENTS=4;
	public static final int LOADER_LAST_SEEN=5;




	public static final String LAST_SEEN="last_seen";

	private AppUtils(){
		date_format= new SimpleDateFormat("dd.MM.yyyy HH:mm");
		//0=Line, 1=Column,2=Scatter
		type_charts= new ArrayList<String>(Arrays.asList
				(new String[]{"Line","Column","Scatter"}));
	}



}
