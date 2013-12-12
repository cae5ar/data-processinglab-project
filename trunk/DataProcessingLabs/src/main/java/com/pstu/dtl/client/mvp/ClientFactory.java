package com.pstu.dtl.client.mvp;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.pstu.dtl.client.components.NavigationBar;
import com.pstu.dtl.client.components.PortalHeader;
import com.pstu.dtl.client.mvp.iview.ISeriesEditPageView;

public interface ClientFactory {

    EventBus getEventBus();

    PlaceController getPlaceController();

    NavigationBar getNavigationBar();

    ISeriesEditPageView getSeriesEditPageView();

    PortalHeader getHeader();

}
