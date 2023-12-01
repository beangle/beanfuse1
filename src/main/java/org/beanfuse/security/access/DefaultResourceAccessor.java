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

import org.beanfuse.security.access.log.AccessLogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

// Referenced classes of package org.beanfuse.security.access:
//            ResourceAccessor, AccessLog

public class DefaultResourceAccessor
    implements ResourceAccessor {

  public DefaultResourceAccessor() {
  }

  public AccessLog beginAccess(HttpServletRequest request, long time) {
    AccessLog accessLog = AccessLogFactory.getLog(request);
    accessLog.setBeginAt(time);
    if (logger.isDebugEnabled())
      logger.debug(accessLog.toString());
    return accessLog;
  }

  public void endAccess(AccessLog accessLog, long time) {
    accessLog.setEndAt(time);
    if (logger.isDebugEnabled())
      logger.debug(accessLog.toString());
  }

  public void finish() {
  }

  public void start() {
  }

  private static final Logger logger;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.security.access.DefaultResourceAccessor.class);
  }
}
