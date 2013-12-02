package com.pstu.dtl.client.components;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Боковая панель. Под навигацию и ссылки
 * 
 * @author Kasimov A.D.
 */
public class VerticalNavigationBar implements IsWidget {

    private FlowPanel panel = new FlowPanel();
    private String emblemImg = "<img class='gerb' src='img/gerb.png' border='0' align='middle'/>";
    private Element navButtonConatinerLeft;
    private Map<ENavButtons, Element> buttons = new HashMap<ENavButtons, Element>();
    private Element lastActiveButton;

    public VerticalNavigationBar() {
        panel.addStyleName("left-vertical-bar");
        HTMLPanel emblem = new HTMLPanel(emblemImg);
        emblem.addStyleName("emblem-container");
        panel.add(emblem);
        navButtonConatinerLeft = DOM.createElement("ul");
        navButtonConatinerLeft.setClassName("nav");
        panel.getElement().appendChild(navButtonConatinerLeft);
        FlowPanel infoPanel = new FlowPanel();
        infoPanel.addStyleName("outer-links-block");
        HTMLPanel eaLink = new HTMLPanel("<a href='#'><img src='img/ea-icon-small.png'><br/> Электронный атлас города Москвы</a>");
        eaLink.addStyleName("out-link-block");
        infoPanel.add(eaLink);
        HTMLPanel sodgLink = new HTMLPanel("<a href='#'><img src='img/sodg-icon.png'><br/> Система оперативного доступа к геоданным</a>");
        sodgLink.addStyleName("out-link-block");
        infoPanel.add(sodgLink);
        HTMLPanel portalName = new HTMLPanel("<span style='font-size:22px'>ИАИС ЕГИП</span> <span style='font-size:11px'> технологический портал</span>");
        portalName.addStyleName("portal-name");
        infoPanel.add(portalName);
        panel.add(infoPanel);
        initButtons();
    }

    /**
     * Все кнопки из ENavButtons надо инициализировать тут
     */
    private void initButtons() {
        addButton(ENavButtons.SERIES_EDIT_PAGE);
    }

    public void addButton(ENavButtons button) {
        Element li = DOM.createElement("li");
        li.setClassName("text-center");
        CustomHyperlink link = new CustomHyperlink(button.text, button.link);
        li.appendChild(link.getElement());
        navButtonConatinerLeft.appendChild(li);
        buttons.put(button, li);
    }

    public void setActiveButton(ENavButtons button) {
        Element li = buttons.get(button);
        if (li != null && li != lastActiveButton) {
            if (lastActiveButton != null) {
                lastActiveButton.removeClassName("active-tab");
            }
            li.addClassName("active-tab");
            lastActiveButton = li;
        }
    }

    public Widget asWidget() {
        return panel;
    }

}
