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
package org.beanfuse.commons.utils.persistence.impl;

import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.model.LongIdEntity;
import org.beanfuse.commons.query.AbstractQuery;
import org.beanfuse.commons.query.limit.Page;
import org.beanfuse.commons.query.limit.PageLimit;
import org.beanfuse.commons.utils.persistence.UtilDao;
import org.beanfuse.commons.utils.persistence.UtilService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;

import java.io.Serializable;
import java.util.*;

public class UtilServiceImpl
    implements UtilService {

  public UtilServiceImpl() {
  }

  public Entity load(Class clazz, Serializable id) {
    return utilDao.load(clazz, id);
  }

  public List loadAll(Class entity) {
    return utilDao.loadAll(entity);
  }

  public Entity get(Class clazz, Serializable id) {
    return utilDao.get(clazz, id);
  }

  public Entity get(String entityName, Serializable id) {
    return utilDao.get(entityName, id);
  }

  public List load(Class entity, String attr, Collection values) {
    return utilDao.load(entity, attr, values.toArray());
  }

  public List load(Class entity, String attr, Object value) {
    return utilDao.load(entity, attr, new Object[]{
        value
    });
  }

  public List load(Class entity, String attr, Object values[]) {
    return utilDao.load(entity, attr, values);
  }

  public List load(String entityName, String attr, Object values[]) {
    return utilDao.load(entityName, attr, values);
  }

  public List load(Class entity, String attrs[], Object values[]) {
    Map params = new HashMap();
    for (int i = 0; i < attrs.length; i++)
      params.put(attrs[i], values[i]);

    return load(entity, params);
  }

  public List load(Class entity, Map parameterMap) {
    if (entity == null || parameterMap == null || parameterMap.isEmpty())
      return Collections.EMPTY_LIST;
    String entityName = entity.getName();
    StringBuffer hql = new StringBuffer();
    hql.append("select entity from ").append(entityName).append(" as entity ").append(" where ");
    Set keySet = parameterMap.keySet();
    Map m = new HashMap(keySet.size());
    int i = 0;
    for (Iterator ite = keySet.iterator(); ite.hasNext(); ) {
      String keyName = (String) ite.next();
      if (StringUtils.isEmpty(keyName))
        return null;
      i++;
      Object keyValue = parameterMap.get(keyName);
      String tempName[] = StringUtils.split(keyName, "\\.");
      String name = tempName[tempName.length - 1] + i;
      m.put(name, keyValue);
      if (keyValue != null && (keyValue.getClass().isArray() || (keyValue instanceof Collection)))
        hql.append("entity.").append(keyName).append(" in (:").append(name).append(") and ");
      else
        hql.append("entity.").append(keyName).append(" = :").append(name).append(" and ");
    }

    hql.append(" (1=1) ");
    return utilDao.searchHQLQuery(hql.toString(), m);
  }

  public Collection search(AbstractQuery query) {
    return utilDao.search(query);
  }

  public Page paginateCriteria(Criteria criteria, PageLimit limit) {
    return utilDao.paginateCriteria(criteria, limit);
  }

  public Page paginateHQLQuery(String hql, Map params, PageLimit limit) {
    return utilDao.paginateHQLQuery(hql, params, limit);
  }

  public Page paginateNamedQuery(String queryName, Map params, PageLimit limit) {
    return utilDao.paginateNamedQuery(queryName, params, limit);
  }

  public Page paginateQuery(Query query, Map params, PageLimit limit) {
    return utilDao.paginateQuery(query, params, limit);
  }

  public List searchHQLQuery(String hql, Map params, boolean cacheable) {
    return utilDao.searchHQLQuery(hql, params, cacheable);
  }

  public List searchHQLQuery(String hql, Map params) {
    return utilDao.searchHQLQuery(hql, params);
  }

  public List searchHQLQuery(String hql, Object params[]) {
    return utilDao.searchHQLQuery(hql, params);
  }

  public List searchHQLQuery(String hql) {
    return utilDao.searchHQLQuery(hql);
  }

  public List searchNamedQuery(String queryName, Map params, boolean cacheable) {
    return utilDao.searchNamedQuery(queryName, params, cacheable);
  }

  public List searchNamedQuery(String queryName, Map params) {
    return utilDao.searchNamedQuery(queryName, params);
  }

  public List searchNamedQuery(String queryName, Object params[]) {
    return utilDao.searchNamedQuery(queryName, params);
  }

  public int update(Class entityClass, String attr, Object values[], String argumentName[], Object argumentValue[]) {
    if (null == values || values.length == 0)
      return 0;
    Map updateParams = new HashMap();
    for (int i = 0; i < argumentValue.length; i++)
      updateParams.put(argumentName[i], argumentValue[i]);

    return update(entityClass, attr, values, updateParams);
  }

  public int update(Class entityClass, String attr, Object values[], Map updateParams) {
    if (null == values || values.length == 0 || updateParams.isEmpty())
      return 0;
    String entityName = entityClass.getName();
    StringBuffer hql = new StringBuffer();
    hql.append("update ").append(entityName).append(" set ");
    Set parameterNameSet = updateParams.keySet();
    Iterator ite = parameterNameSet.iterator();
    do {
      if (!ite.hasNext())
        break;
      String parameterName = (String) ite.next();
      if (null != parameterName)
        hql.append(parameterName).append(" = ").append(":").append(StringUtils.replaceChars(parameterName, '.', '_')).append(",");
    } while (true);
    hql.deleteCharAt(hql.length() - 1);
    hql.append(" where ").append(attr).append(" in (:ids)");
    updateParams.put("ids", ((Object) (values)));
    return utilDao.executeUpdateHql(hql.toString(), updateParams);
  }

  public void remove(Object entity) {
    utilDao.remove(entity);
  }

  public void remove(Collection entities) {
    if (null != entities && !entities.isEmpty())
      utilDao.remove(entities);
  }

  public boolean remove(Class entityClass, Map parameterMap) {
    return utilDao.remove(entityClass, parameterMap);
  }

  public boolean remove(Class entityClass, String attr, Collection values) {
    return utilDao.remove(entityClass, attr, values);
  }

  public boolean remove(Class entityClass, String attr, Object values[]) {
    return utilDao.remove(entityClass, attr, values);
  }

  public void saveOrUpdate(Collection entities) {
    if (null != entities && !entities.isEmpty())
      utilDao.saveOrUpdate(entities);
  }

  public void saveOrUpdate(Object entity) {
    if (entity != null)
      utilDao.saveOrUpdate(entity);
  }

  public void saveOrUpdate(String entityName, Collection entities) {
    utilDao.saveOrUpdate(entityName, entities);
  }

  public void saveOrUpdate(String entityName, Object entity) {
    utilDao.saveOrUpdate(entityName, entity);
  }

  public int count(String entityName, String keyName, Object value) {
    String hql = "select count(*) from " + entityName + " where " + keyName + "=:value";
    Map params = new HashMap();
    params.put("value", value);
    List rs = utilDao.searchHQLQuery(hql, params);
    if (rs.isEmpty())
      return 0;
    else
      return ((Number) rs.get(0)).intValue();
  }

  public int count(Class entityClass, String keyName, Object value) {
    return count(entityClass.getName(), keyName, value);
  }

  public int count(Class entityClass, String attrs[], Object values[], String countAttr) {
    String entityName = entityClass.getName();
    StringBuffer hql = new StringBuffer();
    if (StringUtils.isNotEmpty(countAttr))
      hql.append("select count(distinct ").append(countAttr).append(") from ");
    else
      hql.append("select count(*) from ");
    hql.append(entityName).append(" as entity where ");
    Map params = new HashMap();
    for (int i = 0; i < attrs.length; i++) {
      String keyName = attrs[i];
      if (StringUtils.isEmpty(keyName))
        continue;
      Object keyValue = values[i];
      params.put(keyName, keyValue);
      String tempName[] = StringUtils.split(attrs[i], "\\.");
      attrs[i] = tempName[tempName.length - 1] + i;
      if (keyValue != null && (keyValue.getClass().isArray() || (keyValue instanceof Collection)))
        hql.append("entity.").append(keyName).append(" in (:").append(attrs[i]).append(") and ");
      else
        hql.append("entity.").append(keyName).append(" = :").append(attrs[i]).append(" and ");
    }

    hql.append(" (1=1) ");
    return ((Number) utilDao.searchHQLQuery(hql.toString(), params).get(0)).intValue();
  }

  public boolean exist(Class entityClass, String attr, Object value) {
    return count(entityClass, attr, value) > 0;
  }

  public boolean exist(String entityName, String attr, Object value) {
    return count(entityName, attr, value) > 0;
  }

  public boolean exist(Class entity, String attrs[], Object values[]) {
    return count(entity, attrs, values, null) > 0;
  }

  public boolean duplicate(Class clazz, Long id, String codeName, Object codeValue) {
    if (null != codeValue && StringUtils.isNotEmpty(codeValue.toString())) {
      List list = utilDao.load(clazz, codeName, new Object[]{
          codeValue
      });
      if (list != null && list.size() != 0) {
        if (id == null)
          return true;
        for (Iterator it = list.iterator(); it.hasNext(); ) {
          Entity info = (Entity) it.next();
          if (!info.getEntityId().equals(id))
            return true;
        }

        return false;
      }
    }
    return false;
  }

  public void initialize(Object entity) {
    utilDao.initialize(entity);
  }

  public void refresh(Object entity) {
    utilDao.refresh(entity);
  }

  public void evict(Object entity) {
    utilDao.evict(entity);
  }

  public void setUtilDao(UtilDao utilDAO) {
    utilDao = utilDAO;
  }

  public UtilDao getUtilDao() {
    return utilDao;
  }

  public boolean duplicate(String entityName, Long id, Map params) {
    label0:
    {
      StringBuffer b = new StringBuffer("from ");
      b.append(entityName).append(" where (1=1)");
      Map paramsMap = new HashMap();
      int i = 0;
      for (Iterator iterator = params.keySet().iterator(); iterator.hasNext(); ) {
        String key = (String) iterator.next();
        b.append(" and ").append(key).append('=').append(":param" + i);
        paramsMap.put("param" + i, params.get(key));
        i++;
      }

      List list = utilDao.searchHQLQuery(b.toString(), paramsMap);
      if (list.isEmpty())
        break label0;
      if (null == id)
        return false;
      Iterator iter = list.iterator();
      LongIdEntity one;
      do {
        if (!iter.hasNext())
          break label0;
        one = (LongIdEntity) iter.next();
      } while (one.getId().equals(id));
      return false;
    }
    return true;
  }

  private UtilDao utilDao;
}
