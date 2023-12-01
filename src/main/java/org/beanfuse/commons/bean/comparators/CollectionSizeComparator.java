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
package org.beanfuse.commons.bean.comparators;

import java.util.Collection;
import java.util.Comparator;

public class CollectionSizeComparator
    implements Comparator {

  public CollectionSizeComparator() {
  }

  public int compare(Object o1, Object o2) {
    Collection c1 = (Collection) o1;
    Collection c2 = (Collection) o2;
    if (c1.equals(c2))
      return 0;
    return c1.size() <= c2.size() ? -1 : 1;
  }
}
