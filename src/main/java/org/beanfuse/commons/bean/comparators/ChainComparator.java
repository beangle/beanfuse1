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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ChainComparator
    implements Comparator {

  public int compare(Object first, Object second) {
    int rs = 0;
    for (Iterator iterator = comparators.iterator(); iterator.hasNext(); ) {
      Comparator com = (Comparator) iterator.next();
      rs = com.compare(first, second);
      if (0 != rs)
        return rs;
    }

    return rs;
  }

  public ChainComparator() {
    comparators = new ArrayList();
  }

  public ChainComparator(List comparators) {
    this.comparators = comparators;
  }

  public void addComparator(Comparator com) {
    comparators.add(com);
  }

  protected List comparators;
}
