package com.pstu.dtl.server.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pstu.dtl.client.GwtRpcService;
import com.pstu.dtl.server.dao.PeriodDao;
import com.pstu.dtl.server.dao.SeriesDao;
import com.pstu.dtl.server.domain.Period;
import com.pstu.dtl.server.domain.Series;
import com.pstu.dtl.server.math.Clusterizator;
import com.pstu.dtl.server.math.OLS;
import com.pstu.dtl.shared.dto.Cluster;
import com.pstu.dtl.shared.dto.PeriodDto;
import com.pstu.dtl.shared.dto.SeriesDto;
import com.pstu.dtl.shared.exception.AnyServiceException;

@SuppressWarnings("serial")
@Transactional
@Service
public class GwtRpcServiceImpl extends RemoteServiceServlet implements GwtRpcService {

    @Autowired
    SeriesDao seriesDao;
    @Autowired
    PeriodDao periodDao;

    public Long saveSeries(SeriesDto bean) throws AnyServiceException {
        Series entity = seriesDao.save(bean);
        return entity.getId();
    }

    public void deleteSeries(Long seriesId) throws AnyServiceException {
        if (seriesId != null) {
            seriesDao.remove(seriesId);
        }
        else {
            throw new AnyServiceException("ID field is null");
        }
    }

    public SeriesDto getSeriesById(Long seriesId) throws AnyServiceException {
        return seriesDao.findById(seriesId).toDto();
    }

    public List<SeriesDto> getAllSeries() throws AnyServiceException {
        List<SeriesDto> res = new ArrayList<SeriesDto>();
        List<Series> list = seriesDao.getAllSeries();
        for (Series layer : list) {
            res.add(layer.toDto());
        }
        return res;
    }

    public Map<Long, String> getAllPeriods() throws AnyServiceException {
        List<Period> allPeriods = periodDao.getAllperiod();
        Map<Long, String> out = new LinkedHashMap<Long, String>();
        for (Period p : allPeriods) {
            out.put(p.getId(), p.getName());
        }
        return out;
    }

    public void deletePeriod(Long periodId) throws AnyServiceException {
        periodDao.remove(periodId);
    }

    public List<PeriodDto> savePeriods(List<PeriodDto> periods) throws AnyServiceException {
        return periodDao.savePeriodList(periods);
    }

    public Map<String, List<Double>> calculateSquareRegression(List<Long> idList) throws AnyServiceException {
        return new OLS().calculate(getSeriesByList(idList), periodDao.getPeriodIdList(), getAllSeries());
    }

    public List<SeriesDto> getSeriesByList(List<Long> idList) {
        List<SeriesDto> res = new ArrayList<SeriesDto>();
        List<Series> list = seriesDao.getSeriesByList(idList);
        for (Series layer : list) {
            res.add(layer.toDto());
        }
        return res;
    }

    public List<PeriodDto> getPeriodsByList(List<Long> idList) {
        List<PeriodDto> res = new ArrayList<PeriodDto>();
        List<Period> list = periodDao.getPeriodsByList(idList);
        for (Period p : list) {
            res.add(p.toDto());
        }
        return res;
    }

    public List<Cluster> doClustering(List<Long> seriesList, List<Long> periodList, Integer clusterCount)
            throws AnyServiceException {
        return new Clusterizator().doKMeansClustering(getSeriesByList(seriesList), periodList, clusterCount);
    }
}
