package com.chart.ui;



import static com.chart.AppUtils.LOADER_ADD_COMMENTS;
import static com.chart.AppUtils.LOADER_LIST_COMMENTS;
import static com.chart.AppUtils.LOADER_REMOVE_COMMENT;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.chart.AppUtils;
import com.chart.R;
import com.chart.browser.adapters.CommentAdapter;
import com.chart.callbacks.AddCommentLoaderCallback;
import com.chart.callbacks.DelCommentLoaderCallback;
import com.chart.callbacks.OnCustomClickListener;
import com.chart.callbacks.ProgressCallback;
import com.chart.loaders.CommentsLoader;
import com.chart.pojos.BaseChartModel;
import com.chart.pojos.CommentModel;

public class ChartDetailsAcitivy extends BaseSherlockActivity{
	private BaseChartModel chart;
	//Option == 0 -> InfoFragment | Option == 1 -> CommentsFragment
	private int option;

	//	private Context aContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.simple_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		chart = (BaseChartModel) getIntent().getParcelableExtra("chart");

		setTitle(chart.name);

		if (savedInstanceState == null)
			option = getIntent().getExtras().getInt("Option");
		
		else
			option = savedInstanceState.getInt("opt");
		

		FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
		if (option == 0){	
			Fragment f= getSupportFragmentManager().findFragmentByTag("Info_" + chart.id);
			if (f == null){
				ft.replace(android.R.id.content,InfoFragment.newInstance(chart), "Info_" + chart.id)
				.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit)
				.commit();
			}
		}else if (option == 1){
			Fragment f= getSupportFragmentManager().findFragmentByTag("Comment_" + chart.id);
			if (f == null){
				ft.replace(android.R.id.content,CommentsFragment.newInstance(chart.id), "Comment_" + chart.id)
				.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit)
				.commit();
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("opt", option);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().toString().startsWith("Info")){
			option=0;
			Fragment frag=InfoFragment.newInstance(chart);
			FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
			ft.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit);
			ft.replace(android.R.id.content, frag, "Info_" + chart.id).commit();
			return true;
		}else if (item.getTitle().toString().startsWith("Comments")){
			option=1;
			Fragment frag=CommentsFragment.newInstance(chart.id);
			FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
			ft.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit);
			ft.replace(android.R.id.content, frag, "Comments_"+ chart.id).commit();
			return true;
		}else if (item.getItemId() == android.R.id.home){
			Intent intent = new Intent();
			intent.setClass(this, ChartActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("chart", chart);			
			startActivity(intent);
			return true;
		}
		else
			return super.onOptionsItemSelected(item);

	}



	//InfoFragment class (contains the relevant information of a chart)
	public static class InfoFragment extends SherlockListFragment{
		private BaseChartModel chart;

		public static InfoFragment newInstance(BaseChartModel chart) {
			InfoFragment f = new InfoFragment();
			// Supply index input as an argument.
			Bundle args = new Bundle();
			args.putParcelable("chart", chart);
			f.setArguments(args);
			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			chart = (BaseChartModel) (getArguments() != null ? getArguments().getParcelable("chart") : null);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// Give some text to display if there is no data.
			setEmptyText("No data for Chart found");

			// We have a menu item to show in action bar.
			setHasOptionsMenu(true);

			List<Map<String, String>> data = new ArrayList<Map<String,String>>();
			Map<String, String> datum = new HashMap<String, String>(2);

			datum.put("title", "Name");
			datum.put("date", chart.name);
			data.add(datum);

			datum = new HashMap<String, String>(2);
			datum.put("title", "Description");
			datum.put("date", chart.description);
			data.add(datum);

			datum = new HashMap<String, String>(2);
			datum.put("title", "Date of creation");
			datum.put("date", AppUtils.i.date_format.format(chart.date));
			data.add(datum);

			datum = new HashMap<String, String>(2);
			datum.put("title", "Category");
			datum.put("date", chart.category.name);
			data.add(datum);

			datum = new HashMap<String, String>(2);
			datum.put("title", "Num. votes");
			datum.put("date", String.valueOf(chart.votes));
			data.add(datum);
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), data,android.R.layout.simple_list_item_2, new String[] {"title", "date"},
					new int[] {android.R.id.text1,android.R.id.text2});
			setListAdapter(adapter);

		}

		@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// Place an action bar item for searching.
			menu.clear();
			MenuItem item = menu.add("Comments");
			item.setIcon(android.R.drawable.ic_menu_edit);
			item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}

	}



	//CommentsFragment class (contains the list of comments of a chart)
	public static class CommentsFragment extends SherlockFragment implements LoaderCallbacks<List<CommentModel>>, OnCustomClickListener{
		private int chart_id;
		private CommentAdapter mAdapter;
		private ListView listView;
		private EditText text;
		private ImageButton send;
		private ProgressBar progress;
		private AddCommentLoaderCallback addCommentCallback;
		private DelCommentLoaderCallback delCommentCallback;
		private ProgressCallback progressCall;


		public static CommentsFragment newInstance(int chart_id) {
			CommentsFragment f = new CommentsFragment();
			// Supply index input as an argument.
			Bundle args = new Bundle();
			args.putInt("chart_id", chart_id);
			f.setArguments(args);
			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);	
			chart_id = (int) (getArguments() != null ? getArguments().getInt("chart_id") : 0);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.chart_comment_group, container, false);
			listView= (ListView) view.findViewById(android.R.id.list);
			text=(EditText)view.findViewById(R.id.text_comments);
			send=(ImageButton)view.findViewById(R.id.add_comment);
			progress=(ProgressBar)view.findViewById(R.id.progress);	
			return view;
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			progressCall = new ProgressCallback() {
				
				@Override public void isloading(boolean val) {
					if (val){
						AppUtils.i.fadeAnimation(progress, listView);
					}
					else
						AppUtils.i.fadeAnimation(listView, progress);
				}

				@Override public void addComment(CommentModel data) {
					Loader<List<CommentModel>> loader=getSherlockActivity().getSupportLoaderManager().getLoader(LOADER_LIST_COMMENTS);
					CommentsLoader cloader=(CommentsLoader) loader;
					cloader.addComment(data);
					mAdapter.add(data);
					isloading(false);
					
				}

				@Override public void delComment(CommentModel data) {
					Loader<List<CommentModel>> loader=getSherlockActivity().getSupportLoaderManager().getLoader(LOADER_LIST_COMMENTS);
					CommentsLoader cloader=(CommentsLoader) loader;
					cloader.delComment(data);
					mAdapter.remove(data);	
					isloading(false);

				}
			};

			super.onViewCreated(view, savedInstanceState);

		}


		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// Give some text to display if there is no data.  In a real
			// application this would come from a resource.
			//			setEmptyText("No comments found");

			// We have a menu item to show in action bar.
			setHasOptionsMenu(true);

			// Create an empty adapter we will use to display the loaded data.
			//			View footerView = View.inflate(getSherlockActivity(), R.layout.chart_comment_footer, null);
			//			text=(EditText)footerView.findViewById(R.id.text_comments);
			//			send=(Button)footerView.findViewById(R.id.add_comment);
			send.setOnClickListener(new OnClickListener() {		
				@Override
				public void onClick(View v) {
					addComment();	
				}
			});
			textManagment();
			listView.setTextFilterEnabled(true);

			mAdapter = new CommentAdapter(getActivity(), this);
			listView.setAdapter(mAdapter);

			// Prepare the loader.  Either re-connect with an existing one,
			// or start a new one.

			getSherlockActivity().getSupportLoaderManager().initLoader(LOADER_LIST_COMMENTS, null, this);
			addCommentCallback = new AddCommentLoaderCallback(getSherlockActivity(), progressCall);
			delCommentCallback= new DelCommentLoaderCallback(getSherlockActivity(),progressCall );
		}

		private void textManagment(){
			//Listener for the area of a text comment. When ENTER is pressed, call addComment() method
			text.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// If the event is a key-down event on the "enter" button
					if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
							(keyCode == KeyEvent.KEYCODE_ENTER)) {
						// Perform action on key press
						addComment();
						return true;
					}
					return false;
				}
			});
		}
		private void addComment(){
			InputMethodManager imm = (InputMethodManager)getSherlockActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(text.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);	
			if (!text.getText().toString().equals("")){
				CommentModel c = new CommentModel(text.getText().toString());
				Bundle b = new Bundle();
				b.putInt("chart_id", chart_id);
				b.putParcelable("comment", c);
				getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_ADD_COMMENTS,b,addCommentCallback);
				text.setText("");
			}
		}

		@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// Place an action bar item for searching.
			menu.clear();
			MenuItem item = menu.add("Info");
			item.setIcon(android.R.drawable.ic_menu_info_details);
			item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}

		@Override
		public Loader<List<CommentModel>> onCreateLoader(int id, Bundle args) {
			//			isLoading(true);
			AppUtils.i.fadeAnimation(progress, listView);
			return new CommentsLoader(getSherlockActivity(), chart_id);
		}


		@Override
		public void onLoadFinished(Loader<List<CommentModel>> loader, List<CommentModel> data) {
			// Set the new data in the adapter.
			AppUtils.i.fadeAnimation(listView, progress);

			//			isLoading(false);
			mAdapter.setData(data);
		}

		@Override
		public void onLoaderReset(Loader<List<CommentModel>> arg0) {
			// Clear the data in the adapter.
			AppUtils.i.fadeAnimation(listView, progress);
			mAdapter.setData(null);	}

		//Callback for when the delete button from the adapter is clicked
		@Override
		public void OnCustomClick(View aView, CommentModel comment) {
			Bundle b = new Bundle();
			b.putInt("chart_id", chart_id);
			b.putParcelable("comment", comment);
			getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_REMOVE_COMMENT,b,delCommentCallback);
		}

	}
}
