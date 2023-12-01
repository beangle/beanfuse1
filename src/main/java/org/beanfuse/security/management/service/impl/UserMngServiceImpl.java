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
import org.beanfuse.security.management.ManagedUser;
import org.beanfuse.security.management.UserManager;
import org.beanfuse.security.management.service.UserMngService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UserMngServiceImpl extends BaseServiceImpl
    implements UserMngService {

  public UserMngServiceImpl() {
  }

  public void create(UserManager creator, ManagedUser newUser) {
    newUser.setCreator(creator);
    newUser.setModifyAt(new Date(System.currentTimeMillis()));
    newUser.setCreateAt(new Date(System.currentTimeMillis()));
    creator.getMngUsers().add(newUser);
    List batchSaved = new ArrayList();
    batchSaved.add(newUser);
    batchSaved.add(creator);
    utilDao.saveOrUpdate(batchSaved);
  }

  public void remove(UserManager manager, ManagedUser user) {
    manager.getMngUsers().remove(user);
    utilService.saveOrUpdate(manager);
    if (manager.isUserAdmin() || user.getCreator().equals(manager))
      removeUser(user);
  }

  protected void removeUser(ManagedUser user) {
    if (user instanceof UserManager) {
      Set mngUsers = ((UserManager) user).getMngUsers();
      Iterator iter = mngUsers.iterator();
      do {
        if (!iter.hasNext())
          break;
        ManagedUser mngUser = (ManagedUser) iter.next();
        if (mngUser.getCreator().equals(user))
          removeUser(mngUser);
      } while (true);
    }
    utilService.remove(user);
  }
}
