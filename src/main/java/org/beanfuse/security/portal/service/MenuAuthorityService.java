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
import org.beanfuse.security.model.Role;
import org.beanfuse.security.model.User;
import org.beanfuse.security.portal.dao.MenuAuthorityDao;
import org.beanfuse.security.portal.model.Menu;
import org.beanfuse.security.portal.model.MenuAuthority;
import org.beanfuse.security.portal.model.MenuProfile;
import org.beanfuse.security.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface MenuAuthorityService {

  MenuAuthority getUserMenuAuthority(User user, Menu menu);

  List getMenuAuthorities(MenuProfile menuprofile, User user);

  List getMenuAuthorities(MenuProfile menuprofile, User user, int i, String s);

  List getUserMenuAuthorities(MenuProfile menuprofile, User user);

  List getUserMenuAuthorities(MenuProfile menuprofile, User user, int i, String s);

  MenuAuthority getRoleMenuAuthority(Role role, Menu menu);

  List getRoleMenuAuthorities(MenuProfile menuprofile, Role role, int i, String s);

  List getRoleMenuAuthorities(MenuProfile menuprofile, Role role, int i);

  List getRoleMenuAuthorities(MenuProfile menuprofile, Role role);

  List getMenus(MenuProfile menuprofile, User user);

  List getMenus(MenuProfile menuprofile, User user, int i, String s);

  List getUserMenus(MenuProfile menuprofile, User user);

  List getRoleMenus(MenuProfile menuprofile, Role role);

  void saveOrUpdate(MenuAuthority menuauthority);

  void authorize(AuthorityObject authorityobject, Set set, Class class1);

  void copyAuthority(MenuProfile menuprofile, Role role, Collection collection);

  void setMenuAuthorityDao(MenuAuthorityDao menuauthoritydao);

  void setUserService(UserService userservice);
}
