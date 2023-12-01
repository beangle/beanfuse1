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
package org.beanfuse.security.management.service.impl;

import org.beanfuse.commons.utils.persistence.impl.BaseServiceImpl;
import org.beanfuse.security.Role;
import org.beanfuse.security.management.ManagedRole;
import org.beanfuse.security.management.RoleManager;
import org.beanfuse.security.management.service.RoleMngService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoleMngServiceImpl extends BaseServiceImpl
    implements RoleMngService {

  public RoleMngServiceImpl() {
  }

  public void create(RoleManager creator, ManagedRole newRole) {
    newRole.setModifyAt(new Date(System.currentTimeMillis()));
    newRole.setCreateAt(new Date(System.currentTimeMillis()));
    newRole.setCreator(creator);
    creator.getMngRoles().add(newRole);
    Role admin = (Role) utilDao.get(org.beanfuse.security.Role.class, Role.ADMIN_ID);
    RoleManager oneAdmin;
    for (Iterator iter = admin.getUsers().iterator(); iter.hasNext(); oneAdmin.getMngRoles().add(newRole))
      oneAdmin = (RoleManager) iter.next();

    utilDao.saveOrUpdate(newRole);
    List batchSaved = new ArrayList();
    batchSaved.add(creator);
    batchSaved.addAll(admin.getUsers());
    utilDao.saveOrUpdate(batchSaved);
  }

  public void remove(RoleManager manager, List roles) {
    Iterator iterator = roles.iterator();
    do {
      if (!iterator.hasNext())
        break;
      ManagedRole role = (ManagedRole) iterator.next();
      if (!role.getId().equals(Role.ADMIN_ID)) {
        if (manager.getMngRoles().contains(role))
          manager.getMngRoles().remove(role);
        if (role.getCreator().equals(manager) || manager.isRoleAdmin())
          utilService.remove(role);
      }
    } while (true);
    utilService.saveOrUpdate(manager);
  }
}
