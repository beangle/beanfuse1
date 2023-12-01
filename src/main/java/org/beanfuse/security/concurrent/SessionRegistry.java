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

import org.beanfuse.security.Authentication;

import java.util.List;

public interface SessionRegistry {

  public void register(String sessionId, Authentication authentication);

  public void remove(String sessionId);

  /**
   * 查询在线记录
   *
   * @return
   */
  public List getSessionInfos();

  public List getSessionInfos(Object principal, boolean includeExpiredSessions);

  public SessionInfo getSessionInfo(String sessionId);

  public boolean isRegisted(Object principal);

  public void refreshLastRequest(String sessionId);

  public int count();
}
