package com.chart.pojos;

import java.text.DateFormat;
import java.util.Date;

public class ChartEntry implements Comparable<ChartEntry>{
	public int id;
	public String name;
	public String description;
	public String category;
	public String date;
	
	
	public ChartEntry() {
		super();
	}
	
	public ChartEntry(int id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.date=DateFormat.getDateInstance().format(new Date());
	}
	public ChartEntry(int id, String name, String category, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description=description;
		this.category=category;
		this.date=DateFormat.getDateInstance().format(new Date());
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(ChartEntry another) {
		return name.compareTo(another.name);
	}

}
