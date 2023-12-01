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
package org.beanfuse.security.monitor.providers;

import org.beanfuse.security.Authentication;
import org.beanfuse.security.AuthenticationException;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

// Referenced classes of package org.beanfuse.security.monitor.providers:
//            AuthenticationProvider

public abstract class AbstractSsoUserProvider
    implements AuthenticationProvider {

  public AbstractSsoUserProvider() {
  }

  public Authentication authenticate(Authentication auth, HttpServletRequest request)
      throws AuthenticationException {
    String userName = doGetUserLoginName(request);
    if (StringUtils.isEmpty(userName)) {
      throw new AuthenticationException("sso user not found!");
    } else {
      auth.setPrincipal(userName);
      auth.setAuthenticated(true);
      return auth;
    }
  }

  protected abstract String doGetUserLoginName(HttpServletRequest httpservletrequest);

  public boolean supports(Class authTokenType) {
    return (org.beanfuse.security.monitor.SsoAuthentication.class).isAssignableFrom(authTokenType);
  }

  public String toString() {
    return getClass().getName();
  }
}
