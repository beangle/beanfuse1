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
import org.beanfuse.security.Authority;
import org.beanfuse.security.Role;
import org.beanfuse.security.User;
import org.beanfuse.security.portal.dao.MenuAuthorityDao;
import org.beanfuse.security.portal.model.Menu;
import org.beanfuse.security.portal.model.MenuAuthority;
import org.beanfuse.security.portal.model.MenuProfile;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuAuthorityDaoHibernate extends HibernateDaoSupport
    implements MenuAuthorityDao {

  public MenuAuthorityDaoHibernate() {
  }

  public MenuAuthority getUserMenuAuthority(User user, Menu menu) {
    Map params = new HashMap();
    params.put("user", user);
    params.put("menu", menu);
    List authorityList = utilDao.searchNamedQuery("getUserMenu", params, false);
    if (authorityList.isEmpty())
      return null;
    else
      return (MenuAuthority) authorityList.get(0);
  }

  public List getUserMenuAuthorities(MenuProfile profile, User user, int depth, String ancestorCode) {
    StringBuffer hql = new StringBuffer(" select a  from UserMenuAuthority a join a.user as u");
    hql.append(" join a.menu as m where u = :user and m.enabled=true ");
    if (null != profile)
      hql.append(" and m.profile=:profile");
    if (StringUtils.isNotEmpty(ancestorCode))
      hql.append(" and m.code like :ancestorCode and length(m.code)>:ancestorCodeLength");
    if (depth > 0)
      hql.append(" and length(m.code)/2 <= :depth");
    Query query = getSession().createQuery(hql.toString());
    query.setParameter("user", user);
    if (StringUtils.isNotEmpty(ancestorCode)) {
      query.setParameter("ancestorCode", ancestorCode + "%");
      query.setParameter("ancestorCodeLength", new Long(ancestorCode.length()));
    }
    if (depth > 0)
      query.setParameter("depth", new Long(depth));
    if (null != profile)
      query.setParameter("profile", profile);
    return query.list();
  }

  public List getUserMenus(MenuProfile profile, User user, int depth, String ancestorCode) {
    StringBuffer hql = new StringBuffer(" select m  from UserMenuAuthority a join a.user as u");
    hql.append(" join a.menu as m where u = :user and m.enabled=true ");
    if (null != profile)
      hql.append(" and m.profile=:profile");
    if (StringUtils.isNotEmpty(ancestorCode))
      hql.append(" and m.code like :ancestorCode and length(m.code)>:ancestorCodeLength");
    if (depth > 0)
      hql.append(" and length(m.code)/2 <= :depth");
    Query query = getSession().createQuery(hql.toString());
    query.setParameter("user", user);
    if (StringUtils.isNotEmpty(ancestorCode)) {
      query.setParameter("ancestorCode", ancestorCode + "%");
      query.setParameter("ancestorCodeLength", new Long(ancestorCode.length()));
    }
    if (depth > 0)
      query.setParameter("depth", new Long(depth));
    if (null != profile)
      query.setParameter("profile", profile);
    return query.list();
  }

  public MenuAuthority getRoleMenuAuthority(Role role, Menu menu) {
    Map params = new HashMap();
    params.put("role", role);
    params.put("menu", menu);
    List authorityList = utilDao.searchNamedQuery("getRoleMenu", params, false);
    if (authorityList.isEmpty())
      return null;
    else
      return (MenuAuthority) authorityList.get(0);
  }

  public List getRoleMenuAuthorities(MenuProfile profile, Role role, int depth, String ancestorCode) {
    StringBuffer hql = new StringBuffer(" select a  from RoleMenuAuthority a join a.role as r");
    hql.append(" join a.menu as m where r = :role and m.enabled=true ");
    if (null != profile)
      hql.append(" and m.profile=:profile");
    if (StringUtils.isNotEmpty(ancestorCode))
      hql.append(" and m.code like :ancestorCode and length(m.code)>:ancestorCodeLength");
    if (depth > 0)
      hql.append(" and length(m.code)/2 <= :depth");
    Query query = getSession().createQuery(hql.toString());
    query.setParameter("role", role);
    if (StringUtils.isNotEmpty(ancestorCode)) {
      query.setParameter("ancestorCode", ancestorCode + "%");
      query.setParameter("ancestorCodeLength", new Long(ancestorCode.length()));
    }
    if (depth > 0)
      query.setParameter("depth", new Long(depth));
    if (null != profile)
      query.setParameter("profile", profile);
    return query.list();
  }

  public List getRoleMenus(MenuProfile profile, Role role, int depth, String ancestorCode) {
    StringBuffer hql = new StringBuffer(" select m from RoleMenuAuthority a join a.role as r");
    hql.append(" join a.menu as m where r = :role and m.enabled=true ");
    if (null != profile)
      hql.append(" and m.profile=:profile");
    if (StringUtils.isNotEmpty(ancestorCode))
      hql.append(" and m.code like :ancestorCode and length(m.code)>:ancestorCodeLength");
    if (depth > 0)
      hql.append(" and length(m.code)/2 <= :depth");
    Query query = getSession().createQuery(hql.toString());
    query.setParameter("role", role);
    if (StringUtils.isNotEmpty(ancestorCode)) {
      query.setParameter("ancestorCode", ancestorCode + "%");
      query.setParameter("ancestorCodeLength", new Long(ancestorCode.length()));
    }
    if (depth > 0)
      query.setParameter("depth", new Long(depth));
    if (null != profile)
      query.setParameter("profile", profile);
    query.setCacheable(true);
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
