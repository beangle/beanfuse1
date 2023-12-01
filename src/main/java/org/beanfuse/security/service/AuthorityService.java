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

import org.beanfuse.security.Authority;
import org.beanfuse.security.AuthorityObject;
import org.beanfuse.security.Resource;
import org.beanfuse.security.Role;
import org.beanfuse.security.dao.AuthorityDao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

// Referenced classes of package org.beanfuse.security.service:
//            UserService

public interface AuthorityService {

  public abstract Resource getResource(String s);

  public abstract List getResources(Long long1);

  public abstract List getAuthorities(Long long1);

  public abstract Authority getAuthority(Long long1, Long long2);

  public abstract Authority getUserAuthority(Long long1, Long long2);

  public abstract List getUserAuthorities(Long long1);

  public abstract Authority getRoleAuthority(Long long1, Long long2);

  public abstract List getRoleAuthorities(Long long1);

  public abstract List getUserResourceNames(Long long1);

  public abstract List getRoleResourceNames(Long long1);

  public abstract void saveOrUpdate(Authority authority);

  public abstract void remove(Authority authority);

  public abstract void authorize(AuthorityObject authorityobject, Set set, Class class1);

  public abstract void copyAuthority(Role role, Collection collection);

  public abstract void setAuthorityDao(AuthorityDao authoritydao);

  public abstract void setUserService(UserService userservice);

  public abstract List getCategoryResources(Long long1);
}
