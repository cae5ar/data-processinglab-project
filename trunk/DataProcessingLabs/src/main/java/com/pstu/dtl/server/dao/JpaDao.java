package com.pstu.dtl.server.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.pstu.dtl.server.domain.AbstractEntity;
import com.pstu.dtl.server.security.CurrentUser;
import com.pstu.dtl.shared.exception.AnyServiceException;
import com.pstu.dtl.shared.exception.ValidationException;


public abstract class JpaDao<E /*extends AbstractEntity*/> {// implements
    // Dao<E> {

    public static final Logger log = Logger.getLogger(JpaDao.class);

    public abstract Class<E> getEntityClass();

    @PersistenceContext
    protected EntityManager em;

    protected Session getHibernateSession() {
        return (Session) em.getDelegate();
    }

    public void persist(E entity) throws AnyServiceException {
        log.debug(getEntityClass().getSimpleName() + " " + CurrentUser.getLogin() + " persist: ");
        try {
            em.persist(entity);
            em.flush();
        }
        catch (Exception e) {
            log.error(getEntityClass().getSimpleName() + " " + CurrentUser.getLogin() + " persist", e);
            throw new AnyServiceException("Ошибка сохранения. Возможно не все обязательные поля заполнены", e);
        }
    }

    public void remove(E entity) throws AnyServiceException {
        log.debug(getEntityClass().getSimpleName() + " " + CurrentUser.getLogin() + " remove: ");
        em.remove(entity);
    }

    public void remove(Long id) throws AnyServiceException {
        log.debug(getEntityClass().getSimpleName() + " " + CurrentUser.getLogin() + " remove: " + id);
        em.remove(findById(id));
    }

    public E findById(Long id) {
        if (id == null) return null;
        log.debug(getEntityClass().getSimpleName() + " " + CurrentUser.getLogin() + " findById: " + id);
        return em.find(getEntityClass(), id);
    }

    public <T extends AbstractEntity> T find(Class<T> cls, Long id) {
        if (log.isDebugEnabled()) {
            log.debug(getEntityClass().getSimpleName() + " " + CurrentUser.getLogin() + " findById: " + id);
        }
        if (id == null) {
            return null;
        }
        return em.find(cls, id);
    }

    @SuppressWarnings("unchecked")
    public List<E> selectAll() throws AnyServiceException {
        log.debug(getEntityClass().getSimpleName() + " " + CurrentUser.getLogin() + " selectAll");
        Query q = em.createQuery("SELECT e FROM " + getEntityClass().getName() + " e");
        return q.getResultList();
    }

    public void assertLength(String string, int length, String name) throws AnyServiceException {
        if (string != null && string.length() > length)
            throw new ValidationException("Длина атрибута " + name + " превышает максимальное значение " + length);
    }

    public void assertNull(String string, String name) throws AnyServiceException {
        if (string == null || string.trim().isEmpty())
            throw new ValidationException("Значение атрибута " + name + " не задано");
    }

    public void assertNull(Object object, String name) throws AnyServiceException {
        if (object == null) throw new ValidationException("Значение атрибута " + name + " не задано");
    }

    public void assertEntity(Object object, String name) throws AnyServiceException {
        if (object == null)
            throw new AnyServiceException("Объект " + name + "(" + getEntityClass().toString() + ") в базе не найден");
    }
}
