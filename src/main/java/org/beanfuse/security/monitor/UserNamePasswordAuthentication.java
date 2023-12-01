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
package org.beanfuse.security.monitor;

import org.beanfuse.security.Authentication;
import org.beanfuse.security.UserDetails;

public class UserNamePasswordAuthentication
    implements Authentication {

  public UserNamePasswordAuthentication() {
    authenticated = false;
  }

  public UserNamePasswordAuthentication(String userName, String password) {
    authenticated = false;
    this.userName = userName;
    this.password = password;
  }

  public Object getCredentials() {
    return password;
  }

  public Object setPrincipal(Object principal) {
    return userName = (String) principal;
  }

  public Object getDetails() {
    return details;
  }

  public Object getPrincipal() {
    return userName;
  }

  public void setDetails(UserDetails details) {
    this.details = details;
  }

  public boolean isAuthenticated() {
    return authenticated;
  }

  public void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }

  private String userName;
  private String password;
  private UserDetails details;
  private boolean authenticated;
}
