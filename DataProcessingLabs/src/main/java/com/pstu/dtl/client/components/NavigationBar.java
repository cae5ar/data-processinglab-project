package com.pstu.dtl.client.components;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.pstu.dtl.client.Site;
import com.pstu.dtl.client.components.Btn.EButtonStyle;

public class NavigationBar implements IsWidget {

    private static final int paddingInCollapsedHeader = 8;
    private static final int collapsedHeaderItemHeight = 40;
    private Element navButtonConatinerLeft;
    Map<ENavButtons, Element> buttons = new HashMap<ENavButtons, Element>();
    Element lastActiveButton;
    private boolean collapsed = false;
    private String collapsedHeaderSize = "auto";
    private FlowPanel navBar;
    private FlowPanel navCollapse;

    public NavigationBar() {
        navBar = new FlowPanel();
        navBar.addStyleName("navbar navbar-default navbar-static-top blue");

        FlowPanel navbarHeader = new FlowPanel();
        navbarHeader.setStyleName("navbar-header");
        navBar.add(navbarHeader);

        navCollapse = new FlowPanel();
        navCollapse.setStyleName("navbar-collapse collapse");
        navBar.add(navCollapse);

        Btn collapseButton = new Btn("<span class='icon-bar'></span><span class='icon-bar'></span><span class='icon-bar'></span>", new ClickHandler() {

            public void onClick(ClickEvent event) {
                collapsed = !collapsed;
                navCollapse.getElement().setAttribute("style", "height:"
                        + ((collapsed == true) ? collapsedHeaderSize : "0px"));
            }
        });
        collapseButton.addStyleName("navbar-toggle");
        navbarHeader.add(collapseButton);

        navButtonConatinerLeft = DOM.getElementById("nav-buttons-container");

        initButtons();
        initSearchPanel();
        // createToServiceLink();
        initCollapsedHeaderSize();
    }

    /**
     * Метод высчитывает высоту выпадающей шапки в зависимости от количества
     * кнопкок
     */
    private void initCollapsedHeaderSize() {
        collapsedHeaderSize = (collapsedHeaderItemHeight * navButtonConatinerLeft.getChildCount() + paddingInCollapsedHeader)
                + "px";
    }

    /**
     * Поисковую панель рисуем
     */
    private void initSearchPanel() {
        FlowPanel searchPanel = new FlowPanel();
        searchPanel.addStyleName("input-group input-group-sm navbar-right col-md-4 col-sm-4 search-form");
        TextBox tb = new TextBox();
        tb.getElement().setAttribute("placeholder", Site.messages.msgSearchOnPortal());
        tb.setStyleName("form-control");
        Element addon = DOM.createSpan();
        Btn searchBtn = new Btn("<span class='glyphicon glyphicon-search'></span>", EButtonStyle.DEFAULT);
        addon.addClassName("input-group-btn");
        // search.addClassName("glyphicon glyphicon-search");
        DOM.sinkEvents(searchBtn.getElement(), Event.ONCLICK);
        DOM.setEventListener(searchBtn.getElement(), new EventListener() {
            public void onBrowserEvent(Event event) {}
        });
        addon.appendChild(searchBtn.getElement());
        searchPanel.add(tb);
        searchPanel.getElement().appendChild(addon);
        navCollapse.add(searchPanel);
    }

    /**
     * Все кнопки из ENavButtons надо инициализировать тут
     */
    private void initButtons() {
//        addButton(ENavButtons.SERIES_EDIT_PAGE);
    }

    public void addButton(ENavButtons button) {
        Element li = DOM.createElement("li");
        CustomHyperlink link = new CustomHyperlink(button.text, button.link);
        li.appendChild(link.getElement());
        navButtonConatinerLeft.appendChild(li);
        buttons.put(button, li);
    }

    public void setActiveButton(ENavButtons button) {
        Element li = buttons.get(button);
        if (li != null && li != lastActiveButton) {
            if (lastActiveButton != null) {
                lastActiveButton.removeClassName("active");
            }
            li.addClassName("active");
            lastActiveButton = li;
        }
    }

    public Widget asWidget() {
        return navBar;
    }
}
