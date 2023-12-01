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
import org.beanfuse.security.concurrent.SessionInfo;
import org.beanfuse.security.concurrent.SessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SessionRegistryImpl implements SessionRegistry {

  protected static final Logger logger = LoggerFactory.getLogger(SessionRegistryImpl.class);

  // <principal:Object,SessionIdSet>
  protected Map principals = Collections.synchronizedMap(new HashMap());
  // <sessionId:Object,SessionInfo>
  protected Map sessionIds = Collections.synchronizedMap(new HashMap());

  public List getSessionInfos() {
    return new ArrayList(sessionIds.values());
  }

  public boolean isRegisted(Object principal) {
    Set sessionsUsedByPrincipal = (Set) principals.get(principal);
    return (null != sessionsUsedByPrincipal && !sessionsUsedByPrincipal.isEmpty());
  }

  public List getSessionInfos(Object principal, boolean includeExpiredSessions) {
    Set sessionsUsedByPrincipal = (Set) principals.get(principal);
    if (sessionsUsedByPrincipal == null) {
      return null;
    }
    List list = new ArrayList();
    synchronized (sessionsUsedByPrincipal) {
      for (Iterator iter = sessionsUsedByPrincipal.iterator(); iter.hasNext(); ) {
        String sessionId = (String) iter.next();
        SessionInfo sessionInfo = getSessionInfo(sessionId);
        if (sessionInfo == null) {
          continue;
        }
        if (includeExpiredSessions || !sessionInfo.isExpired()) {
          list.add(sessionInfo);
        }
      }
    }

    return list;
  }

  public SessionInfo getSessionInfo(String sessionId) {
    return (SessionInfo) sessionIds.get(sessionId);
  }

  public void refreshLastRequest(String sessionId) {
    SessionInfo info = getSessionInfo(sessionId);
    if (info != null) {
      info.refreshLastRequest();
    }
  }

  public synchronized void register(String sessionId, Authentication authentication) {
    Object principal = authentication.getPrincipal();
    logger.debug("Registering session {} for {}", sessionId, principal);
    SessionInfo existed = getSessionInfo(sessionId);
    if (null != existed) {
      existed.addRemark(" expired with replacement.");
      remove(sessionId);
    }
    sessionIds.put(sessionId, new SessionInfo(principal, authentication.getDetails(), sessionId, new Date()));

    Set sessionsUsedByPrincipal = (Set) principals.get(principal);

    if (sessionsUsedByPrincipal == null) {
      sessionsUsedByPrincipal = Collections.synchronizedSet(new HashSet(4));
      principals.put(principal, sessionsUsedByPrincipal);
    }

    sessionsUsedByPrincipal.add(sessionId);
  }

  public void remove(String sessionId) {
    SessionInfo info = getSessionInfo(sessionId);
    if (info == null) {
      return;
    }
    logger.debug("Removing session {} from set of registered sessions", sessionId);
    sessionIds.remove(sessionId);
    Set sessionsUsedByPrincipal = (Set) principals.get(info.getPrincipal());

    if (sessionsUsedByPrincipal == null) {
      return;
    }

    synchronized (sessionsUsedByPrincipal) {
      sessionsUsedByPrincipal.remove(sessionId);
      // No need to keep object in principals Map anymore
      if (sessionsUsedByPrincipal.size() == 0) {
        logger.debug("Removing principal {} from registry", info.getPrincipal());
        principals.remove(info.getPrincipal());
      }
    }
  }

  public int count() {
    return sessionIds.size();
  }

}
