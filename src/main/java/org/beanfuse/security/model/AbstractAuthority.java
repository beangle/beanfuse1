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
import org.beanfuse.security.restriction.RestrictionHolder;

import java.util.Set;

public abstract class AbstractAuthority extends LongIdObject
    implements RestrictionHolder, Authority {

  public AbstractAuthority() {
  }

  public Resource getResource() {
    return resource;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
  }

  public Set getRestrictions() {
    return restrictions;
  }

  public void setRestrictions(Set restrictions) {
    this.restrictions = restrictions;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public abstract void merge(Authority authority);

  public abstract Object clone();

  private static final long serialVersionUID = 0x83b59d46dec7006aL;
  protected Resource resource;
  protected Set restrictions;
  protected boolean enabled;
}
