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

import org.beanfuse.commons.utils.persistence.hibernate.BaseDaoHibernate;
import org.beanfuse.security.model.Role;
import org.beanfuse.security.dao.RoleDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class RoleDaoHibernate extends BaseDaoHibernate
    implements RoleDao {

  public RoleDaoHibernate() {
  }

  public void remove(Role role) {
    utilDao.remove(role);
  }

  public void remove(Long roleId) {
    Role role = (Role) utilDao.get(Role.class, roleId);
    utilDao.remove(role);
  }

  public List get(Long userIds[]) {
    Criteria criteria = getSession().createCriteria(Role.class);
    criteria.add(Restrictions.in("id", userIds));
    return criteria.list();
  }

  public Role get(Long roleId) {
    return (Role) utilDao.get("Role", roleId);
  }

  public void saveOrUpdate(Role role) {
    utilDao.saveOrUpdate(role);
  }
}
