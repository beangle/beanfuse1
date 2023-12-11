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
import org.beanfuse.security.model.UserCategory;

import java.util.ArrayList;
import java.util.List;

public class MenuProfile extends LongIdObject {

  public MenuProfile() {
    menus = new ArrayList(0);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List getMenus() {
    return menus;
  }

  public void setMenus(List menus) {
    this.menus = menus;
  }

  public UserCategory getCategory() {
    return category;
  }

  public void setCategory(UserCategory category) {
    this.category = category;
  }

  private static final long serialVersionUID = 0x7ef2acf996b66df0L;
  private String name;
  private List menus;
  private UserCategory category;
}
