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

import org.beanfuse.commons.query.AbstractQuery;

import java.util.Collection;
import java.util.Iterator;

// Referenced classes of package org.beanfuse.commons.query.limit:
//            PageLimit, PageIterator, Page, SinglePage

public abstract class AbstractQueryPage
    implements Page {

  public abstract Page moveTo(int i);

  public AbstractQueryPage() {
    pageNo = 0;
    maxPageNo = 0;
  }

  public AbstractQueryPage(AbstractQuery query) {
    pageNo = 0;
    maxPageNo = 0;
    this.query = query;
    if (null != query)
      if (null == query.getLimit())
        query.setLimit(new PageLimit(pageNo, 20));
      else
        pageNo = query.getLimit().getPageNo() - 1;
  }

  protected void setPageData(SinglePage page) {
    this.page = page;
    pageNo = page.getPageNo();
    maxPageNo = page.getMaxPageNo();
  }

  public Page next() {
    return moveTo(pageNo + 1);
  }

  public Page previous() {
    return moveTo(pageNo - 1);
  }

  public boolean hasNext() {
    return maxPageNo > pageNo;
  }

  public boolean hasPrevious() {
    return pageNo > 1;
  }

  public int size() {
    return page.getTotal();
  }

  public boolean isEmpty() {
    return page.isEmpty();
  }

  public SinglePage getPage() {
    return page;
  }

  public void setPage(SinglePage page) {
    this.page = page;
  }

  public Iterator iterator() {
    return new PageIterator(this);
  }

  public int getFirstPageNo() {
    return 1;
  }

  public int getMaxPageNo() {
    return maxPageNo;
  }

  public int getNextPageNo() {
    return page.getNextPageNo();
  }

  public int getPageNo() {
    return pageNo;
  }

  public int getPageSize() {
    return query.getLimit().getPageSize();
  }

  public int getPreviousPageNo() {
    return page.getPreviousPageNo();
  }

  public int getTotal() {
    return page.getTotal();
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

  public boolean remove(Object obj) {
    return page.remove(obj);
  }

  public boolean removeAll(Collection datas) {
    return page.removeAll(datas);
  }

  public boolean retainAll(Collection datas) {
    return page.retainAll(datas);
  }

  public Object[] toArray() {
    return page.toArray();
  }

  public Object[] toArray(Object datas[]) {
    return page.toArray(datas);
  }

  protected int pageNo;
  protected int maxPageNo;
  protected AbstractQuery query;
  protected SinglePage page;
}
