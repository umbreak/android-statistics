package com.chart.pojos;

public class ChartCategory implements Comparable<ChartCategory>{
	public int id;
	public String name;
	public String description;
	
	
	public ChartCategory() {
		super();
	}
	
	public ChartCategory(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(ChartCategory another) {
		return name.compareTo(another.name);
	}

}
