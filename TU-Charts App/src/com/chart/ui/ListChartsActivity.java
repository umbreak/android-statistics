package com.chart.ui;

import static com.chart.AppUtils.LAST_SEEN;
import static com.chart.AppUtils.LOADER_LAST_SEEN;
import static com.chart.AppUtils.LOADER_LIST_CHARTS;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsAdapterView.OnItemSelectedListener;
import com.actionbarsherlock.internal.widget.IcsLinearLayout;
import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.chart.R;
import com.chart.browser.adapters.BaseChartAdapter;
import com.chart.loaders.BaseChartLoader;
import com.chart.pojos.BaseChartModel;
import com.chart.pojos.CategoryModel;

public class ListChartsActivity extends BaseSherlockActivity{
	private ArrayList<CategoryModel> categories;
	private int position;
	private static final String TAG="ListChartsActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle("");

		categories = getIntent().getParcelableArrayListExtra("categories");
		if (savedInstanceState == null)
			position = getIntent().getIntExtra("position", 0);
		else
			position = savedInstanceState.getInt("pos");

		Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<CategoryModel> list = new ArrayAdapter<CategoryModel>(context, R.layout.sherlock_spinner_item, categories);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		createSpinner(list);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("pos", position);
	}

	private void createSpinner(ArrayAdapter<CategoryModel> list){
		// create ICS spinner 
		IcsSpinner spinner = new IcsSpinner(this, null, 
				R.attr.actionDropDownStyle); 
		spinner.setAdapter(list); 

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(IcsAdapterView<?> parent, View view,
					int pos, long id) {
				position=pos;
				String TAG="Detailed_Charts_" + categories.get(pos).id;
				Fragment f= getSupportFragmentManager().findFragmentByTag(TAG);
				if (f == null){
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
					ft.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit);
					ft.replace(android.R.id.content, ListChartsFragment.newInstance(categories.get(pos).id, false),TAG);
					ft.commit();
				}
			}

			@Override
			public void onNothingSelected(IcsAdapterView<?> parent) {

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

	public static class ListChartsFragment extends SherlockListFragment implements LoaderCallbacks<List<BaseChartModel>>{

		public static ListChartsFragment newInstance(int id, boolean visibility) {
			ListChartsFragment f = new ListChartsFragment();
			// Supply index input as an argument.
			Bundle args = new Bundle();
			args.putInt("id_category", id);
			args.putBoolean("visibility", visibility);
			f.setArguments(args);
			return f;
		}
		private BaseChartAdapter mAdapter;

		// If non-null, this is the current filter the user has provided.
		private String mCurFilter;
		private boolean categoryVisibility;
		private int id_category;

		OnQueryTextListenerCompat mOnQueryTextListenerCompat;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			id_category = getArguments() != null ? getArguments().getInt("id_category") : -2;
			categoryVisibility =getArguments() != null ? getArguments().getBoolean("visibility") : false;
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			outState.putInt("category", id_category);
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
			mAdapter = new BaseChartAdapter(getActivity(), categoryVisibility);
			setListAdapter(mAdapter);

			// Start out with a progress indicator.

			// Prepare the loader.  Either re-connect with an existing one,
			// or start a new one.

			if (savedInstanceState == null)
				getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_LIST_CHARTS, null, this);
			else
				if (savedInstanceState.getInt("category") == id_category)
					getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_LIST_CHARTS, null, this);
				else
					getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_LIST_CHARTS, null, this);
					

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

			MenuItem item2 = menu.add("Update");
			item2.setIcon(R.drawable.ic_menu_refresh);
			item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if (item.getTitle().equals("Update"))
				getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_LIST_CHARTS, null, this);
			return super.onOptionsItemSelected(item);
		}

		@Override public void onListItemClick(ListView l, View v, int position, long id) {
			// Insert desired behavior here.
			Log.i("LoaderCustom", "Item clicked: " + id);
			BaseChartModel chart=mAdapter.getItem(position);
			Intent intent = new Intent();
			intent.setClass(getActivity(), ChartActivity.class);
			intent.putExtra("chart", chart);			
			addToLastSeen(chart.id);
//			System.out.println("1 chart id =" + chart.id + " name=" + chart.name + " date="+ chart.date.getTime() +  " expires=" + chart.expires);
			startActivity(intent);			
		}

		@Override
		public Loader<List<BaseChartModel>> onCreateLoader(int id, Bundle args) {
			setListShown(false);
			return new BaseChartLoader(getActivity(), id_category);
		}

		@Override
		public void onLoadFinished(Loader<List<BaseChartModel>> loader, List<BaseChartModel> data) {

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
		public void onLoaderReset(Loader<List<BaseChartModel>> arg0) {
			// Clear the data in the adapter.
			mAdapter.setData(null);
		}
		
		//Adding id to the String LAST_SEEN in SharedPreferences.
		//Is a FIFO QUEUE of fixed lenght.
		private void addToLastSeen(int id){
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());
			String result=prefs.getString(LAST_SEEN, "");
			if (!result.isEmpty())
				if (result.contains(";"+ id + ";"))
					result=result.replace(id + ";", "");
				else if (result.split(";")[0].equals(String.valueOf(id)))
					result=result.replace(id + ";", "");
			
			result=id+";"+result;
			if (result.split(";").length >= 5)
				result=result.substring(0,result.lastIndexOf(";"));
			prefs.edit().putString(LAST_SEEN, result).commit();
			try{
				getSherlockActivity().getSupportLoaderManager().getLoader(LOADER_LAST_SEEN).forceLoad();
			}catch (NullPointerException e){
				Log.i(TAG,"Loader LOADER_LAST_SEEN is not available right now");
			}
		}
	}

}
