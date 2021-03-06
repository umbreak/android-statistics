package com.chart;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import com.chart.pojos.ChartModel;
import com.chart.pojos.SerieModel;

public class ChartGenerator extends AbstractChart{

	public XYMultipleSeriesRenderer renderer;

	public GraphicalView getView(Context context, ChartModel chartEntry, int sel, long xMax, long xMin, double yMax, double yMin) throws ParseException {
		List<SerieModel> results=chartEntry.yValues;
		if (results.size() == 0) return null;
		Resources r=context.getResources();
		int[] all_colors= new int[]{r.getColor(R.color.chart_blue),r.getColor(R.color.chart_red),
				r.getColor(R.color.chart_green),r.getColor(R.color.chart_brown),r.getColor(R.color.chart_pink),r.getColor(R.color.chart_violet)};
		int[] colors = new int[results.size()];
		for (int i = 0; i < colors.length; i++) 
			colors[i]=all_colors[(results.get(i).id%all_colors.length)];
		int[] yEdges=getMaxMin(results);
		//		XYMultipleSeriesRenderer renderer = buildRenderer(colors, PointStyle.CIRCLE);
		renderer = buildRenderer(colors);

		renderPointAtributes(renderer);
//		renderer.setZoomButtonsVisible(true);
		renderer.setZoomEnabled(true);
		renderer.setInScroll(false);
		renderer.setShowGrid(true);

		//Checking for the type of data on the X axis. It can be a double or a Date
		if (!isDate(chartEntry.xValues[0])){
			long xVal[]=chartEntry.xValues;
			setChartSettings(renderer, chartEntry.name,chartEntry.xLegend, chartEntry.yLegend, xVal[0], xVal[xVal.length-1],
					yEdges[0], yEdges[1], r.getColor(R.color.chart_darkgrey), Color.BLACK, context);
			return ChartFactory.getLineChartView(context,buildDataset(xVal, results), renderer);
		}else{
			Date xVal[]=new Date[chartEntry.xValues.length];
			for (int i = 0; i < xVal.length; i++)
				xVal[i]=new Date((long)chartEntry.xValues[i]);
			String title="";
			Calendar cal=Calendar.getInstance();
			cal.setTime(xVal[0]);
			if (sel == 0) title=" (Year: "+ cal.get(Calendar.YEAR) + ")";
			else if (sel == 1) title=" (Month: " + new DateFormatSymbols().getMonths()[cal.get(Calendar.MONTH)] + ")";
			else if (sel == 2) title=" (Week: " + cal.get(Calendar.WEEK_OF_MONTH) + ")";
			else if (sel == 3) title=" (Day: " + cal.get(Calendar.DAY_OF_MONTH) + ")";
//			System.out.println(xMax + " " + xMin);
			if (xMax != Long.MAX_VALUE && xMin != Long.MAX_VALUE)
				setChartSettings(renderer, chartEntry.name + title,chartEntry.xLegend, chartEntry.yLegend, xMin, xMax,
						yMin, yMax, r.getColor(R.color.chart_darkgrey), Color.BLACK, context);
			else
				setChartSettings(renderer, chartEntry.name + title,chartEntry.xLegend, chartEntry.yLegend, xVal[0].getTime(), xVal[xVal.length-1].getTime(),
						yEdges[0], yEdges[1], r.getColor(R.color.chart_darkgrey), Color.BLACK, context);
			return ChartFactory.getTimeChartView(context, buildDateDataset(xVal, results), renderer, getDateFormat(xVal, sel));
		}			
	}

	private String getDateFormat(Date xVal[], int sel){
		String resul="dd.MM.yy";
		if (xVal.length <= 2) return resul;
		long diff = Math.abs(xVal[1].getTime()  - xVal[0].getTime());
		if (diff > 2160000000L) //Bigger than 25 days
			resul="MM.yy";
		else if (diff > 54000000L) //Bigger than 15 hours
			resul="dd.MM.yy";
		else if (sel == 3){
			if (diff > 3000000) //Bigger than 50 min
				resul="dd.MM.yy HH";
			else if (diff > 50000) //Bigger than 50 sec
				resul="dd.MM.yy HH:mm";
			else if (diff > 900) //Bigger than 900ms
				resul="dd.MM.yy HH:mm:ss";	
		}
		return resul;
	}

	private void renderPointAtributes(XYMultipleSeriesRenderer renderer){
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
			//			seriesRenderer.setFillPoints(true);
			seriesRenderer.setLineWidth(1.5f);
			//			seriesRenderer.setDisplayChartValues(true);
			//			seriesRenderer.setChartValuesTextSize(9f);
		}
	}

	private boolean isDate(long d) {
		try {new Date(d);}
		catch (Exception e) {return false;}
		return true;
	}
	private int[] getMaxMin(List <SerieModel> list){
		int resul[]=new int[2];
		double max=(double)(list.get(0).max);
		double min=(double)(list.get(0).max);
		for (SerieModel serie : list) {
			if (min> serie.min) min= serie.min;
			if (max < serie.max) max=serie.max;
		}
		resul[0]=(int)min;
		resul[1]=(int)max;
		return resul;

	}

}