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
package org.beanfuse.security.service.impl;

import org.beanfuse.commons.model.pojo.PojoExistException;
import org.beanfuse.commons.utils.persistence.impl.BaseServiceImpl;
import org.beanfuse.security.model.Role;
import org.beanfuse.security.dao.RoleDao;
import org.beanfuse.security.service.RoleService;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

public class RoleServiceImpl extends BaseServiceImpl
    implements RoleService {

  public RoleServiceImpl() {
  }

  public Role get(Long roleId) {
    return roleDao.get(roleId);
  }

  public List get(Long roleIds[]) {
    if (null == roleIds || roleIds.length < 1)
      return Collections.EMPTY_LIST;
    else
      return roleDao.get(roleIds);
  }

  public void saveOrUpdate(Role role)
      throws PojoExistException {
    role.setModifyAt(new Date(System.currentTimeMillis()));
    if (role.isVO())
      role.setCreateAt(new Date(System.currentTimeMillis()));
    roleDao.saveOrUpdate(role);
  }

  public void setRoleDao(RoleDao roleDao) {
    this.roleDao = roleDao;
  }

  private RoleDao roleDao;
}
