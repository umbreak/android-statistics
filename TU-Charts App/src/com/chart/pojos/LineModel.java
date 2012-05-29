package com.chart.pojos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;

public class LineModel implements Parcelable{
	public int id;
	public String name;
	public String description;
	public double[] values;
	public LineModel() {
		super();
	}
	public LineModel(int id, String name, double[] values){
		super();
		this.id=id;
		this.name=name;
		this.values=values;
	}
	
	public static final Parcelable.Creator<LineModel> CREATOR = new
			Parcelable.Creator<LineModel>() {
		public LineModel createFromParcel(Parcel in) {
			return new LineModel(in);
		}

		public LineModel[] newArray(int size) {
			return new LineModel[size];
		}
	};
	
	private LineModel(Parcel in) {
//		this();
		in.readDoubleArray(values);
		id=in.readInt();
		name=in.readString();
		description=in.readString();
	}
	
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDoubleArray(values);
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(description);
	}
	@Override
	public String toString() {
		return "LineModel [id=" + id + ", values=" + Arrays.toString(values) + "]";
	}
	
}
