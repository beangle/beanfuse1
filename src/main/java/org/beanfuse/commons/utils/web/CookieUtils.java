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
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.beanfuse.commons.utils.web;

import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
  protected static final Logger logger;

  public CookieUtils() {
  }

  public static String getCookieValue(Cookie cookie) {
    try {
      return (new URLCodec()).decode(cookie.getValue(), "UTF-8");
    } catch (Exception var2) {
      return null;
    }
  }

  public static String getCookieValue(HttpServletRequest request, String cookieName) {
    try {
      Cookie cookie = getCookie(request, cookieName);
      return null == cookie ? null : (new URLCodec("UTF-8")).decode(cookie.getValue());
    } catch (Exception var3) {
      return null;
    }
  }

  public static Cookie getCookie(HttpServletRequest request, String name) {
    Cookie[] cookies = request.getCookies();
    Cookie returnCookie = null;
    if (cookies == null) {
      return returnCookie;
    } else {
      for (int i = 0; i < cookies.length; ++i) {
        Cookie thisCookie = cookies[i];
        if (thisCookie.getName().equals(name) && !thisCookie.getValue().equals("")) {
          returnCookie = thisCookie;
          break;
        }
      }

      return returnCookie;
    }
  }

  public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, String path, int age) throws Exception {
    if (logger.isDebugEnabled()) {
      logger.debug("add cookie[name:" + name + ",value=" + value + ",path=" + path + "]");
    }

    Cookie cookie = new Cookie(name, (new URLCodec()).encode(value, "UTF-8"));
    cookie.setSecure(false);
    cookie.setPath(path);
    cookie.setMaxAge(age);
    response.addCookie(cookie);
  }

  public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int age) throws Exception {
    String contextPath = request.getContextPath();
    if (!contextPath.endsWith("/")) {
      contextPath = contextPath + "/";
    }

    setCookie(request, response, name, value, contextPath, age);
  }

  public static void deleteCookieByName(HttpServletRequest request, HttpServletResponse response, String name) {
    deleteCookie(response, getCookie(request, name), "");
  }

  public static void deleteCookie(HttpServletResponse response, Cookie cookie, String path) {
    if (cookie != null) {
      cookie.setMaxAge(0);
      cookie.setPath(path);
      response.addCookie(cookie);
    }

  }

  static {
    logger = LoggerFactory.getLogger(CookieUtils.class);
  }
}
