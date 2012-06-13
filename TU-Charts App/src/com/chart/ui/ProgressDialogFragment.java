package com.chart.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment {
	private ProgressDialog dialog;
	private String name;
	private String description;
	

	public ProgressDialogFragment(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	@Override
	public ProgressDialog onCreateDialog(Bundle savedInstanceState) {
		dialog = new ProgressDialog(getActivity());
		dialog.setTitle(name);
		dialog.setMessage(description);
		dialog.setCancelable(false);	//Disables back button
//		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		dialog.setProgress(0);
		dialog.setIndeterminate(true);
		return dialog;
	}

	public void setProgress(int p) {
		if(dialog != null)
			dialog.setProgress(p);
	}
}
