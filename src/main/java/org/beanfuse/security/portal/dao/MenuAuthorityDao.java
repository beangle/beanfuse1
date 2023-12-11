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
package org.beanfuse.security.portal.dao;

import org.beanfuse.security.model.Authority;
import org.beanfuse.security.model.Role;
import org.beanfuse.security.model.User;
import org.beanfuse.security.portal.model.Menu;
import org.beanfuse.security.portal.model.MenuAuthority;
import org.beanfuse.security.portal.model.MenuProfile;

import java.util.List;

public interface MenuAuthorityDao {

  public abstract MenuAuthority getUserMenuAuthority(User user, Menu menu);

  public abstract List getUserMenuAuthorities(MenuProfile menuprofile, User user, int i, String s);

  public abstract List getUserMenus(MenuProfile menuprofile, User user, int i, String s);

  public abstract MenuAuthority getRoleMenuAuthority(Role role, Menu menu);

  public abstract List getRoleMenuAuthorities(MenuProfile menuprofile, Role role, int i, String s);

  public abstract List getRoleMenus(MenuProfile menuprofile, Role role, int i, String s);

  public abstract void saveOrUpdate(Authority authority);

  public abstract void remove(Authority authority);
}
