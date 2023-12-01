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
package org.beanfuse.security.restriction.model;

import org.beanfuse.commons.model.pojo.LongIdObject;

import java.util.HashSet;
import java.util.Set;

public class RestrictionPattern extends LongIdObject
    implements org.beanfuse.security.restriction.RestrictionPattern {

  public RestrictionPattern() {
    params = new HashSet(0);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String pattern) {
    content = pattern;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String discription) {
    description = discription;
  }

  public Set getParams() {
    return params;
  }

  public void setParams(Set params) {
    this.params = params;
  }

  private static final long serialVersionUID = 0x30749872dc884585L;
  private String name;
  private String content;
  private String description;
  private Set params;
}
