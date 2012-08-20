package com.chart.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.util.LruCache;

import com.chart.memory.DiskCacheManager;
import com.chart.pojos.ChartModel;

public class GetInBackgroundLoaderCallback implements LoaderCallbacks<Boolean>{
	private Context context;
	private LruCache<Integer, ChartModel> mMemoryCache;
	private DiskCacheManager mDiskCache;
	
	
	public GetInBackgroundLoaderCallback(Context context, LruCache<Integer, ChartModel> mMemoryCache, DiskCacheManager mDiskCache) {
		super();
		this.context = context;	
		this.mMemoryCache=mMemoryCache;
		this.mDiskCache=mDiskCache;
	}

	@Override
	public Loader<Boolean> onCreateLoader(int arg0, Bundle b) {
		return new GetInBackgroundLoader(context, b.getInt("chart_id"),b.getInt("width"), b.getInt("type"), b.getInt("year"), b.getInt("month"), mDiskCache, mMemoryCache);
	}

	@Override
	public void onLoadFinished(Loader<Boolean> arg0, Boolean data) {
		
	}

	@Override
	public void onLoaderReset(Loader<Boolean> arg0) {
	}

}
