package com.pstu.dtl.client.components;

import com.google.gwt.user.client.ui.Hyperlink;

/**
 * Hyperlnk который поддерживает историю GWT и в котором линк не оборачивается в
 * div
 * 
 * @author Kasimov A.D.
 */
public class CustomHyperlink extends Hyperlink {

    public CustomHyperlink(String text, String targetHistoryToken) {
        super(null);
        directionalTextHelper.setTextOrHtml(text, false);
        setTargetHistoryToken(targetHistoryToken);
    }
}
