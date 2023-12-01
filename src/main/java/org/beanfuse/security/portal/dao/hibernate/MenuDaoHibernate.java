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
package org.beanfuse.security.portal.dao.hibernate;

import org.beanfuse.commons.utils.persistence.UtilDao;
import org.beanfuse.commons.utils.persistence.hibernate.BaseDaoHibernate;
import org.beanfuse.commons.utils.persistence.hibernate.CriterionUtils;
import org.beanfuse.security.portal.dao.MenuDao;
import org.beanfuse.security.portal.model.Menu;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MenuDaoHibernate extends BaseDaoHibernate
    implements MenuDao {

  public MenuDaoHibernate() {
  }

  public Menu get(Long menuId) {
    return (Menu) utilDao.get("Menu", menuId);
  }

  public Menu getByName(String name) {
    Map params = new HashMap();
    params.put("name", name);
    List rs = utilDao.searchHQLQuery("from Menu as menu where menu.name=:name", params, true);
    if (rs.isEmpty())
      return null;
    else
      return (Menu) rs.get(0);
  }

  public Menu getByCode(String code) {
    Map params = new HashMap();
    params.put("code", code);
    List rs = utilDao.searchHQLQuery("from Menu as menu where menu.code=:code", params, true);
    if (rs.isEmpty())
      return null;
    else
      return (Menu) rs.get(0);
  }

  public List get(Menu menu) {
    Criteria criteria = getSession().createCriteria(org.beanfuse.security.portal.model.Menu.class);
    List criterions = CriterionUtils.getEntityCriterions(menu);
    Criterion one;
    for (Iterator iterator = criterions.iterator(); iterator.hasNext(); criteria.add(one))
      one = (Criterion) iterator.next();

    return criteria.list();
  }

  public List get(Long menuIds[]) {
    Criteria criteria = getSession().createCriteria(org.beanfuse.security.portal.model.Menu.class);
    criteria.add(Restrictions.in("id", menuIds));
    return criteria.list();
  }

  public List getDescendants(String ancestorCode, int depth) {
    return getDescendants(ancestorCode, depth, null);
  }

  public List getActiveDescendants(String ancestorCode, int depth) {
    return getDescendants(ancestorCode, depth, Boolean.TRUE);
  }

  private List getDescendants(String ancestorCode, int depth, Boolean isEnabled) {
    String hql = "select  a from  Menu as a where\t\ta.code like :ancestorCode and\t\tlength(a.code) > :ancestorCodeLength and\t\t(:depth=-1 or length(a.code)/2 <= :depth)";
    if (null != isEnabled)
      hql = hql + " and a.enabled=" + isEnabled;
    hql = hql + " order by a.code";
    Query query = getSession().createQuery(hql);
    query.setParameter("ancestorCode", ancestorCode + "%");
    query.setParameter("depth", new Integer(depth));
    query.setParameter("ancestorCodeLength", new Long(ancestorCode.length() + 1));
    query.setCacheable(true);
    return query.list();
  }

  public List getActiveChildren(String parentCode) {
    return getChildren(parentCode, Boolean.TRUE);
  }

  public List getChildren(String parentCode) {
    return getChildren(parentCode, null);
  }

  private List getChildren(String parentCode, Boolean isEnabled) {
    String hql = "select from  Menu as a where a.code like :parentCode\t\tand length(a.code) - parentCodeLength<=2    \tand length(a.code) >parentCodeLength";
    if (null != isEnabled)
      hql = hql + " and a.enabled=" + isEnabled;
    hql = hql + " order by a.code";
    Query query = getSession().createQuery(hql);
    query.setParameter("parentCode", parentCode + "%");
    query.setParameter("parentCodeLength", new Integer(parentCode.length() + 1));
    query.setCacheable(true);
    return query.list();
  }

  public void saveOrUpdate(Menu menu) {
    utilDao.saveOrUpdate(menu);
  }

  public void setUtilDao(UtilDao utilDao) {
    this.utilDao = utilDao;
  }
}
