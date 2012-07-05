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
	public static final String DISPLAY_W="width";
	public static final String DISPLAY_H="height";


	public static final String PREFS_FAVORITES="favorites";

	private AppUtils(){
		date_format= new SimpleDateFormat("dd.MM.yyyy HH:mm");
		//0=Line, 1=Column,2=Scatter
		type_charts= new ArrayList<String>(Arrays.asList
				(new String[]{"Line","Column","Scatter"}));
	}



}
