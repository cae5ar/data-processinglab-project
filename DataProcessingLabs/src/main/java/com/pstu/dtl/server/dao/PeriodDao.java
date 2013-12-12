package com.pstu.dtl.server.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.pstu.dtl.server.domain.Period;
import com.pstu.dtl.shared.dto.PeriodDto;
import com.pstu.dtl.shared.exception.AnyServiceException;

@Repository
public class PeriodDao extends JpaDao<Period> {

    @Override
    public Class<Period> getEntityClass() {
        return Period.class;
    }

    @SuppressWarnings("unchecked")
    public List<Period> getAllperiod() {
        Query q = em.createQuery("SELECT p FROM " + getEntityClass().getName() + " p");
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Long> getPeriodIdList() {
        Query q = em.createQuery("SELECT p.id FROM " + getEntityClass().getName() + " p");
        return q.getResultList();
    }

    public Period save(PeriodDto bean) throws AnyServiceException {
        Period entity;
        if (bean.getId() != null) {
            entity = em.find(getEntityClass(), bean.getId());
        }
        else {
            entity = new Period();
        }
        entity.setName(bean.getName());
        persist(entity);
        return entity;
    }

    public List<PeriodDto> savePeriodList(List<PeriodDto> periods) throws AnyServiceException {
        for (PeriodDto dto : periods) {
            Period save = save(dto);
            dto.setId(save.getId());
        }
        return periods;
    }

    @SuppressWarnings("unchecked")
    public List<Period> getPeriodsByList(List<Long> idList) {
        Criteria criteria = getHibernateSession().createCriteria(getEntityClass());
        criteria.add(Restrictions.in("id", idList));
        List<Period> list = criteria.list();
        return list;
    }

}
