package com.chart.ui;

import java.util.List;
import static com.chart.AppUtils.LOADER_LAST_SEEN;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.chart.R;
import com.chart.browser.adapters.BaseChartAdapter;
import com.chart.loaders.BaseChartLoader;
import com.chart.pojos.BaseChartModel;
import com.chart.ui.ListChartsActivity.ListChartsFragment;

public class LastSeenFragment extends SherlockListFragment implements LoaderCallbacks<List<BaseChartModel>>{

	private BaseChartAdapter mAdapter;

	// If non-null, this is the current filter the user has provided.
	private String mCurFilter;

	OnQueryTextListenerCompat mOnQueryTextListenerCompat;


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Give some text to display if there is no data.  In a real
		// application this would come from a resource.
		setEmptyText("No Charts Seen yet!");

		// We have a menu item to show in action bar.
		setHasOptionsMenu(true);

		// Create an empty adapter we will use to display the loaded data.
		mAdapter = new BaseChartAdapter(getActivity(), true);
		setListAdapter(mAdapter);

		// Start out with a progress indicator.

		// Prepare the loader.  Either re-connect with an existing one,
		// or start a new one.


		getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_LAST_SEEN, null, this);

	}

	@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Place an action bar item for searching.
		MenuItem item = menu.add("Search");
		item.setIcon(android.R.drawable.ic_menu_search);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		View searchView = SearchViewCompat.newSearchView(getActivity().getApplicationContext());
		if (searchView != null) {
			SearchViewCompat.setOnQueryTextListener(searchView,
					new OnQueryTextListenerCompat() {
				@Override
				public boolean onQueryTextChange(String newText) {
					// Called when the action bar search text has changed.  Since this
					// is a simple array adapter, we can just have it do the filtering.
					mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
					mAdapter.getFilter().filter(mCurFilter);
					return true;
				}
			});
			item.setActionView(searchView);
		}
		//
		//		MenuItem item2 = menu.add("Update");
		//		item2.setIcon(R.drawable.ic_menu_refresh);
		//		item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Update"))
			getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_LAST_SEEN, null, this);
		return super.onOptionsItemSelected(item);
	}

	@Override public void onListItemClick(ListView l, View v, int position, long id) {
		// Insert desired behavior here.
		Log.i("LoaderCustom", "Item clicked: " + id);
		Intent intent = new Intent();
		intent.setClass(getActivity(), ChartActivity.class);
		intent.putExtra("chart", mAdapter.getItem(position));
		startActivity(intent);			
	}

	@Override
	public Loader<List<BaseChartModel>> onCreateLoader(int id, Bundle args) {
		setListShown(false);
		return new BaseChartLoader(getActivity(), -2);
	}

	@Override
	public void onLoadFinished(Loader<List<BaseChartModel>> loader, List<BaseChartModel> data) {

		// Set the new data in the adapter.
		mAdapter.setData(data);

		// The list should now be shown.
		try{
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}catch(Exception e){}

	}

	@Override
	public void onLoaderReset(Loader<List<BaseChartModel>> arg0) {
		// Clear the data in the adapter.
		mAdapter.setData(null);
	}
}
