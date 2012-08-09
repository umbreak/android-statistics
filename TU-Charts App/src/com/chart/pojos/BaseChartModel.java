package com.chart.pojos;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseChartModel implements Parcelable, Comparable<BaseChartModel>{
	public int id;
	public String name;
	public String description;
	public String xLegend;
	public String yLegend;
	public int votes;
	public int type;
	public int firstYear;
	public int lastYear;
	public Date date;
	public CategoryModel category;
	
	public BaseChartModel() {
		super();
		this.date=new Date();
	}
	public BaseChartModel(int id, String name) {
		this();
		this.id = id;
		this.name = name;
	}
	public BaseChartModel(int id, String name, String description, CategoryModel category) {
		this(id,name);
		this.description=description;
		this.category=category;
	}
	
	public static final Parcelable.Creator<BaseChartModel> CREATOR = new
			Parcelable.Creator<BaseChartModel>() {
		public BaseChartModel createFromParcel(Parcel in) {
			return new BaseChartModel(in);
		}

		public BaseChartModel[] newArray(int size) {
			return new BaseChartModel[size];
		}
	};

	protected BaseChartModel(Parcel in) {
		category=in.readParcelable(CategoryModel.class.getClassLoader());
		id = in.readInt();
		votes=in.readInt();
		type = in.readInt();
		firstYear=in.readInt();
		lastYear=in.readInt();
		name= in.readString();	
		description=in.readString();
		date=new Date(in.readLong());
	}

	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(category, flags);
		dest.writeInt(id);
		dest.writeInt(votes);
		dest.writeInt(type);
		dest.writeInt(firstYear);
		dest.writeInt(lastYear);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeLong(date.getTime());
	}


	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(BaseChartModel another) {
		return name.compareTo(another.name);
	}
	

}
