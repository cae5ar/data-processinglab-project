package com.pstu.dtl.client.components;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Bootstrap alert messages. Use @link {@link #showDialogBox(CustomDialogBox)}
 * to create and show alert messages
 * 
 * @author Kasimov A.D.
 */
public class CustomDialogBox extends Composite {

    private static final int DELAY_BEFORE_SHOW = 100;
    private static final int DELAY_BEFORE_CLOSE = 500;
    private static final int POPUP_SHOW_LENGTH = 2000;

    public enum EAlertType {
        INFO("alert-info"),
        SUCCES("alert-success"),
        ERROR("alert-error"),
        WARNING("alert-warning");

        String style;

        EAlertType(String style) {
            this.style = style;
        }

        public String ToString() {
            return this.style;
        }
    }

    FlowPanel mainPanel = new FlowPanel();
    Button closeButton = new Button("×");
    Element requiredText = DOM.createElement("strong");
    Element text = DOM.createSpan();
    String selectedType = "";

    public CustomDialogBox() {
        initWidget(mainPanel);
        mainPanel.addStyleName("alert fade");
        closeButton.addStyleName("close");
        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        mainPanel.add(closeButton);
        mainPanel.getElement().appendChild(requiredText);
        mainPanel.getElement().appendChild(text);
    }

    public CustomDialogBox(String boldText) {
        this();
        setBoldText(boldText);
    }

    public CustomDialogBox(String boldText, String text) {
        this(boldText);
        setText(text);
    }

    public CustomDialogBox(String boldText, String text, EAlertType type) {
        this(boldText, text);
        setType(type);
    }

    public void setBoldText(String text) {
        if (text != null && !text.isEmpty()) requiredText.setInnerHTML(text + "<br/>");
    }

    public void setText(String text) {
        if (text != null && !text.isEmpty()) this.text.setInnerText(text);
    }

    public void setType(EAlertType type) {
        if (!selectedType.isEmpty()) {
            mainPanel.removeStyleName(selectedType);
        }
        mainPanel.addStyleName(type.ToString());
        selectedType = type.ToString();
    }

    private void hide() {
        mainPanel.removeStyleName("in");
        Timer t = new Timer() {
            @Override
            public void run() {
                CustomDialogBox.this.removeFromParent();
            }
        };
        t.schedule(DELAY_BEFORE_CLOSE);
    }

    public static void showDialogBox(final CustomDialogBox popup) {
        RootPanel.get().add(popup);
        popup.getElement().getStyle().setPosition(Position.FIXED);
        popup.getElement().getStyle().setBottom(0, Unit.PX);
        popup.getElement().getStyle().setRight(15, Unit.PX);
        popup.show();
        Timer timer = new Timer() {
            @Override
            public void run() {
                popup.hide();
            }
        };
        timer.schedule(POPUP_SHOW_LENGTH);
    }

    public static void showDialogBox(String strong) {
        showDialogBox(new CustomDialogBox(strong, "", EAlertType.SUCCES));
    }

    public static void showDialogBox(String strong, String text) {
        showDialogBox(new CustomDialogBox(strong, text, EAlertType.SUCCES));
    }

    public static void showDialogBox(String strong, String text, EAlertType type) {
        showDialogBox(new CustomDialogBox(strong, text, type));
    }

    private void show() {
        Timer t = new Timer() {
            @Override
            public void run() {
                mainPanel.addStyleName("in");
            }
        };
        t.schedule(DELAY_BEFORE_SHOW);
    }
}
