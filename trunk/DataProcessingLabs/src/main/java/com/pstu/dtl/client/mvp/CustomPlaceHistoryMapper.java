package com.pstu.dtl.client.mvp;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.pstu.dtl.client.mvp.place.SeriesEditPagePlace;

@WithTokenizers({ SeriesEditPagePlace.Tokenizer.class})
public interface CustomPlaceHistoryMapper extends PlaceHistoryMapper {}
