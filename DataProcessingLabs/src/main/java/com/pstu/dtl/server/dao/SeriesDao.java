package com.pstu.dtl.server.dao;

import java.util.List;
import java.util.Map.Entry;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pstu.dtl.server.domain.Series;
import com.pstu.dtl.shared.dto.SeriesDto;
import com.pstu.dtl.shared.exception.AnyServiceException;

@Repository
public class SeriesDao extends JpaDao<Series> {

    @Autowired
    ValueDao valueDao;

    @Override
    public Class<Series> getEntityClass() {
        return Series.class;
    }

    public Series save(SeriesDto bean) throws AnyServiceException {
        Series entity;
        if (bean.getId() == null) {
            entity = new Series();
        }
        else {
            entity = findById(bean.getId());
        }
        entity.setName(bean.getName());
        persist(entity);
        for (Entry<Long, Double> e : bean.getValues().entrySet()) {
            valueDao.save(e.getKey(), e.getValue(), entity);
        }
        return entity;
    }

    public Series findByName(String name) {
        Query q = em.createQuery("SELECT s FROM " + getEntityClass().getName() + " s WHERE s.name = :name");
        q.setParameter("name", name);
        return (Series) q.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<Series> getAllSeries() {
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        List<Series> list = criteria.list();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Series> getSeriesByList(List<Long> idList) {
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        criteria.add(Restrictions.in("id", idList));
        List<Series> list = criteria.list();
        return list;
    }
}
