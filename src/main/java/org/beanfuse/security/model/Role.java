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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.beanfuse.security.model.UserCategory;
import org.beanfuse.security.management.ManagedRole;
import org.beanfuse.security.management.RoleManager;
import org.beanfuse.security.portal.model.MenuAuthorityObject;

import java.util.HashSet;
import java.util.Set;

public class Role extends AbstractAuthorityObject
    implements ManagedRole, MenuAuthorityObject {

  public static final Long ADMIN_ID = 1L;

  public Role() {
    users = new HashSet();
  }

  public Role(Long id) {
    users = new HashSet();
    setId(id);
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Set getUsers() {
    return users;
  }

  public void setUsers(Set users) {
    this.users = users;
  }

  public RoleManager getCreator() {
    return creator;
  }

  public void setCreator(RoleManager creator) {
    this.creator = creator;
  }

  public UserCategory getCategory() {
    return category;
  }

  public void setCategory(UserCategory userCategory) {
    category = userCategory;
  }

  public String toString() {
    return (new ToStringBuilder(this)).append("name", getName()).append("id", id).append("description", getRemark()).append("users", users).toString();
  }

  private static final long serialVersionUID = 0xd0c1ea8d9533d3b4L;
  private Set users;
  private RoleManager creator;
  private UserCategory category;
  public boolean enabled;
}
