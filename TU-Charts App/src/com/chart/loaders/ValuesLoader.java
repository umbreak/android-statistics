package com.chart.loaders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.pojos.Point;

public class ValuesLoader extends AsyncTaskLoader<List<Point>> {

	private List<Point> values;
	private Calendar c;
	private int id;

	public ValuesLoader(Context context, int id) {
		super(context);
		this.id=id;
		c=Calendar.getInstance();
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public List<Point> loadInBackground() {
		// Retrieve all known data.
		//****************** NOW IT'S FAKE ****************
		List<Point> list_values=new ArrayList<Point>();
		c.set(2012, 9, 1);
		list_values.add(new Point(c.getTime(), 8.8));
		c.clear();
		c.set(2012, 9, 8);
		list_values.add(new Point(c.getTime(), 9.0));
		c.clear();
		c.set(2012, 9, 15);
		list_values.add(new Point(c.getTime(), 10.0));
		c.clear();
		c.set(2012, 9, 22);
		list_values.add(new Point(c.getTime(), 9.5));
		c.clear();
		c.set(2012, 9, 23);
		list_values.add(new Point(c.getTime(), 11.0));
		c.clear();
		c.set(2012, 9, 25);
		list_values.add(new Point(c.getTime(), 10.8));
		c.clear();
		c.set(2012, 9, 27);
		list_values.add(new Point(c.getTime(), 12.5));
		c.clear();
		c.set(2012, 9, 28);
		list_values.add(new Point(c.getTime(), 6.5));
		c.clear();
		c.set(2012, 9, 29);
		list_values.add(new Point(c.getTime(), 9.5));
		return list_values;
	}

	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override public void deliverResult(List<Point> apps) {
		if (isReset()) {
			// An async query came in while the loader is stopped.  We
			// don't need the result.
			if (apps != null) {
				onReleaseResources(apps);
			}
		}
		List<Point> oldApps = apps;
		values = apps;

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
		if (values != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(values);
		}
		if (values == null) forceLoad();

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
	@Override public void onCanceled(List<Point> apps) {
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
		if (values != null) {
			onReleaseResources(values);
			values = null;
		}

	}

	/**
	 * Helper function to take care of releasing resources associated
	 * with an actively loaded data set.
	 */
	protected void onReleaseResources(List<Point> apps) {
		// For a simple List<> there is nothing to do.  For something
		// like a Cursor, we would close it here.
	}
}
