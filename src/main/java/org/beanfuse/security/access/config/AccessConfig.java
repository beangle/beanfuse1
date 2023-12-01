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
package org.beanfuse.security.access.config;

import org.beanfuse.security.access.ResourceAccessor;

public class AccessConfig {

  public AccessConfig() {
  }

  public String toString() {
    String toStr = "{minDuration=" + minDuration + ";accessorClass=" + accessorClass + ";accessLogClass=" + accessLogClass + ";userKey=" + userKey + ";cachedLogSize=" + cachedLogSize + "}";
    return toStr;
  }

  public ResourceAccessor getAccessor() {
    ResourceAccessor accessor = null;
    try {
      accessor = (ResourceAccessor) Class.forName(accessorClass).newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    return accessor;
  }

  public String getUserKey() {
    return userKey;
  }

  public void setUserKey(String userKey) {
    this.userKey = userKey;
  }

  public Long getMinDuration() {
    return minDuration;
  }

  public void setMinDuration(Long minDuration) {
    this.minDuration = minDuration;
  }

  public String getAccessorClass() {
    return accessorClass;
  }

  public void setAccessorClass(String accessorClass) {
    this.accessorClass = accessorClass;
  }

  public String getAccessLogClass() {
    return accessLogClass;
  }

  public void setAccessLogClass(String accessLogClass) {
    this.accessLogClass = accessLogClass;
  }

  public Integer getCachedLogSize() {
    return cachedLogSize;
  }

  public void setCachedLogSize(Integer cachedLogSize) {
    this.cachedLogSize = cachedLogSize;
  }

  private String userKey;
  private Long minDuration;
  private String accessorClass;
  private String accessLogClass;
  private Integer cachedLogSize;
}
