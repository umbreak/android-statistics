package com.chart.loaders;

import java.util.Calendar;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.pojos.ChartModel;
import com.chart.restclient.Processor;

public class ChartDataLoader extends AsyncTaskLoader<ChartModel> {
	private ChartModel chart=null;
	private int chart_id;


	public ChartDataLoader(Context context, int chart_id) {
		super(context);
		this.chart_id=chart_id;
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public ChartModel loadInBackground() {
		System.out.println("** ChartDataLoader: Retrieve the DATA from one Chart");

//		Calendar c=Calendar.getInstance();	
//		String[] xValues=new String[NUM_VALUES];
//		int month=2;
//		int day[]=new int[]{1,5,8,14,22};
//		int num_day=0;
//		for (int i=0; i < NUM_VALUES; i++){
//			c.set(2012,month, day[num_day]);
//			xValues[i]=AppUtils.i.date_format.format(c.getTime());
//			if (num_day == 4){
//				num_day=0;
//				month++;
//			}else
//				num_day++;
//		}
//
//		System.out.println(Arrays.toString(xValues));
//		//2 Lines with 10 values (yAxis = int)
//		double yValues0[] = new double[NUM_VALUES];
//		double yValues1[] = new double[NUM_VALUES];
//		double yValues2[] = new double[NUM_VALUES];
//		
//		for (int i=0; i < NUM_VALUES; i++){
//			yValues0[i] = getRandom(6, 20);
//			yValues1[i] = getRandom(4, 10);
//			yValues2[i] = getRandom(12, 18);
//		}
//		List<SerieModel> yValues = new ArrayList<SerieModel>();
//		yValues.add(new SerieModel(1,"Line 1", yValues0));
//		yValues.add(new SerieModel(2,"Line 2", yValues1));
//		yValues.add(new SerieModel(3,"Line 3", yValues2));
//		
//		realChart.xValues=xValues;
//		realChart.yValues=yValues;
//		realChart.min=4;
//		realChart.max=20;
		return Processor.i.getChart(chart_id);
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
