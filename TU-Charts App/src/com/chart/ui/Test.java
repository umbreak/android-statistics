package com.chart.ui;

import java.util.Calendar;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.chart.R;

public class Test extends SherlockFragmentActivity implements OnClickListener{
	private int year,month,week,day=0;
	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.chart_activity);
		year=2012;month=0;week=1;
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, 2012);

		super.onCreate(arg0);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.previous){
			moveDate(false);
		}
		else if (v.getId() == R.id.next){
			moveDate(true);
		}

	}
	private void moveDate(boolean isNext){
		int val = isNext? 1 : -1;
		Calendar cal=Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
//		cal.get(Calendar.YEAR); cal.get(Calendar.MONTH); cal.get(Calendar.DATE);

		cal.set(Calendar.WEEK_OF_MONTH, week);
//		cal.get(Calendar.YEAR); cal.get(Calendar.MONTH); cal.get(Calendar.DATE);
		
		if (isNext)
			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		else
			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek()+6);
		
		cal.add(Calendar.WEEK_OF_MONTH, val);
		if (cal.get(Calendar.MONTH) != month){
			if (isNext)
				cal.set(Calendar.DAY_OF_MONTH, 1);
			else
				cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			year=cal.get(Calendar.YEAR);
			month=cal.get(Calendar.MONTH);
		}

		week=cal.get(Calendar.WEEK_OF_MONTH);
		System.out.println(year+"/"+month+"/" + cal.get(Calendar.DATE) + " WEEK=" + week);
	}

}
