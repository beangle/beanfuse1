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

import java.text.Collator;

// Referenced classes of package org.beanfuse.commons.bean.comparators:
//            StringComparator

public class CollatorStringComparator
    implements StringComparator {

  public CollatorStringComparator() {
    collator = Collator.getInstance();
  }

  public CollatorStringComparator(boolean isAsc) {
    this();
    this.isAsc = isAsc;
  }

  public CollatorStringComparator(boolean isAsc, Collator collator) {
    this.collator = collator;
    this.isAsc = isAsc;
  }

  public int compare(Object what0, Object what1) {
    if (isAsc)
      return collator.compare(null != what0 ? what0.toString() : "", null != what1 ? what1.toString() : "");
    else
      return collator.compare(null != what1 ? what1.toString() : "", null != what0 ? what0.toString() : "");
  }

  private boolean isAsc;
  Collator collator;
}
