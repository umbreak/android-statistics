package com.chart.loaders;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.chart.memory.DiskCacheManager;
import com.chart.pojos.ChartModel;
import com.chart.restclient.Processor;

public class GetInBackgroundLoader extends AsyncTaskLoader<Boolean> {
	private final static String TAG="GetInBackgroundLoader";
	private final static int NUM_MONTHS=12;
	private final static int NUM_WEEKS=4;
	private int width;
	private boolean isReal;
	private int chart_id;
	private int year,month;
	private int type;
	private LruCache<Integer, ChartModel> mMemoryCache;
	private DiskCacheManager mDiskCache;
	private Boolean result;

	public GetInBackgroundLoader(Context context, int id, boolean isReal,int width, int type, int year, int month,DiskCacheManager mDiskCache, LruCache<Integer, ChartModel> mMemoryCache) {
		super(context);
		this.chart_id=id;
		this.mMemoryCache=mMemoryCache;
		this.mDiskCache=mDiskCache;
		this.year=year;
		this.result=null;
		this.isReal=isReal;
		this.width=width;
		this.type=type;
		this.month=month;
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public Boolean loadInBackground() {
		Log.i(TAG,"Start GerInBackgroundLoader");
		Log.w(TAG, "PUT - EVICT =" + (mMemoryCache.putCount()-mMemoryCache.evictionCount()));
		//Don't do anything until the year is not selected (cause can be a huge amount of data to load)
		if (year == 0) return false;

		if (isReal) width=0;
		String weeksKey[]=null;
		String monthsKey[]=null;

		if (month !=0){
			//Create the keys
			weeksKey= new String[NUM_WEEKS];
			for (int i = 0; i < NUM_WEEKS; i++) 
				weeksKey[i]="charts/"+chart_id+ "?x=" + width + "&year=" + year + "&month=" + month + "&week=" + (i+1) + "&day=0&type=" + type;
		}

		//Get List of months, in a random order (we don't know which one the user us gonna request. 0-12)
		ArrayList<Integer> monthsValue= new ArrayList<Integer>();
		for (int i = 1; i < NUM_MONTHS+1; i++) 
			monthsValue.add(i);
		Collections.shuffle(monthsValue);

		//Create the keys
		monthsKey= new String[NUM_MONTHS+1];
		for (int i = 0; i < NUM_MONTHS; i++) 
			monthsKey[i]="charts/"+chart_id+ "?x=" + width + "&year=" + year + "&month=" + monthsValue.get(i).intValue() + "&week=0&day=0&type=" + type;
		monthsKey[NUM_MONTHS]="charts/"+chart_id+ "?x=" + width + "&year=" + year + "&month=0&week=0&day=0&type=" + type;

		if (month != 0) retrieveElems(weeksKey);
		retrieveElems(monthsKey);

		return true;
	}

	private void retrieveElems(String key[]){
		ChartModel chart=null;
		for (int i = 0; i < key.length; i++) {
			chart=mMemoryCache.get(key[i].hashCode());
			if (chart == null){ //If the month doesn't exist in the Memory Cache, try disk
				if (mDiskCache.get(key[i].hashCode()) == null){ //If it doesn't exist in the Disk Cache, download
					chart=Processor.i.getChart(key[i]);	
					Log.i(TAG, "Inserting element '"+key[i]+ "' in the Memory Cache & Disk Cache");
					if (chart!=null){
						mMemoryCache.put(key[i].hashCode(), chart);
						mDiskCache.putChart(key[i].hashCode(), chart);
					}

				}else{
					chart=mDiskCache.getChart(key[i].hashCode());
					if (chart !=null){
						mMemoryCache.put(key[i].hashCode(), chart);
						Log.i(TAG, "Obtained Data="+key[i]+ " from Disk Cache and inserted in Memory Cache");
					} else
						Log.i(TAG, "Obtained Data="+key[i]+ " from Disk Cache but it's EMPTY");

				}
			}else
				Log.i(TAG, "Obtained Data=" + key[i] + " from the Memory Cache");

			chart=null;
		}
	}

	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override public void deliverResult(Boolean data) {
		if (isReset()) 
			onReleaseResources(data);
		result=data;
		if (isStarted()) 
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(data);
	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override protected void onStartLoading() {
		if(result!=null) 
			deliverResult(result);
		else
			forceLoad();
	}

	/**
	 * Handles a request to stop the Loader.
	 */
	@Override protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	/**
	 * Handles a request to cancel a load.
	 */
	@Override public void onCanceled(Boolean apps) {
		super.onCanceled(apps);

		// At this point we can release the resources associated with 'apps'
		// if needed.
		onReleaseResources(apps);
	}

	/**
	 * Handles a request to completely reset the Loader.
	 */
	@Override protected void onReset() {
		super.onReset();
		// Ensure the loader is stopped
		onStopLoading();
		if (result!= null)
			result=null;

	}

	/**
	 * Helper function to take care of releasing resources associated
	 * with an actively loaded data set.
	 */
	protected void onReleaseResources(Boolean data) {
	}
}
