package com.pstu.dtl.client.components;

import java.util.Date;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.pstu.dtl.shared.dto.DateRange;

public class CustomDateBox extends AbstractSimpleInput {

    private DateBox leftDate = new DateBox();
    private DateBox rightDate = new DateBox();
    private FlowPanel valuePanel = new FlowPanel();
    private DateTimeFormat df = DateTimeFormat.getFormat("HH:mm dd.MM.yyyy");

    public CustomDateBox(Boolean isPeriod) {
        inputWrap.removeFromParent();
        helpInline.removeFromParent();
        valuePanel.add(leftDate);
        if (isPeriod) {
            Element span = DOM.createSpan();
            span.setInnerText(" по ");
            valuePanel.getElement().appendChild(span);
            valuePanel.add(rightDate);
        }
        controls.add(valuePanel);
        leftDate.setFormat(new DateBox.DefaultFormat(df));
        rightDate.setFormat(new DateBox.DefaultFormat(df));
        controls.getElement().appendChild(helpInline);
    }

    public CustomDateBox() {
        this(false);
    }

    public String getInputValue() {
        Date value = leftDate.getValue();
        if (value != null) {
            return df.format(value) + "Z";
        }
        return "";
    }

    public void setLeftValue(String strValue) {
        Date date = null;
        if (strValue != null && !strValue.isEmpty()) {
            date = df.parse(strValue);
        }
        setLeftValue(date);
    }

    public void setRightValue(String strValue) {
        Date date = null;
        if (strValue != null && !strValue.isEmpty()) {
            date = df.parse(strValue);
        }
        setRightValue(date);
    }

    private void setLeftValue(Date date) {
        leftDate.setValue(date);
    }

    private void setRightValue(Date date) {
        rightDate.setValue(date);
    }

    @Override
    public void addInputStyleName(String style) {
        valuePanel.addStyleName(style);
    }

    public void setPeriodValue(DateRange period) {
        if (period != null) {
            setLeftValue(period.getLeftDate());
            setRightValue(period.getRightDate());
        }
        else {
            setLeftValue("");
            setRightValue("");
        }
    }

}
