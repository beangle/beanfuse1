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
package org.beanfuse.security.service;

import org.beanfuse.commons.model.pojo.PojoExistException;
import org.beanfuse.security.model.Role;
import org.beanfuse.security.dao.RoleDao;

import java.util.List;

public interface RoleService {

  public abstract Role get(Long long1);

  public abstract List get(Long along[]);

  public abstract void saveOrUpdate(Role role)
      throws PojoExistException;

  public abstract void setRoleDao(RoleDao roledao);
}
