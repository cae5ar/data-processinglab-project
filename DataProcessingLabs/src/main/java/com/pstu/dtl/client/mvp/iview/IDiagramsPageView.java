package com.pstu.dtl.client.mvp.iview;

import java.util.Map;

import com.google.gwt.user.client.ui.IsWidget;

public interface IDiagramsPageView extends IsWidget {
    public void buildWidget();
    public void setPeriods(Map<Long, String> periods);
}
