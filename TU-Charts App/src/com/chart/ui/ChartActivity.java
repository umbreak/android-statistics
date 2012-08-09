package com.chart.ui;

import static com.chart.AppUtils.DAY;
import static com.chart.AppUtils.DIALOG_DAY;
import static com.chart.AppUtils.DIALOG_MONTH;
import static com.chart.AppUtils.DIALOG_WEEK;
import static com.chart.AppUtils.DIALOG_YEAR;
import static com.chart.AppUtils.LOADER_CHART;
import static com.chart.AppUtils.LOADER_FILL_CACHE;
import static com.chart.AppUtils.MONTH;
import static com.chart.AppUtils.TYPE_AVERAGE;
import static com.chart.AppUtils.TYPE_WIDTH;
import static com.chart.AppUtils.WEEK;
import static com.chart.AppUtils.YEAR;

import java.text.ParseException;
import java.util.ArrayList;

import org.achartengine.GraphicalView;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.chart.ChartGenerator;
import com.chart.R;
import com.chart.loaders.ChartDataLoader;
import com.chart.loaders.GetInBackgroundLoaderCallback;
import com.chart.memory.DiskCacheManager;
import com.chart.pojos.BaseChartModel;
import com.chart.pojos.ChartModel;
import com.chart.ui.dialog.SeriesDialog;
import com.chart.ui.dialog.TimePeriodDialog;

@SuppressLint("NewApi")
public class ChartActivity extends SherlockFragmentActivity implements LoaderCallbacks<ChartModel>{
	private static final String TAG="ChartActivity";
	private BaseChartModel chart;
	private ChartModel full_chart;
	private ChartModel newChart;

	private ChartGenerator chartBuilder;
	private GraphicalView mChart;
	private LinearLayout mLayout;
	private int calendar,year,month,week,day;
	private boolean isReal;
	private int type;
	private int width;
	private TextView textView;
//	private Handler hCloseDisplay;
	private ProgressBar progress;
	private SubMenu subMenuCalendar;


	private ArrayList<String> namesNotSelected;
	private ScaleGestureDetector mScaleDetector;

	private LruCache<Integer, ChartModel> mMemoryCache;
	public DiskCacheManager mDiskCache;



	//	private Context aContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.chart_fragment);
		mLayout= (LinearLayout) findViewById(R.id.chart);
		textView=(TextView) findViewById(R.id.info);
		progress=(ProgressBar) findViewById(R.id.progress);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		chart = getIntent().getParcelableExtra("chart");

		setTitle("");

		chartBuilder =  new ChartGenerator();
		if (savedInstanceState == null){
			calendar=year=month=week=day=0;
			if (chart.lastYear ==0) year=1;
			isReal=false;
			type=TYPE_AVERAGE;
			namesNotSelected= new ArrayList<String>();
		}else{
			namesNotSelected=savedInstanceState.getStringArrayList("NotSelected");
			Log.i(TAG,"Names NOT selected=" + namesNotSelected);
			calendar=savedInstanceState.getInt("calendar");
			year = savedInstanceState.getInt("year");
			month = savedInstanceState.getInt("month");
			week = savedInstanceState.getInt("week");
			day = savedInstanceState.getInt("day");
			isReal=savedInstanceState.getBoolean("isReal");
			type=savedInstanceState.getInt("type");
		}

