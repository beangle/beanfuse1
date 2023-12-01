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
package org.beanfuse.security.access;

import org.beanfuse.security.access.config.ConfigLoader;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

// Referenced classes of package org.beanfuse.security.access:
//            ResourceAccessor

public class ResourceAccessFilter
    implements Filter {

  public ResourceAccessFilter() {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest hr = (HttpServletRequest) request;
    AccessLog log = accessor.beginAccess(hr, System.currentTimeMillis());
    chain.doFilter(request, response);
    accessor.endAccess(log, System.currentTimeMillis());
  }

  public void init(FilterConfig filterConfig)
      throws ServletException {
    accessor = ConfigLoader.getInstance().getConfig().getAccessor();
    filterConfig.getServletContext().setAttribute("ResourceAccessor", accessor);
    accessor.start();
  }

  public void destroy() {
    accessor.finish();
    accessor = null;
  }

  private ResourceAccessor accessor;
  public static final String SESSION_ATTRIBUTE_KEY = "ResourceAccessor";
}
