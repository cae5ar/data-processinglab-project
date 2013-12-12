package com.pstu.dtl.client.components;

import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.ToolTip;
import org.moxieapps.gwt.highcharts.client.plotOptions.ColumnPlotOptions;

public class ColumnChartPresenter extends AbstractChartPresenter {

    @Override
    protected void initDiagram() {
        // --стоблцовая диаграмма//
        chart.setType(Series.Type.COLUMN).setChartTitleText("Столбчатая диаграмма").setChartSubtitleText("Годовая динамика").setColumnPlotOptions(
            new ColumnPlotOptions().setPointPadding(0.2).setBorderWidth(0)).setLegend(
                new Legend().setLayout(Legend.Layout.VERTICAL).setAlign(Legend.Align.LEFT).setVerticalAlign(Legend.VerticalAlign.TOP).setX(65).setY(30).setFloating(true).setBackgroundColor("#FFFFFF").setShadow(true)).setToolTip(
                    new ToolTip().setFormatter(toolTipFormatter));
        chart.getYAxis().setMin(0);
    }
}
