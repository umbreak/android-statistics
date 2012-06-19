package com.chart.loaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.pojos.Categories;
import com.chart.pojos.BaseChartModel;
import com.chart.pojos.CategoryModel;
import com.chart.restclient.Processor;

public class BaseChartLoader extends AsyncTaskLoader<List<BaseChartModel>> {

	private List<BaseChartModel> mCharts;
	private int category_id;

	public BaseChartLoader(Context context, int id) {
		super(context);
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
		else if (category_id == -2)
			//PERFORM THE CORRECT ACTION, NOW IS FAKE!!!
			return new ArrayList<BaseChartModel>(Arrays.asList(Processor.i.getNewCharts()));
		else if (category_id== 0)
			return new ArrayList<BaseChartModel>(Arrays.asList(Processor.i.getCharts()));
		else
			return new ArrayList<BaseChartModel>(Arrays.asList(Processor.i.getCharts(category_id)));

		//****************** NOW IT'S FAKE ****************
		//		if (id==-1 || id == 0){
		//			charts.add(new BaseChartModel(1, "Chart 1","Test of an idnosnds dsindios ffnieuehtur trui trubguitg euribguieif",Categories.i.signal));
		//			charts.add(new BaseChartModel(2, "Chart 2", "Simulation of an fjfdnjfd fdnfieo feifoefmoir iriojfiore firejfioreoif reifoierjfi iforiejif", Categories.i.electronics));
		//			charts.add(new BaseChartModel(3, "Phys Performance 3","Show the performance of an engine using IONODSD", Categories.i.physics));
		//			charts.add(new BaseChartModel(4, "TCP Comparision 1","Difference in performance between TCP Westwood and TCP Reno in Wifi environments", Categories.i.internet));
		//			charts.add(new BaseChartModel(5, "Piezoelectric 1", "Performance of the Piezoelectric IDONSIDS Sensor", Categories.i.electronics));
		//			charts.add(new BaseChartModel(6, "TCP Comparision 2","Difference in performance between TCP Westwood and FastTCP in Wifi environments", Categories.i.internet));
		//			charts.add(new BaseChartModel(7, "Backbone Throughtput", "Show the Backbone traffic on the NETCAT", Categories.i.internet));
		//			charts.add(new BaseChartModel(8, "Unemployement 1", "Unemployement state in the EU Countries", Categories.i.economics));
		//			charts.add(new BaseChartModel(9, "Salaries 1", "Minimum salaries in the EU state Countries", Categories.i.economics));
		//		}else if (id == -2){
		//			charts.add(new BaseChartModel(1, "Chart 1","Test of an idnosnds dsindios ffnieuehtur trui trubguitg euribguieif",Categories.i.signal));
		//			charts.add(new BaseChartModel(2, "Chart 2", "Simulation of an fjfdnjfd fdnfieo feifoefmoir iriojfiore firejfioreoif reifoierjfi iforiejif", Categories.i.electronics));
		//		}
		//		else if (id == 1){
		//			charts.add(new BaseChartModel(2, "Chart 2", "Simulation of an fjfdnjfd fdnfieo feifoefmoir iriojfiore firejfioreoif reifoierjfi iforiejif", Categories.i.electronics));
		//			charts.add(new BaseChartModel(5, "Piezoelectric 1", "Performance of the Piezoelectric IDONSIDS Sensor", Categories.i.electronics));
		//
		//		}else if (id == 2)
		//			charts.add(new BaseChartModel(3, "Phys Performance 3","Show the performance of an engine using IONODSD", Categories.i.physics));
		//		else if (id == 4){
		//			charts.add(new BaseChartModel(6, "TCP Comparision 2","Difference in performance between TCP Westwood and FastTCP in Wifi environments", Categories.i.internet));
		//			charts.add(new BaseChartModel(7, "Backbone Throughtput", "Show the Backbone traffic on the NETCAT", Categories.i.internet));
		//			charts.add(new BaseChartModel(4, "TCP Comparision 1","Difference in performance between TCP Westwood and TCP Reno in Wifi environments", Categories.i.internet));
		//
		//		}else if (id == 6)
		//			charts.add(new BaseChartModel(1, "Chart 1","Test of an idnosnds dsindios ffnieuehtur trui trubguitg euribguieif",Categories.i.signal));
		//		else if (id == 7){
		//			charts.add(new BaseChartModel(8, "Unemployement 1", "Unemployement state in the EU Countries", Categories.i.economics));
		//			charts.add(new BaseChartModel(9, "Salaries 1", "Minimum salaries in the EU state Countries", Categories.i.economics));
		//		}
		//
		//		Collections.sort(charts);
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
