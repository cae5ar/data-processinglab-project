package com.pstu.dtl.client.mvp.view;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.pstu.dtl.client.SimpleAsyncCallback;
import com.pstu.dtl.client.Site;
import com.pstu.dtl.client.components.AlertDialogBox;
import com.pstu.dtl.client.components.AlertDialogBox.EAlertType;
import com.pstu.dtl.client.components.Btn;
import com.pstu.dtl.client.components.Btn.EButtonStyle;
import com.pstu.dtl.client.components.Calculator;
import com.pstu.dtl.client.components.ClusterPresenter;
import com.pstu.dtl.client.components.ColumnChartPresenter;
import com.pstu.dtl.client.components.LineChartPresenter;
import com.pstu.dtl.client.components.PeriodEditPopup;
import com.pstu.dtl.client.components.PeriodEditPopup.PeriodSaveHandler;
import com.pstu.dtl.client.components.SeriesEditPopup;
import com.pstu.dtl.client.components.SeriesEditPopup.ISaveClickHandler;
import com.pstu.dtl.client.components.SeriesTable;
import com.pstu.dtl.client.components.SeriesTable.ActionType;
import com.pstu.dtl.client.components.SeriesTable.IActionHandler;
import com.pstu.dtl.client.mvp.iview.ISeriesEditPageView;
import com.pstu.dtl.shared.dto.Cluster;
import com.pstu.dtl.shared.dto.PeriodDto;
import com.pstu.dtl.shared.dto.SeriesDto;

public class SeriesEditPageView implements ISeriesEditPageView {

    FlowPanel panel = new FlowPanel();
    private Map<Long, String> periods;
    private SimplePanel tp;
    private SeriesTable table;
    private List<SeriesDto> result;
    private IActionHandler actionHandler;
    private ColumnChartPresenter ccp;
    private LineChartPresenter lcp;
    private Label correlationLabel = new Label();
    private ClusterPresenter clusterPresenter;

    public Widget asWidget() {
        return panel;
    }

