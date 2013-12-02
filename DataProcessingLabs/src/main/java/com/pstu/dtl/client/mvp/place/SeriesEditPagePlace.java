package com.pstu.dtl.client.mvp.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class SeriesEditPagePlace extends Place {
    public static final String VIEW_HISTORY_TOKEN = "series_edit_page";


    @Prefix(value = VIEW_HISTORY_TOKEN)
    public static class Tokenizer implements PlaceTokenizer<SeriesEditPagePlace> {

        public SeriesEditPagePlace getPlace(String token) {
            return new SeriesEditPagePlace();
        }

        public String getToken(SeriesEditPagePlace place) {
            return "";
        }
    }

    public String toString() {
        return VIEW_HISTORY_TOKEN + ":" + new Tokenizer().getToken(this);
    }
}
