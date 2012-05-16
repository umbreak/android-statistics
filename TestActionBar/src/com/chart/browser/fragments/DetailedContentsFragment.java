package com.chart.browser.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsAdapterView.OnItemSelectedListener;
import com.actionbarsherlock.internal.widget.IcsLinearLayout;
import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.chart.R;
import com.chart.browser.adapters.ContentsAdapter;
import com.chart.loaders.ContentsLoader;
import com.chart.pojos.ChartCategory;
import com.chart.pojos.ChartEntry;

public class DetailedContentsFragment extends SherlockFragmentActivity{
	private ArrayList<ChartCategory> categories;
	private int position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_layout);
		categories = getIntent().getParcelableArrayListExtra("categories");
		if (savedInstanceState == null)
			position = getIntent().getIntExtra("position", 0);
		else
			position = savedInstanceState.getInt("pos");

			Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<ChartCategory> list = new ArrayAdapter<ChartCategory>(context, R.layout.sherlock_spinner_item, categories);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		createSpinner(list);
	}
	
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pos", position);
    }

	private void createSpinner(ArrayAdapter<ChartCategory> list){
		// create ICS spinner 
		IcsSpinner spinner = new IcsSpinner(this, null, 
				R.attr.actionDropDownStyle); 
		spinner.setAdapter(list); 
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(IcsAdapterView<?> parent, View view,
					int pos, long id) {
				position=pos;
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(android.R.id.content, ContentsFragment.newInstance(categories.get(pos).id, false));
				fragmentTransaction.commit();
			}

			@Override
			public void onNothingSelected(IcsAdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		// configure custom view 
		IcsLinearLayout listNavLayout = (IcsLinearLayout)getLayoutInflater().inflate(R.layout.abs__action_bar_tab_bar_view, null); 
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT); 
		params.gravity = Gravity.CENTER; 
		listNavLayout.addView(spinner, params); 
		listNavLayout.setGravity(Gravity.RIGHT);        

		// configure action bar 
		getSupportActionBar().setCustomView(listNavLayout, new ActionBar.LayoutParams(Gravity.LEFT)); 
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		spinner.setSelection(position, true);
	}

	public static class ContentsFragment extends SherlockListFragment implements LoaderCallbacks<List<ChartEntry>>{

		public static ContentsFragment newInstance(int id, boolean visibility) {
			ContentsFragment f = new ContentsFragment();
			// Supply index input as an argument.
			Bundle args = new Bundle();
			args.putInt("id", id);
			args.putBoolean("visibility", visibility);
			f.setArguments(args);
			return f;
		}
		private ContentsAdapter mAdapter;

		// If non-null, this is the current filter the user has provided.
		private String mCurFilter;
		private boolean categoryVisibility;
		private int id_fragment;

		OnQueryTextListenerCompat mOnQueryTextListenerCompat;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			id_fragment = getArguments() != null ? getArguments().getInt("id") : -2;
			categoryVisibility =getArguments() != null ? getArguments().getBoolean("visibility") : false;

		}
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// Give some text to display if there is no data.  In a real
			// application this would come from a resource.
			setEmptyText("No New Charts found");

			// We have a menu item to show in action bar.
			setHasOptionsMenu(true);

			// Create an empty adapter we will use to display the loaded data.
			mAdapter = new ContentsAdapter(getActivity(), categoryVisibility);
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
			return new ContentsLoader(getActivity(), id_fragment);
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

}
