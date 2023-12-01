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


// Referenced classes of package org.beanfuse.security:
//            UserDetails

public interface Authentication {

  public abstract Object getPrincipal();

  public abstract Object setPrincipal(Object obj);

  public abstract Object getCredentials();

  public abstract Object getDetails();

  public abstract void setDetails(UserDetails userdetails);

  public abstract boolean isAuthenticated();

  public abstract void setAuthenticated(boolean flag);

  public static final String NAME = "name";
  public static final String PASSWORD = "password";
  public static final String USERID = "security.userId";
  public static final String LOGINNAME = "security.loginName";
  public static final String USERNAME = "security.userName";
  public static final String USER_CATEGORYID = "security.categoryId";
  public static final String ERROR_PASSWORD = "security.error.password";
  public static final String ERROR_NOTEXIST = "security.error.userNotExist";
  public static final String ERROR_UNACTIVE = "security.error.userUnactive";
  public static final String ERROR_OVERMAX = "security.error.overmax";
}
