package com.chart.pojos;

import java.util.Date;

public class Point implements Comparable<Point> {
	public int id;
	public Date date;
	public Double value;
	public Point(Date date, Double value) {
		super();
		id=0;
		this.date = date;
		this.value = value;
	}
	@Override
	public int compareTo(Point rs) {
		return value.compareTo(rs.value);
	}
	@Override
	public String toString() {
		return "Point [date=" + date + ", value=" + value + "]";
	}


}
