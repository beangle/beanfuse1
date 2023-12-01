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
package org.beanfuse.security.dao.hibernate;

import org.beanfuse.commons.utils.persistence.UtilDao;
import org.beanfuse.security.Authority;
import org.beanfuse.security.dao.AuthorityDao;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorityDaoHibernate extends HibernateDaoSupport
    implements AuthorityDao {

  public AuthorityDaoHibernate() {
  }

  public Authority getUserAuthority(Long userId, Long resourceId) {
    Map params = new HashMap();
    params.put("userId", userId);
    params.put("resourceId", resourceId);
    List authorityList = utilDao.searchNamedQuery("getUserAuthorityByResourceId", params, false);
    if (authorityList.isEmpty())
      return null;
    else
      return (Authority) authorityList.get(0);
  }

  public List getUserAuthorities(Long userId) {
    Map params = new HashMap();
    params.put("userId", userId);
    return utilDao.searchNamedQuery("getUserAuthorities", params, false);
  }

  public List getUserResourceNames(Long userId) {
    String hql = "select r.name from  User as u  join u.authorities as a join a.resource as r  where  u.id = :userId and r.enabled=true";
    Query query = getSession().createQuery(hql);
    query.setParameter("userId", userId);
    return query.list();
  }

  public Authority getRoleAuthority(Long roleId, Long resourceId) {
    Map params = new HashMap();
    params.put("roleId", roleId);
    params.put("resourceId", resourceId);
    List authorityList = utilDao.searchNamedQuery("getRoleAuthorityByResourceId", params, false);
    if (authorityList.isEmpty())
      return null;
    else
      return (Authority) authorityList.get(0);
  }

  public List getRoleAuthorities(Long roleId) {
    Map params = new HashMap();
    params.put("roleId", roleId);
    return utilDao.searchNamedQuery("getRoleAuthorities", params, false);
  }

  public List getRoleResourceNames(Long roleId) {
    String hql = "select distinct m.name from Role as r join r.authorities as a join a.resource as m where  r.id = :roleId and  m.enabled=true";
    Query query = getSession().createQuery(hql);
    query.setParameter("roleId", roleId);
    query.setCacheable(true);
    return query.list();
  }

  public List getCategoryResources(Long categoryId) {
    String hql = "select  r  from Resource as r join r.categories as c c.id = :categoryId and r.enabled=true";
    Query query = getSession().createQuery(hql);
    query.setParameter("categoryId", categoryId);
    return query.list();
  }

  public void saveOrUpdate(Authority authority) {
    utilDao.saveOrUpdate(authority);
  }

  public void remove(Authority authority) {
    utilDao.remove(authority);
  }

  public void setUtilDao(UtilDao utilDao) {
    this.utilDao = utilDao;
  }

  private UtilDao utilDao;
}
