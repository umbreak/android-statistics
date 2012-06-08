package com.chart.ui;

import java.text.ParseException;

import org.achartengine.GraphicalView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.chart.ChartGenerator;
import com.chart.R;
import com.chart.loaders.ChartDataLoader;
import com.chart.pojos.BaseChartModel;
import com.chart.pojos.ChartModel;

public class ChartFragment extends SherlockFragment implements LoaderCallbacks<ChartModel>{
	private ChartModel chart;
	private FragmentManager fm;
	private ChartGenerator chartBuilder;
	private GraphicalView mChart;
	private LinearLayout mLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseChartModel parent = (BaseChartModel) (getArguments() != null ? getArguments().getParcelable("chart") : null);
		chart= new ChartModel(parent);
		fm = getActivity().getSupportFragmentManager();
		chartBuilder =  new ChartGenerator();

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.hello_world, container, false);
		mLayout= (LinearLayout) v.findViewById(R.id.chart);
//		mChart= chartBuilder.getView(v.getContext(), getPoints(),chart);
//		mLayout.addView(mChart, new LayoutParams(LayoutParams.FILL_PARENT,
//		          LayoutParams.FILL_PARENT));
		getLoaderManager().initLoader(0, null, this);
		return v;
	}

	@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Place an action bar item for searching.
		menu.clear();
		MenuItem item = menu.add("Info");
		item.setIcon(android.R.drawable.ic_menu_info_details);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}

//	@Override
//	public void onResume() {
//		super.onResume();
//		if (chartBuilder != null)
//			mChart.repaint();
//		System.out.println("AFTER");
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment frag=ChartActivity.InfoFragment.newInstance(chart);
		FragmentTransaction ft= fm.beginTransaction();
		ft.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit);
		//		ft.remove(this);
		//		ft.add(android.R.id.content, frag, "Info");
		ft.replace(android.R.id.content, frag, "Info");
		ft.addToBackStack("Chart").commit();
		return true;
	}
	
	@Override
	public Loader<ChartModel> onCreateLoader(int id, Bundle args) {
		return new ChartDataLoader(getActivity(), chart);
	}

	@Override
	public void onLoadFinished(Loader<ChartModel> loader, ChartModel data) {
		// Draw the graph
		try {
			mChart= chartBuilder.getView(getActivity().getBaseContext(), data);
		} catch (ParseException e) {}
		mLayout.addView(mChart, new LayoutParams(LayoutParams.FILL_PARENT,
		          LayoutParams.FILL_PARENT));

		// The list should now be shown.
		if (isResumed()) {
			if (chartBuilder != null)
				mChart.repaint();
		} 
//		else {
//			setListShownNoAnimation(true);
//		}

	}

	@Override
	public void onLoaderReset(Loader<ChartModel> arg0) {
		// Clear the data in the adapter.
//		mAdapter.setData(null);
	}
}
