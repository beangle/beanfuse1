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
package org.beanfuse.commons.mvc.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter
    implements Filter {

  public EncodingFilter() {
    encoding = "utf-8";
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    request.setCharacterEncoding(encoding);
    chain.doFilter(request, response);
  }

  public void init(FilterConfig filterConfig)
      throws ServletException {
    encoding = filterConfig.getInitParameter("encoding");
  }

  public void destroy() {
    encoding = null;
  }

  protected String encoding;
}
