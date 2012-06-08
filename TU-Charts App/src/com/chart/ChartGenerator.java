package com.chart;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

import com.chart.pojos.ChartModel;
import com.chart.pojos.SerieModel;

public class ChartGenerator extends AbstractChart{

	public GraphicalView getView(Context context, ChartModel chartEntry) throws ParseException {
		
//		List <Date> xVal= chartEntry.xValues;
		List<SerieModel> results=chartEntry.yValues;
		System.out.println("Xvalues="+ chartEntry.xValues);
		for (SerieModel lineModel : results) {
			System.out.println("Yvalues=" + lineModel);
		}
		int[] all_colors= new int[]{Color.BLUE,Color.RED,Color.GREEN,Color.MAGENTA,Color.CYAN,Color.YELLOW};

		int[] colors = new int[results.size()];
		for (int i = 0; i < colors.length; i++) 
			colors[i]=all_colors[i];
		
		
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, PointStyle.CIRCLE);
		renderer.setAntialiasing(true);

		
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
			seriesRenderer.setFillPoints(true);
			seriesRenderer.setLineWidth(2f);
			seriesRenderer.setDisplayChartValues(true);
			seriesRenderer.setChartValuesTextSize(9f);
		}
		
		//Checking for the type of data on the X axis. It can be a double or a Date
		if (isNum(chartEntry.xValues[0])){
			double xVal[]=new double[chartEntry.xValues.length];
			for (int i = 0; i < xVal.length; i++)
				xVal[i]=Double.parseDouble(chartEntry.xValues[i]);
			setChartSettings(renderer, chartEntry.name,chartEntry.xLegend, chartEntry.yLegend, xVal[0], xVal[xVal.length-1],
					chartEntry.min, chartEntry.max, Color.DKGRAY, Color.BLACK, context);
			return ChartFactory.getLineChartView(context,buildDataset(xVal, results), renderer);
		}else{
			Date xVal[]=new Date[chartEntry.xValues.length];
			for (int i = 0; i < xVal.length; i++)
				xVal[i]=AppUtils.i.date_format.parse(chartEntry.xValues[i]);
			setChartSettings(renderer, chartEntry.name,chartEntry.xLegend, chartEntry.yLegend, xVal[0].getTime(), xVal[xVal.length-1].getTime(),
					chartEntry.min, chartEntry.max, Color.DKGRAY, Color.BLACK, context);
			return ChartFactory.getTimeChartView(context,
					buildDateDataset(xVal, results), renderer, AppUtils.i.date_format.toPattern());
		}			
//		renderer.setXLabels(5);
//		renderer.setYLabels(10);	
	}

	private boolean isNum(String s) {
		try {
			Double.parseDouble(s);
		}
		catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}