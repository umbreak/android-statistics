package com.chart.loaders;

import static com.chart.AppUtils.LAST_SEEN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.pojos.BaseChartModel;
import com.chart.restclient.Processor;

public class BaseChartLoader extends AsyncTaskLoader<List<BaseChartModel>> {

	private List<BaseChartModel> mCharts;
	private int category_id;
	private Context context;

	public BaseChartLoader(Context context, int id) {
		super(context);
		this.context=context;
		this.category_id=id;
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public List<BaseChartModel> loadInBackground() {
		System.out.println("** BaseChartLoader: Retrieve a List<BaseChartModel> from the server");
		//category_id=-1 --> New
		//category_id=-2 --> Last seen (load from SharedPreferences)
		//Other = REAL category_id
		// Retrieve all known data.
		if (category_id == -1)
			return new ArrayList<BaseChartModel>(Arrays.asList(Processor.i.getNewCharts()));
		else if (category_id == -2){
			String result=PreferenceManager.getDefaultSharedPreferences(context).getString(LAST_SEEN, "");
			if (result.isEmpty())
				return null;
			return new ArrayList<BaseChartModel>(Arrays.asList(Processor.i.getConcreteCharts(result)));
		}
		else if (category_id== 0)
			return new ArrayList<BaseChartModel>(Arrays.asList(Processor.i.getCharts()));
		else
			return new ArrayList<BaseChartModel>(Arrays.asList(Processor.i.getCharts(category_id)));
	}

	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override public void deliverResult(List<BaseChartModel> apps) {
		if (isReset()) {
			// An async query came in while the loader is stopped.  We
			// don't need the result.
			if (apps != null) {
				onReleaseResources(apps);
			}
		}
		List<BaseChartModel> oldApps = apps;
		mCharts = apps;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(apps);
		}

		// At this point we can release the resources associated with
		// 'oldApps' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldApps != null) {
			onReleaseResources(oldApps);
		}
	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override protected void onStartLoading() {
		if (mCharts != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(mCharts);
		}
		if (mCharts == null) forceLoad();

		//		if (takeContentChanged() || mCharts == null || configChange) {
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
	@Override public void onCanceled(List<BaseChartModel> apps) {
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
		if (mCharts != null) {
			onReleaseResources(mCharts);
			mCharts = null;
		}

	}

	/**
	 * Helper function to take care of releasing resources associated
	 * with an actively loaded data set.
	 */
	protected void onReleaseResources(List<BaseChartModel> apps) {
		// For a simple List<> there is nothing to do.  For something
		// like a Cursor, we would close it here.
	}
}
