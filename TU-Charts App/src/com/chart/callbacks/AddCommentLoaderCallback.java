package com.chart.callbacks;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import static com.chart.AppUtils.LOADER_LIST_COMMENTS;


import com.chart.browser.adapters.CommentAdapter;
import com.chart.loaders.AddCommentLoader;
import com.chart.loaders.CommentsLoader;
import com.chart.pojos.CommentModel;

public class AddCommentLoaderCallback implements LoaderCallbacks<CommentModel>{
	private Context context;
	private ProgressCallback progress;
	
	public AddCommentLoaderCallback(Context context, ProgressCallback progress) {
		super();
		this.progress=progress;
		this.context = context;		
	}

	@Override
	public Loader<CommentModel> onCreateLoader(int arg0, Bundle b) {
		progress.isloading(true);
		return new AddCommentLoader(context, b.getInt("chart_id"), (CommentModel)b.getParcelable("comment"));
	}

	@Override
	public void onLoadFinished(Loader<CommentModel> arg0, CommentModel data) {
//		System.out.println(data);
		progress.addComment(data);
	}

	@Override
	public void onLoaderReset(Loader<CommentModel> arg0) {
	}

}
