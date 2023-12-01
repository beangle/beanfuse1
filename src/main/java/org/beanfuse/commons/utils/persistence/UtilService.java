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
package org.beanfuse.commons.utils.persistence;

import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.query.AbstractQuery;
import org.beanfuse.commons.query.limit.Page;
import org.beanfuse.commons.query.limit.PageLimit;
import org.hibernate.Criteria;
import org.hibernate.Query;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

// Referenced classes of package org.beanfuse.commons.utils.persistence:
//            UtilDao

public interface UtilService {

  public abstract Entity load(Class class1, Serializable serializable);

  public abstract List load(Class class1, String s, Collection collection);

  public abstract List load(Class class1, String s, Object aobj[]);

  public abstract List load(String s, String s1, Object aobj[]);

  public abstract List load(Class class1, String s, Object obj);

  public abstract List load(Class class1, String as[], Object aobj[]);

  public abstract List load(Class class1, Map map);

  public abstract List loadAll(Class class1);

  public abstract Entity get(Class class1, Serializable serializable);

  public abstract Entity get(String s, Serializable serializable);

  public abstract Collection search(AbstractQuery abstractquery);

  public abstract List searchNamedQuery(String s, Map map);

  public abstract List searchNamedQuery(String s, Object aobj[]);

  public abstract List searchNamedQuery(String s, Map map, boolean flag);

  public abstract List searchHQLQuery(String s);

  public abstract List searchHQLQuery(String s, Map map);

  public abstract List searchHQLQuery(String s, Object aobj[]);

  public abstract List searchHQLQuery(String s, Map map, boolean flag);

  public abstract Page paginateNamedQuery(String s, Map map, PageLimit pagelimit);

  public abstract Page paginateHQLQuery(String s, Map map, PageLimit pagelimit);

  public abstract Page paginateCriteria(Criteria criteria, PageLimit pagelimit);

  public abstract Page paginateQuery(Query query, Map map, PageLimit pagelimit);

  public abstract void remove(Object obj);

  public abstract void remove(Collection collection);

  public abstract boolean remove(Class class1, String s, Object aobj[]);

  public abstract boolean remove(Class class1, String s, Collection collection);

  public abstract boolean remove(Class class1, Map map);

  public abstract int update(Class class1, String s, Object aobj[], Map map);

  public abstract int update(Class class1, String s, Object aobj[], String as[], Object aobj1[]);

  public abstract void saveOrUpdate(Object obj);

  public abstract void saveOrUpdate(String s, Object obj);

  public abstract void evict(Object obj);

  public abstract void refresh(Object obj);

  public abstract void initialize(Object obj);

  public abstract boolean exist(Class class1, String s, Object obj);

  public abstract boolean exist(String s, String s1, Object obj);

  public abstract boolean exist(Class class1, String as[], Object aobj[]);

  public abstract int count(Class class1, String s, Object obj);

  public abstract int count(String s, String s1, Object obj);

  public abstract int count(Class class1, String as[], Object aobj[], String s);

  public abstract boolean duplicate(Class class1, Long long1, String s, Object obj);

  public abstract boolean duplicate(String s, Long long1, Map map);

  public abstract void setUtilDao(UtilDao utildao);

  public abstract UtilDao getUtilDao();
}
