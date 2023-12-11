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
package org.beanfuse.security.portal.model;

import org.beanfuse.commons.model.pojo.LongIdObject;

import java.util.HashSet;
import java.util.Set;


public class Menu extends LongIdObject
    implements Comparable {

  public Menu() {
    resources = new HashSet(0);
  }

  public Boolean getIsLeaf() {
    return null != entry;
  }

  public int compareTo(Object object) {
    Menu other = (Menu) object;
    return getCode().compareTo(other.getCode());
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getEngTitle() {
    return engTitle;
  }

  public void setEngTitle(String engTitle) {
    this.engTitle = engTitle;
  }

  public String getEntry() {
    return entry;
  }

  public void setEntry(String entry) {
    this.entry = entry;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set getResources() {
    return resources;
  }

  public void setResources(Set resources) {
    this.resources = resources;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public MenuProfile getProfile() {
    return profile;
  }

  public void setProfile(MenuProfile profile) {
    this.profile = profile;
  }

  private static final long serialVersionUID = 0x35a1a9c5838f20faL;
  private String code;
  private String title;
  private String engTitle;
  private String entry;
  private String description;
  private Set resources;
  private boolean enabled;
  private MenuProfile profile;
}
