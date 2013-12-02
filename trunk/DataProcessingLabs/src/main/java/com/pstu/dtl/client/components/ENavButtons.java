package com.pstu.dtl.client.components;

import com.pstu.dtl.client.mvp.place.SeriesEditPagePlace;

public enum ENavButtons {

    SERIES_EDIT_PAGE("Ряды", new SeriesEditPagePlace().toString());
    public String text;
    public String link;

    private ENavButtons(String text, String link) {
        this.text = text;
        this.link = link;
    }
}