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
package org.beanfuse.security.concurrent;

import org.beanfuse.security.UserDetails;

import java.io.Serializable;
import java.util.Date;

public class SessionInfo
    implements Serializable {

  public SessionInfo() {
    expired = false;
  }

  public SessionInfo(Object principal, Object userDetails, String sessionId, Date lastRequest) {
    expired = false;
    this.sessionId = sessionId;
    this.principal = principal;
    this.lastRequest = lastRequest;
    details = (UserDetails) userDetails;
  }

  public void expireNow() {
    expired = true;
  }

  public void refreshLastRequest() {
    lastRequest = new Date();
  }

  public Date getLastRequest() {
    return lastRequest;
  }

  public void setLastRequest(Date lastRequest) {
    this.lastRequest = lastRequest;
  }

  public Object getPrincipal() {
    return principal;
  }

  public void setPrincipal(Object principal) {
    this.principal = principal;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public boolean isExpired() {
    return expired;
  }

  public void setExpired(boolean expired) {
    this.expired = expired;
  }

  public void addRemark(String added) {
    if (null == remark)
      remark = added;
    else
      remark += added;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public UserDetails getDetails() {
    return details;
  }

  public void setDetails(UserDetails details) {
    this.details = details;
  }

  public Long getOnlineTime() {
    return new Long(System.currentTimeMillis() - getDetails().getLoginAt().getTime());
  }

  private static final long serialVersionUID = 0xbcff5a22b7ea78d2L;
  private Object principal;
  private String sessionId;
  private UserDetails details;
  private String remark;
  private Date lastRequest;
  private boolean expired;
}
