package com.chart;

import java.util.Collections;
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
import com.chart.pojos.ChartEntry;
import com.chart.pojos.Point;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class ChartGenerator {

	private static final String DATE_FORMAT = "dd/MM/yyyy";

	public GraphicalView getView(Context context, List<Point> results, ChartEntry chartEntry) {

		int[] colors = new int[] { Color.BLUE };
		PointStyle[] styles = new PointStyle[] { PointStyle.POINT};
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);

		Point max = Collections.max(results);
		Point min = Collections.min(results);
		System.out.println("max=" + max + " min="+min);
		System.out.println(results);

		setChartSettings(renderer, chartEntry.name,"Dates", "Values", results.get(0)
				.date.getTime(), results.get(results.size() - 1)
						.date.getTime(), min.value, max.value, Color.DKGRAY, Color.BLACK, context);
		renderer.setXLabels(5);
		renderer.setYLabels(10);
		SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(0);
		seriesRenderer.setDisplayChartValues(true);
		return ChartFactory.getTimeChartView(context,
				buildDateDataset(chartEntry.name, results), renderer, DATE_FORMAT);
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor, Context context) {
//		renderer.setChartTitle(title);
		renderer.setChartTitle("");
		renderer.setAntialiasing(true);

		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
		renderer.setYLabelsColor(0, Color.DKGRAY);
//		renderer.setXLabelsColor(Color.GRAY);

	    renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setApplyBackgroundColor(true);
		int holo_bg= context.getResources().getColor(R.color.abs__background_holo_light);
		renderer.setBackgroundColor(holo_bg);
		renderer.setMarginsColor(holo_bg);
	}

	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRendererProperties(renderer, colors, styles);
		return renderer;
	}

	protected XYMultipleSeriesDataset buildDateDataset(String title,
			List<Point> results) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries(title);
		for (Point result : results) {
			series.add(result.date, result.value);
		}
		dataset.addSeries(series);
		return dataset;
	}

	protected void setRendererProperties(XYMultipleSeriesRenderer renderer, int[] colors,
			PointStyle[] styles) {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 15, 30, 15, 15 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
	}
}

