package com.chart;

import java.text.SimpleDateFormat;

public enum AppUtils {
	i;
	public SimpleDateFormat date_format;
	
	private AppUtils(){
		date_format= new SimpleDateFormat("dd.MM.yyyy HH:mm");
	}
	
}
