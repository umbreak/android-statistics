package com.chart.loaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.pojos.ChartEntry;

public class ContentsLoader extends AsyncTaskLoader<List<ChartEntry>> {

	List<ChartEntry> mCharts;

	public ContentsLoader(Context context) {
		super(context);
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public List<ChartEntry> loadInBackground() {
		// Retrieve all known data.
		//****************** NOW IT'S FAKE ****************
		List<ChartEntry> charts=new ArrayList<ChartEntry>();
		charts.add(new ChartEntry(1, "Chart 1", "Signal Procesing", "Test of an idnosnds dsindios ffnieuehtur trui trubguitg euribguieif"));
		charts.add(new ChartEntry(2, "Chart 2", "Electronics", "Simulation of an fjfdnjfd fdnfieo feifoefmoir iriojfiore firejfioreoif reifoierjfi iforiejif"));
		charts.add(new ChartEntry(3, "Phys Performance 3", "Physics", "Show the performance of an engine using IONODSD"));
		charts.add(new ChartEntry(4, "TCP Comparision 1", "Internet", "Difference in performance between TCP Westwood and TCP Reno in Wifi environments"));
		charts.add(new ChartEntry(5, "Piezoelectric 1", "Electronics", "Performance of the Piezoelectric IDONSIDS Sensor"));
		charts.add(new ChartEntry(6, "TCP Comparision 2", "Internet", "Difference in performance between TCP Westwood and FastTCP in Wifi environments"));
		charts.add(new ChartEntry(7, "Backbone Throughtput", "Internet", "Show the Backbone traffic on the NETCAT"));
		//**********************************
		System.out.println("Finishing of loead CHARTS");
		Collections.sort(charts);
		return charts;
	}

	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override public void deliverResult(List<ChartEntry> apps) {
		if (isReset()) {
			// An async query came in while the loader is stopped.  We
			// don't need the result.
			if (apps != null) {
				onReleaseResources(apps);
			}
		}
		List<ChartEntry> oldApps = apps;
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
	@Override public void onCanceled(List<ChartEntry> apps) {
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
	protected void onReleaseResources(List<ChartEntry> apps) {
		// For a simple List<> there is nothing to do.  For something
		// like a Cursor, we would close it here.
	}
}
