package com.chart;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;

public enum AppUtils {
	i;
	public SimpleDateFormat date_format;
	public Context context;
	private MessageDigest digest=null;
	
	//SharedPreferences
	public static final String ID="userid";
	public static final String UPDATE_ALWAYS="checkbox";
	public static final String USER="username";
	public static final String PASS="password";
	public static final String EMAIL="email";
	public static final String LAST_SEEN="last_seen";
	public static final String SERVER_IP="server_ip";
	public static final String NULL="null_value";
	public static Double NULL_VAL=null;

		
	//Scale factors (4:1, 2:1, 1:1, 1:2, 1:4)
	public static final int NO_SCALE=0;
	public static final int SCALE_4_1=1;
	public static final int SCALE_2_1=2;
	public static final int SCALE_1=3;	
	public static final int SCALE_1_2=4;
	public static final int SCALE_1_4=5;

	
	//LOADERS
	public static final int LOADER_CATEGORIES=0;
	public static final int LOADER_LIST_CHARTS=1;
	public static final int LOADER_CHART=2;
	public static final int LOADER_LIST_COMMENTS=3;
	public static final int LOADER_ADD_COMMENTS=4;
	public static final int LOADER_LAST_SEEN=5;
	public static final int LOADER_LOGIN=6;






	private AppUtils(){
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		date_format= new SimpleDateFormat("dd.MM.yyyy HH:mm");
	}
	public boolean isInternetAvailable() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null)
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		else
			return false;
	}
	
	public String getHash(String password) {
		digest.reset();
		byte[] data=digest.digest(password.getBytes());
		return bin2hex(data);
	}
	static String bin2hex(byte[] data) {
		return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
	}



}
