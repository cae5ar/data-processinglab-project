package com.pstu.dtl.client.mvp;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.pstu.dtl.client.components.Btn;
import com.pstu.dtl.client.components.Btn.EButtonStyle;

public class SearchWidget implements IsWidget {

    private FlowPanel searchPanel;
    private TextBox text;
    private ISearchHandler handler;

    public interface ISearchHandler {
        void search(String query);
    }

    public SearchWidget(final ISearchHandler handler) {
        this.handler = handler;
        searchPanel = new FlowPanel();
        searchPanel.addStyleName("input-group input-group-sm search-form");
        text = new TextBox();
        text.setStyleName("form-control search-text");
        Element addon = DOM.createSpan();
        addon.addClassName("input-group-btn search-icon");

        Btn searchBtn = new Btn("<span class='glyphicon glyphicon-search'></span>", EButtonStyle.LINK);
        DOM.sinkEvents(searchBtn.getElement(), Event.ONCLICK);
        DOM.setEventListener(searchBtn.getElement(), new EventListener() {
            public void onBrowserEvent(Event event) {
                if (SearchWidget.this.handler != null) SearchWidget.this.handler.search(text.getText());
            }
        });
        addon.appendChild(searchBtn.getElement());

        searchPanel.add(text);
        searchPanel.getElement().appendChild(addon);
    }

    public SearchWidget(final ISearchHandler handler, String placeholderText) {
        this(handler);
        text.getElement().setAttribute("placeholder", placeholderText);
    }

    public Widget asWidget() {
        return searchPanel;
    }

    public void setHandler(ISearchHandler handler) {
        this.handler = handler;
    }

}
