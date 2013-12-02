package com.pstu.dtl.server.dao;

import org.springframework.stereotype.Repository;

import com.pstu.dtl.server.domain.Period;
import com.pstu.dtl.server.domain.Series;
import com.pstu.dtl.server.domain.Value;
import com.pstu.dtl.shared.exception.AnyServiceException;

@Repository
public class ValueDao extends JpaDao<Value> {

    @Override
    public Class<Value> getEntityClass() {
        return Value.class;
    }

    /**
     * Описание тут {@link #save(Long, String, Series)}
     * 
     * @param seriesId - id ряда
     * @param periodId - id периода
     * @param value - значение
     * @return - сущность значения
     * @throws AnyServiceException
     */
    public Value save(Long seriesId, Long periodId, Double value) throws AnyServiceException {
        Series series = em.find(Series.class, seriesId);
        return save(periodId, value, series);
    }

    /**
     * Сохраняем значения ряда
     * 
     * @param periodId - ид периода
     * @param value - значение
     * @param series - сущность ряда
     * @return
     * @throws AnyServiceException
     */
    public Value save(Long periodId, Double value, Series series) throws AnyServiceException {
        Period period = em.find(Period.class, periodId);
        Value entity = new Value();
        entity.setSeries(series);
        entity.setPeriod(period);
        entity.setValue(value);
        persist(entity);
        return entity;
    }

}
