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
package org.beanfuse.security.access.log;

import org.beanfuse.security.access.AccessLog;

import java.util.Date;

public class DefaultAccessLog
    implements AccessLog {

  public DefaultAccessLog() {
  }

  public String toString() {
    if (0L == endAt)
      return "access " + uri + (null != params ? "?" + params : "") + " begin at " + new Date(beginAt);
    else
      return "access " + uri + (null != params ? "?" + params : "") + " begin at " + new Date(beginAt) + " end at " + new Date(endAt) + " duration " + (endAt - beginAt);
  }

  public long getDuration() {
    if (0L == endAt)
      return System.currentTimeMillis() - beginAt;
    else
      return endAt - beginAt;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public long getBeginAt() {
    return beginAt;
  }

  public void setBeginAt(long beginAt) {
    this.beginAt = beginAt;
  }

  public long getEndAt() {
    return endAt;
  }

  public void setEndAt(long endAt) {
    this.endAt = endAt;
  }

  public Date getBeignTime() {
    if (0L != beginAt)
      return new Date(beginAt);
    else
      return null;
  }

  public Date getEndTime() {
    if (0L != endAt)
      return new Date(endAt);
    else
      return null;
  }

  String uri;
  String params;
  long beginAt;
  long endAt;
}
