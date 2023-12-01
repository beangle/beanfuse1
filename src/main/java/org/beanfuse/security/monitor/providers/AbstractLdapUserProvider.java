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

import javax.servlet.http.HttpServletRequest;

// Referenced classes of package org.beanfuse.security.monitor.providers:
//            BadLdapCredentialsException, AuthenticationProvider

public abstract class AbstractLdapUserProvider
    implements AuthenticationProvider {

  public AbstractLdapUserProvider() {
  }

  public final Authentication authenticate(Authentication auth, HttpServletRequest httpRequest)
      throws AuthenticationException {
    if (!auth.isAuthenticated() && doVerify(auth)) {
      auth.setAuthenticated(true);
      return auth;
    } else {
      throw new BadLdapCredentialsException();
    }
  }

  protected abstract boolean doVerify(Authentication authentication);

  public boolean supports(Class authTokenType) {
    return (org.beanfuse.security.monitor.UserNamePasswordAuthentication.class).isAssignableFrom(authTokenType);
  }

  public String toString() {
    return getClass().getName();
  }
}
