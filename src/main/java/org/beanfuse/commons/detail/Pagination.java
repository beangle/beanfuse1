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
package org.beanfuse.commons.detail;

import java.util.List;

public class Pagination {

  public Pagination(Result result) {
    this(1, 2, result);
  }

  public Pagination(int pageNumber, int pageSize, Result result) {
    this.result = result;
    if (pageSize < 1)
      this.pageSize = 2;
    else
      this.pageSize = pageSize;
    if (pageNumber < 1)
      this.pageNumber = 1;
    else if (pageNumber > getMaxPageNumber())
      this.pageNumber = getMaxPageNumber();
    else
      this.pageNumber = pageNumber;
  }

  public int getFirstPageNumber() {
    return 1;
  }

  public int getMaxPageNumber() {
    if (getItemCount() < getPageSize()) {
      return 1;
    } else {
      int remainder = getItemCount() % getPageSize();
      int quotient = getItemCount() / getPageSize();
      return remainder != 0 ? quotient + 1 : quotient;
    }
  }

  public int getNextPageNumber() {
    if (getPageNumber() == getMaxPageNumber())
      return getMaxPageNumber();
    else
      return getPageNumber() + 1;
  }

  public int getPreviousPageNumber() {
    if (getPageNumber() == 1)
      return getPageNumber();
    else
      return getPageNumber() - 1;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public int getPageSize() {
    return pageSize;
  }

  public Result getResult() {
    return result;
  }

  public List getItems() {
    return result.getItem();
  }

  public int getItemCount() {
    return result.getItemCount();
  }

  public static final int DEFAULT_PAGE_NUM = 1;
  public static final int DEFAULT_PAGE_SIZE = 2;
  private int pageNumber;
  private int pageSize;
  private Result result;
}
