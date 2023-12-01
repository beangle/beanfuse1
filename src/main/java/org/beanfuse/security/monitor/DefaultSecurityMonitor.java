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
import org.beanfuse.security.AuthenticationBreakException;
import org.beanfuse.security.AuthenticationException;
import org.beanfuse.security.UserDetails;
import org.beanfuse.security.concurrent.SessionInfo;
import org.beanfuse.security.concurrent.category.CategorySessionController;
import org.beanfuse.security.monitor.filters.HttpSessionIntegrationFilter;
import org.beanfuse.security.monitor.providers.AuthenticationProvider;
import org.beanfuse.security.monitor.providers.ProviderNotFoundException;
import org.beanfuse.security.monitor.rememberme.RememberMeService;
import org.beanfuse.security.service.AuthorityDecisionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package org.beanfuse.security.monitor:
//            SecurityMonitor

public class DefaultSecurityMonitor
    implements SecurityMonitor, InitializingBean {

  public DefaultSecurityMonitor() {
    enableRememberMe = true;
    providers = new ArrayList();
  }

  public boolean isIgnored(String actionName) {
    return authorityDecisionService.isIgnored(actionName);
  }

  public void afterPropertiesSet()
      throws Exception {
    if (providers.isEmpty()) {
      throw new RuntimeException("authentication provider list is empty");
    } else {
      logger.info("providers:" + providers);
      return;
    }
  }

  public boolean isAuthorized(Long userId, String actionName) {
    return authorityDecisionService.authorized(userId, actionName);
  }

  public void logout(HttpSession session) {
    String sessionId = session.getId();
    SessionInfo info = sessionController.getSessionRegistry().getSessionInfo(sessionId);
    if (null != info) {
      sessionController.removeAuthentication(sessionId);
      if (!sessionController.getSessionRegistry().isRegisted(info.getPrincipal()))
        authorityDecisionService.removeAuthorities(info.getDetails().getId());
      httpSessionIntegrationFilter.clear(session);
    }
  }

  public Authentication authenticate(Authentication authentication, HttpServletRequest request)
      throws AuthenticationException {
    Iterator iter = getProviders().iterator();
    Class toTest = authentication.getClass();
    AuthenticationException lastException = null;
    do {
      if (!iter.hasNext())
        break;
      AuthenticationProvider provider = (AuthenticationProvider) iter.next();
      if (!provider.supports(toTest))
        continue;
      logger.debug("Authentication attempt using " + provider.getClass().getName());
      Authentication result;
      try {
        result = provider.authenticate(authentication, request);
      } catch (AuthenticationException ae) {
        lastException = ae;
        result = null;
      }
      if (lastException instanceof AuthenticationBreakException)
        break;
      if (null == result || null == result.getDetails())
        continue;
      Exception concurrentException = null;
      try {
        sessionController.checkAuthenticationAllowed(result);
      } catch (AuthenticationException ae) {
        lastException = ae;
        concurrentException = ae;
      }
      if (null == concurrentException) {
        UserDetails details = (UserDetails) authentication.getDetails();
        sessionController.registerAuthentication(result);
        authorityDecisionService.registerAuthorities(details.getId());
        httpSessionIntegrationFilter.register(request.getSession(), authentication);
        request.getSession().setMaxInactiveInterval(1800);
        return result;
      }
      break;
    } while (true);
    if (lastException == null)
      lastException = new ProviderNotFoundException();
    throw lastException;
  }

  public void changeCategory(String sessionId, Long category) {
    sessionController.changeCategory(sessionId, category);
  }

  public boolean enableRememberMe() {
    return enableRememberMe;
  }

  public List getProviders() {
    return providers;
  }

  public void setProviders(List providers) {
    this.providers = providers;
  }

  public RememberMeService getRememberMeService() {
    return rememberMeService;
  }

  public void setRememberMeService(RememberMeService rememberMeService) {
    this.rememberMeService = rememberMeService;
  }

  public void setAuthorityDecisionService(AuthorityDecisionService authorityDecisionService) {
    this.authorityDecisionService = authorityDecisionService;
  }

  public void setSessionController(CategorySessionController sessionController) {
    this.sessionController = sessionController;
  }

  public CategorySessionController getSessionController() {
    return sessionController;
  }

  public boolean isEnableRememberMe() {
    return enableRememberMe;
  }

  public void setEnableRememberMe(boolean enableRememberMe) {
    this.enableRememberMe = enableRememberMe;
  }

  public HttpSessionIntegrationFilter getHttpSessionIntegrationFilter() {
    return httpSessionIntegrationFilter;
  }

  public void setHttpSessionIntegrationFilter(HttpSessionIntegrationFilter httpSessionIntegrationFilter) {
    this.httpSessionIntegrationFilter = httpSessionIntegrationFilter;
  }

  protected static Logger logger;
  protected boolean enableRememberMe;
  protected List providers;
  protected AuthorityDecisionService authorityDecisionService;
  protected CategorySessionController sessionController;
  protected RememberMeService rememberMeService;
  protected HttpSessionIntegrationFilter httpSessionIntegrationFilter;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.security.monitor.DefaultSecurityMonitor.class);
  }
}
