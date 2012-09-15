package com.chart.callbacks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.chart.browser.adapters.CommentAdapter;
import com.chart.loaders.DelCommentLoader;
import com.chart.pojos.CommentModel;

public class DelCommentLoaderCallback implements LoaderCallbacks<Boolean>{
	private Context context;
	private CommentModel comment;
	private ProgressCallback progress;


	public DelCommentLoaderCallback(Context context, ProgressCallback progress) {
		super();
		this.context = context;
		this.progress=progress;

	}

	@Override
	public Loader<Boolean> onCreateLoader(int arg0, Bundle b) {
		progress.isloading(true);
		comment=b.getParcelable("comment");
		return new DelCommentLoader(context, b.getInt("chart_id"), comment.id);
	}

	@Override
	public void onLoadFinished(Loader<Boolean> arg0, Boolean result) {
		progress.isloading(false);
		if (result)
			progress.delComment(comment);
		else
			progress.isloading(false);

	}

	@Override
	public void onLoaderReset(Loader<Boolean> arg0) {
	}

}
