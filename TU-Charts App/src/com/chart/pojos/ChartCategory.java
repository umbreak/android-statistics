package com.chart.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class ChartCategory implements Parcelable, Comparable<ChartCategory>{
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
	
	public static final Parcelable.Creator<ChartCategory> CREATOR = new
			Parcelable.Creator<ChartCategory>() {
		public ChartCategory createFromParcel(Parcel in) {
			return new ChartCategory(in);
		}

		public ChartCategory[] newArray(int size) {
			return new ChartCategory[size];
		}
	};
	
	private ChartCategory(Parcel in) {
		id = in.readInt();
		name= in.readString();
		description=in.readString();
	
	}
	
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(description);
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
