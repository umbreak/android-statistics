package com.chart.ui.dialog;

import static com.chart.AppUtils.DIALOG_MONTH;
import static com.chart.AppUtils.DIALOG_WEEK;
import static com.chart.AppUtils.DIALOG_YEAR;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.chart.R;
import com.chart.ui.ChartActivity;

public class TimePeriodDialog extends DialogFragment {
	int num, pos, firstYear, lastYear;
	public static TimePeriodDialog newInstance(int num, int ...pos){
		TimePeriodDialog f = new TimePeriodDialog();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		args.putInt("pos", pos[0]);
		if (num == DIALOG_YEAR){
			args.putInt("first", pos[1]);
			args.putInt("last", pos[2]);

		}
		f.setArguments(args);
		return f;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("num", num);
		outState.putInt("pos", pos);
		if (num == DIALOG_YEAR){
			outState.putInt("first", firstYear);
			outState.putInt("last", lastYear);

		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		num = getArguments().getInt("num");
		pos = getArguments().getInt("pos");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String array[]=null;
		if (num == DIALOG_YEAR){
			firstYear=getArguments().getInt("first");
			lastYear=getArguments().getInt("last");

			array=new String[(lastYear-firstYear)+2];
			array[0]="All";
			for(int i=1,year=firstYear; i < array.length; i++,year++)
				array[i]=String.valueOf(year);
			builder.setTitle("Choose the year to display");	
		}else if (num == DIALOG_MONTH){
			array=getResources().getStringArray(R.array.months);
			builder.setTitle("Choose the month to display");
		}else if (num == DIALOG_WEEK){
			array=new String[]{"All", "1st", "2nd", "3rd", "4th", "5th"};
			builder.setTitle("Choose the week to display");
		}else{
			array=new String[]{"All", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21"
					, "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
			builder.setTitle("Choose the day to display");
		}
		builder.setSingleChoiceItems(array, pos, new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int position) {
				((ChartActivity)getActivity()).doDialogClick(num, position);
				dismiss();
			}
		});
		return builder.create();
	}
}
