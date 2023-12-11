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
import org.beanfuse.commons.query.Condition;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.utils.persistence.impl.BaseServiceImpl;
import org.beanfuse.security.model.Role;
import org.beanfuse.security.model.User;
import org.beanfuse.security.dao.UserDao;
import org.beanfuse.security.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UserServiceImpl extends BaseServiceImpl
    implements UserService {

  public UserServiceImpl() {
  }

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  public User get(String name, String password) {
    return userDao.get(name, password);
  }

  public User get(Long id) {
    return userDao.get(id);
  }

  public User get(String loginName) {
    if (StringUtils.isEmpty(loginName))
      return null;
    EntityQuery entityQuery = new EntityQuery(User.class, "user");
    entityQuery.add(new Condition("user.name=:name", loginName));
    Collection users = utilService.search(entityQuery);
    if (users.isEmpty())
      return null;
    else
      return (User) users.iterator().next();
  }

  public List getUsers(Long userIds[]) {
    return userDao.get(userIds);
  }

  public void updateState(Long ids[], Integer state) {
    if (null == ids || ids.length < 1)
      return;
    List users = userDao.get(ids);
    for (int i = 0; i < users.size(); i++) {
      User cur = (User) users.get(i);
      cur.setState(state);
    }

    utilService.saveOrUpdate(users);
  }

  public void saveOrUpdate(User user)
      throws PojoExistException {
    try {
      user.setModifyAt(new Date(System.currentTimeMillis()));
      if (user.isVO())
        user.setCreateAt(new Date(System.currentTimeMillis()));
      utilService.saveOrUpdate(user);
    } catch (DataIntegrityViolationException e) {
      throw new PojoExistException("User already exits:" + user);
    } catch (Exception e) {
      throw new PojoExistException("User already exits:" + user);
    }
  }

  public Long[] getRoleIds(User user) {
    Set roles = getRoles(user);
    Long roleIds[] = new Long[roles.size()];
    int roleIndex = 0;
    for (Iterator iterator = roles.iterator(); iterator.hasNext(); ) {
      Role role = (Role) iterator.next();
      roleIds[roleIndex] = role.getId();
      roleIndex++;
    }

    return roleIds;
  }

  public Set getRoles(User user) {
    return user.getRoles();
  }

  private UserDao userDao;
}
