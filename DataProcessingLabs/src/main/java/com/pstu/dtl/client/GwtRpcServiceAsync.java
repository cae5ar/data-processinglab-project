package com.pstu.dtl.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.pstu.dtl.shared.dto.Cluster;
import com.pstu.dtl.shared.dto.PeriodDto;
import com.pstu.dtl.shared.dto.SeriesDto;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GwtRpcServiceAsync {
    void saveSeries(SeriesDto bean, AsyncCallback<Long> callback);

    void getAllSeries(AsyncCallback<List<SeriesDto>> callback);

    void getAllPeriods(AsyncCallback<Map<Long, String>> callback);

    void deleteSeries(Long seriesId, AsyncCallback<Void> callback);

    void deletePeriod(Long periodId, AsyncCallback<Void> callback);

    void savePeriods(List<PeriodDto> periods, AsyncCallback<List<PeriodDto>> callback);

    void calculateSquareRegression(List<Long> seriesList, AsyncCallback<Map<String, List<Double>>> callback);

    void doClustering(List<Long> seriesList, List<Long> periodList, Integer clusterCount,
            AsyncCallback<List<Cluster>> callback);

}