		Display display = getWindowManager().getDefaultDisplay();
		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR2){
			Point size = new Point();
			display.getSize(size);
			width=size.x;
		}else
			width=display.getWidth();
		
		//Creating the mMemoryCache or retrieving from the Fragment (Handling Configuration Changes)
		initCaches();
		initCheckbox(this);
		mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

		

		getSupportLoaderManager().initLoader(LOADER_CHART, null, this);
		getSupportLoaderManager().initLoader(LOADER_FILL_CACHE, getBackgroundLoaderBundle(), new GetInBackgroundLoaderCallback(this, mMemoryCache, mDiskCache));
	}
	private void initCaches(){
		RetainFragment mRetainFragment =
				RetainFragment.findOrCreateRetainFragment(getSupportFragmentManager());
		mMemoryCache = mRetainFragment.mMemoryCache;
		if (mMemoryCache == null) {
			// Get memory class of this device, exceeding this amount will throw an
			// OutOfMemory exception.
			final int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();

			// Use 1/7th of the available memory for this memory cache. (32/7MB=4,5MB)
			final int cacheSize = 1024 * 1024 * memClass / 7;
			mMemoryCache = new LruCache<Integer, ChartModel>(cacheSize){

				@Override
				protected int sizeOf(Integer key, ChartModel value) {
					Log.i(TAG,"Size of LruCache");
					return DiskCacheManager.getBytes(value);
				}
			};
			mRetainFragment.mMemoryCache = mMemoryCache;
		}
		mDiskCache = mRetainFragment.mDiskCache;
		if (mDiskCache == null){
			mDiskCache= new DiskCacheManager(this);
			mRetainFragment.mDiskCache = mDiskCache;
		}
	}
	private void initCheckbox(final LoaderManager.LoaderCallbacks<ChartModel> loader){
		//Inflate the custom view
        View customNav = LayoutInflater.from(getSupportActionBar().getThemedContext()).inflate(R.layout.custom_actionbar_view, null);
        //Bind to its state change  
        CheckBox check= (CheckBox)customNav.findViewById(android.R.id.checkbox);
        check.setChecked(isReal);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isReal=isChecked;
				getSupportLoaderManager().restartLoader(LOADER_CHART, null,loader );
			}
		});
        //Attach to the action bar
        getSupportActionBar().setCustomView(customNav);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
	}
//	private void initHandler(){
//		hCloseDisplay = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				try{
//					ProgressDialogFragment d1 = (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag("DIALOG_F1");
//					d1.dismiss();	
//				}catch (NullPointerException e) { }
//				super.handleMessage(msg);
//			}
//		};
//	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("calendar", calendar);
		outState.putInt("year", year);
		outState.putInt("month", month);
		outState.putInt("week", week);
		outState.putInt("day", day);
		outState.putBoolean("isReal", isReal);
		outState.putInt("type", type);
		outState.putStringArrayList("NotSelected", namesNotSelected);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		SubMenu subMenu1 = menu.addSubMenu(2, 9, 0, "Resol");
//		subMenu1.add(3, NO_SCALE, 0, "Real");
//		subMenu1.add(3, SCALE_4_1, 1, "4:1");
//		subMenu1.add(3, SCALE_2_1, 2, "2:1");
//		subMenu1.add(3, SCALE_1, 3, "1:1");
//		subMenu1.add(3, SCALE_1_2, 4, "1:2");
//		subMenu1.add(3, SCALE_1_4, 5, "1:4");
//		subMenu1.setGroupCheckable(3, true, true);
//		subMenu1.getItem(resol).setChecked(true);

