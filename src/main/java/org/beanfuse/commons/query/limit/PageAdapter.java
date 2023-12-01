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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package org.beanfuse.commons.query.limit:
//            PageLimit, SinglePage, Page

public class PageAdapter
    implements Page {

  public PageAdapter() {
    datas = new ArrayList();
    pageNo = 0;
  }

  public PageAdapter(List datas, int pageSize) {
    this(datas, new PageLimit(1, pageSize));
  }

  public PageAdapter(List datas, PageLimit limit) {
    this.datas = new ArrayList();
    pageNo = 0;
    this.datas = datas;
    pageSize = limit.getPageSize();
    pageNo = limit.getPageNo() - 1;
    if (datas.size() <= pageSize) {
      maxPageNo = 1;
    } else {
      int remainder = getTotal() % getPageSize();
      int quotient = getTotal() / getPageSize();
      maxPageNo = 0 != remainder ? quotient + 1 : quotient;
    }
    next();
  }

  public int getFirstPageNo() {
    return 1;
  }

  public int getMaxPageNo() {
    return maxPageNo;
  }

  public int getPageNo() {
    return pageNo;
  }

  public int getPageSize() {
    return pageSize;
  }

  public int getTotal() {
    return datas.size();
  }

  public final int getNextPageNo() {
    return page.getNextPageNo();
  }

  public final int getPreviousPageNo() {
    return page.getPreviousPageNo();
  }

  public boolean hasNext() {
    return getPageNo() < getMaxPageNo();
  }

  public boolean hasPrevious() {
    return getPageNo() > 1;
  }

  public Page next() {
    return moveTo(pageNo + 1);
  }

  public Page previous() {
    return moveTo(pageNo - 1);
  }

  public Page moveTo(int pageNo) {
    if (pageNo < 1) {
      throw new RuntimeException("error pageNo:" + pageNo);
    } else {
      this.pageNo = pageNo;
      int toIndex = pageNo * pageSize;
      page = new SinglePage(pageNo, pageSize, datas.size(), datas.subList((pageNo - 1) * pageSize, toIndex >= datas.size() ? datas.size() : toIndex));
      return this;
    }
  }

  public Iterator iterator() {
    return page.iterator();
  }

  public boolean add(Object obj) {
    return page.add(obj);
  }

  public boolean addAll(Collection datas) {
    return page.addAll(datas);
  }

  public void clear() {
    page.clear();
  }

  public boolean contains(Object obj) {
    return page.contains(obj);
  }

  public boolean containsAll(Collection datas) {
    return page.containsAll(datas);
  }

  public boolean isEmpty() {
    return page.isEmpty();
  }

  public int size() {
    return page.size();
  }

  public Object[] toArray() {
    return page.toArray();
  }

  public Object[] toArray(Object datas[]) {
    return page.toArray(datas);
  }

  public boolean remove(Object obj) {
    return page.remove(obj);
  }

  public boolean removeAll(Collection datas) {
    return page.removeAll(datas);
  }

  public boolean retainAll(Collection datas) {
    return page.retainAll(datas);
  }

  private List datas;
  private SinglePage page;
  private int pageNo;
  private int maxPageNo;
  private int pageSize;
}
