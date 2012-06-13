package com.chart.ui;

import java.text.ParseException;

import org.achartengine.GraphicalView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.chart.ChartGenerator;
import com.chart.R;
import com.chart.loaders.ChartDataLoader;
import com.chart.pojos.BaseChartModel;
import com.chart.pojos.ChartModel;
import com.actionbarsherlock.app.ActionBar;

public class ChartFragment extends SherlockFragment implements ActionBar.OnNavigationListener, LoaderCallbacks<ChartModel>{
	//Change chart for chart_id -----> chart IT WON'T BE NECESSARY ANYMORE!!!
	private ChartModel chart;
	private FragmentManager fm;
	private ChartGenerator chartBuilder;
	private GraphicalView mChart;
	private LinearLayout mLayout;
	private int month;
	private TextView textView;
	private ArrayAdapter<CharSequence> list;
	private DialogFragment dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		BaseChartModel parent= (BaseChartModel)(getArguments() != null ? getArguments().getParcelable("chart") : null);
		chart= new ChartModel(parent);
		fm = getActivity().getSupportFragmentManager();

		chartBuilder =  new ChartGenerator();
		if (savedInstanceState == null)
			month=0;
		else
			month = savedInstanceState.getInt("month");

		getSherlockActivity().getSupportLoaderManager().initLoader(0, null, this);


		Context context = getSherlockActivity().getSupportActionBar().getThemedContext();
		list = ArrayAdapter.createFromResource(context, R.array.months, R.layout.sherlock_spinner_item);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		setHasOptionsMenu(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("month", month);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.chart_fragment, container, false);
		mLayout= (LinearLayout) view.findViewById(R.id.chart);
		textView=(TextView) view.findViewById(R.id.info);
		//		try {
		//				mChart= chartBuilder.getView(getActivity().getBaseContext(), chart);
		//
		//			mLayout.addView(mChart, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		//			mChart.repaint();
		//		} catch (ParseException e) {}
		//		catch (NullPointerException e) {
		//			getView().findViewById(R.id.info).setVisibility(View.VISIBLE);
		//		}
		return view;
	}

	@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Place an action bar item for searching.
		menu.clear();
		MenuItem item = menu.add("Info");
		item.setIcon(android.R.drawable.ic_menu_info_details);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}


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
		new ProgressDialogFragment("Charts", "Loading data chart...").show(fm, "DIALOG_F1");

		return new ChartDataLoader(getSherlockActivity(), chart.id);
	}

	@Override
	public void onLoadFinished(Loader<ChartModel> loader, ChartModel data) {
		chart=data;
		getSherlockActivity().getSupportActionBar().setListNavigationCallbacks(list, this);
		getSherlockActivity().getSupportActionBar().setSelectedNavigationItem(month);

	}

	@Override
	public void onLoaderReset(Loader<ChartModel> arg0) {
		// Clear the data in the adapter.
	}
	@Override
	public void onResume() {
		super.onResume();
		if (mChart != null)
			updateChart();

	}
	private void updateChart(){
		try {
			mLayout.removeAllViews();
			if (month != 0)
				mChart= chartBuilder.getView(getActivity().getBaseContext(), chart, month);
			else
				mChart= chartBuilder.getView(getActivity().getBaseContext(), chart);
			mLayout.addView(mChart);
			//			mChart.repaint();
		} catch (ParseException e) {}
		catch (NullPointerException e) {
			textView.setVisibility(View.VISIBLE);
			mLayout.addView(textView);
		}
		try{
			ProgressDialogFragment d1 = (ProgressDialogFragment) fm.findFragmentByTag("DIALOG_F1");
			d1.dismiss();
		}catch (NullPointerException e) { }
	}
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		System.out.println("Item=" + itemPosition + " selected");
		month=itemPosition;
		updateChart();
		return true;
	}
}
