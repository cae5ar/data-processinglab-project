package com.pstu.dtl.client.mvp.activity;

import java.util.Map;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.pstu.dtl.client.SimpleAsyncCallback;
import com.pstu.dtl.client.Site;
import com.pstu.dtl.client.components.ENavButtons;
import com.pstu.dtl.client.mvp.ClientFactory;
import com.pstu.dtl.client.mvp.iview.ISeriesEditPageView;
import com.pstu.dtl.client.mvp.place.SeriesEditPagePlace;

public class SeriesEditPageActivity extends MainAbstractActivity implements Activity {

    @SuppressWarnings("unused")
    private SeriesEditPagePlace place;

    public SeriesEditPageActivity(SeriesEditPagePlace place, ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
        this.place = place;
    }

    public SeriesEditPageActivity(SeriesEditPagePlace place, ClientFactory clientFactory, ENavButtons buttonName) {
        this(place, clientFactory);
        this.buttonName = buttonName;
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        clientFactory.getNavigationBar().setActiveButton(buttonName);
        final ISeriesEditPageView view = clientFactory.getSeriesEditPageView();
        Site.service.getAllPeriods(new SimpleAsyncCallback<Map<Long, String>>() {
            public void onSuccess(Map<Long, String> result) {
                view.setPeriods(result);
                panel.setWidget(view);
                view.buildWidget();
            }
        });
    }
}
