package com.chart.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable{
	public String username;
	public String password;
	public String description;
	public String email;
	public static final Parcelable.Creator<UserModel> CREATOR = new
			Parcelable.Creator<UserModel>() {
		public UserModel createFromParcel(Parcel in) {
			return new UserModel(in);
		}

		public UserModel[] newArray(int size) {
			return new UserModel[size];
		}
	};

	private UserModel(Parcel in) {
		username=in.readString();
		password=in.readString();
		description=in.readString();
		email=in.readString();
	}

	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(username);
		dest.writeString(password);
		dest.writeString(description);
		dest.writeString(email);
	}
	
	@Override
	public String toString() {
		return email;
	}
}
