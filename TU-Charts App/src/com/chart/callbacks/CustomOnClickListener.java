package com.chart.callbacks;
import com.chart.pojos.CommentModel;

import android.view.View;
import android.view.View.OnClickListener;

public class CustomOnClickListener implements OnClickListener{
	private CommentModel comment;
	private OnCustomClickListener callback;

	// Pass in the callback (this'll be the activity) and the row position
	public CustomOnClickListener(OnCustomClickListener  callback, CommentModel comment) {
		this.comment=comment;
		this.callback = callback;
	}

	// The onClick method which has NO position information
	public void onClick(View v) {
		// Let's call our custom callback with the position we added in the constructor
		callback.OnCustomClick(v, comment);
	}
}
