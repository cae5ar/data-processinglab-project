package com.pstu.dtl.client.components;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class CustomPopup extends PopupPanel {

    protected static final int DELAY_BEFORE_SHOW = 100;
    private static final int DELAY_BEFORE_CLOSE = 500;
    protected FlowPanel panel = new FlowPanel();
    protected FlowPanel modalDialog = new FlowPanel();
    protected FlowPanel modalContent = new FlowPanel();
    protected HTMLPanel modalHeader = new HTMLPanel("");
    protected FlowPanel modalBody = new FlowPanel();
    protected FlowPanel modalFooter = new FlowPanel();
    protected Button close = new Button("×");
    private boolean animate;

    public void setAnimationEnabled(boolean enable) {
        animate = enable;
        if (animate == false) panel.addStyleName("in");
    };

    public CustomPopup() {
        super();
        setStyleName("");
        modalContent.addStyleName("modal-content");
        modalDialog.addStyleName("modal-dialog");
        modalHeader.setStyleName("modal-header");
        modalBody.setStyleName("modal-body");
        modalFooter.addStyleName("modal-footer");
        panel.setStyleName("modal fade");

        panel.add(modalDialog);
        modalDialog.add(modalContent);
        modalContent.add(modalHeader);
        modalContent.add(modalBody);
        modalContent.add(modalFooter);

        close.setStyleName("close");
        close.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                CustomPopup.this.hide();
            }
        });
        setWidget(panel);
        setGlassEnabled(true);
        setModal(true);
        setAnimationEnabled(true);
        setHeader("this is upstep");
    }

    public void setHeader(String text) {
        modalHeader.clear();
        modalHeader.getElement().setInnerHTML("");
        modalHeader.add(close);
        Element h4 = DOM.createElement("h4");
        h4.setInnerText(text);
        modalHeader.getElement().appendChild(h4);

    }

    public void setHeader(Widget w) {
        modalHeader.clear();
        modalHeader.add(close);
        modalHeader.add(w);
    }

    @Override
    public void show() {
        super.show();
        if (animate) animateIn();
    }

    private void animateIn() {
        Timer timer = new Timer() {
            @Override
            public void run() {
                panel.addStyleName("in");
            }
        };
        timer.schedule(DELAY_BEFORE_SHOW);
        panel.getElement().getStyle().setDisplay(Display.BLOCK);
    }

    @Override
    public void hide() {
        if (animate)
            animateOut();
        else
            super.hide();
    }

    private void animateOut() {
        Timer timer = new Timer() {
            @Override
            public void run() {
                CustomPopup.super.hide();
            }
        };
        panel.removeStyleName("in");
        timer.schedule(DELAY_BEFORE_CLOSE);
    }
}
