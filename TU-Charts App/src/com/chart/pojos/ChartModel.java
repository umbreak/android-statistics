package com.chart.pojos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ChartModel extends BaseChartModel implements Parcelable{
	public UserModel user;
	public long[] xValues;
	public List<SerieModel> yValues;

	public ChartModel() {
		super();
	}

	public ChartModel(int id, String name) {
		super(id,name);
		yValues = new ArrayList<SerieModel>();
	}
	public ChartModel(int id, String name, String description, CategoryModel category) {
		super(id,name, description,category);
		yValues = new ArrayList<SerieModel>();

	}
	public ChartModel(int id, String name, long[] xValues, List<SerieModel> yValues) {
		super(id,name);
		this.xValues=xValues;
		this.yValues=yValues;
	}
	public ChartModel(BaseChartModel parent){
		super(parent.id, parent.name, parent.description, parent.category);
		yValues = new ArrayList<SerieModel>();
	}
	public ChartModel(ChartModel other){
		super(other.id, other.name, other.description, other.category);
		this.yValues = new ArrayList<SerieModel>(other.yValues);
		this.xValues=other.xValues;
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
		super(in);
		yValues = new ArrayList<SerieModel>();
		
		user=in.readParcelable(UserModel.class.getClassLoader());
		in.readLongArray(xValues);
		in.readTypedList(yValues, SerieModel.CREATOR);
		id = in.readInt();
	}

	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		
		dest.writeParcelable(user, flags);
		dest.writeLongArray(xValues);
		dest.writeTypedList(yValues);
		dest.writeInt(id);
	}


	@Override
	public String toString() {
		return name;
	}

}
