package com.chart.loaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.pojos.CommentModel;
import com.chart.restclient.Processor;

public class CommentsLoader extends AsyncTaskLoader<List<CommentModel>> {

	private List<CommentModel> mCharts;
	private int chart_id;

	public CommentsLoader(Context context, int id) {
		super(context);
		this.chart_id=id;
	}
	public void addComment(CommentModel c){
		mCharts.add(c);
	}
	public void delComment(CommentModel c){
		mCharts.remove(c);
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public List<CommentModel> loadInBackground() {
		System.out.println("** CommentsLoader: Retrieve a List<CommentModel> from the server");
		return new ArrayList<CommentModel>(Arrays.asList(Processor.i.getChartComments(chart_id)));
	
	}

	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override public void deliverResult(List<CommentModel> apps) {
		if (isReset()) {
			// An async query came in while the loader is stopped.  We
			// don't need the result.
			if (apps != null) {
				onReleaseResources(apps);
			}
		}
		List<CommentModel> oldApps = apps;
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
	@Override public void onCanceled(List<CommentModel> apps) {
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
	protected void onReleaseResources(List<CommentModel> apps) {
		// For a simple List<> there is nothing to do.  For something
		// like a Cursor, we would close it here.
	}
}
