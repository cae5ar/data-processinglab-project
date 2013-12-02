package com.pstu.dtl.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.pstu.dtl.client.components.ENavButtons;
import com.pstu.dtl.client.mvp.activity.SeriesEditPageActivity;
import com.pstu.dtl.client.mvp.place.SeriesEditPagePlace;

public class CustomActivityMapper implements ActivityMapper {

    private ClientFactory clientFactory;

    public CustomActivityMapper(ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
    }

    public Activity getActivity(Place place) {
        
        PortalHeader header = clientFactory.getHeader();
        header.setButtonInCaption(null);
        header.setTextInCaption(null);
        
        if (place instanceof SeriesEditPagePlace) {
            return new SeriesEditPageActivity((SeriesEditPagePlace) place, clientFactory, ENavButtons.SERIES_EDIT_PAGE);
        }

        return null;
    }
}
