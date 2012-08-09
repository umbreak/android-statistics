package com.chart.loaders;

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
	private boolean isReal;
	private int type;
	private int year,month,week,day;
	private LruCache<Integer, ChartModel> mMemoryCache;
	private DiskCacheManager mDiskCache;

	public ChartDataLoader(Context context, int chart_id, boolean isReal,int width, int type, int year, int month, int week,int day, DiskCacheManager mDiskCache, LruCache<Integer, ChartModel> mMemoryCache) {
		super(context);
		this.chart_id=chart_id;
		this.isReal=isReal;
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
		ChartModel chart;
		String key;
		if (isReal) width=0;
			key="charts/"+chart_id+ "?x=" + width + "&year=" + year + "&month=" + month + "&week=" + week + "&day=" + day + "&type=" + type;
		
		Log.w(TAG, "key=" + key);

		chart=mMemoryCache.get(key.hashCode());
		if(chart != null){
			Log.i(TAG, "Data=" + key + " obtained from the Memory Cache");
			return chart;
		}

		chart=mDiskCache.getChart(key.hashCode());
		if (chart != null){
			Log.i(TAG, "Data=" + key + " obtained from the Disk Cache");
			mMemoryCache.put(key.hashCode(), chart);
			return chart;
		}

		chart= Processor.i.getChart(key);
		//		if (chart!=null && week == 0 && day== 0){
		Log.i(TAG, "Inserting element '"+key+ "' in the Memory & Disk Cache");
		if (chart != null){
			mMemoryCache.put(key.hashCode(), chart);
			mDiskCache.putChart(key.hashCode(), chart);
		}
		//		}
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
