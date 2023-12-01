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
package org.beanfuse.security.monitor.filters;

import org.beanfuse.security.Authentication;
import org.beanfuse.security.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

// Referenced classes of package org.beanfuse.security.monitor.filters:
//            HttpSessionIntegrationFilter

public class HttpSessionCategoryIntegrationFilter
    implements HttpSessionIntegrationFilter {

  public HttpSessionCategoryIntegrationFilter() {
  }

  public void register(HttpSession session, Authentication auth) {
    logger.debug("add session security attribute USERID,LOGINNAME,USERNAME,USER_CATEGORYID for {}", auth.getPrincipal());
    UserDetails details = (UserDetails) auth.getDetails();
    session.setAttribute("security.userId", details.getId());
    session.setAttribute("security.loginName", auth.getPrincipal());
    session.setAttribute("security.userName", details.getUserName());
    session.setAttribute("security.categoryId", details.getCategory());
  }

  public void clear(HttpSession session) {
    logger.debug("clean session security attribute USERID,LOGINNAME,USERNAME,USER_CATEGORYID");
    session.removeAttribute("security.userId");
    session.removeAttribute("security.loginName");
    session.removeAttribute("security.userName");
    session.removeAttribute("security.categoryId");
  }


  private static final Logger logger;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.security.monitor.filters.HttpSessionCategoryIntegrationFilter.class);
  }
}
