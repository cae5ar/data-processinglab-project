package com.pstu.dtl.client.mvp.view;

import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.pstu.dtl.client.components.Btn;
import com.pstu.dtl.client.components.Btn.EButtonStyle;
import com.pstu.dtl.shared.dto.SeriesDto;

public class SeriesTable extends Composite {

    public enum ActionType {
        EDIT,
        REMOVE;
    }

    public interface IActionHandler {
        void action(ActionType type, Long seriesId);
    }

    public interface ICheckHandler {
        void action(Long seriesId, boolean isChecked, CheckBox sender);
    }

    public interface IDrawHandler {
        void action(Long seriesId, boolean isChecked, CheckBox sender, boolean isLineChart);
    }

    private FlexTable table = new FlexTable();
    private int index = 0;
    private int columnCount = 0;
    private Map<Long, String> periods = null;
    private IActionHandler actionHandler;
    private ICheckHandler checkHandler;
    protected IDrawHandler drawHandler;

    public SeriesTable(Map<Long, String> periods) {
        initWidget(table);
        table.addStyleName("table table-striped");
        this.periods = periods;
        createThead(periods);
    }

    /**
     * @param periods
     */
    private void createThead(Map<Long, String> periods) {
        Element thead = DOM.createTHead();
        Element tr = DOM.createTR();
        Element th;
        th = DOM.createTH();
        th.setInnerHTML("<span title='Корреляция'>К-Я<span>");
        th.addClassName("text-center");
        tr.appendChild(th);
        columnCount++;
        th = DOM.createTH();
        th.setInnerHTML("<span title='Линейная диаграмма'>ЛД<span>");
        th.addClassName("text-center");
        tr.appendChild(th);
        columnCount++;
        th = DOM.createTH();
        th.setInnerHTML("<span title='Столбчатая диаграмма'>СД<span>");
        th.addClassName("text-center");
        tr.appendChild(th);
        columnCount++;
        th = DOM.createTH();
        th.setInnerText("Имя ряда");
        tr.appendChild(th);
        columnCount++;
        for (String p : periods.values()) {
            addColumn(tr, p);
            columnCount++;
        }
        th = DOM.createTH();
        th.setInnerHTML("<span class='glyphicon glyphicon-pencil' style='margin: 6px 12px'></span>");
        tr.appendChild(th);
        columnCount++;
        th = DOM.createTH();
        th.setInnerHTML("<span class='glyphicon glyphicon-minus' style='margin: 6px 12px'></span>");
        tr.appendChild(th);
        columnCount++;
        thead.appendChild(tr);
        Element tbody = table.getElement().getElementsByTagName("tbody").getItem(0);
        tbody.removeFromParent();
        table.getElement().appendChild(thead);
        table.getElement().appendChild(tbody);

    }

    private void addColumn(Element tr, String columnName) {
        Element th = DOM.createTH();
        th.setInnerText(columnName);
        tr.appendChild(th);
    }

    public void additem(final SeriesDto series) {
        final CheckBox correlationCheckBox = new CheckBox();
        correlationCheckBox.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (checkHandler != null) {
                    checkHandler.action(series.getId(), correlationCheckBox.getValue(), correlationCheckBox);
                }
            }
        });
        table.setWidget(index, 0, correlationCheckBox);
        table.getCellFormatter().addStyleName(index, 0, "text-center");

        final CheckBox lineChartCheckBox = new CheckBox();
        lineChartCheckBox.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (drawHandler != null) {
                    drawHandler.action(series.getId(), lineChartCheckBox.getValue(), correlationCheckBox, true);
                }
            }
        });
        table.setWidget(index, 1, lineChartCheckBox);
        table.getCellFormatter().addStyleName(index, 1, "text-center");

        final CheckBox columnChartCheckBox = new CheckBox();
        columnChartCheckBox.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (drawHandler != null) {
                    drawHandler.action(series.getId(), columnChartCheckBox.getValue(), correlationCheckBox, false);
                }
            }
        });
        table.setWidget(index, 2, columnChartCheckBox);
        table.getCellFormatter().addStyleName(index, 2, "text-center");

        table.setText(index, 3, series.getName());
        table.getCellFormatter().addStyleName(index, 3, "series-cell name");
        // ///////////////////////////////
        insertValues(series.getValues());
        // ///////////////////////////////
        Btn editBtn = new Btn("<span class='glyphicon glyphicon-pencil'></span>", EButtonStyle.LINK);
        Btn removeBtn = new Btn("<span class='glyphicon glyphicon-minus'></span>", EButtonStyle.LINK);
        editBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (actionHandler != null) {
                    actionHandler.action(ActionType.EDIT, series.getId());
                }
            }
        });
        removeBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (actionHandler != null) {
                    actionHandler.action(ActionType.REMOVE, series.getId());
                }
            }
        });
        table.setWidget(index, columnCount - 2, editBtn);
        table.setWidget(index, columnCount - 1, removeBtn);
        index++;
    }

    private void insertValues(Map<Long, Double> values) {
        int columnIndex = 4;
        for (Long id : periods.keySet()) {
            table.setText(index, columnIndex, values.get(id) != null ? values.get(id).toString().replace('.', ',') : "null");
            table.getCellFormatter().addStyleName(index, columnIndex, "series-cell value");
            columnIndex++;
        }
    }

    public void addItems(List<SeriesDto> list) {
        for (SeriesDto s : list) {
            additem(s);
        }
    }

    public void removeRow(SeriesDto series) {

    }

    public void clear() {
        table.removeAllRows();
    }

    public void setActionHandler(IActionHandler handler) {
        this.actionHandler = handler;
    }

    public void setCheckHandler(ICheckHandler handler) {
        this.checkHandler = handler;
    }

    public void setDrawHandler(IDrawHandler drawHandler) {
        this.drawHandler = drawHandler;
    }
}