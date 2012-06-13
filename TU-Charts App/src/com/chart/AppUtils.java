package com.chart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AppUtils {
	i;
	public SimpleDateFormat date_format;
	
	public List<String> type_charts;

	
	private AppUtils(){
		date_format= new SimpleDateFormat("dd.MM.yyyy HH:mm");
		//0=Line, 1=Column,2=Scatter
		type_charts= new ArrayList<String>(Arrays.asList
				(new String[]{"Line","Column","Scatter"}));
	}
	
}
