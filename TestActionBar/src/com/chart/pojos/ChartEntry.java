package com.chart.pojos;

import java.text.DateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class ChartEntry implements Parcelable, Comparable<ChartEntry>{
	public int id;
	public String name;
	public String description;
	public ChartCategory category;
	public String date;
	public int num_comments;
	public int votes;
	
	
	public ChartEntry() {
		super();
		votes=0;
		num_comments=0;
	}
	
	public ChartEntry(int id, String name) {
		this();
		this.id = id;
		this.name = name;
		this.date=DateFormat.getDateInstance().format(new Date());
	}
	public ChartEntry(int id, String name, String description, ChartCategory category) {
		this(id,name);
		this.description=description;
		this.category=category;
	}
	
	public static final Parcelable.Creator<ChartEntry> CREATOR = new
			Parcelable.Creator<ChartEntry>() {
		public ChartEntry createFromParcel(Parcel in) {
			return new ChartEntry(in);
		}

		public ChartEntry[] newArray(int size) {
			return new ChartEntry[size];
		}
	};
	
	private ChartEntry(Parcel in) {
		category=in.readParcelable(ChartCategory.class.getClassLoader());
		id = in.readInt();
		name= in.readString();	
		description=in.readString();
		date= in.readString();
		num_comments=in.readInt();
		votes=in.readInt();
	}
	
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(category, flags);
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(date);
		dest.writeInt(num_comments);
		dest.writeInt(votes);
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
