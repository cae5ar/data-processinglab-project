package com.pstu.dtl.client.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
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

        void action(ActionType type, List<Long> seriesList, List<Long> periodList, String clusterCount);

    }

    private FlowPanel panel = new FlowPanel();
    private FlexTable table = new FlexTable();
    private CustomListBox actionListBox = new CustomListBox(null);
    private CustomTextBox textBox = new CustomTextBox();
    private Btn actionBtn = new Btn("Выполнить", EButtonStyle.INFO);
    private int index = 0;
    private int columnCount = 0;
    private Map<Long, String> periods = null;
    private List<Long> selectedSeries = new ArrayList<Long>();
    private List<Long> selectedPeriods = new ArrayList<Long>();
    private List<Long> allSeries = new ArrayList<Long>();
    private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
    private IActionHandler actionHandler;
    private NumberFormat nf = NumberFormat.getFormat("0.00000");
    private CheckBox selectAllCheckBox;

    public SeriesTable(Map<Long, String> periods) {
        initWidget(panel);
        panel.add(table);
        table.addStyleName("table table-striped");
        this.periods = periods;
        createThead(periods);
        actionListBox.addStyleName("action-list-box");
        actionListBox.addValue("Выберите действие", "");
        actionListBox.addValue("Построить линейные диаграммы", ActionType.LINEAR_DIAGRAM.name());
        actionListBox.addValue("Построить столбчатые диаграммы", ActionType.COLUMN_DIAGRAM.name());
        actionListBox.addValue("Посчитать регрессию МНК", ActionType.REGRESSION.name());
        actionListBox.addValue("Посчитать корреляцию", ActionType.CORRELATION.name());
        actionListBox.addValue("Выполнить кластеризацию", ActionType.CLUSTERING.name());
        Grid actionPanel = new Grid(1, 3);
        actionBtn.getElement().getStyle().setMarginBottom(8, Unit.PX);
        actionPanel.addStyleName("action-panel");
        panel.add(actionPanel);
        actionPanel.setWidget(0, 0, actionListBox);
        actionPanel.setWidget(0, 1, textBox);
        textBox.addStyleName("horizontal-input hide");
        textBox.getElement().getParentElement().setAttribute("style", "vertical-align: top;padding-top: 8px;padding-right: 8px;");
        textBox.setPlaceholder("Кол-во кластеров: макс.14");
        textBox.getTextBox().addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    doAction();
                }
            }
        });
        actionPanel.setWidget(0, 2, actionBtn);
        actionBtn.setEnabled(false);
        actionBtn.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                doAction();
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
        Element th = DOM.createTH();
        th.setClassName("text-center remove-cell");
        selectAllCheckBox = new CheckBox();
        selectAllCheckBox.addStyleName("need-to-margin-botom");
        DOM.sinkEvents(selectAllCheckBox.getElement(), Event.ONCHANGE);
        DOM.setEventListener(selectAllCheckBox.getElement(), new EventListener() {
            @Override
            public void onBrowserEvent(Event event) {
                if (!selectAllCheckBox.getValue()) {
                    selectAllSeries();
                }
                else {
                    unselectAllSeries();
                }
                checkStatus();
            }
        });
        th.appendChild(selectAllCheckBox.getElement());
        tr.appendChild(th);
        columnCount++;
        th = DOM.createTH();
        th.setInnerText("Имя ряда");
        tr.appendChild(th);
        columnCount++;
        for (Entry<Long, String> e : periods.entrySet()) {
            addColumn(tr, e);
            columnCount++;
        }
        th = DOM.createTH();
        th.setClassName("remove-cell");
        th.setInnerHTML("<span class='glyphicon glyphicon-pencil' style='margin: 6px 12px'></span>");
        tr.appendChild(th);
        columnCount++;
        th = DOM.createTH();
        th.setClassName("remove-cell");
        th.setInnerHTML("<span class='glyphicon glyphicon-minus' style='margin: 6px 12px'></span>");
        tr.appendChild(th);
        thead.appendChild(tr);
        Element tbody = table.getElement().getElementsByTagName("tbody").getItem(0);
        tbody.removeFromParent();
        table.getElement().appendChild(thead);
        table.getElement().appendChild(tbody);

    }

    private void addColumn(Element tr, Entry<Long, String> e) {
        Element th = DOM.createTH();
        th.setInnerText(e.getValue());
        final Long key = e.getKey();
        selectedPeriods.add(key);
        final CheckBox selectionBox = new CheckBox();
        selectionBox.setValue(true);
        DOM.sinkEvents(selectionBox.getElement(), Event.ONCHANGE);
        DOM.setEventListener(selectionBox.getElement(), new EventListener() {
            public void onBrowserEvent(Event event) {
                if (selectionBox.getValue()) {
                    selectedPeriods.remove(key);
                    selectionBox.setValue(false);
                }
                else {
                    selectedPeriods.add(key);
                    selectionBox.setValue(true);
                }
            }
        });
        th.appendChild(selectionBox.getElement());
        tr.appendChild(th);
    }

    public void additem(final SeriesDto series) {
        final CheckBox selectionBox = new CheckBox();
        selectionBox.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (selectionBox.getValue()) {
                    selectionBox.getElement().getParentElement().getParentElement().addClassName("selected-row");
                    selectedSeries.add(series.getId());
                }
                else {
                    selectionBox.getElement().getParentElement().getParentElement().removeClassName("selected-row");
                    selectedSeries.remove(series.getId());
                }
                checkStatus();
            }
        });
        checkBoxList.add(selectionBox);
        int column = 0;
        table.setWidget(index, column, selectionBox);
        table.getCellFormatter().addStyleName(index, column++, "text-center remove-cell");
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
        table.getCellFormatter().addStyleName(index, columnCount - 1, "remove-cell");
        table.setWidget(index, columnCount, removeBtn);
        table.getCellFormatter().addStyleName(index, columnCount, "remove-cell");
        index++;
    }

    protected void checkStatus() {
        ListBox nativeListBox = actionListBox.getNativeListBox();
        if (selectedSeries.size() > 0 && !nativeListBox.getValue(nativeListBox.getSelectedIndex()).equals("")) {
            actionBtn.setEnabled(true);
        }
        else {
            actionBtn.setEnabled(false);
        }
        if (nativeListBox.getValue(nativeListBox.getSelectedIndex()).equals("CLUSTERING")) {
            textBox.removeStyleName("hide");
        }
        else {
            textBox.addStyleName("hide");
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
            allSeries.add(s.getId());
            additem(s);
        }
        selectAllSeries();
    }

    public void clear() {
        table.removeAllRows();
        selectedSeries.clear();
        allSeries.clear();
        checkBoxList.clear();
    }

    public void setActionHandler(IActionHandler handler) {
        this.actionHandler = handler;
    }

    public FlowPanel getSimpleCloneTable(Map<String, List<Double>> columns) {
        FlowPanel panel = new FlowPanel();
        Element clone = (Element) table.getElement().cloneNode(true);
        panel.addStyleName("text-right");
        clone.setClassName("table cleared-table");
        panel.getElement().appendChild(clone);
        Element theadTR = clone.getElementsByTagName("thead").getItem(0).getElementsByTagName("tr").getItem(0);
        for (String str : columns.keySet()) {
            if (!str.equals("ALPHA")) {
                Element th = DOM.createTH();
                th.setInnerText("Y".equals(str) ? theadTR.getElementsByTagName("th").getItem(2).getInnerText() + "'" : str);
                theadTR.appendChild(th);
            }
        }
        NodeList<Element> bodyTRs = clone.getElementsByTagName("tbody").getItem(0).getElementsByTagName("tr");
        List<Double> list = columns.get("Y");
        for (int i = 0; i < list.size(); i++) {
            Element row = bodyTRs.getItem(i);
            Element td = DOM.createTD();
            td.setInnerText(nf.format(list.get(i)));
            td.setClassName("series-cell value new");
            row.appendChild(td);
        }
        List<Double> alpha = columns.get("ALPHA");
        Element alphaCaption = DOM.createTR();
        clone.getElementsByTagName("tbody").getItem(0).appendChild(alphaCaption);
        alphaCaption.appendChild(DOM.createTD());
        Element alphaTR = DOM.createTR();
        clone.getElementsByTagName("tbody").getItem(0).appendChild(alphaTR);
        alphaTR.appendChild(DOM.createTD());
        for (int i = 0; i < alpha.size(); i++) {
            Element tdAlphaCaption = DOM.createTD();
            tdAlphaCaption.setInnerHTML("<em>&alpha;<sub>" + i + "</sub></em>");
            alphaCaption.appendChild(tdAlphaCaption);

            Element td = DOM.createTD();
            td.setInnerText(nf.format(alpha.get(i)));
            td.setClassName("series-cell value alpha");
            alphaTR.appendChild(td);
        }
        alphaCaption.appendChild(DOM.createTD());
        alphaTR.appendChild(DOM.createTD());
        return panel;
    }

    private void doAction() {
        ListBox nativeListBox = actionListBox.getNativeListBox();
        String value = nativeListBox.getValue(nativeListBox.getSelectedIndex());
        if (value != null && !value.isEmpty()) {
            ActionType type = ActionType.valueOf(value);
            actionHandler.action(type, selectedSeries, selectedPeriods, textBox.getValue());
        }
    }

    private void unselectAllSeries() {
        for (CheckBox cb : checkBoxList) {
            cb.setValue(false);
            cb.getElement().getParentElement().getParentElement().removeClassName("selected-row");
        }
        selectedSeries.clear();
        selectAllCheckBox.setValue(false);
    }

    private void selectAllSeries() {
        for (CheckBox cb : checkBoxList) {
            cb.setValue(true);
            cb.getElement().getParentElement().getParentElement().addClassName("selected-row");
        }
        selectedSeries.clear();
        selectedSeries.addAll(allSeries);
        selectAllCheckBox.setValue(true);
    }
}