//		MenuItem subMenu1Item = subMenu1.getItem();
//		subMenu1Item.setIcon(android.R.drawable.ic_menu_zoom);
//		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem item1 = menu.add(2,6,0,"Serie");
		item1.setIcon(android.R.drawable.ic_menu_view);
		item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		subMenuCalendar = menu.addSubMenu(2,9,0,"Calendar");
		if (chart.lastYear!=0)
			subMenuCalendar.add(4, YEAR, 0, "Year");
		subMenuCalendar.add(4, MONTH, 1, "Month");
		subMenuCalendar.add(4, WEEK, 2, "Week");
		subMenuCalendar.add(4, DAY, 3, "Day");
		subMenuCalendar.setGroupCheckable(4, true, true);
		subMenuCalendar.getItem(calendar).setChecked(true);
		if (month == 0){
			if (chart.lastYear != 0)
				subMenuCalendar.getItem(subMenuCalendar.size()-3).setEnabled(false);
			subMenuCalendar.getItem(subMenuCalendar.size()-2).setEnabled(false);
			subMenuCalendar.getItem(subMenuCalendar.size()-1).setEnabled(false);

		}
		//		if (calendar == 0){
		//			if (year == 0)
		//				subMenuCalendar.getItem().setEnabled(false);
		//			subMenuCalendar.getItem(2).setEnabled(false);
		//			subMenuCalendar.getItem(3).setEnabled(false);
		//
		//		}else if (calendar == 1)
		//			subMenuCalendar.getItem(3).setEnabled(false);


		MenuItem subMenu2Item = subMenuCalendar.getItem();
		subMenu2Item.setIcon(android.R.drawable.ic_menu_recent_history);
		subMenu2Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);


		SubMenu subMenu3 = menu.addSubMenu(2, 9, 0, "Resol");
		subMenu3.add(5, TYPE_AVERAGE, 0, "Average");
		subMenu3.add(5, TYPE_WIDTH, 1, "Width");
		subMenu3.setGroupCheckable(5, true, true);
		subMenu3.getItem(type-1).setChecked(true);

		MenuItem subMenu3Item = subMenu3.getItem();
		subMenu3Item.setIcon(android.R.drawable.ic_menu_zoom);
		subMenu3Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);


		MenuItem item2 = menu.add(1,8,0,"Info");
		item2.setIcon(android.R.drawable.ic_menu_info_details);
		item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		MenuItem item3 = menu.add(1,7,0,"Comments");
		item3.setIcon(android.R.drawable.ic_menu_edit);
		item3.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
	}
	@Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case TYPE_AVERAGE:
			if (type!=TYPE_AVERAGE){
				type=TYPE_AVERAGE;
				getSupportLoaderManager().restartLoader(LOADER_FILL_CACHE, getBackgroundLoaderBundle(), new GetInBackgroundLoaderCallback(this, mMemoryCache, mDiskCache));
				getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
				item.setChecked(true);
			}
			return true;
		case TYPE_WIDTH:
			if (type!=TYPE_WIDTH){
				type=TYPE_WIDTH;
				getSupportLoaderManager().restartLoader(LOADER_FILL_CACHE, getBackgroundLoaderBundle(), new GetInBackgroundLoaderCallback(this, mMemoryCache, mDiskCache));
				getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
				item.setChecked(true);
			}
			return true;