    public SeriesEditPageView() {
        Btn addSeriesButton = new Btn("<span style='padding-right:10px' class='glyphicon glyphicon-plus'></span><strong>Добавить ряд данных</strong>", EButtonStyle.PRIMARY, new ClickHandler() {
            public void onClick(ClickEvent event) {
                addSeries(null);
            }
        });
        Btn addPeriodButton = new Btn("<span style='padding-right:10px' class='glyphicon glyphicon-plus'></span><strong>Добавить период</strong>", EButtonStyle.INFO, new ClickHandler() {
            public void onClick(ClickEvent event) {
                addPeriod();
            }
        });
        addSeriesButton.addStyleName("float-right");
        actionHandler = new IActionHandler() {
            public void action(ActionType type, Long seriesId) {
                switch (type) {
                    case EDIT:
                        addSeries(getSeriesById(seriesId));
                        break;
                    case REMOVE:
                        removeSeries(seriesId);
                    default:
                        break;
                }

            }

            @Override
            public void action(ActionType type, List<Long> seriesList, List<Long> periodList, String clusterCount) {
                switch (type) {
                    case LINEAR_DIAGRAM:
                        lcp.removeAllSeries();
                        for (Long id : seriesList) {
                            lcp.drawChart(id);
                        }
                        break;
                    case COLUMN_DIAGRAM:
                        ccp.removeAllSeries();
                        for (Long id : seriesList) {
                            ccp.drawChart(id);
                        }
                        break;
                    case CORRELATION:
                        if (seriesList.size() == 2) {
                            refreshCorellation(getSeriesById(seriesList.get(0)), getSeriesById(seriesList.get(1)));
                        }
                        else {
                            AlertDialogBox.showDialogBox("Внимание", "Для подсчета корреляции выберите 2 ряда ", EAlertType.WARNING);
                            correlationLabel.addStyleName("hide");
                        }
                        break;
                    case REGRESSION:
                        //@formatter:off
//                        if (seriesList.size() > 2) {
//                            Site.service.calculateSquareRegression(seriesList, new SimpleAsyncCallback<Map<String, List<Double>>>() {
//                                @Override
//                                public void onSuccess(Map<String, List<Double>> result) {
//                                    final DialogBox cdb = new DialogBox();
//                                    cdb.setGlassEnabled(true);
//                                    cdb.setAnimationEnabled(true);
//                                    cdb.setText("Результат регрессионного анализа методом наименьших квадратов");
//                                    FlowPanel simpleCloneTable = table.getSimpleCloneTable(result);
//                                    Btn closeBtn = new Btn("Закрыть", EButtonStyle.DEFAULT, new ClickHandler() {
//                                        @Override
//                                        public void onClick(ClickEvent event) {
//                                            cdb.hide();
//                                        }
//                                    });
//                                    simpleCloneTable.add(closeBtn);
//                                    cdb.setWidget(simpleCloneTable);
//                                    cdb.center();
//                                    cdb.show();
//                                }
//                            });
//                        }else{
//                            AlertDialogBox.showDialogBox("Внимание", "Для подсчета корреляции выберите миниму 3 ряда ", EAlertType.WARNING);
//                        }
                      //@formatter:on
                        AlertDialogBox.showDialogBox("Эта функция на данный момент не доступна", "", EAlertType.WARNING);
                        break;
                    case CLUSTERING:
                        Integer parseInt = null;
                        try {
                            parseInt = Integer.parseInt(clusterCount);
                        }
                        catch (NumberFormatException e) {
                            AlertDialogBox.showDialogBox("Внимание", "Укажите количество кластеррв", EAlertType.WARNING);
                            return;
                        }
                        if (periodList.size() > 0 && seriesList.size() > 0 && parseInt > 0) {
                            clusterPresenter.setCategories(periods,periodList);
                            Site.service.doClustering(seriesList, periodList, parseInt, new SimpleAsyncCallback<List<Cluster>>() {
                                @Override
                                public void onSuccess(List<Cluster> result) {
                                    clusterPresenter.showClusters(result);
                                }
                            });
                        }
                        else {
                            AlertDialogBox.showDialogBox("Внимание", "Для выполнения кластеризации необходимо вырать как минимум 1 ряд, 1 столбец и количество кластеров должно быть хотя 1", EAlertType.WARNING);
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        FlowPanel bp = new FlowPanel();
        bp.add(addPeriodButton);
        bp.add(addSeriesButton);
        bp.addStyleName("row");
        panel.add(bp);
        tp = new SimplePanel();
        tp.addStyleName("row table-container");
        panel.add(tp);
        correlationLabel.setStyleName("row correlation-label hide");
        panel.add(correlationLabel);
        clusterPresenter = new ClusterPresenter();
        panel.add(clusterPresenter);
        ccp = new ColumnChartPresenter();
        panel.add(ccp);
        lcp = new LineChartPresenter();
        panel.add(lcp);
    }

    private void refreshCorellation(SeriesDto seriesDto, SeriesDto seriesDto2) {
        Collection<Double> s1 = seriesDto.getValues().values();
        Collection<Double> s2 = seriesDto2.getValues().values();
        int length = s1.size();
        if (length != s2.size()) {
            Site.handleError(new Exception("Количество значений рядов отличаются"));
            return;
        }
        if (length == 0) {
            Site.handleError(new Exception("Нет значений рядов "));
            return;
        }
        Double result = Calculator.caclucalteCorrelation(s1, s2, length);
        correlationLabel.getElement().setInnerHTML("Корреляция для рядов <strong>" + seriesDto.getName()
                + "</strong> и <strong>" + seriesDto2.getName() + "</strong>: " + "<em>"
                + NumberFormat.getFormat("0.000000000").format(result) + "</em>");
        correlationLabel.removeStyleName("hide");
    }

    private void addSeries(SeriesDto dto) {
        SeriesEditPopup popup = new SeriesEditPopup(dto, periods, new ISaveClickHandler() {
            public void click(final SeriesEditPopup sender, SeriesDto bean) {
                Site.service.saveSeries(bean, new SimpleAsyncCallback<Long>() {
                    public void onSuccess(Long result) {
                        sender.hide();
                        AlertDialogBox.showDialogBox("OK!", "Ряд успешно сохранен", EAlertType.SUCCESS);
                        reset();
                    }
                });
            }
        });
        popup.show();
    }

    protected void addPeriod() {
        PeriodEditPopup periodEditPopup = new PeriodEditPopup(new PeriodSaveHandler() {
            public void save(List<PeriodDto> list, final PeriodEditPopup sender) {
                Site.service.savePeriods(list, new SimpleAsyncCallback<List<PeriodDto>>() {
                    public void onSuccess(List<PeriodDto> result) {
                        AlertDialogBox.showDialogBox("OK!", "Все изменения внесены", EAlertType.SUCCESS);
                        sender.hide();
                        setPeriods(result);
                        reset();
                    }
                });
            }
        });
        periodEditPopup.show();
    }

    protected SeriesDto getSeriesById(Long seriesId) {
        try {
            for (SeriesDto dto : result)
                if (dto.getId().equals(seriesId)) return dto;
            throw new Exception("Что пошло не так, пожалуйста обновите страницу");
        }
        catch (Exception e) {
            Site.handleError(e);
            return null;
        }
    }

    public void buildWidget() {
        reset();
    }

    private void reset() {
        Site.setWaitingBlockVisible(true);
        Site.service.getAllSeries(new SimpleAsyncCallback<List<SeriesDto>>() {
            public void onSuccess(List<SeriesDto> result) {
                table.clear();
                ccp.reset();
                lcp.reset();
                correlationLabel.addStyleName("hide");
                SeriesEditPageView.this.result = result;
                Site.setWaitingBlockVisible(false);
                table.addItems(result);
                ccp.addSeriesItem(result);
                lcp.addSeriesItem(result);
            }
        });
    }

    protected void removeSeries(Long seriesId) {
        Site.service.deleteSeries(seriesId, new SimpleAsyncCallback<Void>() {
            public void onSuccess(Void result) {
                reset();
                AlertDialogBox.showDialogBox("OK!", "Ряд успешно удален", EAlertType.SUCCESS);
            }
        });
    }

    public void setPeriods(Map<Long, String> periods) {
        this.periods = periods;
        tp.clear();
        table = new SeriesTable(periods);
        table.setActionHandler(actionHandler);
        tp.setWidget(table);
        ccp.setPeriods(periods);
        lcp.setPeriods(periods);
    }

    public void setPeriods(List<PeriodDto> list) {
        try {
            setPeriods(listPeriodsToMap(list));
        }
        catch (Exception e) {
            Site.handleError(e);
        }
    }

    private Map<Long, String> listPeriodsToMap(List<PeriodDto> list) {
        Map<Long, String> out = new LinkedHashMap<Long, String>();
        for (PeriodDto p : list) {
            out.put(p.getId(), p.getName());
        }
        return out;
    }
}
