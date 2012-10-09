package com.chart.loaders;

import java.util.Calendar;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.chart.memory.DiskCacheManager;
import com.chart.pojos.ChartModel;
import com.chart.restclient.Processor;

public class ChartDataLoader extends AsyncTaskLoader<ChartModel> {
	private final static String TAG="ChartDataLoader";
	public ChartModel chart=null;
	private int chart_id;
	private int width;
	private int type;
	private int year,month,week,day;
	public LruCache<Integer, ChartModel> mMemoryCache;
	public DiskCacheManager mDiskCache;

	public ChartDataLoader(Context context, int chart_id,int width, int type, int year, int month, int week,int day, DiskCacheManager mDiskCache, LruCache<Integer, ChartModel> mMemoryCache) {
		super(context);
		this.chart_id=chart_id;
		this.width=width;
		this.mMemoryCache=mMemoryCache;
		this.mDiskCache=mDiskCache;
		this.type=type;
		this.year=year;
		this.month=month;
		this.week=week;
		this.day=day;
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public ChartModel loadInBackground() {
		Log.i(TAG, "Start ChartDataLoader");
		//Ensure that the selected date is before in time than the actual date
		Calendar cal= Calendar.getInstance();
		Calendar actual = Calendar.getInstance();
		actual.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		if (day != 0)
			cal.set(Calendar.DATE, day);
		else
			cal.set(Calendar.WEEK_OF_MONTH, week);
		if (year != 0 && cal.after(actual))
			return null;
		//Start the retrieving process
		ChartModel chart=null;

		String key="charts/"+chart_id+ "?x=" + width + "&year=" + year + "&month=" + month + "&week=" + week + "&day=" + day + "&type=" + type;

		Log.w(TAG, "key=" + key);
		int hashKey=key.hashCode();
		chart=mMemoryCache.get(hashKey);
		if(chart != null){
			if (chart.expires < System.currentTimeMillis() && chart.expires >0){
				mDiskCache.remove(hashKey);
			}else{
				Log.i(TAG, "Data=" + key + " fetched from the Memory Cache");
				return chart;
			}
		}
		chart=mDiskCache.getChart(hashKey);
		if (chart != null){
			if (chart.expires < System.currentTimeMillis() && chart.expires > 0){
				mDiskCache.remove(hashKey);
			}else{
				Log.i(TAG, "Data=" + key + " fetched from the Disk Cache");
				return chart;
			}
		}


		chart= Processor.i.getChart(key);
		if (chart != null){
			mMemoryCache.put(hashKey, chart);
			mDiskCache.putChart(hashKey, chart);
			Log.i(TAG, "Data=" + key + " (" + hashKey + ") fetched from INTERNET");
		}
		return chart;
	}


	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override public void deliverResult(ChartModel model) {
		if (isReset()) {
			// An async query came in while the loader is stopped.  We
			// don't need the result.
			if (model != null) {
				onReleaseResources(model);
			}
		}
		ChartModel oldModel = model;
		chart = model;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(model);
		}

		// At this point we can release the resources associated with
		// 'oldApps' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldModel != null) {
			onReleaseResources(oldModel);
		}
	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override protected void onStartLoading() {
		if (chart != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(chart);
		}
		if (chart == null) forceLoad();

		//		if (takeContentChanged() || values == null || configChange) {
		//			// If the data has changed since the last time it was loaded
		//			// or is not currently available, start a load.
		//			forceLoad();
		//		}
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
	@Override public void onCanceled(ChartModel apps) {
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

		// At this point we can release the resources associated with 'charts'
		// if needed.
		if (chart != null) {
			onReleaseResources(chart);
			chart = null;
		}

	}

	/**
	 * Helper function to take care of releasing resources associated
	 * with an actively loaded data set.
	 */
	protected void onReleaseResources(ChartModel apps) {
		// For a simple List<> there is nothing to do.  For something
		// like a Cursor, we would close it here.
	}
}
