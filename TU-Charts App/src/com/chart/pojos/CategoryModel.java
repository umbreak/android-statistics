package com.chart.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryModel implements Parcelable, Comparable<CategoryModel>{
	public int id;
	public String name;
	public String description;
	
	
	public CategoryModel() {
		super();
	}
	
	public CategoryModel(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public static final Parcelable.Creator<CategoryModel> CREATOR = new
			Parcelable.Creator<CategoryModel>() {
		public CategoryModel createFromParcel(Parcel in) {
			return new CategoryModel(in);
		}

		public CategoryModel[] newArray(int size) {
			return new CategoryModel[size];
		}
	};
	
	private CategoryModel(Parcel in) {
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
	public int compareTo(CategoryModel another) {
		return name.compareTo(another.name);
	}

}
