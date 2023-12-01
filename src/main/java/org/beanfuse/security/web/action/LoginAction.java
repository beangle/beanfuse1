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
package org.beanfuse.security.web.action;

import org.beanfuse.commons.mvc.struts.action.BaseAction;
import org.beanfuse.security.AuthenticationException;
import org.beanfuse.security.monitor.SecurityMonitor;
import org.beanfuse.security.monitor.UserNamePasswordAuthentication;
import org.beanfuse.security.web.formbean.LoginForm;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class LoginAction extends BaseAction {

  public LoginAction() {
  }

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    LoginForm loginForm = (LoginForm) form;
    String errorMsg = login(request, loginForm);
    if (StringUtils.isNotEmpty(errorMsg))
      return forward(mapping, request, errorMsg, "failure");
    String language = loginForm.getLang();
    if (language.equals("english"))
      request.getSession().setAttribute("org.apache.struts.action.LOCALE", Locale.US);
    else
      request.getSession().setAttribute("org.apache.struts.action.LOCALE", Locale.SIMPLIFIED_CHINESE);
    return mapping.findForward("success");
  }

  protected String login(HttpServletRequest request, LoginForm loginForm) {
    UserNamePasswordAuthentication auth = new UserNamePasswordAuthentication(loginForm.getName(), loginForm.getPassword());
    try {
      securityMonitor.authenticate(auth, request);
    } catch (AuthenticationException e) {
      return e.getMessage();
    }
    return "";
  }

  public void setSecurityMonitor(SecurityMonitor securityMonitor) {
    this.securityMonitor = securityMonitor;
  }

  private SecurityMonitor securityMonitor;
}
