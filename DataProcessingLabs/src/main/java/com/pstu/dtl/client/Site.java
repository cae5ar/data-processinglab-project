package com.pstu.dtl.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.pstu.dtl.client.components.AlertDialogBox;
import com.pstu.dtl.client.components.AlertDialogBox.EAlertType;
import com.pstu.dtl.client.mvp.ClientFactory;
import com.pstu.dtl.client.mvp.CustomActivityMapper;
import com.pstu.dtl.client.mvp.CustomPlaceHistoryMapper;
import com.pstu.dtl.client.mvp.place.SeriesEditPagePlace;

public class Site implements EntryPoint {

    public static final GwtRpcServiceAsync service = GWT.create(GwtRpcService.class);
    public static final AppMessages messages = GWT.create(AppMessages.class);
    public static Logger logger = Logger.getLogger("");
    public static final Element loadingElement = DOM.getElementById("loading");
    public static final Element waitingBlock = DOM.getElementById("loading");
    public static final int STATUS_CODE_OK = 200;

    public static ScrollPanel contentPanel = new ScrollPanel();
    HandlerRegistration historyHandlerRegistration = null;

    public static void handleError(Throwable caught) {
        System.out.println();
        logger.log(Level.WARNING, caught.getMessage(), caught);
        AlertDialogBox.showDialogBox(new AlertDialogBox("Ошибка!", caught.getMessage(), EAlertType.ERROR));
    }

    public static String getServletUrl(String servlet, String name, Long id) {
        UrlBuilder builder = new UrlBuilder();
        builder.setParameter("id", (id + ""));
        builder.setProtocol("http");
        builder.setPath(GWT.getHostPageBaseURL() + servlet + name.replace("х", "x"));

        return builder.buildString().substring(8);
    }

    public static String getGetFileServletUrl(String name, Long id) {
        return getServletUrl("service/getfile/", name, id);
    }

    public static String getAddFileServletUrl() {
        return GWT.getHostPageBaseURL() + "service/addfile";
    }

    public static String getXSDTemplateServletUrl() {
        return GWT.getHostPageBaseURL() + "service/getxsdtemplate/";
    }

    public void onModuleLoad() {
        setWaitingBlockVisible(false);
         initPlaceHistoryHandler();
//        Chart chart = new Chart().setType(Series.Type.SPLINE).setChartTitleText("Lawn Tunnels").setMarginRight(10);
//        Series series = chart.createSeries().setName("Moles per Yard").setPoints(new Number[] {163, 203, 276, 408, 547,
//                729, 628});
//        chart.addSeries(series);
//        RootPanel.get().add(chart);
    }

    public static void setWaitingBlockVisible(boolean visible) {
        waitingBlock.getStyle().setDisplay(visible ? Display.BLOCK : Display.NONE);
    }

    @SuppressWarnings("deprecation")
    private void initPlaceHistoryHandler() {
        final ClientFactory clientFactory = GWT.create(ClientFactory.class);
        logger.log(Level.INFO, "Create ClientFactory");
        // RootPanel.get().add(clientFactory.getNavigationBar());
        RootPanel.get().add(contentPanel);
        // RootPanel.get().add(columnChart);
        contentPanel.addStyleName("container");
        ActivityMapper activityMapper = new CustomActivityMapper(clientFactory);
        ActivityManager activityManager = new ActivityManager(activityMapper, clientFactory.getEventBus());
        activityManager.setDisplay(contentPanel);
        logger.log(Level.INFO, "Create mapper");

        // // // display default view with activated history processing
        final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler((PlaceHistoryMapper) GWT.create(CustomPlaceHistoryMapper.class));
        historyHandlerRegistration = historyHandler.register(clientFactory.getPlaceController(), clientFactory.getEventBus(), new SeriesEditPagePlace());
        historyHandler.handleCurrentHistory();
        logger.log(Level.INFO, "Redirect to welcome view");

    }
}
