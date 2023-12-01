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
package org.beanfuse.security.portal.service;

import org.beanfuse.security.AuthorityObject;
import org.beanfuse.security.Role;
import org.beanfuse.security.User;
import org.beanfuse.security.portal.dao.MenuAuthorityDao;
import org.beanfuse.security.portal.model.Menu;
import org.beanfuse.security.portal.model.MenuAuthority;
import org.beanfuse.security.portal.model.MenuProfile;
import org.beanfuse.security.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface MenuAuthorityService {

  public abstract MenuAuthority getUserMenuAuthority(User user, Menu menu);

  public abstract List getMenuAuthorities(MenuProfile menuprofile, User user);

  public abstract List getMenuAuthorities(MenuProfile menuprofile, User user, int i, String s);

  public abstract List getUserMenuAuthorities(MenuProfile menuprofile, User user);

  public abstract List getUserMenuAuthorities(MenuProfile menuprofile, User user, int i, String s);

  public abstract MenuAuthority getRoleMenuAuthority(Role role, Menu menu);

  public abstract List getRoleMenuAuthorities(MenuProfile menuprofile, Role role, int i, String s);

  public abstract List getRoleMenuAuthorities(MenuProfile menuprofile, Role role, int i);

  public abstract List getRoleMenuAuthorities(MenuProfile menuprofile, Role role);

  public abstract List getMenus(MenuProfile menuprofile, User user);

  public abstract List getMenus(MenuProfile menuprofile, User user, int i, String s);

  public abstract List getUserMenus(MenuProfile menuprofile, User user);

  public abstract List getRoleMenus(MenuProfile menuprofile, Role role);

  public abstract void saveOrUpdate(MenuAuthority menuauthority);

  public abstract void authorize(AuthorityObject authorityobject, Set set, Class class1);

  public abstract void copyAuthority(MenuProfile menuprofile, Role role, Collection collection);

  public abstract void setMenuAuthorityDao(MenuAuthorityDao menuauthoritydao);

  public abstract void setUserService(UserService userservice);
}
