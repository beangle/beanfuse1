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

import org.beanfuse.commons.utils.web.RequestUtils;
import org.beanfuse.security.access.AccessLog;
import org.beanfuse.security.access.config.ConfigLoader;

import javax.servlet.http.HttpServletRequest;

// Referenced classes of package org.beanfuse.security.access.log:
//            UserAccessLog, DefaultAccessLog

public class AccessLogFactory {

  public AccessLogFactory() {
  }

  public static AccessLog getLog(HttpServletRequest request) {
    DefaultAccessLog log = null;
    if ((org.beanfuse.security.access.log.UserAccessLog.class).getName().equals(ConfigLoader.getInstance().getConfig().getAccessLogClass())) {
      UserAccessLog userAccessLog = new UserAccessLog();
      userAccessLog.setUser(request.getSession().getAttribute(ConfigLoader.getInstance().getConfig().getUserKey()));
      log = userAccessLog;
    } else {
      log = new DefaultAccessLog();
    }
    log.setUri(RequestUtils.getRequestURI(request));
    log.setParams(request.getQueryString());
    return log;
  }
}
