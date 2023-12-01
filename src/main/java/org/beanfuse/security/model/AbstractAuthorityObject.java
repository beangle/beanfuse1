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

import org.beanfuse.commons.model.pojo.LongIdObject;
import org.beanfuse.security.AuthorityObject;
import org.beanfuse.security.restriction.RestrictionHolder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractAuthorityObject extends LongIdObject
    implements AuthorityObject, RestrictionHolder {

  public AbstractAuthorityObject() {
    restrictions = new HashSet();
    authorities = new HashSet();
    menuAuthorities = new HashSet();
    managers = new HashSet();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  public Date getModifyAt() {
    return modifyAt;
  }

  public void setModifyAt(Date modifyAt) {
    this.modifyAt = modifyAt;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Set getRestrictions() {
    return restrictions;
  }

  public void setRestrictions(Set restrictions) {
    this.restrictions = restrictions;
  }

  public Set getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set authorities) {
    this.authorities = authorities;
  }

  public Set getMenuAuthorities() {
    return menuAuthorities;
  }

  public void setMenuAuthorities(Set menuAuthorities) {
    this.menuAuthorities = menuAuthorities;
  }

  public Set getManagers() {
    return managers;
  }

  public void setManagers(Set managers) {
    this.managers = managers;
  }

  protected String name;
  protected Set restrictions;
  protected Set authorities;
  private Set menuAuthorities;
  protected Set managers;
  protected Date createAt;
  protected Date modifyAt;
  protected String remark;
}
