package com.pstu.dtl.client.mvp;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.pstu.dtl.client.Site;
import com.pstu.dtl.client.mvp.SearchWidget.ISearchHandler;

/**
 * Верхняя шапка сайта
 * 
 * @author Kasimov A.D.
 */
public class PortalHeader implements IsWidget {
    private FlowPanel panel = new FlowPanel();
    private Label captionText;
    private SimplePanel captionButtonContainer;
    private ISearchHandler handler;
    private SearchWidget searchWidget;
    private Element name;
    private Element login;

    public PortalHeader() {
        panel.addStyleName("header-block");
        FlowPanel userInfoAndSearchBlock = new FlowPanel();
        userInfoAndSearchBlock.addStyleName("row without-margin");

        FlowPanel userInfoBlock = new FlowPanel();
        userInfoBlock.addStyleName("user-info-block");

        FlowPanel searchBlock = new FlowPanel();
        searchBlock.addStyleName("search-block text-right");
        searchWidget = new SearchWidget(handler, Site.messages.msgSearchOnPortal());
        searchBlock.add(searchWidget);

        name = DOM.createElement("strong");
        login = DOM.createSpan();
        FlowPanel nameBlock = new FlowPanel();
        nameBlock.addStyleName("name-block");
        nameBlock.getElement().appendChild(name);
        nameBlock.getElement().appendChild(login);
        userInfoBlock.add(nameBlock);

        FlowPanel userLinksBlock = new FlowPanel();
        userLinksBlock.addStyleName("user-links-block");
        Element eventsLink = DOM.createSpan();
        eventsLink.setInnerHTML("<a href='#'>События</a>");
        Element profileLink = DOM.createSpan();
        profileLink.setInnerHTML("<a href='#'>Профиль</a>");
        Element logoutLink = DOM.createSpan();
        logoutLink.setInnerHTML("<a href='#'>Выход</a>");
        userLinksBlock.getElement().appendChild(eventsLink);
        userLinksBlock.getElement().appendChild(profileLink);
        userLinksBlock.getElement().appendChild(logoutLink);
        userInfoBlock.add(userLinksBlock);

        userInfoAndSearchBlock.add(userInfoBlock);
        userInfoAndSearchBlock.add(searchBlock);

        FlowPanel captionBlock = new FlowPanel();
        captionBlock.addStyleName("row without-margin caption-block");

        captionButtonContainer = new SimplePanel();
        captionButtonContainer.addStyleName("button-container");

        captionText = new Label("Здесь могла быть ваша реклама)");
        captionText.addStyleName("caption");

        Grid captionBlockGrid = new Grid(1, 2);
        captionBlockGrid.setStyleName("caption-block-table");

        captionBlockGrid.setWidget(0, 0, captionButtonContainer);
        captionBlockGrid.getCellFormatter().setWidth(0, 0, "50%");
        captionBlockGrid.setWidget(0, 1, captionText);
        captionBlockGrid.getCellFormatter().setWidth(0, 1, "50%");
        captionBlockGrid.getCellFormatter().setStyleName(0, 1, "text-right");

        captionBlock.add(captionBlockGrid);

        panel.add(userInfoAndSearchBlock);
        panel.add(captionBlock);
    }

    public Widget asWidget() {
        return panel;
    }

    public void setButtonInCaption(IsWidget button) {
        captionButtonContainer.clear();
        if (button != null) {
            captionButtonContainer.setWidget(button);
        }
    }

    public void setTextInCaption(String text) {
        captionText.setText(text != null ? text : "");
    }

    public void setSearchHandler(ISearchHandler handler) {
        searchWidget.setHandler(handler);
    }

    public void setUserInfo(String username, String userLogin) {
        name.setInnerText(username != null ? username : "%USERNAME% ");
        login.setInnerText(userLogin != null ? " / " + userLogin + " / " : " / %login% / ");
    }
}
