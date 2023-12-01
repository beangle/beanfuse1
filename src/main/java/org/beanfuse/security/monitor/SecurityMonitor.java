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
package org.beanfuse.security.monitor;

import org.beanfuse.security.Authentication;
import org.beanfuse.security.AuthenticationException;
import org.beanfuse.security.concurrent.category.CategorySessionController;
import org.beanfuse.security.monitor.filters.HttpSessionIntegrationFilter;
import org.beanfuse.security.monitor.rememberme.RememberMeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface SecurityMonitor {

  public abstract Authentication authenticate(Authentication authentication, HttpServletRequest httpservletrequest)
      throws AuthenticationException;

  public abstract void logout(HttpSession httpsession);

  public abstract boolean enableRememberMe();

  public abstract RememberMeService getRememberMeService();

  public abstract void setRememberMeService(RememberMeService remembermeservice);

  public abstract List getProviders();

  public abstract void setProviders(List list);

  public abstract boolean isAuthorized(Long long1, String s);

  public abstract boolean isIgnored(String s);

  public abstract CategorySessionController getSessionController();

  public abstract void setSessionController(CategorySessionController categorysessioncontroller);

  public abstract HttpSessionIntegrationFilter getHttpSessionIntegrationFilter();
}