//		case NO_SCALE:
//		case SCALE_1:
//		case SCALE_1_2:
//		case SCALE_1_4:
//		case SCALE_2_1:
//		case SCALE_4_1:
//			resol=item.getItemId();
//			if (item.isChecked()) item.setChecked(false);
//			else item.setChecked(true);
//			getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
//			return true;
		case 7:
		case 8:
			Intent intent = new Intent();
			intent.setClass(this, ChartDetailsAcitivy.class);
			intent.putExtra("chart", chart);

			if (item.getTitle().equals("Info"))
				intent.putExtra("Option", 0);
			else
				intent.putExtra("Option", 1);

			startActivity(intent);	
			return true;
		case 6:
			try{
				showSeries();
			}catch (NullPointerException e){ return false;}
			return true;
		case YEAR:
			showYear();			
			return true;
		case MONTH:
			showMonth();
			return true;
		case WEEK:
			showWeek();
			return true;
		case DAY:
			showDay();
			return true;
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent2 = new Intent(this, HomeActivity.class);
			intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent2);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public Loader<ChartModel> onCreateLoader(int id, Bundle args) {
//		new ProgressDialogFragment("Charts", "Loading data chart...").show(getSupportFragmentManager(), "DIALOG_F1");
		progress.setVisibility(View.VISIBLE);
		textView.setVisibility(View.GONE);

		int yearVal=0;
		if (year!=0)
			yearVal=chart.firstYear+year-1;
		return new ChartDataLoader(this, chart.id, isReal,width, type,yearVal, month, week,day,mDiskCache,mMemoryCache);
	}

	@Override
	public void onLoadFinished(Loader<ChartModel> loader, ChartModel data) {
		full_chart=data;
		progress.setVisibility(View.GONE);

		if (data == null)
			newChart=null;
		else{
			newChart=new ChartModel(full_chart);
			for (String value : namesNotSelected) 
				removeElem(value);
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
			mChart= chartBuilder.getView(this, newChart);
			mChart.addZoomListener(new ZoomListener() {
				@Override
				public void zoomReset() {					
				}
				@Override
				public void zoomApplied(ZoomEvent event) {
					Log.i(TAG,"Is zoom in?" + event.isZoomIn());
					Log.i(TAG,"Xmax=" + chartBuilder.renderer.getXAxisMax() + " Xmin=" + chartBuilder.renderer.getXAxisMin() + " Ymax=" + chartBuilder.renderer.getYAxisMax() + " Ymin=" + chartBuilder.renderer.getYAxisMin());
				}
			}, true, false);
			setChartListener(mChart);
			mLayout.addView(mChart);
			//			mChart.repaint();
		} catch (ParseException e) {}
		catch (NullPointerException e) {
			textView.setVisibility(View.VISIBLE);
		}
//		hCloseDisplay.sendEmptyMessage(0);
//		try{
//			ProgressDialogFragment d1 = (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag("DIALOG_F1");
//			d1.dismiss();	
//		}catch (NullPointerException e) { }
	}

	public void doDialogClick(int num, int value){
		calendar=num;
		if (chart.lastYear == 0)
			subMenuCalendar.getItem(calendar-1).setChecked(true);
		else
			subMenuCalendar.getItem(calendar).setChecked(true);
		switch (num) {
		case DIALOG_YEAR:
			year=value;
			if (year == 0)
				subMenuCalendar.getItem(1).setEnabled(false);
			else
				subMenuCalendar.getItem(1).setEnabled(true);

			subMenuCalendar.getItem(2).setEnabled(false);
			subMenuCalendar.getItem(3).setEnabled(false);

			month=week=day=0;
			getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
			getSupportLoaderManager().restartLoader(LOADER_FILL_CACHE, getBackgroundLoaderBundle(), new GetInBackgroundLoaderCallback(this, mMemoryCache, mDiskCache));

			break;
		case DIALOG_MONTH:
			month=value;
			week=day=0;
			if (month == 0){
				subMenuCalendar.getItem(subMenuCalendar.size()-1).setEnabled(false);
				subMenuCalendar.getItem(subMenuCalendar.size()-2).setEnabled(false);
				subMenuCalendar.getItem(0).setChecked(true);

			}else{
				subMenuCalendar.getItem(subMenuCalendar.size()-1).setEnabled(true);
				subMenuCalendar.getItem(subMenuCalendar.size()-2).setEnabled(true);
			}
			getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
			getSupportLoaderManager().restartLoader(LOADER_FILL_CACHE, getBackgroundLoaderBundle(), new GetInBackgroundLoaderCallback(this, mMemoryCache, mDiskCache));

			break;
		case DIALOG_WEEK:
			if (week== 0)
				subMenuCalendar.getItem(subMenuCalendar.size()-3).setEnabled(true);
			else{
				subMenuCalendar.getItem(subMenuCalendar.size()-1).setEnabled(true);
				subMenuCalendar.getItem(subMenuCalendar.size()-2).setEnabled(true);
			}
			week=value;
			day=0;
			getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
			break;
		case DIALOG_DAY:
			day=value;
			if (day== 0){
				if (week==0)
					subMenuCalendar.getItem(subMenuCalendar.size()-3).setEnabled(true);
				else
					subMenuCalendar.getItem(subMenuCalendar.size()-2).setEnabled(true);
			}
			else{
				subMenuCalendar.getItem(subMenuCalendar.size()-1).setEnabled(true);
				subMenuCalendar.getItem(subMenuCalendar.size()-2).setEnabled(true);
			}
			getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
			break;
		default:
			break;
		}
	}
	public void doSeriesClick(boolean isChecked, String name){
		if (!isChecked){
			removeElem(name);
			namesNotSelected.add(name);
		}else{
			addElem(name);
			namesNotSelected.remove(name);
		}
		updateChart();
	}
	private void showSeries(){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		DialogFragment prev = (DialogFragment)getSupportFragmentManager().findFragmentByTag("dialog_series");
		if (prev == null){
			final String[] series= new String[full_chart.yValues.size()];
			final boolean[] checkeds= new boolean[series.length];
			for (int i=0; i < series.length; i++){
				series[i] = full_chart.yValues.get(i).name;
				if (namesNotSelected.contains(series[i])){
					checkeds[i]=false;
				}else
					checkeds[i]=true;
			}
			prev=SeriesDialog.newInstance(series, checkeds);	
		}
		prev.show(ft, "dialog_series");
	}
	private void showYear(){
		DialogFragment newDialog= TimePeriodDialog.newInstance(DIALOG_YEAR, year, chart.firstYear,chart.lastYear);
		newDialog.show(getSupportFragmentManager(), "dialog");
	}
	private void showMonth(){
		DialogFragment newDialog= TimePeriodDialog.newInstance(DIALOG_MONTH, month);
		newDialog.show(getSupportFragmentManager(), "dialog");
	}
	private void showWeek(){
		DialogFragment newDialog= TimePeriodDialog.newInstance(DIALOG_WEEK, week);
		newDialog.show(getSupportFragmentManager(), "dialog");
	}
	private void showDay(){
		DialogFragment newDialog= TimePeriodDialog.newInstance(DIALOG_DAY, day);
		newDialog.show(getSupportFragmentManager(), "dialog");
	}
	private Bundle getBackgroundLoaderBundle(){
		Bundle b = new Bundle();
		b.putInt("chart_id", chart.id);
		if (year != 0)
		b.putInt("year", chart.firstYear+year-1);
		else
			b.putInt("year", year);
		b.putBoolean("isReal", isReal);
		b.putInt("width", width);
		b.putInt("month", month);
		b.putInt("type", type);
		return b;
	}

	private void setChartListener(final GraphicalView view){
		view.setOnTouchListener(new View.OnTouchListener() {
			boolean move=false;

			public boolean onTouch(View v, MotionEvent event) {
				view.onTouchEvent(event);
				if (event.getAction() == MotionEvent.ACTION_MOVE && !mScaleDetector.isInProgress())
					move=true;
				if (event.getAction() == MotionEvent.ACTION_UP && move){
					Log.i(TAG,"Movement END");
					move=false;
				}
				mScaleDetector.onTouchEvent(event);
				return true;
			}
		});
	}
	//Find Chart by Name and REMOVE from the newChart instance (for not displaying)
	private boolean removeElem(String name){
		for (int i = 0; i < newChart.yValues.size(); i++) 
			if (newChart.yValues.get(i).name.equals(name)){
				newChart.yValues.remove(i);
				return true;
			}
		return false;
	}

	//Find Chart by Name and ADD from the newChart instance (for displaying)
	private boolean addElem(String name){

		for (int i = 0; i < full_chart.yValues.size(); i++) 
			if (full_chart.yValues.get(i).name.equals(name)){
				newChart.yValues.add(full_chart.yValues.get(i));
				return true;
			}
		return false;
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			Log.i(TAG,"Scale BEGIN");
			return super.onScaleBegin(detector);
		}
		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			Log.i(TAG,"Scale END");
			super.onScaleEnd(detector);
		}
	}
	private static class RetainFragment extends SherlockFragment {
		private static final String TAG = "RetainFragment";
		public LruCache<Integer, ChartModel> mMemoryCache;
		public DiskCacheManager mDiskCache;

		public RetainFragment() {}

		public static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
			RetainFragment fragment = (RetainFragment) fm.findFragmentByTag(TAG);
			if (fragment == null) {
				fragment = new RetainFragment();
			}
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
		}
	}
}
