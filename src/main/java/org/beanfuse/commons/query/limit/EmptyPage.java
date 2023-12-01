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
package org.beanfuse.commons.query.limit;

import java.util.AbstractList;

// Referenced classes of package org.beanfuse.commons.query.limit:
//            Page

public class EmptyPage extends AbstractList
    implements Page {

  public EmptyPage() {
  }

  public int getFirstPageNo() {
    return 0;
  }

  public int getMaxPageNo() {
    return 0;
  }

  public int getNextPageNo() {
    return 0;
  }

  public int getPageNo() {
    return 0;
  }

  public int getPageSize() {
    return 0;
  }

  public int getPreviousPageNo() {
    return 0;
  }

  public int getTotal() {
    return 0;
  }

  public boolean hasNext() {
    return false;
  }

  public boolean hasPrevious() {
    return false;
  }

  public Page next() {
    return this;
  }

  public Page previous() {
    return this;
  }

  public Object get(int index) {
    return null;
  }

  public int size() {
    return 0;
  }

  public Page moveTo(int pageNo) {
    return this;
  }
}
