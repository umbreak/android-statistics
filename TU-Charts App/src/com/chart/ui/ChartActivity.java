package com.chart.ui;

import static com.chart.AppUtils.DAY;
import static com.chart.AppUtils.MONTH;
import static com.chart.AppUtils.TYPE_AVERAGE;
import static com.chart.AppUtils.TYPE_WIDTH;
import static com.chart.AppUtils.WEEK;
import static com.chart.AppUtils.YEAR;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.chart.R;
import com.chart.memory.DiskCacheManager;
import com.chart.pojos.BaseChartModel;
import com.chart.pojos.ChartModel;

@SuppressLint("NewApi")
public class ChartActivity extends SherlockFragmentActivity implements OnClickListener{
	private static final String TAG="ChartActivity";
	private BaseChartModel chart;
	private ChartFragment activeFragment;

	public LruCache<Integer, ChartModel> mMemoryCache;
	public DiskCacheManager mDiskCache;


	//	private Context aContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.chart_activity);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		chart = getIntent().getParcelableExtra("chart");
		initCaches();
		setTitle("");
		System.out.println("Resize Cache");

		activeFragment= (ChartFragment)getSupportFragmentManager().findFragmentByTag("Chart_" + chart.id);
		if (activeFragment == null){
			Bundle b = new Bundle();
			b.putParcelable("chart", chart);
			activeFragment= (ChartFragment)Fragment.instantiate(this, ChartFragment.class.getName(), b);
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.frag, activeFragment, "Chart_" + chart.id)
			.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit)
			.commit();
		}

		//Creating the mMemoryCache or retrieving from the Fragment (Handling Configuration Changes)
		

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item1 = menu.add(2,6,1,"Serie");
		item1.setIcon(android.R.drawable.ic_menu_view);
		item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem item2 = menu.add(1,8,4,"Info");
		item2.setIcon(android.R.drawable.ic_menu_info_details);
		item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		MenuItem item3 = menu.add(1,7,5,"Comments");
		item3.setIcon(android.R.drawable.ic_menu_edit);
		item3.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return super.onCreateOptionsMenu(menu);
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

	public void doSeriesClick(boolean isChecked, String name){
		activeFragment.doSeriesClick(isChecked, name);
	}
	public void doDialogClick(int num, int value){
		activeFragment.doDialogClick(num, value);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.previous){
			activeFragment.moveDate(false);
		}
		else if (v.getId() == R.id.next){
			activeFragment.moveDate(true);
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
