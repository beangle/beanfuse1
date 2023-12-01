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
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Set;

public class Resource extends LongIdObject
    implements org.beanfuse.security.Resource {

  public Resource() {
    patterns = new HashSet();
    categories = new HashSet();
  }

  public String toString() {
    return (new ToStringBuilder(this)).append("name", name).append("id", id).append("description", description).toString();
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Set getPatterns() {
    return patterns;
  }

  public void setPatterns(Set patterns) {
    this.patterns = patterns;
  }

  public Set getCategories() {
    return categories;
  }

  public void setCategories(Set categories) {
    this.categories = categories;
  }

  private static final long serialVersionUID = 0x8d0506a947fd912cL;
  private String name;
  private String title;
  private String description;
  private boolean enabled;
  private Set patterns;
  private Set categories;
}
