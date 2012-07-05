package com.chart.loaders;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.chart.browser.adapters.CommentAdapter;
import com.chart.pojos.CommentModel;

public class AddLoaderCallback implements LoaderCallbacks<CommentModel>{
	private Context context;
	private CommentAdapter mAdapter;
	
	public AddLoaderCallback(Context context, CommentAdapter mAdapter) {
		super();
		this.context = context;
		this.mAdapter = mAdapter;
		
	}

	@Override
	public Loader<CommentModel> onCreateLoader(int arg0, Bundle b) {
		return new AddCommentLoader(context, b.getInt("chart_id"), (CommentModel)b.getParcelable("comment"));
	}

	@Override
	public void onLoadFinished(Loader<CommentModel> arg0, CommentModel data) {
		System.out.println(data);
		mAdapter.add(data);
		
	}

	@Override
	public void onLoaderReset(Loader<CommentModel> arg0) {
	}

}
