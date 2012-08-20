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
import java.util.Calendar;

import org.achartengine.GraphicalView;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
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
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
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
public class ChartFragment extends SherlockFragment implements LoaderCallbacks<ChartModel>{
	private static final String TAG="ChartActivity";
	private BaseChartModel chart;
	private ChartModel full_chart;
	private ChartModel newChart;
	private FragmentManager fm;
	private ChartGenerator chartBuilder;
	private GraphicalView mChart;
	private LinearLayout mLayout;
	public int calendar,year,month,week,day;
	public boolean isReal;
	public int type;
	private int width;
	private TextView textView;
	//	private Handler hCloseDisplay;
	private ProgressBar progress;
	private SubMenu subMenuCalendar;


	private ArrayList<String> namesNotSelected;
	private ScaleGestureDetector mScaleDetector;

	private LruCache<Integer, ChartModel> mMemoryCache;
	private DiskCacheManager mDiskCache;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		chart= (BaseChartModel)(getArguments() != null ? getArguments().getParcelable("chart") : null);
		fm = getActivity().getSupportFragmentManager();
		chartBuilder =  new ChartGenerator();
		mMemoryCache=((ChartActivity)getSherlockActivity()).mMemoryCache;
		mDiskCache=((ChartActivity)getSherlockActivity()).mDiskCache;

		if (savedInstanceState == null){
			calendar=year=month=week=day=0;
			if (chart.lastYear ==0) year=chart.firstYear;
			isReal=false;
			type=TYPE_AVERAGE;
			namesNotSelected= new ArrayList<String>();

		}else{
			calendar=savedInstanceState.getInt("calendar");
			year = savedInstanceState.getInt("year");
			month = savedInstanceState.getInt("month");
			week = savedInstanceState.getInt("week");
			day = savedInstanceState.getInt("day");
			isReal=savedInstanceState.getBoolean("isReal");
			type=savedInstanceState.getInt("type");
			namesNotSelected=savedInstanceState.getStringArrayList("NotSelected");
		}

		setHasOptionsMenu(true);

