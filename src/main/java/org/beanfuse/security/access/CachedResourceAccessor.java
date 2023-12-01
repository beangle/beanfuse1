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
package org.beanfuse.security.access;

import org.beanfuse.security.access.config.ConfigLoader;
import org.beanfuse.security.access.log.AccessLogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package org.beanfuse.security.access:
//            AccessLog, ResourceAccessor

public class CachedResourceAccessor
    implements ResourceAccessor {

  public CachedResourceAccessor() {
    accessLogs = new ArrayList();
    cacheSize = ConfigLoader.getInstance().getConfig().getCachedLogSize().intValue();
  }

  public AccessLog beginAccess(HttpServletRequest request, long time) {
    AccessLog accessLog = AccessLogFactory.getLog(request);
    accessLog.setBeginAt(time);
    synchronized (accessLogs) {
      accessLogs.add(accessLog);
      if (accessLogs.size() >= cacheSize) {
        shrink(accessLogs);
        if (accessLogs.size() >= cacheSize)
          flush(accessLogs);
      }
    }
    return accessLog;
  }

  public void endAccess(AccessLog log, long time) {
    log.setEndAt(time);
  }

  public void finish() {
    if (!accessLogs.isEmpty())
      flush(accessLogs);
  }

  public void start() {
  }

  public List getAccessLogs() {
    return accessLogs;
  }

  protected void shrink(List accessLogs) {
    long minDuration = ConfigLoader.getInstance().getConfig().getMinDuration().longValue();
    Iterator iterator = accessLogs.iterator();
    do {
      if (!iterator.hasNext())
        break;
      AccessLog accessLog = (AccessLog) iterator.next();
      if (accessLog.getDuration() < minDuration)
        iterator.remove();
    } while (true);
  }

  protected void flush(List accessLogs) {
    AccessLog accessLog;
    for (Iterator iterator = accessLogs.iterator(); iterator.hasNext(); logger.info(accessLog.toString()))
      accessLog = (AccessLog) iterator.next();

    accessLogs.clear();
  }

  private static final Logger logger;
  private List accessLogs;
  private int cacheSize;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.security.access.CachedResourceAccessor.class);
  }
}
