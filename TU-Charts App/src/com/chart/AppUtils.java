package com.chart;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

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
	
	//Type agorithm
	public static final int TYPE_AVERAGE=1;
	public static final int TYPE_WIDTH=2;
	
	//Calendar (Year, Month, Day)
	public static final int YEAR=15;
	public static final int MONTH=16;
	public static final int WEEK=17;

	public static final int DAY=18;
	
	//LOADERS
	public static final int LOADER_CATEGORIES=0;
	public static final int LOADER_LIST_CHARTS=1;
	public static final int LOADER_CHART=2;
	public static final int LOADER_LIST_COMMENTS=3;
	public static final int LOADER_ADD_COMMENTS=4;
	public static final int LOADER_LAST_SEEN=5;
	public static final int LOADER_LOGIN=6;
	public static final int LOADER_FILL_CACHE=7;


	//DIALOGS
	public static final int DIALOG_YEAR=0;
	public static final int DIALOG_MONTH=1;
	public static final int DIALOG_WEEK=2;
	public static final int DIALOG_DAY=3;

	//Disk cache dir and size
	public static final String DISK_CACHE_DIR="charts";
	public static final int DISK_CACHE_SIZE = 1024 * 1024 * 20; // 20MB








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
