package com.pstu.dtl.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.pstu.dtl.shared.dto.Cluster;
import com.pstu.dtl.shared.dto.PeriodDto;
import com.pstu.dtl.shared.dto.SeriesDto;
import com.pstu.dtl.shared.exception.AnyServiceException;

@RemoteServiceRelativePath("service.rpc")
public interface GwtRpcService extends RemoteService {
    Long saveSeries(SeriesDto bean) throws AnyServiceException;
    List<SeriesDto> getAllSeries() throws AnyServiceException;
    Map<Long, String> getAllPeriods() throws AnyServiceException;
    void deleteSeries(Long seriesId) throws AnyServiceException;
    void deletePeriod(Long periodId) throws AnyServiceException;
    List<PeriodDto> savePeriods(List<PeriodDto> periods) throws AnyServiceException;
    Map<String,List<Double>> calculateSquareRegression(List<Long> seriesList) throws AnyServiceException;
    List<Cluster> doClustering(List<Long> seriesList, List<Long> periodList,Integer clusterCount) throws AnyServiceException;
}
