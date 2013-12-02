package com.pstu.dtl.client.mvp;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.pstu.dtl.client.components.NavigationBar;
import com.pstu.dtl.client.mvp.iview.ISeriesEditPageView;
import com.pstu.dtl.client.mvp.view.SeriesEditPageView;

public class ClientFactoryImpl implements ClientFactory {

    private final EventBus eventBus = new SimpleEventBus();
    private final NavigationBar navigationBar = new NavigationBar();
    private final ISeriesEditPageView seriesEditPageView = new SeriesEditPageView();
    private final PortalHeader portalHeader = new PortalHeader();

    @SuppressWarnings("deprecation")
    private final PlaceController placeController = new PlaceController(eventBus);

    public EventBus getEventBus() {
        return eventBus;
    }

    public PlaceController getPlaceController() {
        return placeController;
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public PortalHeader getHeader() {
        return portalHeader;
    }

    public ISeriesEditPageView getSeriesEditPageView() {
        return seriesEditPageView;
    }

}
