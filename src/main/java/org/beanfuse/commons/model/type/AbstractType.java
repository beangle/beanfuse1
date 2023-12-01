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
package org.beanfuse.commons.model.type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package org.beanfuse.commons.model.type:
//            Type

public abstract class AbstractType
    implements Type {
  protected static final Logger logger = LoggerFactory.getLogger(org.beanfuse.commons.model.type.AbstractType.class);

  public AbstractType() {
  }

  public boolean isCollectionType() {
    return false;
  }

  public boolean isComponentType() {
    return false;
  }

  public boolean isEntityType() {
    return false;
  }

  public Type getPropertyType(String property) {
    return null;
  }

  public boolean equals(Object obj) {
    return getName().equals(((Type) obj).getName());
  }

  public int hashCode() {
    return getName().hashCode();
  }

  public String toString() {
    return getName();
  }

  public abstract String getName();

  public abstract Class getReturnedClass();

  public Object newInstance() {
    try {
      return this.getReturnedClass().newInstance();
    } catch (Exception var2) {
      throw new RuntimeException(var2.getMessage());
    }
  }


}
