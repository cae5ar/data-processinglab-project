package com.pstu.dtl.client.objects;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.ClickListener;
import com.pstu.dtl.client.components.Btn;

@SuppressWarnings("deprecation")
public class CustomToggleButton extends Btn {

    private final String PRESSED_STYLE_SUFIX = "pressed";
    private boolean isPressed = false;

    public CustomToggleButton() {
        super();
        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                setPressed(!isPressed);
            }
        });
    }

    public CustomToggleButton(EButtonStyle style) {
        this();
        addStyleName(style.toString());
    }

    public CustomToggleButton(SafeHtml html) {
        this(html.asString());
    }

    public CustomToggleButton(String html) {
        this();
        setHTML(html);
    }

    public CustomToggleButton(String html, EButtonStyle style) {
        this(html);
        addStyleName(style.toString());
    }

    @Deprecated
    public CustomToggleButton(String html, ClickListener listener) {
        this(html);
        addClickListener(listener);
    }

    /**
     * Creates a button with the given HTML caption and click listener.
     * 
     * @param html the html caption
     * @param handler the click handler
     */
    public CustomToggleButton(SafeHtml html, ClickHandler handler) {
        this(html.asString(), handler);
    }

    /**
     * Creates a button with the given HTML caption and click listener.
     * 
     * @param html the HTML caption
     * @param handler the click handler
     */
    public CustomToggleButton(String html, ClickHandler handler) {
        this(html);
        addClickHandler(handler);
    }

    public CustomToggleButton(String html, EButtonStyle style, ClickHandler handler) {
        this(html, handler);
        addStyleName(style.toString());
    }

    protected CustomToggleButton(com.google.gwt.dom.client.Element element, ClickHandler handler) {
        super(element, handler);
    }

    public void setPressed(boolean isPressed) {
        if (this.isPressed != isPressed) {
            this.isPressed = isPressed;
            changeBtnState();
        }
    }

    private void changeBtnState() {
        if (isPressed) {
            addStyleName(PRESSED_STYLE_SUFIX);
        }
        else {
            removeStyleName(PRESSED_STYLE_SUFIX);
        }
    }
}
