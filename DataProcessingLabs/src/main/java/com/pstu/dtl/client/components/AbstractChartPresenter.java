package com.pstu.dtl.client.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.moxieapps.gwt.highcharts.client.Chart;
import org.moxieapps.gwt.highcharts.client.Series;
import org.moxieapps.gwt.highcharts.client.ToolTipData;
import org.moxieapps.gwt.highcharts.client.ToolTipFormatter;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.pstu.dtl.shared.dto.SeriesDto;

public abstract class AbstractChartPresenter extends Composite {

    private FlowPanel panel = new FlowPanel();
    protected FlowPanel rightBlock = new FlowPanel();
    protected Map<Long, String> periods = new HashMap<Long, String>();
    protected Map<Long, Series> seriesMap = new HashMap<Long, Series>();
    protected Chart chart = new Chart();
    protected ToolTipFormatter toolTipFormatter = new ToolTipFormatter() {
        public String format(ToolTipData toolTipData) {
            return "<b>" + toolTipData.getSeriesName() + "</b><br/>" + toolTipData.getXAsString() + ": "
                    + toolTipData.getYAsDouble() + "%";
        }
    };

    public AbstractChartPresenter() {
        initWidget(panel);
        addStyleName("row chart-presenter");
        panel.add(chart);
        initDiagram();
        reset();
    }

    protected abstract void initDiagram();

    public void reset() {
        seriesMap.clear();
        chart.removeAllSeries();
    }

    public void addSeriesItem(List<SeriesDto> result) {
        addChartSeriesInMap(result);
    }

    public void addChartSeriesInMap(List<SeriesDto> result) {
        seriesMap.clear();
        for (SeriesDto dto : result) {
            Number[] points = new Number[periods.keySet().size()];
            int i = 0;
            for (Long key : periods.keySet()) {
                points[i++] = dto.getValues().get(key);
            }
            Series s = chart.createSeries().setName(dto.getName()).setPoints(points);
            seriesMap.put(dto.getId(), s);
        }
    }

    public void drawChart(Long id, Boolean draw) {
        if (draw) {
            chart.addSeries(seriesMap.get(id));
        }
        else
            chart.removeSeries(seriesMap.get(id));
    }

    public void drawChart(Long id) {
        drawChart(id, true);
    }

    public void removeAllSeries() {
        chart.removeAllSeries();
    }

    public void setPeriods(Map<Long, String> periods) {
        this.periods = periods;
        String[] stringArr = new String[periods.size()];
        int i = 0;
        for (String s : periods.values()) {
            stringArr[i++] = s;
        }
        chart.getXAxis().setCategories(stringArr);
    }
}
