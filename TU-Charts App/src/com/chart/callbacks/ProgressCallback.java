package com.chart.callbacks;

import com.chart.pojos.CommentModel;

public interface ProgressCallback {
	void isloading(boolean val);
	void addComment(CommentModel c);
	void delComment(CommentModel c);
}
