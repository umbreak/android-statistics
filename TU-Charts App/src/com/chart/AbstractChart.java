/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chart;

import static com.chart.AppUtils.NULL_VAL;

import java.util.Date;
import java.util.List;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Paint.Align;

import com.actionbarsherlock.R;
import com.chart.pojos.SerieModel;

/**
 * An abstract class for the demo charts to extend. It contains some methods for
 * building datasets and renderers.
 */
public abstract class AbstractChart {

	/**
	 * Builds an XY multiple dataset using the provided values.
	 * 
	 * @param titles the series titles
	 * @param xValues the values for the X axis
	 * @param yValues the values for the Y axis
	 * @return the XY multiple dataset
	 */
	protected XYMultipleSeriesDataset buildDataset(double[] xValues,
			List<SerieModel> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		addXYSeries(dataset,xValues, yValues, 0);
		return dataset;
	}

	public void addXYSeries(XYMultipleSeriesDataset dataset, double[] xValues,
			List<SerieModel> yValues, int scale) {
		int length = yValues.size();
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(yValues.get(i).name, scale);
			double[] yV = yValues.get(i).yvalues;
			int seriesLength = xValues.length;
			for (int k = 0; k < seriesLength; k++) {
				if(yV[k] != NULL_VAL)
					series.add(xValues[k], yV[k]);
			}
			dataset.addSeries(series);
		}
	}

	/**
	 * Builds an XY multiple series renderer.
	 * 
	 * @param colors the series rendering colors
	 * @param styles the series point styles
	 * @return the XY multiple series renderers
	 */
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer);
		setRenderer(renderer, colors, styles);
		return renderer;
	}
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer);
		setRenderer(renderer, colors);
		return renderer;
	}
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle style) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer);
		setRenderer(renderer, colors, style);
		return renderer;
	}
	protected void setRenderer(XYMultipleSeriesRenderer renderer){
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(22);
		renderer.setLabelsTextSize(13);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(1.5f);
		//margin size values, in this order: top, left, bottom, right
		renderer.setMargins(new int[] { 8, 30, 38, 0 });

	}

	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
	}
	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors) {
		//		PointStyle point = PointStyle.CIRCLE;
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			//			r.setPointStyle(point);
			renderer.addSeriesRenderer(r);
		}
	}
	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle style) {
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(style);
			renderer.addSeriesRenderer(r);
		}
	}

	/**
	 * Sets a few of the series renderer settings.
	 * 
	 * @param renderer the renderer to set the properties to
	 * @param title the chart title
	 * @param xTitle the title for the X axis
	 * @param yTitle the title for the Y axis
	 * @param xMin the minimum value on the X axis
	 * @param xMax the maximum value on the X axis
	 * @param yMin the minimum value on the Y axis
	 * @param yMax the maximum value on the Y axis
	 * @param axesColor the axes color
	 * @param labelsColor the labels color
	 */
	protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
			String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
			int labelsColor, Context context) {
		renderer.setChartTitle(title);
		if (xTitle != null)
			renderer.setXTitle(xTitle);
		if (yTitle != null)
			renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
		renderer.setXLabelsColor(labelsColor);
		renderer.setYLabelsColor(0,labelsColor);
		renderer.setXLabels(25);
		renderer.setYLabels(25);

		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setXLabelsAngle(-90);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setApplyBackgroundColor(true);
		int holo_bg= context.getResources().getColor(R.color.light_background);
		renderer.setBackgroundColor(holo_bg);
		renderer.setMarginsColor(holo_bg);
	}

	/**
	 * Builds an XY multiple time dataset using the provided values.
	 * 
	 * @param titles the series titles
	 * @param xValues the values for the X axis
	 * @param yValues the values for the Y axis
	 * @return the XY multiple time dataset
	 */
	protected XYMultipleSeriesDataset buildDateDataset(Date[] xValues,
			List<SerieModel> yValues) {

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = yValues.size();
		for (int i = 0; i < length; i++) {
			TimeSeries series = new TimeSeries(yValues.get(i).name);
			double[] yV = yValues.get(i).yvalues;
			int seriesLength = xValues.length;
			for (int k = 0; k < seriesLength; k++) {
				if (yV[k] != NULL_VAL)
					series.add(xValues[k], yV[k]);
				
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

	/**
	 * Builds a category series using the provided values.
	 * 
	 * @param titles the series titles
	 * @param values the values
	 * @return the category series
	 */
	protected CategorySeries buildCategoryDataset(String title, double[] values) {
		CategorySeries series = new CategorySeries(title);
		int k = 0;
		for (double value : values) {
			series.add("Project " + ++k, value);
		}

		return series;
	}

	/**
	 * Builds a multiple category series using the provided values.
	 * 
	 * @param titles the series titles
	 * @param values the values
	 * @return the category series
	 */
	protected MultipleCategorySeries buildMultipleCategoryDataset(String title,
			List<String[]> titles, List<double[]> values) {
		MultipleCategorySeries series = new MultipleCategorySeries(title);
		int k = 0;
		for (double[] value : values) {
			series.add(2007 + k + "", titles.get(k), value);
			k++;
		}
		return series;
	}

	/**
	 * Builds a category renderer to use the provided colors.
	 * 
	 * @param colors the colors
	 * @return the category renderer
	 */


	/**
	 * Builds a bar multiple series dataset using the provided values.
	 * 
	 * @param titles the series titles
	 * @param values the values
	 * @return the XY multiple bar dataset
	 */
	protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			double[] v = values.get(i);
			int seriesLength = v.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
	}



}
