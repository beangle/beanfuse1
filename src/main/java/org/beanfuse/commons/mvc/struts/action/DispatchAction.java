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
package org.beanfuse.commons.mvc.struts.action;

import org.beanfuse.commons.mvc.struts.misc.ForwardSupport;
import org.beanfuse.commons.web.dispatch.Action;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.web.struts.DispatchActionSupport;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

public class DispatchAction extends DispatchActionSupport {

  public DispatchAction() {
  }

  protected ActionForward forward(ActionMapping mapping, String forwardTag) {
    return mapping.findForward(forwardTag);
  }

  protected ActionForward forward(ActionMapping mapping, HttpServletRequest request, String msg, String forward) {
    ActionMessages actionMessages = new ActionMessages();
    actionMessages.add("org.apache.struts.action.GLOBAL_MESSAGE", new ActionMessage(msg));
    addErrors(request, actionMessages);
    return mapping.findForward(forward);
  }

  protected ActionForward forward(HttpServletRequest request, Action action, String message) {
    if (StringUtils.isNotEmpty(message))
      saveErrors(request.getSession(), ForwardSupport.buildMessages(new String[]{
          message
      }));
    return ForwardSupport.forward(request, action);
  }

  protected ActionForward forward(HttpServletRequest request, Action action) {
    return ForwardSupport.forward(request, action);
  }

  protected ActionForward forward(HttpServletRequest request, String pagePath) {
    return ForwardSupport.forward(clazz, request, pagePath);
  }

  protected ActionForward forward(HttpServletRequest request) {
    ActionForward f = ForwardSupport.forward(clazz, request, (String) null);
    return f;
  }

  protected ActionForward redirect(HttpServletRequest request, String method, String message)
      throws Exception {
    return redirect(request, new Action("", method), message);
  }

  protected ActionForward redirect(HttpServletRequest request, Action action, String message)
      throws Exception {
    if (StringUtils.isNotEmpty(message))
      saveErrors(request.getSession(), ForwardSupport.buildMessages(new String[]{
          message
      }));
    return new ActionForward(action.getURL(request).toString(), true);
  }

  protected ActionForward redirect(HttpServletRequest request, Action action, String message, String[] prefixes) throws Exception {
    if (StringUtils.isNotEmpty(message)) {
      this.saveErrors(request.getSession(), ForwardSupport.buildMessages(new String[]{message}));
    }

    StringBuffer buf = action.getURL(request);
    URLCodec urlCodec = new URLCodec("UTF-8");
    if (null != prefixes && prefixes.length > 0) {
      Iterator iter = request.getParameterMap().keySet().iterator();

      label41:
      while (true) {
        while (true) {
          String key;
          do {
            if (!iter.hasNext()) {
              break label41;
            }

            key = (String) iter.next();
          } while (StringUtils.equals("params", key));

          for (int i = 0; i < prefixes.length; ++i) {
            if (key.startsWith(prefixes[i])) {
              String value = request.getParameter(key);
              if (StringUtils.isNotEmpty(value)) {
                buf.append("&").append(key).append("=").append(urlCodec.encode(value));
              }
              break;
            }
          }
        }
      }
    }

    if (log.isDebugEnabled()) {
      log.debug("redirect:" + buf.toString());
    }

    return new ActionForward(buf.toString(), true);
  }
}
