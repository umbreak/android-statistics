package com.chart.browser.fragments;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.chart.R;
import com.chart.browser.adapters.ContentsAdapter;
import com.chart.loaders.ContentsLoader;
import com.chart.pojos.ChartEntry;

public class ContentsFragment extends SherlockListFragment implements LoaderCallbacks<List<ChartEntry>>{
	ContentsAdapter mAdapter;

	// If non-null, this is the current filter the user has provided.
	String mCurFilter;

	OnQueryTextListenerCompat mOnQueryTextListenerCompat;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Give some text to display if there is no data.  In a real
		// application this would come from a resource.
		setEmptyText("No New Charts found");

		// We have a menu item to show in action bar.
		setHasOptionsMenu(true);

		// Create an empty adapter we will use to display the loaded data.
		mAdapter = new ContentsAdapter(getActivity());
		setListAdapter(mAdapter);

		// Start out with a progress indicator.
		setListShown(false);

		// Prepare the loader.  Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
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
	}

	@Override public void onListItemClick(ListView l, View v, int position, long id) {
		// Insert desired behavior here.
		Log.i("LoaderCustom", "Item clicked: " + id);
		Toast.makeText(getActivity(), "Pos=" + String.valueOf(id) + " name="+ mAdapter.getItem(position).name, Toast.LENGTH_SHORT).show();
	}

	@Override
	public Loader<List<ChartEntry>> onCreateLoader(int id, Bundle args) {
		return new ContentsLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<ChartEntry>> loader, List<ChartEntry> data) {

		// Set the new data in the adapter.
		mAdapter.setData(data);

		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}

	}

	@Override
	public void onLoaderReset(Loader<List<ChartEntry>> arg0) {
		// Clear the data in the adapter.
		mAdapter.setData(null);
	}
}
