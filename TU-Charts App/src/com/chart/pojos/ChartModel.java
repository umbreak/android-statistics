package com.chart.pojos;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract.Data;

public class ChartModel implements Parcelable, Comparable<ChartModel>{
	public int id;
	public String name;
	public String description;
	public CategoryModel category;
	public String date;
	public int num_comments;
	public int votes;
	public List <Date> xValues;
	public List<LineModel> yValues;


	public ChartModel() {
		super();
		votes=0;
		num_comments=0;
		xValues = new ArrayList<Date>();
		yValues = new ArrayList<LineModel>();
	}

	public ChartModel(int id, String name) {
		this();
		this.id = id;
		this.name = name;
		this.date=DateFormat.getDateInstance().format(new Date());
	}
	public ChartModel(int id, String name, String description, CategoryModel category) {
		this(id,name);
		this.description=description;
		this.category=category;
	}
	public ChartModel(int id, String name, List <Date> xValues, List<LineModel> yValues) {
		this(id,name);
		this.xValues=xValues;
		this.yValues=yValues;
	}
	

	public static final Parcelable.Creator<ChartModel> CREATOR = new
			Parcelable.Creator<ChartModel>() {
		public ChartModel createFromParcel(Parcel in) {
			return new ChartModel(in);
		}

		public ChartModel[] newArray(int size) {
			return new ChartModel[size];
		}
	};

	private ChartModel(Parcel in) {
		this();
		category=in.readParcelable(CategoryModel.class.getClassLoader());
		//		xValues=(Date[])in.readArray(Data.class.getClassLoader());
		//		in.readList(xValues, null);
		in.readList(xValues, Date.class.getClassLoader());
		//		in.readTypedList(xValues, LineModel.CREATOR);
		in.readTypedList(yValues, LineModel.CREATOR);
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
		//		dest.writeArray(xValues);
		dest.writeList(xValues);

		dest.writeTypedList(yValues);

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
	public int compareTo(ChartModel another) {
		return name.compareTo(another.name);
	}

}
