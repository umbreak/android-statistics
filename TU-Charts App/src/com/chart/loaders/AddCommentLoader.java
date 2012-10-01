package com.chart.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.pojos.CommentModel;
import com.chart.restclient.Processor;

public class AddCommentLoader extends AsyncTaskLoader<CommentModel> {

	private int chart_id;
	private CommentModel c;
	private CommentModel comment;


	public AddCommentLoader(Context context, int id, CommentModel c) {
		super(context);
		this.chart_id=id;
		this.comment=c;
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public CommentModel loadInBackground() {
		System.out.println("** AddCommentLoader: ADDs a Comment");
		comment=Processor.i.putComment(chart_id, comment);
		return comment;
	}

	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override public void deliverResult(CommentModel data) {
		if (isReset()) 
			onReleaseResources(data);
		CommentModel oldComment = data;
		c=data;
		if (isStarted()) 
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(data);
		if (oldComment != null)
			onReleaseResources(oldComment);

	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override protected void onStartLoading() {
		System.out.println("start loading");
		if(c!=null) 
			deliverResult(c);
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
	@Override public void onCanceled(CommentModel apps) {
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
		
		if (c != null){
			onReleaseResources(c);
			c=null;
		}
	}

	/**
	 * Helper function to take care of releasing resources associated
	 * with an actively loaded data set.
	 */
	protected void onReleaseResources(CommentModel apps) {
	}
}
