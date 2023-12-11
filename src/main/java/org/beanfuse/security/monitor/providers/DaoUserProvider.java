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

import org.beanfuse.commons.utils.web.RequestUtils;
import org.beanfuse.security.Authentication;
import org.beanfuse.security.AuthenticationException;
import org.beanfuse.security.BadCredentialsException;
import org.beanfuse.security.UserDetails;
import org.beanfuse.security.model.User;
import org.beanfuse.security.monitor.providers.encoding.PasswordEncoder;
import org.beanfuse.security.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class DaoUserProvider implements AuthenticationProvider {

  public DaoUserProvider() {
  }

  public Authentication authenticate(Authentication auth, HttpServletRequest request)
      throws AuthenticationException {
    User user = userService.get((String) auth.getPrincipal());
    if (null == user)
      throw new AuthenticationException("security.error.userNotExist");
    if (!user.isEnabled())
      throw new AuthenticationException("security.error.userUnactive");
    populateDetail(auth, request, user);
    if (!auth.isAuthenticated() && !passwordEncoder.isPasswordValid(user.getPassword(), (String) auth.getCredentials())) {
      throw new BadCredentialsException("security.error.password");
    } else {
      auth.setAuthenticated(true);
      return auth;
    }
  }

  protected void populateDetail(Authentication auth, HttpServletRequest request, User user) {
    if (null == auth.getDetails())
      auth.setDetails(new UserDetails());
    UserDetails details = (UserDetails) auth.getDetails();
    details.setSessionId(request.getSession().getId());
    details.setId(user.getId());
    details.setUserName(user.getUserName());
    details.setCategory(user.getDefaultCategory().getId());
    details.setLoginAt(new Date());
    details.setHost(RequestUtils.getIpAddr(request));
  }

  public boolean supports(Class authTokenType) {
    return true;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public String toString() {
    return getClass().getName();
  }

  UserService userService;
  PasswordEncoder passwordEncoder;
}
