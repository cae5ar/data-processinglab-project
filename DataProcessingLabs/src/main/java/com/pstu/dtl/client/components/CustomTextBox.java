package com.pstu.dtl.client.components;

import com.google.gwt.user.client.ui.TextBox;
import com.pstu.dtl.client.components.AbstractSimpleInput;

public class CustomTextBox extends AbstractSimpleInput {

    private TextBox textBox = new TextBox();

    public CustomTextBox() {
        wrapInput(textBox);
    }

    public String getValue() {
        return textBox.getText();
    }

    public void setValue(String strValue) {
        textBox.setText(strValue);
    }

    protected void setFocus() {
        textBox.setFocus(true);
    }

    @Override
    public void addInputStyleName(String style) {
        textBox.addStyleName(style);
    }
    
    public TextBox getTextBox(){
        return textBox;
    }
}
