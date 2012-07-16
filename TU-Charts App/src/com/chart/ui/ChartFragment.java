package com.chart.ui;

import static com.chart.AppUtils.LOADER_CHART;
import static com.chart.AppUtils.NO_SCALE;
import static com.chart.AppUtils.SCALE_1;
import static com.chart.AppUtils.SCALE_1_2;
import static com.chart.AppUtils.SCALE_1_4;
import static com.chart.AppUtils.SCALE_2_1;
import static com.chart.AppUtils.SCALE_4_1;

import java.text.ParseException;
import java.util.ArrayList;

import org.achartengine.GraphicalView;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	private int resol;
	private TextView textView;
	private ArrayAdapter<CharSequence> list_months;
	private Handler hCloseDisplay;
	private AlertDialog chooseSerieDialog;
	private ChartModel newChart;
	private ArrayList<String> namesNotSelected;
	private XYMultipleSeriesRenderer renderer;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		System.out.println("NEW TIME");
		chart= (BaseChartModel)(getArguments() != null ? getArguments().getParcelable("chart") : null);
		//		chooseSerieDialog=null;
		//		full_chart= new ChartModel(chart);
		fm = getActivity().getSupportFragmentManager();

		chartBuilder =  new ChartGenerator();
		if (savedInstanceState == null){
			month=0;
			resol=SCALE_1;
			namesNotSelected= new ArrayList<String>();
		}
		else{
			namesNotSelected=savedInstanceState.getStringArrayList("NotSelected");
			System.out.println("Names NOT selected=" + namesNotSelected);
			month = savedInstanceState.getInt("month");
			resol = savedInstanceState.getInt("resolution");
		}
		Display display = getSherlockActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		Processor.i.width=size.x;
		Processor.i.height=size.y;



		Context context = getSherlockActivity().getSupportActionBar().getThemedContext();
		list_months = ArrayAdapter.createFromResource(context, R.array.months, R.layout.sherlock_spinner_item);
		list_months.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		hCloseDisplay = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				try{
					ProgressDialogFragment d1 = (ProgressDialogFragment) fm.findFragmentByTag("DIALOG_F1");
					d1.dismiss();	
				}catch (NullPointerException e) { }
				super.handleMessage(msg);
			}

		};

		getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSherlockActivity().getSupportActionBar().setListNavigationCallbacks(list_months, this);
		getSherlockActivity().getSupportActionBar().setSelectedNavigationItem(month);
		setHasOptionsMenu(true);
		getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_CHART, null, this);




		//		alert=createListScales();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("month", month);
		outState.putInt("resolution", resol);
		outState.putStringArrayList("NotSelected", namesNotSelected);

		//		outState.putCharSequenceArrayList(key, value)

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

		SubMenu subMenu1 = menu.addSubMenu(2, 9, 0, "Resol");
		subMenu1.add(3, NO_SCALE, 0, "Real");
		subMenu1.add(3, SCALE_4_1, 1, "4:1");
		subMenu1.add(3, SCALE_2_1, 2, "2:1");
		subMenu1.add(3, SCALE_1, 3, "1:1");
		subMenu1.add(3, SCALE_1_2, 4, "1:2");
		subMenu1.add(3, SCALE_1_4, 5, "1:4");
		subMenu1.setGroupCheckable(3, true, true);
		subMenu1.getItem(resol).setChecked(true);
		subMenu1.setGroupCheckable(4, true, false);
		subMenu1.getItem(resol).setChecked(true);

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(android.R.drawable.ic_menu_zoom);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);


		//		SubMenu subMenu2 = menu.addSubMenu(2, 6, 0, "Serie");
		//		for (int i = 0; i < full_chart.yValues.size(); i++) {
		//			subMenu2.add(4,(20+i),0,full_chart.yValues.get(i).name);
		//		}

		MenuItem item1 = menu.add(5,6,0,"Serie");
		item1.setIcon(android.R.drawable.ic_menu_view);
		item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);



		MenuItem item2 = menu.add(1,8,0,"Info");
		item2.setIcon(android.R.drawable.ic_menu_info_details);
		item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		MenuItem item3 = menu.add(6,7,0,"Comments");
		item3.setIcon(android.R.drawable.ic_menu_edit);
		item3.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case NO_SCALE:
		case SCALE_1:
		case SCALE_1_2:
		case SCALE_1_4:
		case SCALE_2_1:
		case SCALE_4_1:
			resol=item.getItemId();
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
		case 6:
			try{
				chooseSerieDialog.show();
			}catch (NullPointerException e){ return false;}
			return true;
		default:
			return false;
		}
	}

	@Override
	public Loader<ChartModel> onCreateLoader(int id, Bundle args) {
		new ProgressDialogFragment("Charts", "Loading data chart...").show(fm, "DIALOG_F1");

		return new ChartDataLoader(getSherlockActivity(), chart.id, resol, month);
	}

	@Override
	public void onLoadFinished(Loader<ChartModel> loader, ChartModel data) {
		full_chart=data;
		if (data == null)
			newChart=null;
		else{
			newChart=new ChartModel(full_chart);
			createListYvalues();
		}
		updateChart();

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
			mChart= chartBuilder.getView(getActivity().getBaseContext(), newChart);
			renderer=chartBuilder.renderer;
			mChart.addZoomListener(new ZoomListener() {

				@Override
				public void zoomReset() {					
				}

				@Override
				public void zoomApplied(ZoomEvent event) {
					System.out.println("Is zoom in?" + event.isZoomIn());
					System.out.println("Xmax=" + renderer.getXAxisMax() + " Xmin=" + renderer.getXAxisMin() + " Ymax=" + renderer.getYAxisMax() + " Ymin=" + renderer.getYAxisMin());
				}
			}, true, false);
			mLayout.addView(mChart);
			//			mChart.repaint();
		} catch (ParseException e) {}
		catch (NullPointerException e) {
			textView.setVisibility(View.VISIBLE);
			mLayout.addView(textView);
		}
		hCloseDisplay.sendEmptyMessage(0);
	}
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		System.out.println("Item=" + itemPosition + " selected");
		if (itemPosition != month){
			month=itemPosition;
			getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
		}
		month=itemPosition;
		return true;
	}
	private AlertDialog createListYvalues(){
		System.out.println("create");
		if (chooseSerieDialog != null){
			for (String value : namesNotSelected) {
				removeElem(value);
			}
			return chooseSerieDialog;
		}
		final String[] series= new String[full_chart.yValues.size()];
		final boolean[] checkeds= new boolean[series.length];
		for (int i=0; i < series.length; i++){
			series[i] = full_chart.yValues.get(i).name;
			if (namesNotSelected.contains(series[i])){
				removeElem(series[i]);
				checkeds[i]=false;
			}else
				checkeds[i]=true;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
		builder.setTitle("Choose the series to display");

		builder.setMultiChoiceItems(series,checkeds , new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int position, boolean isChecked) {
				System.out.println("click element=" + series[position]);
				if (!isChecked){
					removeElem(series[position]);
					namesNotSelected.add(series[position]);
				}else{
					addElem(series[position]);
					namesNotSelected.remove(series[position]);
				}
				updateChart();
				dialog.dismiss();
			}
		});		
		chooseSerieDialog=builder.create();
		return chooseSerieDialog;
	}
	private boolean removeElem(String name){
		System.out.println(newChart.yValues.size());
		for (int i = 0; i < newChart.yValues.size(); i++) 
			if (newChart.yValues.get(i).name.equals(name)){
				newChart.yValues.remove(i);
				return true;
			}
		return false;
	}
	private boolean addElem(String name){
		System.out.println(newChart.yValues.size());

		for (int i = 0; i < full_chart.yValues.size(); i++) 
			if (full_chart.yValues.get(i).name.equals(name)){
				newChart.yValues.add(full_chart.yValues.get(i));
				return true;
			}
		return false;
	}
}
