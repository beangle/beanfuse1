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
import java.util.Iterator;
import java.util.List;

// Referenced classes of package org.beanfuse.commons.query.limit:
//            Page

public class SinglePage
    implements Page {

  public SinglePage() {
  }

  public SinglePage(int pageNo, int pageSize, int total, List dataItems) {
    pageDatas = dataItems;
    if (pageSize < 1)
      this.pageSize = 2;
    else
      this.pageSize = pageSize;
    if (pageNo < 1)
      this.pageNo = 1;
    else
      this.pageNo = pageNo;
    this.total = total;
  }

  public final int getFirstPageNo() {
    return 1;
  }

  public final int getMaxPageNo() {
    if (getTotal() < getPageSize()) {
      return 1;
    } else {
      int remainder = getTotal() % getPageSize();
      int quotient = getTotal() / getPageSize();
      return 0 != remainder ? quotient + 1 : quotient;
    }
  }

  public final int getNextPageNo() {
    if (getPageNo() == getMaxPageNo())
      return getMaxPageNo();
    else
      return getPageNo() + 1;
  }

  public final int getPreviousPageNo() {
    if (getPageNo() == 1)
      return getPageNo();
    else
      return getPageNo() - 1;
  }

  public final int getPageNo() {
    return pageNo;
  }

  public final int getPageSize() {
    return pageSize;
  }

  public final List getPageDatas() {
    return pageDatas;
  }

  public final int getTotal() {
    return total;
  }

  public boolean add(Object obj) {
    throw new RuntimeException("unsupported add");
  }

  public boolean addAll(Collection datas) {
    throw new RuntimeException("unsupported addAll");
  }

  public void clear() {
    throw new RuntimeException("unsupported clear");
  }

  public boolean contains(Object obj) {
    return pageDatas.contains(obj);
  }

  public boolean containsAll(Collection datas) {
    return pageDatas.containsAll(datas);
  }

  public boolean isEmpty() {
    return pageDatas.isEmpty();
  }

  public Iterator iterator() {
    return pageDatas.iterator();
  }

  public boolean remove(Object obj) {
    throw new RuntimeException("unsupported removeAll");
  }

  public boolean removeAll(Collection datas) {
    throw new RuntimeException("unsupported removeAll");
  }

  public boolean retainAll(Collection datas) {
    throw new RuntimeException("unsupported retailAll");
  }

  public int size() {
    return pageDatas.size();
  }

  public Object[] toArray() {
    return pageDatas.toArray();
  }

  public Object[] toArray(Object datas[]) {
    return pageDatas.toArray(datas);
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public void setPageDatas(List dataItems) {
    pageDatas = dataItems;
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

  public Page moveTo(int pageNo) {
    return this;
  }

  protected int pageNo;
  protected int pageSize;
  protected int total;
  protected List pageDatas;
}
