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

public class RoleAuthority extends AbstractAuthority{

  public RoleAuthority() {
  }

  public RoleAuthority(Role role, Resource resource) {
    this.role = role;
    this.resource = resource;
    enabled = true;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Object clone() {
    RoleAuthority roleAuthority = new RoleAuthority();
    roleAuthority.setResource(resource);
    roleAuthority.setRole(role);
    roleAuthority.setEnabled(enabled);
    return roleAuthority;
  }

  public void merge(Authority authority) {
  }

  public void setAuthorityObject(AuthorityObject ao) {
    if (ao instanceof Role)
      setRole((Role) ao);
  }

  public AuthorityObject getAuthorityObject() {
    return role;
  }

  private static final long serialVersionUID = 0xcdae350991a29194L;
  private Role role;
}
