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
package org.beanfuse.commons.query;

import org.beanfuse.commons.query.limit.PageLimit;

import java.util.Map;

public abstract class AbstractQuery {

  public AbstractQuery() {
    cacheable = false;
  }

  public PageLimit getLimit() {
    return limit;
  }

  public void setLimit(PageLimit limit) {
    this.limit = limit;
  }

  public Map getParams() {
    return params;
  }

  public String getCountStr() {
    return countStr;
  }

  public void setCountStr(String countStr) {
    this.countStr = countStr;
  }

  public String getQueryStr() {
    return queryStr;
  }

  public void setQueryStr(String queryStr) {
    this.queryStr = queryStr;
  }

  public void setParams(Map params) {
    this.params = params;
  }

  public abstract String toQueryString();

  public String toCountString() {
    return countStr;
  }

  public boolean isCacheable() {
    return cacheable;
  }

  public void setCacheable(boolean cacheable) {
    this.cacheable = cacheable;
  }

  protected String queryStr;
  protected String countStr;
  protected PageLimit limit;
  protected Map params;
  protected boolean cacheable;
}
