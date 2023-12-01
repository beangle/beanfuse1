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
package org.beanfuse.security;

import org.beanfuse.security.management.UserManager;

import java.util.Set;

// Referenced classes of package org.beanfuse.security:
//            AuthorityObject, UserCategory

public interface User
    extends AuthorityObject {

  public abstract String getUserName();

  public abstract void setUserName(String s);

  public abstract String getPassword();

  public abstract void setPassword(String s);

  public abstract String getEmail();

  public abstract void setEmail(String s);

  public abstract Set getRoles();

  public abstract void setRoles(Set set);

  public abstract Integer getState();

  public abstract void setState(Integer integer);

  public abstract Set getCategories();

  public abstract void setCategories(Set set);

  public abstract UserCategory getDefaultCategory();

  public abstract void setDefaultCategory(UserCategory usercategory);

  public abstract boolean isCategory(Long long1);

  public abstract void setCreator(UserManager usermanager);

  public static final String DEFAULT_PASSWORD = "1";
  public static final Long ADMIN_ID = new Long(1L);

}
