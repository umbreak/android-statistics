package com.chart.pojos;

public enum Categories {
	i;
	public ChartCategory all;
	public ChartCategory electronics;
	public ChartCategory physics;
	public ChartCategory mechanics;
	public ChartCategory internet;
	public ChartCategory nanotech;
	public ChartCategory signal;
	public ChartCategory economics;
	public ChartCategory multimedia;
	private int num;
	private Categories() {
		num=0;
		all=new ChartCategory(num, "(All categories)");
		num++;
		electronics=new ChartCategory(num, "Electronics"); 
		num++;
		physics=new ChartCategory(num, "Physics");
		num++;
		mechanics=new ChartCategory(num, "Mechanics");
		num++;
		internet=new ChartCategory(num, "Internet");
		num++;
		nanotech=new ChartCategory(num, "Nanotechnology");
		num++;
		signal=new ChartCategory(num, "Signal Processing");
		num++;
		economics=new ChartCategory(num, "Economics");
		num++;
		multimedia=new ChartCategory(num, "Multimedia");
		
	}
}
