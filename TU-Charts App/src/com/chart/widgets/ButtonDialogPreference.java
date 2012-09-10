package com.chart.widgets;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import com.chart.memory.DiskCacheManager;

public final class ButtonDialogPreference extends DialogPreference {

	public ButtonDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult)
			new DiskCacheManager(getContext()).delete();

		super.onDialogClosed(positiveResult);
	}

}
