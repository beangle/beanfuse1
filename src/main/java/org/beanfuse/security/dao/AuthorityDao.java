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
package org.beanfuse.security.dao;

import org.beanfuse.security.model.Authority;

import java.util.List;

public interface AuthorityDao {

  public abstract Authority getUserAuthority(Long long1, Long long2);

  public abstract List getUserAuthorities(Long long1);

  public abstract List getUserResourceNames(Long long1);

  public abstract Authority getRoleAuthority(Long long1, Long long2);

  public abstract List getRoleAuthorities(Long long1);

  public abstract List getRoleResourceNames(Long long1);

  public abstract List getCategoryResources(Long long1);

  public abstract void saveOrUpdate(Authority authority);

  public abstract void remove(Authority authority);
}
