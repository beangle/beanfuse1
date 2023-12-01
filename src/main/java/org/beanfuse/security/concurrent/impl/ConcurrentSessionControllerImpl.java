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
package org.beanfuse.security.concurrent.impl;

import org.beanfuse.security.Authentication;
import org.beanfuse.security.AuthenticationException;
import org.beanfuse.security.concurrent.*;

import java.util.List;

public class ConcurrentSessionControllerImpl
    implements ConcurrentSessionController {

  public ConcurrentSessionControllerImpl() {
    exceptionIfMaximumExceeded = false;
    maximumSessions = 1;
  }

  protected void allowableSessionsExceeded(String sessionId, List sessions, int allowableSessions, SessionRegistry registry) {
    if (exceptionIfMaximumExceeded || sessions == null)
      throw new ConcurrentLoginException("security.error.overmax");
    SessionInfo leastRecentlyUsed = null;
    for (int i = 0; i < sessions.size(); i++)
      if (leastRecentlyUsed == null || ((SessionInfo) sessions.get(i)).getLastRequest().before(leastRecentlyUsed.getLastRequest()))
        leastRecentlyUsed = (SessionInfo) sessions.get(i);

    leastRecentlyUsed.expireNow();
  }

  public void checkAuthenticationAllowed(Authentication request)
      throws AuthenticationException {
    Object principal = request.getPrincipal();
    String sessionId = ((SessionIdentifierAware) request.getDetails()).getSessionId();
    List sessions = sessionRegistry.getSessionInfos(principal, false);
    int sessionCount = 0;
    if (sessions != null)
      sessionCount = sessions.size();
    int allowableSessions = getMaximumSessionsForThisUser(request);
    if (sessionCount < allowableSessions)
      return;
    if (allowableSessions == -1)
      return;
    if (sessionCount == allowableSessions) {
      for (int i = 0; i < sessionCount; i++)
        if (((SessionInfo) sessions.get(i)).getSessionId().equals(sessionId))
          return;

    }
    allowableSessionsExceeded(sessionId, sessions, allowableSessions, sessionRegistry);
  }

  protected int getMaximumSessionsForThisUser(Authentication authentication) {
    return maximumSessions;
  }

  public void registerAuthentication(Authentication authentication) {
    String sessionId = ((SessionIdentifierAware) authentication.getDetails()).getSessionId();
    sessionRegistry.register(sessionId, authentication);
  }

  public SessionInfo getSessionInfo(String sessionId) {
    return sessionRegistry.getSessionInfo(sessionId);
  }

  public void removeAuthentication(String sessionId) {
    sessionRegistry.remove(sessionId);
  }

  public void setExceptionIfMaximumExceeded(boolean exceptionIfMaximumExceeded) {
    this.exceptionIfMaximumExceeded = exceptionIfMaximumExceeded;
  }

  public void setMaximumSessions(int maximumSessions) {
    this.maximumSessions = maximumSessions;
  }

  public void setSessionRegistry(SessionRegistry sessionRegistry) {
    this.sessionRegistry = sessionRegistry;
  }

  public SessionRegistry getSessionRegistry() {
    return sessionRegistry;
  }

  protected SessionRegistry sessionRegistry;
  private boolean exceptionIfMaximumExceeded;
  private int maximumSessions;
}
