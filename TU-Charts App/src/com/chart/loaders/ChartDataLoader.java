package com.chart.loaders;

import java.util.Calendar;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.pojos.ChartModel;
import com.chart.restclient.Processor;

public class ChartDataLoader extends AsyncTaskLoader<ChartModel> {
	private ChartModel chart=null;
	private int chart_id;
	private int scale;


	public ChartDataLoader(Context context, int chart_id, int scale) {
		super(context);
		this.chart_id=chart_id;
		this.scale=scale;
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public ChartModel loadInBackground() {
		System.out.println("** ChartDataLoader: Retrieve the DATA from one Chart");
		return Processor.i.getChart(chart_id, scale);
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