		Display display = getSherlockActivity().getWindowManager().getDefaultDisplay();
		if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR2){
			Point size = new Point();
			display.getSize(size);
			width=size.x;
		}else
			width=display.getWidth();

		//Creating the mMemoryCache or retrieving from the Fragment (Handling Configuration Changes)
		initCheckbox(this);
		mScaleDetector = new ScaleGestureDetector(getSherlockActivity(), new ScaleListener());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.chart_fragment, container, false);
		mLayout= (LinearLayout) view.findViewById(R.id.chart);
		textView=(TextView) view.findViewById(R.id.info);
		progress=(ProgressBar) view.findViewById(R.id.progress);

		return view;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_CHART, null, this);
		getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_FILL_CACHE, getBackgroundLoaderBundle(), new GetInBackgroundLoaderCallback(getSherlockActivity(), mMemoryCache, mDiskCache));
	}

	//INIT REAL CHECKBOX ON ACTIONBAR--------------------------------------------------------------------------------------->
	private void initCheckbox(final LoaderManager.LoaderCallbacks<ChartModel> loader){
		//Inflate the custom view
		View customNav = LayoutInflater.from(getSherlockActivity().getSupportActionBar().getThemedContext()).inflate(R.layout.custom_actionbar_view, null);
		//Bind to its state change  
		CheckBox check= (CheckBox)customNav.findViewById(android.R.id.checkbox);
		check.setChecked(isReal);
		check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isReal=isChecked;
				getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_CHART, null,loader );
			}
		});
		//Attach to the action bar
		getSherlockActivity().getSupportActionBar().setCustomView(customNav);
		getSherlockActivity().getSupportActionBar().setDisplayShowCustomEnabled(true);
	}

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

	//CREATE MENU--------------------------------------------------------------------------------------->
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		subMenuCalendar = menu.addSubMenu(2,9,2,"Calendar");
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


		MenuItem subMenu2Item = subMenuCalendar.getItem();
		subMenu2Item.setIcon(android.R.drawable.ic_menu_recent_history);
		subMenu2Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);


		SubMenu subMenu3 = menu.addSubMenu(2, 9, 3, "Resol");
		subMenu3.add(5, TYPE_AVERAGE, 0, "Average");
		subMenu3.add(5, TYPE_WIDTH, 1, "Width");
		subMenu3.setGroupCheckable(5, true, true);
		subMenu3.getItem(type-1).setChecked(true);

		MenuItem subMenu3Item = subMenu3.getItem();
		subMenu3Item.setIcon(android.R.drawable.ic_menu_zoom);
		subMenu3Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}

	//ACTION ON MENU SELECTION--------------------------------------------------------------------------------------->
	@Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case TYPE_AVERAGE:
			if (type!=TYPE_AVERAGE){
				type=TYPE_AVERAGE;
				getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_FILL_CACHE, getBackgroundLoaderBundle(), new GetInBackgroundLoaderCallback(getSherlockActivity(), mMemoryCache, mDiskCache));
				getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
				item.setChecked(true);
			}
			return true;
		case TYPE_WIDTH:
			if (type!=TYPE_WIDTH){
				type=TYPE_WIDTH;
				getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_FILL_CACHE, getBackgroundLoaderBundle(), new GetInBackgroundLoaderCallback(getSherlockActivity(), mMemoryCache, mDiskCache));
				getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
				item.setChecked(true);
			}
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
			Intent intent2 = new Intent(getSherlockActivity(), HomeActivity.class);
			intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent2);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	//AFTER DIALOG SELECTION--------------------------------------------------------------------------------------->
	public void doDialogClick(int num, int value){
		calendar=num;
		if (chart.lastYear == 0)
			subMenuCalendar.getItem(calendar-1).setChecked(true);
		else
			subMenuCalendar.getItem(calendar).setChecked(true);
		switch (num) {
		case DIALOG_YEAR:
			if (value ==0){
				year=0;
				subMenuCalendar.getItem(1).setEnabled(false);
			}else{
				year=chart.firstYear+value-1;
				subMenuCalendar.getItem(1).setEnabled(true);
			}
			subMenuCalendar.getItem(2).setEnabled(false);
			subMenuCalendar.getItem(3).setEnabled(false);

			month=week=day=0;
			getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
			getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_FILL_CACHE, getBackgroundLoaderBundle(), new GetInBackgroundLoaderCallback(getSherlockActivity(), mMemoryCache, mDiskCache));

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
			getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
			getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_FILL_CACHE, getBackgroundLoaderBundle(), new GetInBackgroundLoaderCallback(getSherlockActivity(), mMemoryCache, mDiskCache));

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
			getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
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
			getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
			break;
		default:
			break;
		}
	}
	//SHOW DIALOGS	--------------------------------------------------------------------------------------->
	private void showYear(){
		int posYear= year!=0 ? (year-chart.firstYear+1) : 0;
		DialogFragment newDialog= TimePeriodDialog.newInstance(DIALOG_YEAR,posYear , chart.firstYear,chart.lastYear);
		newDialog.show(fm, "dialog");
	}
	private void showMonth(){
		DialogFragment newDialog= TimePeriodDialog.newInstance(DIALOG_MONTH, month);
		newDialog.show(fm, "dialog");
	}
	private void showWeek(){
		DialogFragment newDialog= TimePeriodDialog.newInstance(DIALOG_WEEK, week);
		newDialog.show(fm, "dialog");
	}
	private void showDay(){
		DialogFragment newDialog= TimePeriodDialog.newInstance(DIALOG_DAY, day);
		newDialog.show(fm, "dialog");
	}

	//CHART LOADER	--------------------------------------------------------------------------------------->

	@Override
	public Loader<ChartModel> onCreateLoader(int id, Bundle args) {
		//		new ProgressDialogFragment("Charts", "Loading data chart...").show(getSupportFragmentManager(), "DIALOG_F1");
		fadeAnimation(progress, mChart);
		progress.setVisibility(View.VISIBLE);
		progress.bringToFront();
		textView.setVisibility(View.GONE);

		return new ChartDataLoader(getSherlockActivity(), chart.id, isReal,width, type,year, month, week,day,mDiskCache,mMemoryCache);
	}

	@Override
	public void onLoadFinished(Loader<ChartModel> loader, ChartModel data) {
		full_chart=data;
		
		if (data == null)
			newChart=null;
		else{
			newChart=new ChartModel(full_chart);
			for (String value : namesNotSelected) 
				removeElem(value);
		}
		updateChart();
		fadeAnimation(mChart, progress);

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
	//REFRESH CHART CONTENT	--------------------------------------------------------------------------------------->

	private void updateChart(){
		try {
			mLayout.removeAllViews();
			int sel = chart.lastYear==0 ? (calendar+1) : calendar;
			if (!subMenuCalendar.getItem(subMenuCalendar.size()-3).isEnabled()) sel=-1;
			mChart= chartBuilder.getView(getSherlockActivity(), newChart, sel);
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
		} catch (ParseException e) {}
		catch (NullPointerException e) {
			textView.setVisibility(View.VISIBLE);
		}
	}
	//SERIES MANAGER	--------------------------------------------------------------------------------------->
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
	void showSeries(){
		FragmentTransaction ft = fm.beginTransaction();
		DialogFragment prev = (DialogFragment)fm.findFragmentByTag("dialog_series");
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

	//BACKGROUND LOADER BUNDLE	--------------------------------------------------------------------------------------->
	private Bundle getBackgroundLoaderBundle(){
		Bundle b = new Bundle();
		b.putInt("chart_id", chart.id);
		b.putInt("year", year);
		b.putBoolean("isReal", isReal);
		b.putInt("width", width);
		b.putInt("month", month);
		b.putInt("type", type);
		return b;
	}
	private void fadeAnimation(View appear, View dissapear){
		//Appearing
		if (appear != null){
			ObjectAnimator alphaShow = ObjectAnimator.ofFloat(appear,"alpha", 0f, 1f);
			alphaShow.setDuration(300);
			alphaShow.start();
		}
		if (dissapear != null){
			ObjectAnimator alphaHide = ObjectAnimator.ofFloat(dissapear,"alpha", 1f, 0f);
			alphaHide.setDuration(500);
			alphaHide.start();
		}
	}
	//ON NEXT/PREVIOUS BUTTON CLICK--------------------------------------------------------------------------------------->
	public void moveDate(boolean isNext){
		int val=isNext ? 1 : -1;
		//true when we change to the next step in the upper level. Example: If we're in Days zoom and
		//we select NEXT_DAY=29+1, the Month will change to March. This is a stageChange=true.
		boolean stageChange=false;
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		if (day != 0){
			day=day+val;
			cal.set(year, month, day);	
			if (cal.get(Calendar.MONTH) != month){
				stageChange=true;
				day=cal.get(Calendar.DAY_OF_MONTH);
				month=cal.get(Calendar.MONTH)+1;
				year=cal.get(Calendar.YEAR);
			}
		}else if (week != 0){
			week=week+val;
			cal.set(Calendar.WEEK_OF_MONTH, week);
			if (cal.get(Calendar.MONTH) != month){
				stageChange=true;
				week=cal.get(Calendar.WEEK_OF_MONTH);
				month=cal.get(Calendar.MONTH)+1;
				year=cal.get(Calendar.YEAR);
			}
		}else if (month != 0 ){
			month=month+val;
			cal.set(Calendar.MONTH, month);
			if (cal.get(Calendar.YEAR) != year){
				stageChange=true;
				month=cal.get(Calendar.MONTH)+1;
				year=cal.get(Calendar.YEAR);
			}
		}
		else if (year!= 0 && (year+val) <= chart.lastYear && (year+val) >= chart.firstYear){
			cal.set(Calendar.YEAR, (year+val));
			year=year+val;
		}

		getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_CHART, null, this);
		if (stageChange) 
			getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_FILL_CACHE, getBackgroundLoaderBundle(), new GetInBackgroundLoaderCallback(getSherlockActivity(), mMemoryCache, mDiskCache));

	}

	//SCROLL MOVEMENT--------------------------------------------------------------------------------------->
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

	//TWO FINGER LISTENER--------------------------------------------------------------------------------------->
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
}
