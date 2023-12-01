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
package org.beanfuse.commons.utils.web;

import org.beanfuse.commons.bean.converters.DateConverter;
import org.beanfuse.commons.bean.converters.SqlDateConverter;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestUtils {

  public RequestUtils() {
  }

  public static void registerConverter() {
    ConvertUtils.register(new SqlDateConverter(), java.sql.Date.class);
    ConvertUtils.register(new DateConverter(), java.util.Date.class);
    ConvertUtils.register(new BooleanConverter(null), java.lang.Boolean.class);
    ConvertUtils.register(new IntegerConverter(null), java.lang.Integer.class);
    ConvertUtils.register(new LongConverter(null), java.lang.Long.class);
    ConvertUtils.register(new FloatConverter(null), java.lang.Float.class);
    ConvertUtils.register(new DoubleConverter(null), java.lang.Double.class);
  }

  public static Map getParams(HttpServletRequest request, String prefix) {
    return getParamsMap(request, prefix, null, true);
  }

  public static Map getParams(HttpServletRequest request, String prefix, String exclusiveAttrNames) {
    return getParamsMap(request, prefix, exclusiveAttrNames, true);
  }

  public static Map getParamsMap(HttpServletRequest request, String prefix, String exclusiveAttrNames) {
    return getParamsMap(request, prefix, exclusiveAttrNames, false);
  }

  public static Map getParamsMap(HttpServletRequest request, String prefix, String exclusiveAttrNames, boolean stripPrefix) {
    Map params = new HashMap();
    if (StringUtils.isNotEmpty(exclusiveAttrNames))
      exclusiveAttrNames = "," + exclusiveAttrNames + ",";
    else
      exclusiveAttrNames = null;
    Enumeration names = request.getParameterNames();
    do {
      if (!names.hasMoreElements())
        break;
      String attr = (String) names.nextElement();
      if (attr.indexOf(prefix + ".") == 0 && (null == exclusiveAttrNames || !StringUtils.contains(exclusiveAttrNames, "," + attr + ",")))
        params.put(stripPrefix ? ((Object) (attr.substring(prefix.length() + 1))) : ((Object) (attr)), request.getParameter(attr));
    } while (true);
    String queryString = request.getQueryString();
    if (StringUtils.isNotEmpty(queryString)) {
      String paramPairs[] = StringUtils.split(queryString, "&");
      for (int i = 0; i < paramPairs.length; i++) {
        String paramPair = paramPairs[i];
        if (paramPair.indexOf(prefix + ".") != 0)
          continue;
        int equalIndex = paramPair.indexOf("=");
        if (-1 == equalIndex)
          continue;
        String param = paramPair.substring(0, equalIndex);
        if (StringUtils.contains(exclusiveAttrNames, "," + param + ","))
          continue;
        try {
          params.put(stripPrefix ? ((Object) (param.substring(prefix.length() + 1))) : ((Object) (param)), (new URLCodec()).decode(paramPair.substring(equalIndex + 1), "UTF-8"));
        } catch (Exception e) {
        }
      }

    }
    return params;
  }

  public static Object get(HttpServletRequest request, Class clazz, String name) {
    String strValue = get(request, name);
    if (StringUtils.isNotBlank(strValue))
      return ConvertUtils.convert(strValue, clazz);
    else
      return null;
  }

  public static String get(HttpServletRequest request, String name) {
    return request.getParameter(name);
  }

  public static Boolean getBoolean(HttpServletRequest request, String name) {
    return (Boolean) get(request, java.lang.Boolean.class, name);
  }

  public static Boolean getBooleanValue(HttpServletRequest request, String name) {
    String strValue = request.getParameter(name);
    if (StringUtils.isEmpty(strValue))
      return Boolean.FALSE;
    else
      return (Boolean) ConvertUtils.convert(strValue, java.lang.Boolean.class);
  }

  public static Date getDate(HttpServletRequest request, String name) {
    return (Date) get(request, java.sql.Date.class, name);
  }

  public static Float getFloat(HttpServletRequest request, String name) {
    return (Float) get(request, java.lang.Float.class, name);
  }

  public static Integer getInteger(HttpServletRequest request, String name) {
    return (Integer) get(request, java.lang.Integer.class, name);
  }

  public static Long getLong(HttpServletRequest request, String name) {
    return (Long) get(request, java.lang.Long.class, name);
  }

  public static String getIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
      ip = request.getHeader("Proxy-Client-IP");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
      ip = request.getHeader("WL-Proxy-Client-IP");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
      ip = request.getRemoteAddr();
    return ip;
  }

  public static String getRequestAction(HttpServletRequest request) {
    String actionName = request.getServletPath();
    if (actionName.startsWith("/"))
      actionName = actionName.substring(1);
    return actionName;
  }

  public static String getRequestURI(HttpServletRequest request) {
    return getRequestAction(request);
  }

  public static String encodeAttachName(HttpServletRequest request, String attch_name)
      throws Exception {
    attch_name = new String(attch_name.getBytes(), "ISO8859-1");
    return attch_name;
  }

  protected static final Logger logger;
  public static final String DEFAULT_ENCODING_SCHEME = "UTF-8";
  public static final String MULTIPART = "multipart/";

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.commons.utils.web.RequestUtils.class);
    registerConverter();
  }
}
