package com.chart.ui;

import static com.chart.AppUtils.EMAIL;
import static com.chart.AppUtils.LOADER_LOGIN;
import static com.chart.AppUtils.LOADER_REGISTER;
import static com.chart.AppUtils.PASS;
import static com.chart.AppUtils.PERCENTAGE_SSD;
import static com.chart.AppUtils.SERVER_IP;
import static com.chart.AppUtils.USER;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.chart.R;
import com.chart.loaders.LoginLoader;
import com.chart.loaders.RegisterLoader;
import com.chart.restclient.Processor;

public class LoginActivity extends BaseSherlockActivity {
	private String option;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_base_layout);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		if (savedInstanceState != null)
			option = savedInstanceState.getString("opt");
		
		else{
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String email=prefs.getString(EMAIL, "");
			String pass=prefs.getString(PASS, "");
			Processor.i.setUrl(prefs.getString(SERVER_IP, ""));
			if (!(email.isEmpty() || pass.isEmpty()))
				option = "LoginProcessFragment";
			else
				option = "LoginFragment";
		}

		Fragment f= getSupportFragmentManager().findFragmentByTag(option);
		if (f == null){
			FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
			ft.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit);
			ft.replace(R.id.details,Fragment.instantiate(this,LoginActivity.class.getName() + "$" + option), option)
			.commit();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("opt", option);
	}
	public static class LoginProcessFragment extends SherlockFragment implements LoaderCallbacks<Integer>{
		private SharedPreferences prefs;


		@Override
		public void onCreate(Bundle savedInstanceState) {		
			super.onCreate(savedInstanceState);
			prefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View view = inflater.inflate(R.layout.login_process, container, false);

			((TextView)view.findViewById(android.R.id.message)).setText("Login user: " + prefs.getString(EMAIL, ""));

			view.findViewById(R.id.cancel_login).setOnClickListener(new OnClickListener() {		
				public void onClick(View v) {
					goToLoginFragment(null);
				}
			});
			getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_LOGIN, null, this);

			return view;
		}
		private void goToLoginFragment(Bundle b){
			String option="LoginFragment";
			((LoginActivity)getSherlockActivity()).option=option;
			FragmentTransaction ft =getSherlockActivity().getSupportFragmentManager().beginTransaction();
			ft.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit);
			ft.replace(R.id.details,Fragment.instantiate(getSherlockActivity(),LoginActivity.class.getName() + "$" +option,b), option)
			.commitAllowingStateLoss();
		}

		@Override
		public Loader<Integer> onCreateLoader(int arg0, Bundle arg1) {
			return new LoginLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<Integer> arg0, Integer resul) {
			if (resul == 1){
				Intent intent = new Intent(getSherlockActivity(), HomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				getSherlockActivity().finish();
			}else{
				String error="Unknown error";			
				if (resul == -1) error="Password incorrect";
				else if (resul == -3) error="User doesn't exist";
				Bundle b = new Bundle();
				b.putString("error", error);
				goToLoginFragment(b);
			}
		}

		@Override
		public void onLoaderReset(Loader<Integer> arg0) {

		}
	}

	public static class LoginFragment extends SherlockFragment {

		private EditText email, pass;
		private TextView error;
		private SharedPreferences prefs;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.login, container, false);
			prefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());

			email= (EditText) view.findViewById(R.id.email_value);
			pass= (EditText) view.findViewById(R.id.pass_value);

			email.setText(prefs.getString(EMAIL, ""));
			pass.setText(prefs.getString(PASS, ""));

			error= (TextView) view.findViewById(R.id.error_text);
			String errorText=getArguments() != null ? getArguments().getString("error") : "";
			if (!errorText.isEmpty()){
				error.setVisibility(View.VISIBLE);
				error.setText(errorText);
			}else
				error.setVisibility(View.GONE);

			TextView registerScreen = (TextView) view.findViewById(android.R.id.copyUrl);
			registerScreen.setOnClickListener(new View.OnClickListener() {			
				public void onClick(View v) {
					String option="RegisterFragment";
					((LoginActivity)getSherlockActivity()).option=option;
					FragmentTransaction ft =getSherlockActivity().getSupportFragmentManager().beginTransaction();
					ft.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit);
					ft.replace(R.id.details,Fragment.instantiate(getSherlockActivity(),LoginActivity.class.getName() + "$" + option), option)
					.commit();
				}
			});
			view.findViewById(android.R.id.button1).setOnClickListener(new OnClickListener() {	
				public void onClick(View v) {
					if ((email.getText().toString().isEmpty() || pass.getText().toString().isEmpty())){
						error.setText("Fill email and password fields");
						error.setVisibility(View.VISIBLE);
					}else{
						error.setVisibility(View.INVISIBLE);
						SharedPreferences.Editor edit= prefs.edit();
						edit.putString(EMAIL, email.getText().toString());
						edit.putString(PASS, pass.getText().toString());
						edit.commit();
						String option = "LoginProcessFragment";
						((LoginActivity)getSherlockActivity()).option=option;
						FragmentTransaction ft =getSherlockActivity().getSupportFragmentManager().beginTransaction();
						ft.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit);
						ft.replace(R.id.details,Fragment.instantiate(getSherlockActivity(),LoginActivity.class.getName() + "$" + option), option)
						.commit();
					}
				}
			});
			return view;
		}
	}

	public static class RegisterFragment extends SherlockFragment implements LoaderCallbacks<Integer>{

		private EditText username,email, pass;
		private TextView error;
		private SharedPreferences prefs;
		private LoaderCallbacks<Integer> loader;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.register, container, false);
			prefs = PreferenceManager.getDefaultSharedPreferences(getSherlockActivity());
			loader=this;

			username= (EditText) view.findViewById(R.id.user_value);
			email= (EditText) view.findViewById(R.id.email_value);
			pass= (EditText) view.findViewById(R.id.pass_value);
			error= (TextView) view.findViewById(R.id.error_text);

			TextView registerScreen = (TextView) view.findViewById(android.R.id.copyUrl);
			registerScreen.setOnClickListener(new View.OnClickListener() {			
				public void onClick(View v) {
					String option="LoginFragment";
					((LoginActivity)getSherlockActivity()).option=option;
					FragmentTransaction ft =getSherlockActivity().getSupportFragmentManager().beginTransaction();
					ft.setCustomAnimations(R.anim.fragment_slide_left_enter,R.anim.fragment_slide_left_exit,R.anim.fragment_slide_right_enter,R.anim.fragment_slide_right_exit);
					ft.replace(R.id.details,Fragment.instantiate(getSherlockActivity(),LoginActivity.class.getName() + "$" +option), option)
					.commit();
				}
			});
			view.findViewById(android.R.id.button1).setOnClickListener(new OnClickListener() {	
				public void onClick(View v) {
					error.setVisibility(View.GONE);

					if ((email.getText().toString().isEmpty() || pass.getText().toString().isEmpty() || username.getText().toString().isEmpty())){
						error.setText("Fill username, email and password fields");
						error.setVisibility(View.VISIBLE);
					}else{
						SharedPreferences.Editor edit= prefs.edit();
						edit.putString(EMAIL, email.getText().toString());
						edit.putString(USER, username.getText().toString());
						edit.putString(PASS, pass.getText().toString());
						edit.commit();
						getSherlockActivity().getSupportLoaderManager().restartLoader(LOADER_REGISTER, null, loader);
					}
				}
			});
			return view;
		}
		@Override
		public Loader<Integer> onCreateLoader(int arg0, Bundle arg1) {
			return new RegisterLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<Integer> arg0, Integer resul) {
			System.out.println("Finish=" + resul);
			if (resul == 1){
				Intent intent = new Intent(getSherlockActivity(), HomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				getSherlockActivity().finish();
			}else{
				error.setVisibility(View.VISIBLE);
				if (resul == -1) error.setText("User already exist");
				else error.setText("Unknown error");
				SharedPreferences.Editor edit= prefs.edit();
				edit.putString(EMAIL, "");
				edit.putString(USER, "");
				edit.putString(PASS, "");
				edit.commit();
			}
		}

		@Override
		public void onLoaderReset(Loader<Integer> arg0) {

		}
	}
}