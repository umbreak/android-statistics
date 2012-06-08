
package com.chart.pojos;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;

public class SerieModel implements Parcelable{
	public int id;
	public String name;
	public String description;

	public double[] yvalues;
	public SerieModel() {
		super();
	}
	public SerieModel(int id, String name, double[] yvalues){
		super();
		this.id=id;
		this.name=name;
		this.yvalues=yvalues;
	}
	
	public static final Parcelable.Creator<SerieModel> CREATOR = new
			Parcelable.Creator<SerieModel>() {
		public SerieModel createFromParcel(Parcel in) {
			return new SerieModel(in);
		}

		public SerieModel[] newArray(int size) {
			return new SerieModel[size];
		}
	};
	
	private SerieModel(Parcel in) {
//		this();
		in.readDoubleArray(yvalues);
		id=in.readInt();

		name=in.readString();
		description=in.readString();
		
	}
	
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDoubleArray(yvalues);
		dest.writeInt(id);

		dest.writeString(name);
		dest.writeString(description);
	}
	@Override
	public String toString() {
		return "SerieModel [id=" + id + ", name=" + name + ", description="
				+ description + ", yvalues=" + Arrays.toString(yvalues) + "]";
	}
}
