/*
 * Beangle, Agile Development Scaffold and Toolkits.
 *
 * Copyright © 2005, The Beangle Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.beanfuse.commons.utils.persistence.hibernate;

import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.model.EntityUtils;
import org.beanfuse.commons.query.AbstractQuery;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.query.hibernate.HibernateQuerySupport;
import org.beanfuse.commons.query.limit.Page;
import org.beanfuse.commons.query.limit.PageLimit;
import org.beanfuse.commons.query.limit.SinglePage;
import org.beanfuse.commons.utils.persistence.UtilDao;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.util.*;

public class UtilDaoHibernate extends HibernateDaoSupport implements UtilDao {
  public UtilDaoHibernate() {
  }

  public List loadAll(Class clazz) {
    String hql = "from " + EntityUtils.getEntityType(clazz).getEntityName();
    Query query = this.getSession().createQuery(hql);
    return query.list();
  }

  public List load(Class entityClass, String keyName, Object[] values) {
    if (entityClass != null && !StringUtils.isEmpty(keyName) && values != null && values.length != 0) {
      String entityName = EntityUtils.getEntityType(entityClass).getEntityName();
      return this.load(entityName, keyName, values);
    } else {
      return Collections.EMPTY_LIST;
    }
  }

  public List load(String entityName, String keyName, Object[] values) {
    StringBuffer hql = new StringBuffer();
    hql.append("select entity from ").append(entityName).append(" as entity where entity.").append(keyName).append(" in (:keyName)");
    Map parameterMap = new HashMap(1);
    EntityQuery query;
    if (values.length < 500) {
      parameterMap.put("keyName", values);
      query = new EntityQuery(hql.toString());
      query.setParams(parameterMap);
      return (List) this.search(query);
    } else {
      query = new EntityQuery(hql.toString());
      query.setParams(parameterMap);
      List rs = new ArrayList();

      for (int i = 0; i < values.length; i += 500) {
        int end = i + 500;
        if (end > values.length) {
          end = values.length;
        }

        parameterMap.put("keyName", ArrayUtils.subarray(values, i, end));
        rs.addAll(this.search(query));
      }

      return rs;
    }
  }

  public Entity get(Class clazz, Serializable id) {
    return this.get(EntityUtils.getEntityType(clazz).getEntityName(), id);
  }

  public Entity get(String entityName, Serializable id) {
    if (-1 != entityName.indexOf(46)) {
      return (Entity) this.getHibernateTemplate().get(entityName, id);
    } else {
      String hql = "from " + entityName + " where id =:id";
      Query query = this.getSession().createQuery(hql);
      query.setParameter("id", id);
      List rs = query.list();
      return !rs.isEmpty() ? (Entity) rs.get(0) : null;
    }
  }

  public Entity load(Class clazz, Serializable id) {
    return (Entity) this.getHibernateTemplate().load(EntityUtils.getEntityType(clazz).getEntityName(), id);
  }

  public Collection search(AbstractQuery query) {
    return HibernateQuerySupport.search(query, this.getSession());
  }

  public List searchNamedQuery(String queryName, Map params) {
    Query query = this.getSession().getNamedQuery(queryName);
    return HibernateQuerySupport.setParameter(query, params).list();
  }

  public List searchNamedQuery(String queryName, Map params, boolean cacheable) {
    Query query = this.getSession().getNamedQuery(queryName);
    query.setCacheable(cacheable);
    return HibernateQuerySupport.setParameter(query, params).list();
  }

  public List searchNamedQuery(String queryName, Object[] params) {
    Query query = this.getSession().getNamedQuery(queryName);
    return HibernateQuerySupport.setParameter(query, params).list();
  }

  public List searchHQLQuery(String hql) {
    return this.getSession().createQuery(hql).list();
  }

  public List searchHQLQuery(String hql, Map params) {
    Query query = this.getSession().createQuery(hql);
    return HibernateQuerySupport.setParameter(query, params).list();
  }

  public List searchHQLQuery(String hql, Object[] params) {
    Query query = this.getSession().createQuery(hql);
    return HibernateQuerySupport.setParameter(query, params).list();
  }

  public List searchHQLQuery(String hql, Map params, boolean cacheable) {
    Query query = this.getSession().createQuery(hql);
    query.setCacheable(cacheable);
    return HibernateQuerySupport.setParameter(query, params).list();
  }

  public Page paginateNamedQuery(String queryName, Map params, PageLimit limit) {
    Query query = this.getSession().getNamedQuery(queryName);
    return this.paginateQuery(query, params, limit);
  }

  public Page paginateHQLQuery(String hql, Map params, PageLimit limit) {
    Query query = this.getSession().createQuery(hql);
    return this.paginateQuery(query, params, limit);
  }

  public Page paginateCriteria(Criteria criteria, PageLimit limit) {
    CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;
    int totalCount = 0;
    List targetList = null;
    if (null == criteriaImpl.getProjection() && !criteriaImpl.iterateOrderings().hasNext()) {
      criteria.setFirstResult((limit.getPageNo() - 1) * limit.getPageSize()).setMaxResults(limit.getPageSize());
      targetList = criteria.list();
      Projection projection = null;
      criteria.setFirstResult(0).setMaxResults(1);
      projection = Projections.rowCount();
      totalCount = (Integer) criteria.setProjection(projection).uniqueResult();
    } else {
      List list = criteria.list();
      totalCount = list.size();
      criteria.setFirstResult((limit.getPageNo() - 1) * limit.getPageSize()).setMaxResults(limit.getPageSize());
      targetList = criteria.list();
    }

    return new SinglePage(limit.getPageNo(), limit.getPageSize(), totalCount, targetList);
  }

  public void evict(Object entity) {
    this.getSession().evict(entity);
  }

  public int executeUpdateHql(String queryStr, Object[] argument) {
    Query query = this.getSession().createQuery(queryStr);
    return HibernateQuerySupport.setParameter(query, argument).executeUpdate();
  }

  public int executeUpdateHql(String queryStr, Map parameterMap) {
    Query query = this.getSession().createQuery(queryStr);
    return HibernateQuerySupport.setParameter(query, parameterMap).executeUpdate();
  }

  public int executeUpdateNamedQuery(String queryName, Map parameterMap) {
    Query query = this.getSession().getNamedQuery(queryName);
    return HibernateQuerySupport.setParameter(query, parameterMap).executeUpdate();
  }

  public int executeUpdateNamedQuery(String queryName, Object[] argument) {
    Query query = this.getSession().getNamedQuery(queryName);
    return HibernateQuerySupport.setParameter(query, argument).executeUpdate();
  }

  public Blob createBlob(InputStream inputStream, int length) {
    BufferedInputStream imageInput = new BufferedInputStream(inputStream);
    return Hibernate.createBlob(imageInput, length);
  }

  public Blob createBlob(InputStream inputStream) {
    BufferedInputStream imageInput = new BufferedInputStream(inputStream);

    try {
      return Hibernate.createBlob(imageInput);
    } catch (Exception var4) {
      throw new RuntimeException(var4.getMessage());
    }
  }

  public Clob createClob(String str) {
    return Hibernate.createClob(str);
  }

  public void refresh(Object entity) {
    this.getSession().refresh(entity);
  }

  public void initialize(Object entity) {
    this.getHibernateTemplate().initialize(entity);
  }

  public Page paginateQuery(Query query, Map params, PageLimit limit) {
    HibernateQuerySupport.setParameter(query, params);
    query.setFirstResult((limit.getPageNo() - 1) * limit.getPageSize()).setMaxResults(limit.getPageSize());
    List targetList = query.list();
    String queryStr = this.buildCountQueryStr(query);
    Query countQuery = null;
    if (query instanceof SQLQuery) {
      countQuery = this.getSession().createSQLQuery(queryStr);
    } else {
      countQuery = this.getSession().createQuery(queryStr);
    }

    HibernateQuerySupport.setParameter((Query) countQuery, params);
    return new SinglePage(limit.getPageNo(), limit.getPageSize(), ((Number) ((Query) countQuery).uniqueResult()).intValue(), targetList);
  }

  public void saveOrUpdate(Object entity) {
    if (entity instanceof Collection) {
      Collection entities = (Collection) entity;
      Iterator iter = entities.iterator();

      while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof HibernateProxy) {
          this.getHibernateTemplate().saveOrUpdate(obj);
        } else {
          this.getHibernateTemplate().saveOrUpdate(EntityUtils.getEntityType(obj.getClass()).getEntityName(), obj);
        }
      }
    } else if (entity instanceof HibernateProxy) {
      this.getHibernateTemplate().saveOrUpdate(entity);
    } else {
      this.getHibernateTemplate().saveOrUpdate(EntityUtils.getEntityType(entity.getClass()).getEntityName(), entity);
    }

  }

  public void saveOrUpdate(String entityName, Object entity) {
    if (entity instanceof Collection) {
      Collection entities = (Collection) entity;
      Iterator iter = entities.iterator();

      while (iter.hasNext()) {
        this.getHibernateTemplate().saveOrUpdate(entityName, iter.next());
      }
    } else {
      this.getHibernateTemplate().saveOrUpdate(entityName, entity);
    }

  }

  public void remove(Collection entities) {
    Iterator iter = entities.iterator();

    while (iter.hasNext()) {
      this.getHibernateTemplate().delete(iter.next());
    }

  }

  public void remove(Object entity) {
    this.getHibernateTemplate().delete(entity);
  }

  public boolean remove(Class clazz, String attr, Object[] values) {
    if (clazz != null && !StringUtils.isEmpty(attr) && values != null && values.length != 0) {
      String entityName = EntityUtils.getEntityType(clazz).getEntityName();
      StringBuffer hql = new StringBuffer();
      hql.append("delete from ").append(entityName).append(" where ").append(attr).append(" in (:ids)");
      Map parameterMap = new HashMap(1);
      parameterMap.put("ids", values);
      return this.executeUpdateHql(hql.toString(), (Map) parameterMap) > 0;
    } else {
      return false;
    }
  }

  public boolean remove(Class entityClass, String attr, Collection values) {
    return this.remove(entityClass, attr, values.toArray());
  }

  public boolean remove(Class clazz, Map keyMap) {
    if (clazz != null && keyMap != null && !keyMap.isEmpty()) {
      String entityName = EntityUtils.getEntityType(clazz).getEntityName();
      StringBuffer hql = new StringBuffer();
      hql.append("delete from ").append(entityName).append(" where ");
      Set keySet = keyMap.keySet();
      Map params = new HashMap();
      Iterator ite = keySet.iterator();

      while (true) {
        while (ite.hasNext()) {
          String keyName = ite.next().toString();
          Object keyValue = keyMap.get(keyName);
          String paramName = keyName.replace('.', '_');
          params.put(paramName, keyValue);
          if (!keyValue.getClass().isArray() && !(keyValue instanceof Collection)) {
            hql.append(keyName).append(" = :").append(paramName).append(" and ");
          } else {
            hql.append(keyName).append(" in (:").append(paramName).append(") and ");
          }
        }

        hql.append(" (1=1) ");
        return this.executeUpdateHql(hql.toString(), (Map) params) > 0;
      }
    } else {
      return false;
    }
  }

  private String buildCountQueryStr(Query query) {
    String queryStr = "select count(*) ";
    if (query instanceof SQLQuery) {
      queryStr = queryStr + "from (" + query.getQueryString() + ")";
    } else {
      String lowerCaseQueryStr = query.getQueryString().toLowerCase();
      String selectWhich = lowerCaseQueryStr.substring(0, query.getQueryString().indexOf("from"));
      int indexOfDistinct = selectWhich.indexOf("distinct");
      int indexOfFrom = lowerCaseQueryStr.indexOf("from");
      if (-1 != indexOfDistinct) {
        if (StringUtils.contains(selectWhich, ",")) {
          queryStr = "select count(" + query.getQueryString().substring(indexOfDistinct, query.getQueryString().indexOf(",")) + ")";
        } else {
          queryStr = "select count(" + query.getQueryString().substring(indexOfDistinct, indexOfFrom) + ")";
        }
      }

      queryStr = queryStr + query.getQueryString().substring(indexOfFrom);
    }

    return queryStr;
  }
}