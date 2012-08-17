package com.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class TintableImage extends ImageButton{
	private boolean isSelected;
	private int tintUpdateDelay_;
	private int color;
	public TintableImage(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		isSelected=false;
		TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.TintableImage);
		color=typedArray.getColor(R.styleable.TintableImage_tintColorStateList, Color.DKGRAY);
		tintUpdateDelay_ = typedArray.getInt(R.styleable.TintableImage_tintUpdateDelay, 300);
		typedArray.recycle();

		String focusable = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "focusable");
		if (focusable != null)
			setFocusable(Boolean.parseBoolean(focusable));
	}


	//==================================================================================================================
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN && !isSelected) {
			isSelected = true;

			getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
		} else if (event.getAction() == MotionEvent.ACTION_UP && isSelected) {
			isSelected = false;
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() { public void run() { getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP); } }, tintUpdateDelay_);
		}
		return super.onTouchEvent(event);
	}
}
