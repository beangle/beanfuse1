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
package org.beanfuse.commons.query.hibernate;

import org.beanfuse.commons.query.AbstractQuery;
import org.beanfuse.commons.query.Condition;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.query.limit.PageLimit;
import org.beanfuse.commons.query.limit.SinglePage;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

public final class HibernateQuerySupport {

  private HibernateQuerySupport() {
  }

  public static void bindValues(Query query, List conditions) {
    int position = 0;
    boolean hasInterrogation = false;
    for (Iterator iter = conditions.iterator(); iter.hasNext(); ) {
      Condition condition = (Condition) iter.next();
      if (StringUtils.contains(condition.getContent(), "?"))
        hasInterrogation = true;
      if (hasInterrogation) {
        Iterator iterator = condition.getValues().iterator();
        while (iterator.hasNext())
          query.setParameter(position++, iterator.next());
      } else {
        List paramNames = condition.getNamedParams();
        int i = 0;
        while (i < paramNames.size()) {
          String name = (String) paramNames.get(i);
          Object value = condition.getValues().get(i);
          if (value.getClass().isArray())
            query.setParameterList(name, (Object[]) (Object[]) value);
          else if (value instanceof Collection)
            query.setParameterList(name, (Collection) value);
          else
            query.setParameter(name, value);
          i++;
        }
      }
    }

  }

  public static int count(AbstractQuery query, Session hibernateSession) {
    String countQueryStr = query.toCountString();
    if (StringUtils.isEmpty(countQueryStr)) {
      Query hibernateQuery = null;
      if (query instanceof EntityQuery)
        hibernateQuery = hibernateSession.createQuery(query.toQueryString());
      else
        hibernateQuery = hibernateSession.createSQLQuery(query.toQueryString());
      if (query.isCacheable())
        hibernateQuery.setCacheable(query.isCacheable());
      setParameter(hibernateQuery, query.getParams());
      return hibernateQuery.list().size();
    }
    Query countQuery = null;
    if (query instanceof EntityQuery)
      countQuery = hibernateSession.createQuery(countQueryStr);
    else
      countQuery = hibernateSession.createSQLQuery(countQueryStr);
    if (query.isCacheable())
      countQuery.setCacheable(query.isCacheable());
    setParameter(countQuery, query.getParams());
    Number count = (Number) (Number) countQuery.uniqueResult();
    if (null == count)
      return 0;
    else
      return count.intValue();
  }

  public static List find(AbstractQuery query, Session hibernateSession) {
    Query hibernateQuery = null;
    if (query instanceof EntityQuery)
      hibernateQuery = hibernateSession.createQuery(query.toQueryString());
    else
      hibernateQuery = hibernateSession.createSQLQuery(query.toQueryString());
    if (query.isCacheable())
      hibernateQuery.setCacheable(query.isCacheable());
    setParameter(hibernateQuery, query.getParams());
    if (null == query.getLimit()) {
      return hibernateQuery.list();
    } else {
      PageLimit limit = query.getLimit();
      hibernateQuery.setFirstResult((limit.getPageNo() - 1) * limit.getPageSize()).setMaxResults(limit.getPageSize());
      return hibernateQuery.list();
    }
  }

  public static Collection search(AbstractQuery query, Session hibernateSession) {
    if (null == query.getLimit())
      return find(query, hibernateSession);
    else
      return new SinglePage(query.getLimit().getPageNo(), query.getLimit().getPageSize(), count(query, hibernateSession), find(query, hibernateSession));
  }

  public static Object uniqueResult(AbstractQuery query, Session hibernateSession)
      throws HibernateException {
    return uniqueElement(find(query, hibernateSession));
  }

  static Object uniqueElement(List list)
      throws NonUniqueResultException {
    int size = list.size();
    if (size == 0)
      return null;
    Object first = list.get(0);
    for (int i = 1; i < size; i++)
      if (list.get(i) != first)
        throw new NonUniqueResultException(list.size());

    return first;
  }

  public static Query setParameter(Query query, Object argument[]) {
    if (argument != null && argument.length > 0) {
      for (int i = 0; i < argument.length; i++)
        query.setParameter(i, argument[i]);

    }
    return query;
  }

  public static Query setParameter(Query query, Map parameterMap) {
    if (parameterMap != null && !parameterMap.isEmpty()) {
      Set parameterNameSet = parameterMap.keySet();
      Iterator ite = parameterNameSet.iterator();
      do {
        if (!ite.hasNext())
          break;
        String parameterName = (String) ite.next();
        if (null == parameterName)
          break;
        Object parameterValue = parameterMap.get(parameterName);
        if (null == parameterValue)
          query.setParameter(parameterName, (Object) null);
        else if (parameterValue.getClass().isArray())
          query.setParameterList(parameterName, (Object[]) (Object[]) parameterValue);
        else if (parameterValue instanceof Collection)
          query.setParameterList(parameterName, (Collection) parameterValue);
        else
          query.setParameter(parameterName, parameterValue);
      } while (true);
    }
    return query;
  }
}
