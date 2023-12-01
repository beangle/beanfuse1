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
package org.beanfuse.commons.utils.persistence.hibernate;

import org.hibernate.type.Type;

public class NotEmptyPropertySelector
    implements org.hibernate.criterion.Example.PropertySelector {

  public NotEmptyPropertySelector() {
  }

  public boolean include(Object object, String propertyName, Type type) {
    if (object == null)
      return false;
    if ((object instanceof Number) && ((Number) object).longValue() == 0L)
      return false;
    return !"".equals(object);
  }

  private static final long serialVersionUID = 0x1f719f4c18fadf77L;
}
