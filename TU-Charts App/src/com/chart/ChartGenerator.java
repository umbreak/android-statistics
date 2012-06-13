package com.chart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
	private ChartModel chartEntry;



	public GraphicalView getView(Context context, ChartModel chartEntry, int month) throws ParseException {
		this.chartEntry=chartEntry;

		List<Integer> matches=getMatches(month);
		if (matches.size()==0)
			return null;
		List<SerieModel> y_results=modifYval(matches);
		String[] x_results=modifXval(matches);
		
		Resources r=context.getResources();
		int[] all_colors= new int[]{r.getColor(R.color.chart_blue),r.getColor(R.color.chart_red),
				r.getColor(R.color.chart_green),r.getColor(R.color.chart_brown),r.getColor(R.color.chart_pink),r.getColor(R.color.chart_violet)};

		int[] colors = new int[y_results.size()];
		for (int i = 0; i < colors.length; i++) 
			colors[i]=all_colors[i];

		XYMultipleSeriesRenderer renderer = buildRenderer(colors, PointStyle.CIRCLE);
		renderPointAtributes(renderer);

		//Checking for the type of data on the X axis. It can be a double or a Date
		if (isNum(chartEntry.xValues[0])){
			double xVal[]=new double[x_results.length];
			for (int i = 0; i < xVal.length; i++)
				xVal[i]=Double.parseDouble(x_results[i]);
			setChartSettings(renderer, chartEntry.name,chartEntry.xLegend, chartEntry.yLegend, xVal[0], xVal[xVal.length-1],
					chartEntry.min, chartEntry.max, r.getColor(R.color.chart_darkgrey), Color.BLACK, context);
			return ChartFactory.getLineChartView(context,buildDataset(xVal, y_results), renderer);

		}else{
			Date xVal[]=new Date[x_results.length];
			for (int i = 0; i < xVal.length; i++)
				xVal[i]=AppUtils.i.date_format.parse(x_results[i]);
			setChartSettings(renderer, chartEntry.name,chartEntry.xLegend, chartEntry.yLegend, xVal[0].getTime(), xVal[xVal.length-1].getTime(),
					chartEntry.min, chartEntry.max, r.getColor(R.color.chart_darkgrey), Color.BLACK, context);
			return ChartFactory.getTimeChartView(context,buildDateDataset(xVal, y_results), renderer, AppUtils.i.date_format.toPattern());

		}			
	}

	public GraphicalView getView(Context context, ChartModel chartEntry) throws ParseException {
		this.chartEntry=chartEntry;
		List<SerieModel> results=chartEntry.yValues;
		Resources r=context.getResources();
		int[] all_colors= new int[]{r.getColor(R.color.chart_blue),r.getColor(R.color.chart_red),
				r.getColor(R.color.chart_green),r.getColor(R.color.chart_brown),r.getColor(R.color.chart_pink),r.getColor(R.color.chart_violet)};
		int[] colors = new int[results.size()];
		for (int i = 0; i < colors.length; i++) 
			colors[i]=all_colors[i];

		XYMultipleSeriesRenderer renderer = buildRenderer(colors, PointStyle.CIRCLE);
		renderPointAtributes(renderer);

		//Checking for the type of data on the X axis. It can be a double or a Date
		if (isNum(chartEntry.xValues[0])){
			double xVal[]=new double[chartEntry.xValues.length];
			for (int i = 0; i < xVal.length; i++)
				xVal[i]=Double.parseDouble(chartEntry.xValues[i]);
			setChartSettings(renderer, chartEntry.name,chartEntry.xLegend, chartEntry.yLegend, xVal[0], xVal[xVal.length-1],
					chartEntry.min, chartEntry.max, r.getColor(R.color.chart_darkgrey), Color.BLACK, context);
			return ChartFactory.getLineChartView(context,buildDataset(xVal, results), renderer);

		}else{
			Date xVal[]=new Date[chartEntry.xValues.length];
			for (int i = 0; i < xVal.length; i++)
				xVal[i]=AppUtils.i.date_format.parse(chartEntry.xValues[i]);
			setChartSettings(renderer, chartEntry.name,chartEntry.xLegend, chartEntry.yLegend, xVal[0].getTime(), xVal[xVal.length-1].getTime(),
					chartEntry.min, chartEntry.max, r.getColor(R.color.chart_darkgrey), Color.BLACK, context);
			return ChartFactory.getTimeChartView(context, buildDateDataset(xVal, results), renderer, "dd.MM.yy");
		}			
	}

	private void renderPointAtributes(XYMultipleSeriesRenderer renderer){
		renderer.setAntialiasing(true);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
			seriesRenderer.setFillPoints(true);
			seriesRenderer.setLineWidth(2f);
			seriesRenderer.setDisplayChartValues(true);
			seriesRenderer.setChartValuesTextSize(9f);
		}
	}

	private boolean isNum(String s) {
		try {
			Double.parseDouble(s);
		}catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	private List<Integer> getMatches(int month) throws ParseException{
		String[] xValues=chartEntry.xValues;
		List<Integer> result=new ArrayList<Integer>();
		SimpleDateFormat month_format=new SimpleDateFormat("MM");
		for (int i = 0; i < xValues.length; i++){
			if (month_format.format(AppUtils.i.date_format.parse(xValues[i])).equals("0" +month))
				result.add(i);	
		}
		System.out.println("Result=" + result);
		return result;
	}
	private List<SerieModel> modifYval(List<Integer> xValues){
		List<SerieModel> y_results_tmp=chartEntry.yValues;
		List<SerieModel> y_results=new ArrayList<SerieModel>();

		//Modif Y Values
		for (SerieModel serieModel : y_results_tmp) {
			double[] data=new double[xValues.size()];
			for (int i=0; i<xValues.size();i++)
				data[i]=serieModel.yvalues[xValues.get(i)];
			y_results.add(new SerieModel(serieModel.id, serieModel.name, data));
		}
		for (SerieModel lineModel : y_results) 
			System.out.println("Yvalues after=" + lineModel);
		return y_results;
	}
	private String[] modifXval(List<Integer> xValues){
		String[] x_results=new String[xValues.size()];

		//Modif X Values
		for (int i=0; i< xValues.size(); i++)
			x_results[i]=chartEntry.xValues[xValues.get(i)];

		System.out.println("X values="+ Arrays.toString(x_results));
		return x_results;
	}
}