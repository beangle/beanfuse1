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
package org.beanfuse.security.audit;

import org.beanfuse.commons.model.pojo.LongIdObject;

import java.sql.Timestamp;
import java.util.Date;

public class OnlineRecord extends LongIdObject {

  public OnlineRecord() {
  }

  public OnlineRecord(String sessionId, String name, String userName, Long category) {
    this.sessionId = sessionId;
    this.name = name;
    this.userName = userName;
    loginAt = new Date(System.currentTimeMillis());
    lastAccessAt = new Date(System.currentTimeMillis());
    this.category = category;
  }

  public String toString() {
    String str = " User:[" + getName() + "]";
    long onlineTime = System.currentTimeMillis() - loginAt.getTime();
    long minute = onlineTime / 1000L / 60L;
    long second = (onlineTime / 1000L) % 60L;
    str = str + "OnLine time:[" + minute + " minute " + second + " second]";
    return str;
  }

  public void calcOnlineTime() {
    if (null == logoutAt)
      setOnlineTime(new Long(System.currentTimeMillis() - loginAt.getTime()));
    else
      setOnlineTime(new Long(logoutAt.getTime() - loginAt.getTime()));
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Date getLoginAt() {
    return loginAt;
  }

  public void setLoginAt(Date loginAt) {
    this.loginAt = loginAt;
  }

  public Date getLastAccessAt() {
    return lastAccessAt;
  }

  public void setLastAccessAt(Date lastAccessAt) {
    this.lastAccessAt = lastAccessAt;
  }

  public Long getOnlineTime() {
    return onlineTime;
  }

  public void setOnlineTime(Long onlineTime) {
    this.onlineTime = onlineTime;
  }

  public Long getCategory() {
    return category;
  }

  public void setCategory(Long category) {
    this.category = category;
  }

  public Timestamp getLogoutAt() {
    return logoutAt;
  }

  public void setLogoutAt(Timestamp logoutAt) {
    this.logoutAt = logoutAt;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  private static final long serialVersionUID = 0xd45b86dc0b442993L;
  private String sessionId;
  private String name;
  private String userName;
  private String host;
  private Date loginAt;
  private Date lastAccessAt;
  private Long onlineTime;
  private Long category;
  private Timestamp logoutAt;
  private String remark;
}
