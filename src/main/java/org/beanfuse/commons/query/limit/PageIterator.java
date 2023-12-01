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

import java.util.Iterator;

// Referenced classes of package org.beanfuse.commons.query.limit:
//            AbstractQueryPage, SinglePage

class PageIterator
    implements Iterator {

  public PageIterator(AbstractQueryPage queryPage) {
    this.queryPage = queryPage;
    dataIndex = 0;
  }

  public boolean hasNext() {
    return dataIndex < queryPage.getPage().getPageDatas().size() || queryPage.hasNext();
  }

  public Object next() {
    if (dataIndex < queryPage.getPage().size()) {
      return queryPage.getPage().getPageDatas().get(dataIndex++);
    } else {
      queryPage.next();
      dataIndex = 0;
      return queryPage.getPage().getPageDatas().get(dataIndex++);
    }
  }

  public void remove() {
  }

  private AbstractQueryPage queryPage;
  private int dataIndex;
}
