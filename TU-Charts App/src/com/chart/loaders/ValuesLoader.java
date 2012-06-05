package com.chart.loaders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.pojos.ChartModel;
import com.chart.pojos.LineModel;

public class ValuesLoader extends AsyncTaskLoader<ChartModel> {
	private Calendar c;
	private ChartModel chart=null;
	private ChartModel realChart;


	public ValuesLoader(Context context, ChartModel chart) {
		super(context);
		this.realChart=chart;
		c=Calendar.getInstance();
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public ChartModel loadInBackground() {
		System.out.println("LoadBackground CharModel");
		//Chart with 10 values (xAxis = DATE)
		List<Date> xValues=new ArrayList<Date>();
//		Date xValues[] = new Date[10];
		c.set(2012, 9, 1);
		xValues.add(c.getTime());
		c.set(2012, 9, 8);
		xValues.add(c.getTime());
		c.set(2012, 9, 15);
		xValues.add(c.getTime());
		c.set(2012, 9, 22);
		xValues.add(c.getTime());
		c.set(2012, 9, 23);
		xValues.add(c.getTime());
		c.set(2012, 9, 25);
		xValues.add(c.getTime());
		c.set(2012, 9, 27);
		xValues.add(c.getTime());
		c.set(2012, 9, 28);
		xValues.add(c.getTime());
		c.set(2012, 9, 29);
		xValues.add(c.getTime());
		c.set(2012, 9, 30);
		xValues.add(c.getTime());

		//2 Lines with 10 values (yAxis = int)
		float yValues0[] = new float[10];
		float yValues1[] = new float[10];


		for (int i=0; i < 10; i++){
			yValues0[i] = getRandom(6, 20);
			yValues1[i] = getRandom(4, 9);
		}
		List<LineModel> yValues = new ArrayList<LineModel>();
		yValues.add(new LineModel(0,"Line 0", yValues0));
		yValues.add(new LineModel(1,"Line 1", yValues1));
		
		realChart.xValues=xValues;
		realChart.yValues=yValues;
		return realChart;
	}
	private Float getRandom(int min, int max){
		return Float.valueOf((min+ (int)(Math.random()*((max-min)+1))));
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
