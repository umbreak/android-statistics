package com.chart.loaders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.AppUtils;
import com.chart.pojos.ChartModel;
import com.chart.pojos.SerieModel;

public class ChartDataLoader extends AsyncTaskLoader<ChartModel> {
	private Calendar c;
	private ChartModel chart=null;
	private ChartModel realChart;


	public ChartDataLoader(Context context, ChartModel chart) {
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
		Calendar c=Calendar.getInstance();	
		String[] xValues=new String[9];
		c.set(2012, 9, 1);
		xValues[0]=AppUtils.i.date_format.format(c.getTime());
		c.set(2012, 9, 8);
		xValues[0]=AppUtils.i.date_format.format(c.getTime());
		c.set(2012, 9, 15);
		xValues[1]=AppUtils.i.date_format.format(c.getTime());
		c.set(2012, 9, 22);
		xValues[2]=AppUtils.i.date_format.format(c.getTime());
		c.set(2012, 9, 23);
		xValues[3]=AppUtils.i.date_format.format(c.getTime());
		c.set(2012, 9, 25);
		xValues[4]=AppUtils.i.date_format.format(c.getTime());
		c.set(2012, 9, 27);
		xValues[5]=AppUtils.i.date_format.format(c.getTime());
		c.set(2012, 9, 28);
		xValues[6]=AppUtils.i.date_format.format(c.getTime());
		c.set(2012, 9, 29);
		xValues[7]=AppUtils.i.date_format.format(c.getTime());
		c.set(2012, 9, 30);
		xValues[8]=AppUtils.i.date_format.format(c.getTime());

		//2 Lines with 10 values (yAxis = int)
		double yValues0[] = new double[50];
		double yValues1[] = new double[50];
		double yValues2[] = new double[50];
		
		for (int i=0; i < 50; i++){
			yValues0[i] = getRandom(6, 20);
			yValues1[i] = getRandom(4, 15);
			yValues2[i] = getRandom(5, 18);
		}
		List<SerieModel> yValues = new ArrayList<SerieModel>();
		yValues.add(new SerieModel(1,"Line 1", yValues0));
		yValues.add(new SerieModel(2,"Line 2", yValues1));
		yValues.add(new SerieModel(3,"Line 3", yValues2));
		
		realChart.xValues=xValues;
		realChart.yValues=yValues;
		realChart.min=4;
		realChart.max=20;
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
