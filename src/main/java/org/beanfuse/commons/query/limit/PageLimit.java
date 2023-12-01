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

import org.apache.commons.lang.builder.ToStringBuilder;

// Referenced classes of package org.beanfuse.commons.query.limit:
//            Limit

public class PageLimit
    implements Limit {

  public PageLimit() {
  }

  public PageLimit(int pageNo, int pageSize) {
    this.pageNo = pageNo;
    this.pageSize = pageSize;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPageNo() {
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  public boolean isValid() {
    return pageNo > 0 && pageSize > 0;
  }

  public String toString() {
    return (new ToStringBuilder(this)).append("pageNo", pageNo).append("pageSize", pageSize).toString();
  }

  private int pageNo;
  private int pageSize;
}
