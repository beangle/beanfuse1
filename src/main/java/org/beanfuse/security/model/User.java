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

import org.beanfuse.security.UserCategory;
import org.beanfuse.security.management.ManagedUser;
import org.beanfuse.security.management.RoleManager;
import org.beanfuse.security.management.UserManager;
import org.beanfuse.security.portal.model.MenuAuthorityObject;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

// Referenced classes of package org.beanfuse.security.model:
//            AbstractAuthorityObject, Role

public class User extends AbstractAuthorityObject
    implements org.beanfuse.security.User, UserManager, RoleManager, ManagedUser, MenuAuthorityObject {

  public User() {
    roles = new HashSet();
    mngRoles = new HashSet();
    mngUsers = new HashSet();
    categories = new HashSet();
  }

  public User(Long id) {
    roles = new HashSet();
    mngRoles = new HashSet();
    mngUsers = new HashSet();
    categories = new HashSet();
    setId(id);
  }

  public String toString() {
    return (new ToStringBuilder(this)).append("id", id).append("password", password).append("name", getName()).toString();
  }

  public boolean isCategory(Long categoryId) {
    for (Iterator iter = categories.iterator(); iter.hasNext(); ) {
      UserCategory category = (UserCategory) iter.next();
      if (category.getId().equals(categoryId))
        return true;
    }

    return false;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set getMngRoles() {
    return mngRoles;
  }

  public void setMngRoles(Set mngRoles) {
    this.mngRoles = mngRoles;
  }

  public Set getMngUsers() {
    return mngUsers;
  }

  public void setMngUsers(Set mngUsers) {
    this.mngUsers = mngUsers;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set getRoles() {
    return roles;
  }

  public void setRoles(Set roles) {
    this.roles = roles;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public UserManager getCreator() {
    return creator;
  }

  public void setCreator(UserManager creator) {
    this.creator = creator;
  }

  public boolean isUserAdmin() {
    if (getId().equals(ADMIN_ID))
      return true;
    for (Iterator iterator = roles.iterator(); iterator.hasNext(); ) {
      Role role = (Role) iterator.next();
      if (role.getId().equals(Role.ADMIN_ID))
        return true;
    }

    return false;
  }

  public boolean isRoleAdmin() {
    return isUserAdmin();
  }

  public UserCategory getDefaultCategory() {
    return defaultCategory;
  }

  public void setDefaultCategory(UserCategory defaultCategory) {
    this.defaultCategory = defaultCategory;
  }

  public Set getCategories() {
    return categories;
  }

  public void setCategories(Set categories) {
    this.categories = categories;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public boolean isEnabled() {
    return state.intValue() == 1;
  }

  private static final long serialVersionUID = 0xcdae350991a29194L;
  private String userName;
  private String password;
  private String email;
  private Set roles;
  private UserManager creator;
  private Set mngRoles;
  private Set mngUsers;
  protected Set categories;
  private UserCategory defaultCategory;
  protected Integer state;
}
