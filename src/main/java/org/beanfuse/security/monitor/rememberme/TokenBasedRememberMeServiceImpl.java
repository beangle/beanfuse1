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
package org.beanfuse.security.monitor.rememberme;

import org.beanfuse.commons.utils.web.CookieUtils;
import org.beanfuse.security.monitor.RememberMeAuthentication;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

// Referenced classes of package org.beanfuse.security.monitor.rememberme:
//            RememberMeService

public class TokenBasedRememberMeServiceImpl
    implements RememberMeService {

  public TokenBasedRememberMeServiceImpl() {
  }

  public RememberMeAuthentication autoLogin(HttpServletRequest httpRequest) {
    String username = CookieUtils.getCookieValue(httpRequest, "name");
    if (StringUtils.isNotEmpty(username)) {
      String password = CookieUtils.getCookieValue(httpRequest, "password");
      if (StringUtils.isNotEmpty(password))
        return new RememberMeAuthentication(username, password);
    }
    return null;
  }
}
