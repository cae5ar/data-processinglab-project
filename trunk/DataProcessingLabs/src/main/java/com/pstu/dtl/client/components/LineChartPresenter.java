package com.pstu.dtl.client.components;

import org.moxieapps.gwt.highcharts.client.ChartSubtitle;
import org.moxieapps.gwt.highcharts.client.ChartTitle;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.ToolTip;

public class LineChartPresenter extends AbstractChartPresenter {

    @Override
    protected void initDiagram() {
       
        chart.setType(Series.Type.LINE)  
        .setMarginRight(130)  
        .setMarginBottom(25)  
        .setChartTitle(new ChartTitle()  
            .setText("Линейная диаграмма")  
            .setX(-20)  // center  
        )  
        .setChartSubtitle(new ChartSubtitle()  
            .setText("Годовая динамика")  
            .setX(-20)  
        )  
        .setLegend(new Legend()  
            .setLayout(Legend.Layout.VERTICAL)  
            .setAlign(Legend.Align.RIGHT)  
            .setVerticalAlign(Legend.VerticalAlign.TOP)  
            .setX(-10)  
            .setY(100)  
            .setBorderWidth(0)  
        )  
        .setToolTip(new ToolTip()  
            .setFormatter(toolTipFormatter)  
        ); 
        chart.getYAxis().setMin(0);
    }

}
