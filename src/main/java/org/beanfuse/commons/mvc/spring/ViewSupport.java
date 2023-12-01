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
package org.beanfuse.commons.mvc.spring;

import org.beanfuse.commons.utils.web.RequestUtils;
import org.beanfuse.commons.web.dispatch.Action;
import org.beanfuse.commons.web.dispatch.Conventions;
import org.beanfuse.commons.web.dispatch.DispatchUtils;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

public class ViewSupport {

  public ViewSupport() {
  }

  public static String forwardView(HttpServletRequest request, MultiActionController controller) {
    return forwardView(request, controller, null);
  }

  public static String forwardView(HttpServletRequest request, MultiActionController controller, String relativePath) {
    return Conventions.getViewName(request, controller.getClass(), relativePath).toString();
  }

  public static InternalResourceView forwardView(Action action) {
    return new InternalResourceView(action.getURL(null).toString());
  }

  public static String forwardView(HttpServletRequest request, Object controller, String relativePath) {
    return Conventions.getViewName(request, controller.getClass(), relativePath).toString();
  }

  public static RedirectView redirectView(Object controller, String method) {
    return redirectView(controller.getClass(), method);
  }

  public static RedirectView redirectView(Class ctlClass, String method) {
    return new RedirectView(DispatchUtils.getControllerName(ctlClass) + Conventions.urlSuffix + "?" + Conventions.methodParam + "=" + method);
  }

  public static RedirectView redirectView(HttpServletRequest request, String method) {
    return new RedirectView(RequestUtils.getRequestAction(request) + "?" + Conventions.methodParam + "=" + method);
  }

  public static RedirectView redirectView(HttpServletRequest request, Action action) {
    return new RedirectView(action.getURL(request).toString());
  }
}
