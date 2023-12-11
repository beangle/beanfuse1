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
import org.beanfuse.security.restriction.PatternParam;
import org.beanfuse.security.restriction.RestrictionHolder;
import org.beanfuse.security.restriction.RestrictionItem;
import org.beanfuse.security.restriction.RestrictionPattern;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Restriction extends LongIdObject
    implements org.beanfuse.security.restriction.Restriction {

  public Restriction() {
    items = new HashSet();
  }

  public RestrictionHolder getHolder() {
    return holder;
  }

  public void setHolder(RestrictionHolder holder) {
    this.holder = holder;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Set getItems() {
    return items;
  }

  public void setItems(Set items) {
    this.items = items;
  }

  public RestrictionItem getItem(String name) {
    for (Iterator iter = items.iterator(); iter.hasNext(); ) {
      RestrictionItem item = (RestrictionItem) iter.next();
      if (item.getParam().getName().equals(name))
        return item;
    }

    return null;
  }

  public RestrictionItem addItem(RestrictionItem item) {
    item.setRestriction(this);
    getItems().add(item);
    return item;
  }

  public RestrictionItem addItem(PatternParam param) {
    RestrictionItem item = new org.beanfuse.security.restriction.model.RestrictionItem();
    item.setParam(param);
    item.setRestriction(this);
    getItems().add(item);
    return item;
  }

  public RestrictionItem getItem(PatternParam param) {
    return getItem(param.getName());
  }

  protected Object clone()
      throws CloneNotSupportedException {
    return super.clone();
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    if (null != items) {
      RestrictionItem item;
      for (Iterator iterator = items.iterator(); iterator.hasNext(); sb.append(item.toString()).append("\n"))
        item = (RestrictionItem) iterator.next();

    }
    return sb.toString();
  }

  public RestrictionPattern getPattern() {
    return pattern;
  }

  public void setPattern(RestrictionPattern pattern) {
    this.pattern = pattern;
  }

  private static final long serialVersionUID = 0xefee6864a0f02cc1L;
  private RestrictionPattern pattern;
  private boolean enabled;
  private Set items;
  private RestrictionHolder holder;
}
