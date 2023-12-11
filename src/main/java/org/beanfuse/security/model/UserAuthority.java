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
package org.beanfuse.security.model;

import org.beanfuse.security.AuthorityObject;

public class UserAuthority extends AbstractAuthority {

  public UserAuthority() {
  }

  public UserAuthority(User user, Resource resource) {
    this.user = user;
    this.resource = resource;
    enabled = true;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Object clone() {
    UserAuthority userAuthority = new UserAuthority();
    userAuthority.setResource(resource);
    userAuthority.setUser(user);
    userAuthority.setEnabled(enabled);
    return userAuthority;
  }

  public void merge(Authority authority) {
  }

  public void setAuthorityObject(AuthorityObject ao) {
    if (ao instanceof User)
      setUser((User) ao);
  }

  public AuthorityObject getAuthorityObject() {
    return user;
  }

  private static final long serialVersionUID = 0xcdae350991a29194L;
  private User user;
}
