package com.pstu.dtl.client.components;

import com.google.gwt.user.client.ui.ListBox;

public class CustomCheckBox extends AbstractSimpleInput {

    private static final String TRUE = "ДА";
    private static final String FALSE = "НЕТ";
    private ListBox lbox = new ListBox();

    public CustomCheckBox() {
        wrapInput(lbox);
        addValues();
    }

    public String getInputValue() {
        return lbox.getItemText(lbox.getSelectedIndex()).toLowerCase();
    }

    private void addValues() {
        addValue("");
        addValue(TRUE);
        addValue(FALSE);
    }

    private void addValue(String str) {
        lbox.addItem(str);
    }

    public void setInputValue(String strValue) {
        String value = "";
        if (strValue.equalsIgnoreCase("0") || strValue.equalsIgnoreCase("false")) {
            value = FALSE;
        }
        if (strValue.equalsIgnoreCase("1") || strValue.equalsIgnoreCase("true")) {
            value = TRUE;
        }
        for (int i = 0; i < lbox.getItemCount(); i++) {
            if (lbox.getItemText(i).equalsIgnoreCase(value)) {
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
        lbox.setStyleName(style);
    }

}
