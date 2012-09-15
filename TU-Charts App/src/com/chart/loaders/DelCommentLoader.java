package com.chart.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.restclient.Processor;

public class DelCommentLoader extends AsyncTaskLoader<Boolean> {

	private int chart_id;
	private int comment_id;
	private Boolean result=null;


	public DelCommentLoader(Context context, int id, int comment_id) {
		super(context);
		this.chart_id=id;
		this.comment_id=comment_id;
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public Boolean loadInBackground() {
		System.out.println("** DelCommentLoader: REMOVE a Comment");
		return Processor.i.deleteComment(chart_id, comment_id);
	}

	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override public void deliverResult(Boolean data) {
		if (isReset()) 
			onReleaseResources(data);
		Boolean oldValue = data;
		result=data;
		if (isStarted()) 
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(data);
		if (oldValue != null)
			onReleaseResources(oldValue);

	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override protected void onStartLoading() {
		System.out.println("start loading");
		if(result!= null)
			deliverResult(result);
		else
			forceLoad();
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
	@Override public void onCanceled(Boolean apps) {
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

		if (result != null){
			onReleaseResources(result);
			result=null;
		}
	}

	/**
	 * Helper function to take care of releasing resources associated
	 * with an actively loaded data set.
	 */
	protected void onReleaseResources(Boolean apps) {
	}
}
