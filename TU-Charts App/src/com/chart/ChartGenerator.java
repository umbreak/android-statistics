package com.chart;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.actionbarsherlock.R;
import com.chart.pojos.ChartModel;
import com.chart.pojos.LineModel;
import com.chart.pojos.Point;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class ChartGenerator extends AbstractDemoChart{

	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private int max;
	private int min;

	public GraphicalView getView(Context context, ChartModel chartEntry) {
		List <Date> xVal= chartEntry.xValues;
		List<LineModel> results=chartEntry.yValues;
		System.out.println("Xvalues="+ chartEntry.xValues);
		for (LineModel lineModel : results) {
			System.out.println("Yvalues=" + lineModel);
		}

		int[] colors = new int[] { Color.BLUE, Color.RED };
		PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND, PointStyle.POINT};
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);

		getyMargins(results);


		System.out.println("max=" + max + " min="+min);
		System.out.println(results);
		int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	        XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
	        seriesRenderer.setFillPoints(true);
	        seriesRenderer.setLineWidth(2f);
	        seriesRenderer.setDisplayChartValues(true);
	        seriesRenderer.setChartValuesTextSize(9f);
	    }

		setChartSettings(renderer, chartEntry.name,"Dates", "Values", xVal.get(0).getTime(), xVal.get(xVal.size()-1).getTime(),
				min, max, Color.DKGRAY, Color.BLACK, context);
		renderer.setXLabels(5);
		renderer.setYLabels(10);
//		SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(0);
//		seriesRenderer.setDisplayChartValues(true);
		return ChartFactory.getTimeChartView(context,
				buildDateDataset(xVal.toArray(new Date[xVal.size()]), results), renderer, DATE_FORMAT);
	}

	private void getyMargins(List<LineModel> results){
		min=(int)results.get(results.size()-1).values[0];
		max=min;
		for (LineModel line : results) {
			double[] values=line.values.clone();
			Arrays.sort(values);
			if (min > values[0])
				min=(int)values[0];
			if (max < values[values.length-1])
				max=(int)values[values.length-1];
		}
		max=max+1;
		min=min-1;
	}
}

