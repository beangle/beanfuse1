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
import org.beanfuse.security.dao.UserDao;
import org.beanfuse.security.model.User;
import org.hibernate.Query;

import java.util.List;

public class UserDaoHibernate extends BaseDaoHibernate
    implements UserDao {

  public UserDaoHibernate() {
  }

  public User get(String name, String password) {
    Query query = getSession().getNamedQuery("userLogin");
    query.setParameter("name", name);
    query.setParameter("password", password);
    List userList = query.list();
    if (userList.size() > 0)
      return (User) userList.get(0);
    else
      return null;
  }

  public User get(Long userId) {
    return (User) utilDao.get("User", userId);
  }

  public List get(Long userIds[]) {
    String hql = "from User where id in(:ids)";
    Query query = getSession().createQuery(hql);
    query.setParameterList("ids", userIds);
    return query.list();
  }

  public void remove(User user) {
    utilDao.remove(user);
  }

  public void remove(Long userId) {
    utilDao.remove(get(userId));
  }

  public void saveOrUpdate(User user) {
    utilDao.saveOrUpdate(user);
  }

  public void remove(Long ids[]) {
    for (int i = 0; i < ids.length; i++)
      remove(ids[i]);

  }
}
