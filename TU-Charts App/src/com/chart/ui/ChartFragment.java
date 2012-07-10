package com.chart.ui;

import static com.chart.AppUtils.LOADER_CHART;
import static com.chart.AppUtils.SCALE_1;
import static com.chart.AppUtils.SCALE_1_2;
import static com.chart.AppUtils.SCALE_1_4;
import static com.chart.AppUtils.SCALE_4_1;
import static com.chart.AppUtils.SCALE_2_1;



import java.text.ParseException;

import org.achartengine.GraphicalView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.chart.ChartGenerator;
import com.chart.R;
import com.chart.loaders.ChartDataLoader;
import com.chart.pojos.BaseChartModel;
import com.chart.pojos.ChartModel;
import com.chart.restclient.Processor;

public class ChartFragment extends SherlockFragment implements ActionBar.OnNavigationListener, LoaderCallbacks<ChartModel>{
	//Change chart for chart_id -----> chart IT WON'T BE NECESSARY ANYMORE!!!
	private BaseChartModel chart;
	private ChartModel full_chart;
	private FragmentManager fm;
	private ChartGenerator chartBuilder;
	private GraphicalView mChart;
	private LinearLayout mLayout;
	private int month;
	private int scale;
	private TextView textView;
	private ArrayAdapter<CharSequence> list_months;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		chart= (BaseChartModel)(getArguments() != null ? getArguments().getParcelable("chart") : null);
		full_chart= new ChartModel(chart);
		fm = getActivity().getSupportFragmentManager();

		chartBuilder =  new ChartGenerator();
		if (savedInstanceState == null){
			month=0;
			scale=SCALE_1;
		}
		else{
			month = savedInstanceState.getInt("month");
			scale = savedInstanceState.getInt("scale");
		}
		Display display = getSherlockActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		Processor.i.width=size.x;
		Processor.i.height=size.y;

		getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_CHART, null, this);


		Context context = getSherlockActivity().getSupportActionBar().getThemedContext();
		list_months = ArrayAdapter.createFromResource(context, R.array.months, R.layout.sherlock_spinner_item);
		list_months.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);


		getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		setHasOptionsMenu(true);
		
		

//		alert=createListScales();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("month", month);
		outState.putInt("scale", scale);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.chart_fragment, container, false);
		mLayout= (LinearLayout) view.findViewById(R.id.chart);
		textView=(TextView) view.findViewById(R.id.info);

		return view;
	}

	@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Place an action bar item for searching.

		SubMenu subMenu1 = menu.addSubMenu(2, 9, 0, "Scale");
		subMenu1.add(3, SCALE_4_1, 0, "4:1");
		subMenu1.add(3, SCALE_2_1, 1, "2:1");
		subMenu1.add(3, SCALE_1, 2, "1:1");
		subMenu1.add(3, SCALE_1_2, 3, "1:2");
		subMenu1.add(3, SCALE_1_4, 4, "1:2");
		subMenu1.setGroupCheckable(3, true, true);
		subMenu1.getItem(scale-1).setChecked(true);

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(android.R.drawable.ic_menu_view);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem item2 = menu.add(2,8,0,"Info");
		item2.setIcon(android.R.drawable.ic_menu_info_details);
		item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem item3 = menu.add(2,7,0,"Comments");
		item3.setIcon(android.R.drawable.ic_menu_edit);
		item3.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SCALE_1:
		case SCALE_1_2:
		case SCALE_1_4:
		case SCALE_2_1:
		case SCALE_4_1:
			scale=item.getItemId();
			if (item.isChecked()) item.setChecked(false);
			else item.setChecked(true);
			getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
			return true;
		case 7:
		case 8:
			Intent intent = new Intent();
			intent.setClass(getSherlockActivity(), ChartDetailsAcitivy.class);
			intent.putExtra("chart", chart);

			if (item.getTitle().equals("Info"))
				intent.putExtra("Option", 0);
			else
				intent.putExtra("Option", 1);

			startActivity(intent);	
			return true;
		default:
			return false;
		}
	}

	@Override
	public Loader<ChartModel> onCreateLoader(int id, Bundle args) {
		new ProgressDialogFragment("Charts", "Loading data chart...").show(fm, "DIALOG_F1");

		return new ChartDataLoader(getSherlockActivity(), chart.id, scale);
	}

	@Override
	public void onLoadFinished(Loader<ChartModel> loader, ChartModel data) {
		full_chart=data;
		getSherlockActivity().getSupportActionBar().setListNavigationCallbacks(list_months, this);
		getSherlockActivity().getSupportActionBar().setSelectedNavigationItem(month);

	}

	@Override
	public void onLoaderReset(Loader<ChartModel> arg0) {
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
				mChart= chartBuilder.getView(getActivity().getBaseContext(), full_chart, month);
			else
				mChart= chartBuilder.getView(getActivity().getBaseContext(), full_chart);
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
//	private AlertDialog createListScales(){
//		final String[] list_scales= new String[]{"4:1", "2:1", "1:1", "1:2", "1:4"};
//		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
//		builder.setTitle("Choose a scale factor");
//		builder.setSingleChoiceItems(list_scales, 2, new DialogInterface.OnClickListener() {		
//			@Override
//			public void onClick(DialogInterface dialog, int position) {
//				Toast.makeText(getSherlockActivity().getApplicationContext(), list_scales[position], Toast.LENGTH_SHORT).show();
//				dialog.dismiss();
//			}
//		});
//		return builder.create();
//	}
}
