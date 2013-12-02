package com.pstu.dtl.client.components;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;

/**
 * Переопределённый баттон под bootstrap
 * 
 * @author Kasimov A.D.
 */
@SuppressWarnings("deprecation")
public class Btn extends Button {

    public enum EButtonStyle {
        PRIMARY("btn-primary"),
        INFO("btn-info"),
        SUCCESS("btn-success"),
        WARNING("btn-warning"),
        DANGER("btn-danger"),
        INVERSE("btn-inverse"),
        LINK("btn-link"),
        DEFAULT("btn-default");

        private String styleName;

        EButtonStyle(String style) {
            this.styleName = style;
        }

        public String toString() {
            return styleName;
        }
    }

    public Btn() {
        super();
        setStyleName("btn");
    }

    public Btn(EButtonStyle style) {
        this();
        addStyleName(style.toString());
    }

    public Btn(SafeHtml html) {
        this(html.asString());
    }

    public Btn(String html) {
        this();
        setHTML(html);
    }

    public Btn(String html, EButtonStyle style) {
        this(html);
        addStyleName(style.toString());
    }

    @Deprecated
    public Btn(String html, ClickListener listener) {
        this(html);
        addClickListener(listener);
    }

    /**
     * Creates a button with the given HTML caption and click listener.
     * 
     * @param html the html caption
     * @param handler the click handler
     */
    public Btn(SafeHtml html, ClickHandler handler) {
        this(html.asString(), handler);
    }

    /**
     * Creates a button with the given HTML caption and click listener.
     * 
     * @param html the HTML caption
     * @param handler the click handler
     */
    public Btn(String html, ClickHandler handler) {
        this(html);
        addClickHandler(handler);
    }

    public Btn(String html, EButtonStyle style, ClickHandler handler) {
        this(html, handler);
        addStyleName(style.toString());
    }

    public Btn(com.google.gwt.dom.client.Element element, ClickHandler handler) {
        super(element);
        addClickHandler(handler);
    }
}
