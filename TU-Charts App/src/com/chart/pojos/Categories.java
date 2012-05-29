package com.chart.pojos;

public enum Categories {
	i;
	public CategoryModel all;
	public CategoryModel electronics;
	public CategoryModel physics;
	public CategoryModel mechanics;
	public CategoryModel internet;
	public CategoryModel nanotech;
	public CategoryModel signal;
	public CategoryModel economics;
	public CategoryModel multimedia;
	private int num;
	private Categories() {
		num=0;
		all=new CategoryModel(num, "(All categories)");
		num++;
		electronics=new CategoryModel(num, "Electronics"); 
		num++;
		physics=new CategoryModel(num, "Physics");
		num++;
		mechanics=new CategoryModel(num, "Mechanics");
		num++;
		internet=new CategoryModel(num, "Internet");
		num++;
		nanotech=new CategoryModel(num, "Nanotechnology");
		num++;
		signal=new CategoryModel(num, "Signal Processing");
		num++;
		economics=new CategoryModel(num, "Economics");
		num++;
		multimedia=new CategoryModel(num, "Multimedia");
		
	}
}
