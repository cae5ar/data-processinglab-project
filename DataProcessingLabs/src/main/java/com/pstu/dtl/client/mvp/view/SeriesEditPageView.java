package com.pstu.dtl.client.mvp.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.pstu.dtl.client.SimpleAsyncCallback;
import com.pstu.dtl.client.Site;
import com.pstu.dtl.client.components.Btn;
import com.pstu.dtl.client.components.Btn.EButtonStyle;
import com.pstu.dtl.client.components.ColumnChartPresenter;
import com.pstu.dtl.client.components.CustomDialogBox;
import com.pstu.dtl.client.components.CustomDialogBox.EAlertType;
import com.pstu.dtl.client.components.LineChartPresenter;
import com.pstu.dtl.client.components.PeriodEditPopup;
import com.pstu.dtl.client.components.PeriodEditPopup.PeriodSaveHandler;
import com.pstu.dtl.client.components.SeriesEditPopup;
import com.pstu.dtl.client.components.SeriesEditPopup.ISaveClickHandler;
import com.pstu.dtl.client.mvp.iview.ISeriesEditPageView;
import com.pstu.dtl.client.mvp.view.SeriesTable.ActionType;
import com.pstu.dtl.client.mvp.view.SeriesTable.IActionHandler;
import com.pstu.dtl.client.mvp.view.SeriesTable.ICheckHandler;
import com.pstu.dtl.client.mvp.view.SeriesTable.IDrawHandler;
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
    private ICheckHandler checkHandler;
    private List<Long> correlationSelectedItems = new ArrayList<Long>();
    private Label correlationLabel = new Label();
    private IDrawHandler drawHandler;

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
        };
        checkHandler = new ICheckHandler() {
            public void action(Long seriesId, boolean isChecked, CheckBox sender) {
                if (isChecked) {
                    if (correlationSelectedItems.size() > 1) {
                        sender.setValue(false);
                        CustomDialogBox.showDialogBox("Внимание", "Для подсчета корреляции выберите 2 ряда ", EAlertType.WARNING);
                    }
                    else {
                        correlationSelectedItems.add(seriesId);
                    }
                }
                else {
                    for (int i = 0; i < correlationSelectedItems.size(); i++) {
                        if (correlationSelectedItems.get(i).equals(seriesId)) {
                            correlationSelectedItems.remove(i);
                            break;
                        }
                    }
                }
                if (correlationSelectedItems.size() == 2) {
                    refreshCorellation(getSeriesById(correlationSelectedItems.get(0)), getSeriesById(correlationSelectedItems.get(1)));
                }
                else
                    correlationLabel.addStyleName("hide");
            }
        };

        drawHandler = new IDrawHandler() {
            public void action(Long seriesId, boolean isChecked, CheckBox sender, boolean isLineChart) {
                if (isLineChart) {
                    lcp.drawChart(seriesId, isChecked);
                }
                else {
                    ccp.drawChart(seriesId, isChecked);
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
        panel.add(new SimplePanel(new Btn("Метод наименьших квартатов", EButtonStyle.INFO, new ClickHandler() {
            public void onClick(ClickEvent event) {
                Site.service.calculateSquareRegression(new SimpleAsyncCallback<List<Double>>() {
                    @Override
                    public void onSuccess(List<Double> result) {
                        CustomDialogBox.showDialogBox("ок", "всё гуд", EAlertType.SUCCES);
                    }
                });
            }
        })));
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
                        CustomDialogBox.showDialogBox("OK!", "Ряд успешно сохранен", EAlertType.SUCCES);
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
                        CustomDialogBox.showDialogBox("OK!", "Все изменения внесены", EAlertType.SUCCES);
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
                correlationSelectedItems.clear();
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
                CustomDialogBox.showDialogBox("OK!", "Ряд успешно удален", EAlertType.SUCCES);
            }
        });
    }

    public void setPeriods(Map<Long, String> periods) {
        this.periods = periods;
        tp.clear();
        table = new SeriesTable(periods);
        table.setActionHandler(actionHandler);
        table.setCheckHandler(checkHandler);
        table.setDrawHandler(drawHandler);
        tp.setWidget(table);
        table.addStyleName("col-xs-12");
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
