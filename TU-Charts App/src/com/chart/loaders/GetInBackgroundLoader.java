package com.chart.loaders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import static com.chart.AppUtils.NUM_MONTHS;
import static com.chart.AppUtils.NUM_WEEKS;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.chart.memory.DiskCacheManager;
import com.chart.pojos.BaseChartModel;
import com.chart.pojos.ChartModel;
import com.chart.restclient.Processor;

public class GetInBackgroundLoader extends AsyncTaskLoader<Boolean> {
	private final static String TAG="GetInBackgroundLoader";
	private int width;
	private int chart_id;
	private int year,month;
	private int type;
	public LruCache<Integer, ChartModel> mMemoryCache;
	public DiskCacheManager mDiskCache;
	private Boolean result;

	public GetInBackgroundLoader(Context context, int id, int width, int type, int year, int month,DiskCacheManager mDiskCache, LruCache<Integer, ChartModel> mMemoryCache) {
		super(context);
		this.chart_id=id;
		this.mMemoryCache=mMemoryCache;
		this.mDiskCache=mDiskCache;
		this.year=year;
		this.result=null;
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
		
		Calendar cal= Calendar.getInstance();

		Calendar actual = Calendar.getInstance();
		actual.setTimeInMillis(System.currentTimeMillis());
		Log.i(TAG,"Start GerInBackgroundLoader");
		Log.w(TAG, "PUT - EVICT =" + (mMemoryCache.putCount()-mMemoryCache.evictionCount()));
		//Don't do anything until the year is not selected (cause can be a huge amount of data to load)
		if (year == 0) return false;
		cal.set(Calendar.YEAR, year);
		ArrayList<String > weeksKey=null;
		ArrayList<String > monthsKey=null;

		if (month !=0){
			//Create the keys
			weeksKey = new ArrayList<String>();
			cal.set(Calendar.MONTH, (month -1));
			for (int i = 0; i < NUM_WEEKS; i++) {
				cal.set(Calendar.WEEK_OF_MONTH, i);
				if (cal.before(actual))
					weeksKey.add("charts/"+chart_id+ "?x=" + width + "&year=" + year + "&month=" + month + "&week=" + (i+1) + "&day=0&type=" + type);
			}
		}

		//Create the keys
		monthsKey = new ArrayList<String>();
		cal.clear();
		cal.set(Calendar.YEAR, year);

		for (int i = 0; i < NUM_MONTHS; i++) {
			cal.set(Calendar.MONTH, i);
			if (cal.before(actual))
				monthsKey.add("charts/"+chart_id+ "?x=" + width + "&year=" + year + "&month=" + i + "&week=0&day=0&type=" + type);
		}
		Collections.shuffle(monthsKey);
		monthsKey.add("charts/"+chart_id+ "?x=" + width + "&year=" + year + "&month=0&week=0&day=0&type=" + type);
		if (month != 0) retrieveElems(weeksKey);
		retrieveElems(monthsKey);

		return true;
	}

	private void retrieveElems(ArrayList<String> keys){
		ChartModel chart=null;
		for (String key : keys) {
			int hashKey=key.hashCode();

			chart=mMemoryCache.get(hashKey);
			if (chart == null){ //If the month doesn't exist in the Memory Cache, try disk
				if (mDiskCache.get(hashKey) == null){ //If it doesn't exist in the Disk Cache, download
					fromInternet(key, hashKey);
				}else{

					chart=mDiskCache.getChart(hashKey);
					if (chart !=null){
						if (chart.expires < System.currentTimeMillis() && chart.expires > 0){
							mDiskCache.remove(hashKey);
							fromInternet(key, hashKey);
						}else{
							mMemoryCache.put(hashKey, chart);
//							Log.i(TAG, "Obtained Data="+key+ " from Disk Cache and inserted in Memory Cache");
						}
					} 
//					else
//						Log.i(TAG, "Obtained Data="+key+ " from Disk Cache but it's EMPTY");

				}
			}else
				if (chart.expires < System.currentTimeMillis() && chart.expires > 0){
					mMemoryCache.remove(hashKey);
					mDiskCache.remove(hashKey);
					fromInternet(key, hashKey);
				}
//				else
//					Log.i(TAG, "Obtained Data=" + key + " from the Memory Cache");
			chart=null;
		}
	}
	private void fromInternet(String key, int hashKey){
		ChartModel chart=Processor.i.getChart(key);	
//		Log.i(TAG, "Inserting element '"+key+ "' in the Memory Cache & Disk Cache");
		if (chart!=null){
			mMemoryCache.put(hashKey, chart);
			mDiskCache.putChart(hashKey, chart);
		}
//		Log.i(TAG, "Inserting element '"+key+ "' in the Memory Cache & Disk Cache");
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
