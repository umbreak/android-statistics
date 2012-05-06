package com.chart.loaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.pojos.ChartCategory;

public class CategoriesContentsLoader extends AsyncTaskLoader<List<ChartCategory>> {

	List<ChartCategory> mCharts;

	public CategoriesContentsLoader(Context context) {
		super(context);
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public List<ChartCategory> loadInBackground() {
		// Retrieve all known data.
		//****************** NOW IT'S FAKE ****************
		System.out.println("Here");
		List<ChartCategory> categories=new ArrayList<ChartCategory>();
		
		categories.add(new ChartCategory(1,"(All categories)"));
		categories.add(new ChartCategory(2,"Electronics"));
		categories.add(new ChartCategory(3,"Physics"));
		categories.add(new ChartCategory(4,"Mechanics"));
		categories.add(new ChartCategory(5,"Internet"));
		categories.add(new ChartCategory(6,"Nanotechnology"));
		categories.add(new ChartCategory(7,"Signal Processing"));
		categories.add(new ChartCategory(8,"Economics"));
		categories.add(new ChartCategory(9,"Multimedia"));
		//**********************************

		Collections.sort(categories);
		return categories;
	}

	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override public void deliverResult(List<ChartCategory> apps) {
		if (isReset()) {
			// An async query came in while the loader is stopped.  We
			// don't need the result.
			if (apps != null) {
				onReleaseResources(apps);
			}
		}
		List<ChartCategory> oldApps = apps;
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
	@Override public void onCanceled(List<ChartCategory> apps) {
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
	protected void onReleaseResources(List<ChartCategory> apps) {
		// For a simple List<> there is nothing to do.  For something
		// like a Cursor, we would close it here.
	}
}
