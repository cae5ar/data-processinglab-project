package com.pstu.dtl.client.components;

import java.util.List;

import com.google.gwt.user.client.ui.ListBox;

public class CustomListBox extends AbstractSimpleInput {

    private ListBox lbox = new ListBox();

    public CustomListBox(List<String> values) {
        wrapInput(lbox);
        if (values == null) {
            addValues(values);
        }
    }

    public String getInputValue() {
        return lbox.getItemText(lbox.getSelectedIndex());
    }

    private void addValues(List<String> values) {
        addValue("");
        if (values != null) {
            for (String str : values) {
                addValue(str);
            }
        }
    }

    private void addValue(String str) {
        lbox.addItem(str);
    }

    public void setInputValue(String strValue) {
        for (int i = 0; i < lbox.getItemCount(); i++) {
            if (lbox.getItemText(i).equalsIgnoreCase(strValue)) {
                lbox.setSelectedIndex(i);
                return;
            }
        }
    }

    protected void setFocus() {
        lbox.setFocus(true);
    }

    @Override
    public void addInputStyleName(String style) {
        lbox.addStyleName(style);
    }
}
