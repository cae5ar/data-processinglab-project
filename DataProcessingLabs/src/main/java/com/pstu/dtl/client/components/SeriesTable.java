package com.pstu.dtl.client.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;
import com.pstu.dtl.client.components.Btn.EButtonStyle;
import com.pstu.dtl.shared.dto.SeriesDto;

public class SeriesTable extends Composite {

    public enum ActionType {
        LINEAR_DIAGRAM,
        COLUMN_DIAGRAM,
        CORRELATION,
        REGRESSION,
        CLUSTERING,
        EDIT,
        REMOVE;
    }

    public interface IActionHandler {
        void action(ActionType type, Long seriesId);

        void action(ActionType type, List<Long> seriesList);
    }

    private FlowPanel panel = new FlowPanel();
    private FlexTable table = new FlexTable();
    private CustomListBox actionListBox = new CustomListBox(null);
    private Btn actionBtn = new Btn("Выполнить", EButtonStyle.INFO);
    private int index = 0;
    private int columnCount = 0;
    private Map<Long, String> periods = null;
    private List<Long> selectedSeries = new ArrayList<Long>();
    private IActionHandler actionHandler;

    public SeriesTable(Map<Long, String> periods) {
        initWidget(panel);
        panel.add(table);
        table.addStyleName("table table-striped");
        this.periods = periods;
        createThead(periods);
        actionListBox.addStyleName("action-list-box");
        actionListBox.addValue("Построить линейные диаграммы", ActionType.LINEAR_DIAGRAM.name());
        actionListBox.addValue("Построить столбчатые диаграммы", ActionType.COLUMN_DIAGRAM.name());
        actionListBox.addValue("Посчитать регрессию МНК", ActionType.REGRESSION.name());
        actionListBox.addValue("Посчитать корреляцию", ActionType.CORRELATION.name());
        actionListBox.addValue("Выполнить кластеризацию", ActionType.CLUSTERING.name());
        Grid actionPanel = new Grid(1, 2);
        actionBtn.getElement().getStyle().setMarginBottom(8, Unit.PX);
        actionPanel.addStyleName("action-panel");
        panel.add(actionPanel);
        actionPanel.setWidget(0, 0, actionListBox);
        actionPanel.setWidget(0, 1, actionBtn);
        actionBtn.setEnabled(false);
        actionBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ListBox nativeListBox = actionListBox.getNativeListBox();
                String value = nativeListBox.getValue(nativeListBox.getSelectedIndex());
                if (value != null && !value.isEmpty()) {
                    ActionType type = ActionType.valueOf(value);
                    actionHandler.action(type, selectedSeries);
                }
            }
        });
        actionListBox.getNativeListBox().addChangeHandler(new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                checkStatus();
            }
        });
    }

    /**
     * @param periods
     */
    private void createThead(Map<Long, String> periods) {
        Element thead = DOM.createTHead();
        Element tr = DOM.createTR();
        Element th;
        th = DOM.createTH();
        th.setInnerHTML("<span title='Корреляция'>   <span>");
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
        final CheckBox selectionBox = new CheckBox();
        selectionBox.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (selectionBox.getValue()) {
                    selectedSeries.add(series.getId());
                }
                else {
                    selectedSeries.remove(series.getId());
                }
                checkStatus();
            }
        });
        int column = 0;
        table.setWidget(index, column, selectionBox);
        table.getCellFormatter().addStyleName(index, column++, "text-center");
        table.setText(index, column, series.getName());
        table.getCellFormatter().addStyleName(index, column++, "series-cell name");
        // ///////////////////////////////
        insertValues(series.getValues(), column);
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
        table.setWidget(index, columnCount - 1, editBtn);
        table.setWidget(index, columnCount, removeBtn);
        index++;
    }

    protected void checkStatus() {
        ListBox nativeListBox = actionListBox.getNativeListBox();
        if (selectedSeries.size() > 0 || nativeListBox.getValue(nativeListBox.getSelectedIndex()).equals("REGRESSION")) {
            actionBtn.setEnabled(true);
        }
        else {
            actionBtn.setEnabled(false);
        }
    }

    private void insertValues(Map<Long, Double> values, int columnIndex) {
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

    public void clear() {
        table.removeAllRows();
    }

    public void setActionHandler(IActionHandler handler) {
        this.actionHandler = handler;
    }
}