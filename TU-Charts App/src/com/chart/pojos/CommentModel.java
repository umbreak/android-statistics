package com.chart.pojos;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentModel implements Parcelable{
	public int id;
	public String text;
	public UserModel user;
	public Date date;
	
	
	
	public CommentModel(String text) {
		super();
		this.text = text;
	}

	public CommentModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static final Parcelable.Creator<CommentModel> CREATOR = new
			Parcelable.Creator<CommentModel>() {
		public CommentModel createFromParcel(Parcel in) {
			return new CommentModel(in);
		}

		public CommentModel[] newArray(int size) {
			return new CommentModel[size];
		}
	};

	protected CommentModel(Parcel in) {
		user=in.readParcelable(UserModel.class.getClassLoader());
		id = in.readInt();
		text=in.readString();
		date=new Date(in.readLong());

	
	}

	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(user, flags);
		dest.writeInt(id);
		dest.writeString(text);
		dest.writeLong(date.getTime());
	}

	@Override
	public String toString() {
		return "CommentModel [id=" + id + ", text=" + text + ", date=" + date
				+ ", user=" + user + "]";
	}
	
	
}
