package com.chart.loaders;

import static com.chart.AppUtils.EMAIL;
import static com.chart.AppUtils.PASS;
import static com.chart.AppUtils.USER;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import com.chart.pojos.UserModel;
import com.chart.restclient.Processor;

public class RegisterLoader extends AsyncTaskLoader<Integer> {

	private Integer result;
	private Context context;


	public RegisterLoader(Context context) {
		super(context);
		this.context=context;
		result=0;
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override public Integer loadInBackground() {
		System.out.println("** RegisterLoader: Registering the user");

		UserModel user=new UserModel();
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		user.email=settings.getString(EMAIL, "");
		user.password=settings.getString(PASS, "");
		user.username=settings.getString(USER, "");

		return Processor.i.putUser(user);
		//		String message="";
		//		if (result == -1) message="Password incorrect";
		//		else if (result == -3) message="User doesn't exist";
	}

	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override public void deliverResult(Integer data) {
		if (isReset()) 
			onReleaseResources(data);
		Integer oldValue = data;
		result=data;
		if (isStarted()) 
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(data);
		if (oldValue != null)
			onReleaseResources(oldValue);

	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override protected void onStartLoading() {
		System.out.println("start loading");
		if(result!= 0)
			deliverResult(result);
		else
			forceLoad();
	}

	/**
	 * Handles a request to stop the Loader.
	 */
	@Override protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	/**
	 * Handles a request to cancel a load.
	 */
	@Override public void onCanceled(Integer apps) {
		super.onCanceled(apps);

		// At this point we can release the resources associated with 'apps'
		// if needed.
		onReleaseResources(apps);
	}

	/**
	 * Handles a request to completely reset the Loader.
	 */
	@Override protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		if (result != 0){
			onReleaseResources(result);
			result=0;
		}
	}

	/**
	 * Helper function to take care of releasing resources associated
	 * with an actively loaded data set.
	 */
	protected void onReleaseResources(Integer apps) {
	}
}
