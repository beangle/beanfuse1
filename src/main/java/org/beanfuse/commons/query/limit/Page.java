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

import java.util.Collection;

// Referenced classes of package org.beanfuse.commons.query.limit:
//            EmptyPage

public interface Page
    extends Collection {

  public abstract int getFirstPageNo();

  public abstract int getMaxPageNo();

  public abstract int getNextPageNo();

  public abstract int getPreviousPageNo();

  public abstract int getPageNo();

  public abstract int getPageSize();

  public abstract int getTotal();

  public abstract Page next();

  public abstract boolean hasNext();

  public abstract Page previous();

  public abstract boolean hasPrevious();

  public abstract Page moveTo(int i);

  public static final int DEFAULT_PAGE_NUM = 1;
  public static final int DEFAULT_PAGE_SIZE = 20;
  public static final Page EMPTY_PAGE = new EmptyPage();

}
