package com.pstu.dtl.client.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.ChartTitle;
import org.moxieapps.gwt.highcharts.client.Legend;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.XAxis;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.pstu.dtl.shared.dto.Cluster;
import com.pstu.dtl.shared.dto.SeriesDto;

public class ClusterPresenter extends Composite {

    FlowPanel panel = new FlowPanel();
    FlexTable table = new FlexTable();
    String[] colors = new String[] {"#3366FF", "#FFCC00", "#00FF00", "#FF3366", "#00F5FF", "#6600FF", "#33CC99",
            " #006400", "#FF8C00", "#BFEFFF", "#FF1493", " #8B8682", "#EED5D2", "#1C86EE"};
    //@formatter:off
    Chart allSeriesChart = new Chart()  
    .setPolar(true) 
    .setHeight(550)
    .setWidth(560)
    .setType(Series.Type.LINE)  
    .setChartTitle(new ChartTitle()  
        .setText("Нормализованые значения рядов")  
        .setX(-80)  
    )  
    .setLegend(new Legend()  
    .setAlign(Legend.Align.CENTER)  
    .setVerticalAlign(Legend.VerticalAlign.BOTTOM)  
    .setLayout(Legend.Layout.HORIZONTAL)
    );

    
    Chart clusterChart = new Chart()  
    .setPolar(true)  
    .setType(Series.Type.AREA) 
    .setHeight(550)
     .setWidth(560)
    .setZoomType(Chart.ZoomType.X) 
    .setChartTitle(new ChartTitle()  
        .setText("Центры кластеров")  
        .setX(-80)  
    )  
    .setLegend(new Legend()  
        .setAlign(Legend.Align.CENTER)  
        .setVerticalAlign(Legend.VerticalAlign.BOTTOM)  
        .setLayout(Legend.Layout.HORIZONTAL)  
    );
    
    private List<Long> periodList;
    private Element theadtr;
      
    public ClusterPresenter() {
        initWidget(panel);
        addStyleName("row cluster-presenter hide");
        
        allSeriesChart.getXAxis()
        .setTickmarkPlacement(XAxis.TickmarkPlacement.ON)  
        .setLineWidth(0)  
        .setCategories(  
            "Sales", "Marketing", "Development", "Customer Support",  
            "Information Technology", "Administration"  
        );  

        allSeriesChart.getYAxis()  
        .setOption("gridLineInterpolation", "polygon")  
        .setLineWidth(0)  
        .setMax(1)
        .setMin(0);  

        clusterChart.getXAxis()
        .setTickmarkPlacement(XAxis.TickmarkPlacement.ON)  
        .setLineWidth(0);  

        clusterChart.getYAxis()  
        .setOption("gridLineInterpolation", "polygon")  
        .setLineWidth(0)  
        .setMax(1)
        .setMin(0);  

        //@formatter:on
        Element createElement = DOM.createElement("h3");
        createElement.setInnerHTML("Результаты кластеризации");
        panel.getElement().appendChild(createElement);
        panel.add(table);
        table.addStyleName("table");
        Grid grid = new Grid(1, 2);
        grid.setWidget(0, 0, allSeriesChart);
        grid.setWidget(0, 1, clusterChart);
        panel.add(grid);

        Element thead = DOM.createTHead();
        theadtr = DOM.createTR();
        thead.appendChild(theadtr);
        Element tbody = table.getElement().getElementsByTagName("tbody").getItem(0);
        tbody.removeFromParent();
        table.getElement().appendChild(thead);
        table.getElement().appendChild(tbody);
    }

    public Series addSeries(Double[] points, String str, Chart chart, String color, boolean isSelected) {
        Series series = chart.createSeries().setName(str).setPoints(points).setOption("pointPlacement", "on");
        series.setOption("color", color);
        chart.addSeries(series);
        if (!isSelected) series.selectToggle();
        return series;
    }

    public void setCategories(String[] categories) {
        allSeriesChart.getXAxis().setCategories(categories);
        clusterChart.getXAxis().setCategories(categories);
    }

    public void setCategories(Map<Long, String> periods, List<Long> periodList) {
        this.periodList = periodList;
        String[] categories = new String[periodList.size()];
        int i = 0;
        for (Long p : periodList) {
            categories[i++] = periods.get(p);
        }
        setCategories(categories);
    }

    public void showClusters(List<Cluster> result) {
        reset();
        int i = 1;
        int index = 0;
        List<SeriesDto> list = new ArrayList<SeriesDto>();
        for (Cluster c : result) {
            String color = colors[index % 14];
            for (SeriesDto dto : c.getSeries()) {
                addSeries(getArrayPoints(dto.getValues()), dto.getName(), allSeriesChart, color, true);
                list.add(dto);
            }
            if (c.getSeries().size() > 0)
                addSeries(c.getCentroid(), "Кластер:" + i, clusterChart, color, true);
            else
                addSeries(c.getCentroid(), "Кластер:" + i, clusterChart, color, false);
            Element th = DOM.createTH();
            th.setClassName("cluster-name-cell");
            th.setInnerHTML("Кластер " + i);
            theadtr.appendChild(th);
            index++;
            i++;
        }
        Collections.sort(list, new Comparator<SeriesDto>() {
            public int compare(SeriesDto o1, SeriesDto o2) {
                return o1.getId().intValue() - o2.getId().intValue();
            }
        });
        addSeriesInTable(list);
        removeStyleName("hide");
    }

    private void reset() {
        allSeriesChart.removeAllSeries();
        clusterChart.removeAllSeries();
        theadtr.setInnerHTML("");
        table.removeAllRows();
        Element th = DOM.createTH();
        th.setClassName("cluster-name-cell");
        th.setInnerHTML("Расстояние до центра");
        theadtr.appendChild(th);
    }

    private void addSeriesInTable(List<SeriesDto> list) {
        for (SeriesDto dto : list) {
            table.setHTML(table.getRowCount(), 0, dto.getName());
            int j = 1;
            for (Double d : dto.getToClasterDistance()) {
                table.setHTML(table.getRowCount() - 1, j++, NumberFormat.getFormat("0.0000000").format(d));
            }
        }
    }

    private Double[] getArrayPoints(Map<Long, Double> values) {
        Double[] out = new Double[periodList.size()];
        int i = 0;
        for (Long p : periodList) {
            out[i++] = values.get(p);
        }
        return out;
    }
}
