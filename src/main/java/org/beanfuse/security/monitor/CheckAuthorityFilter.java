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

import org.beanfuse.security.AuthenticationException;
import org.beanfuse.security.concurrent.SessionInfo;
import org.beanfuse.security.web.ResourceExtractor;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

// Referenced classes of package org.beanfuse.security.monitor:
//            SecurityMonitor, SsoAuthentication

public class CheckAuthorityFilter
    implements Filter {

  public CheckAuthorityFilter() {
    loginFailPath = null;
    noAuthorityPath = null;
    expiredPath = null;
    resourceExtractorClassName = null;
  }

  public void init(FilterConfig cfg)
      throws ServletException {
    for (Enumeration en = cfg.getInitParameterNames(); en.hasMoreElements(); ) {
      String property = (String) en.nextElement();
      Object value = cfg.getInitParameter(property);
      try {
        PropertyUtils.setProperty(this, property, value);
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
      }
    }

    if (StringUtils.isEmpty(resourceExtractorClassName))
      resourceExtractorClassName = (org.beanfuse.security.web.DefaultResourceExtractor.class).getName();
    try {
      resourceExtractor = (ResourceExtractor) Class.forName(resourceExtractorClassName).newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    if (null == expiredPath)
      expiredPath = loginFailPath;
    logger.info("Filter {} configured successfully", cfg.getFilterName());
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String resource = resourceExtractor.extract(httpRequest);
    request.setAttribute("resourceName", resource);
    HttpSession session = httpRequest.getSession();
    if (null == monitor) {
      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
      monitor = (SecurityMonitor) wac.getBean("securityMonitor", org.beanfuse.security.monitor.SecurityMonitor.class);
    }
    if (!monitor.isIgnored(resource)) {
      SessionInfo info = monitor.getSessionController().getSessionInfo(session.getId());
      if (null == info) {
        org.beanfuse.security.Authentication auth = null;
        if (monitor.enableRememberMe())
          auth = monitor.getRememberMeService().autoLogin(httpRequest);
        if (null == auth)
          auth = new SsoAuthentication();
        try {
          monitor.authenticate(auth, httpRequest);
        } catch (AuthenticationException e) {
          session.setAttribute(PREVIOUS_URL, httpRequest.getRequestURL() + "?" + httpRequest.getQueryString());
          redirectTo((HttpServletRequest) request, (HttpServletResponse) response, loginFailPath);
          return;
        }
      } else {
        if (info.isExpired()) {
          monitor.logout(session);
          session.setAttribute(PREVIOUS_URL, httpRequest.getRequestURL() + "?" + httpRequest.getQueryString());
          redirectTo((HttpServletRequest) request, (HttpServletResponse) response, expiredPath);
          return;
        }
        boolean pass = monitor.isAuthorized(info.getDetails().getId(), resource);
        if (pass) {
          logger.debug("user {} access {} success", info.getPrincipal(), resource);
        } else {
          logger.info("user {} cannot access resource[{}]", info.getPrincipal(), resource);
          redirectTo((HttpServletRequest) request, (HttpServletResponse) response, noAuthorityPath);
          return;
        }
      }
    }
    chain.doFilter(request, response);
  }

  public void redirectTo(HttpServletRequest request, HttpServletResponse response, String path)
      throws IOException {
    if (path.startsWith("/")) {
      String contextPath = request.getContextPath();
      response.sendRedirect((contextPath.equals("/") ? "" : contextPath + "/") + path);
    } else {
      response.sendRedirect(path);
    }
  }

  public void destroy() {
  }

  public String getLoginFailPath() {
    return loginFailPath;
  }

  public void setLoginFailPath(String failRedirect) {
    loginFailPath = failRedirect;
  }

  public String getNoAuthorityPath() {
    return noAuthorityPath;
  }

  public void setNoAuthorityPath(String noAuthorityRedirect) {
    noAuthorityPath = noAuthorityRedirect;
  }

  public String getExpiredPath() {
    return expiredPath;
  }

  public void setExpiredPath(String expiredPath) {
    this.expiredPath = expiredPath;
  }

  private static final Logger logger;
  public static String PREVIOUS_URL = "previousURL";
  private SecurityMonitor monitor;
  private String loginFailPath;
  private String noAuthorityPath;
  private String expiredPath;
  private String resourceExtractorClassName;
  private ResourceExtractor resourceExtractor;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.security.monitor.CheckAuthorityFilter.class);
  }
}
