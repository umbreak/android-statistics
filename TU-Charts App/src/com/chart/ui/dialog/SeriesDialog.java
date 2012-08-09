package com.chart.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.chart.ui.ChartActivity;

public class SeriesDialog extends DialogFragment {
	private String[] series;
	private boolean[] cheecked;
	public static SeriesDialog newInstance(String[] series, boolean[] cheecked){
		SeriesDialog f = new SeriesDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putStringArray("series", series);
        args.putBooleanArray("cheecked", cheecked);
        f.setArguments(args);
        return f;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putStringArray("series", series);
		outState.putBooleanArray("cheecked", cheecked);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		series = getArguments().getStringArray("series");
		cheecked = getArguments().getBooleanArray("cheecked");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setMultiChoiceItems(series, cheecked, new DialogInterface.OnMultiChoiceClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int position, boolean isChecked) {
				((ChartActivity)getActivity()).doSeriesClick(isChecked, series[position]);
				dismiss();
			}
		});
		return builder.create();
	}
}