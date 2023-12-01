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

package org.beanfuse.commons.utils.query;

import org.beanfuse.commons.model.Model;
import org.beanfuse.commons.model.type.EntityType;
import org.beanfuse.commons.query.Condition;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.query.limit.Page;
import org.beanfuse.commons.query.limit.PageLimit;
import org.beanfuse.commons.utils.web.CookieUtils;
import org.beanfuse.commons.utils.web.RequestUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

public class QueryRequestSupport {
  protected static final Logger logger;
  public static final String PAGENO = "pageNo";
  public static final String PAGESIZE = "pageSize";
  public static boolean RESERVED_NULL;

  public QueryRequestSupport() {
  }

  public static void populateConditions(HttpServletRequest request, EntityQuery entityQuery) {
    entityQuery.add(extractConditions(request, entityQuery.getEntityClass(), entityQuery.getAlias(), (String) null));
  }

  public static void populateConditions(HttpServletRequest request, EntityQuery entityQuery, String exclusiveAttrNames) {
    entityQuery.add(extractConditions(request, entityQuery.getEntityClass(), entityQuery.getAlias(), exclusiveAttrNames));
  }

  public static List extractConditions(HttpServletRequest request, Class clazz, String prefix, String exclusiveAttrNames) {
    Object entity = null;

    try {
      if (clazz.isInterface()) {
        EntityType entityType = Model.context.getEntityType(clazz.getName());
        clazz = entityType.getEntityClass();
      }

      entity = clazz.newInstance();
    } catch (Exception var12) {
      throw new RuntimeException("[RequestUtil.extractConditions]: error in in initialize " + clazz);
    }

    List conditions = new ArrayList();
    Map params = RequestUtils.getParams(request, prefix, exclusiveAttrNames);
    Iterator iter = params.keySet().iterator();

    while (true) {
      String attr;
      String strValue;
      do {
        if (!iter.hasNext()) {
          return conditions;
        }

        attr = (String) iter.next();
        strValue = ((String) params.get(attr)).trim();
      } while (!StringUtils.isNotEmpty(strValue));

      try {
        if (RESERVED_NULL && "null".equals(strValue)) {
          conditions.add(new Condition(prefix + "." + attr + " is null"));
        } else {
          Model.populator.populateValue(attr, strValue, entity);
          Object settedValue = PropertyUtils.getProperty(entity, attr);
          if (null != settedValue) {
            if (settedValue instanceof String) {
              conditions.add(new Condition(prefix + "." + attr + " like :" + attr.replace('.', '_'), "%" + (String) settedValue + "%"));
            } else {
              conditions.add(new Condition(prefix + "." + attr + "=:" + attr.replace('.', '_'), settedValue));
            }
          }
        }
      } catch (Exception var11) {
        logger.debug("[populateFromParams]:error in populate entity " + prefix + "'s attribute " + attr);
      }
    }
  }

  public static PageLimit getPageLimit(HttpServletRequest request) {
    PageLimit limit = new PageLimit();
    limit.setPageNo(getPageNo(request));
    limit.setPageSize(getPageSize(request));
    return limit;
  }

  public static int getPageNo(HttpServletRequest request) {
    String pageNo = request.getParameter("pageNo");
    return StringUtils.isNotEmpty(pageNo) ? Integer.valueOf(pageNo) : 1;
  }

  public static int getPageSize(HttpServletRequest request) {
    String pageSize = request.getParameter("pageSize");
    if (StringUtils.isNotEmpty(pageSize)) {
      return Integer.valueOf(pageSize);
    } else {
      pageSize = CookieUtils.getCookieValue(request, "pageSize");
      return StringUtils.isNotEmpty(pageSize) ? Integer.valueOf(pageSize) : 20;
    }
  }

  public static void addPage(HttpServletRequest request, Collection objs) {
    if (objs instanceof Page) {
      Page page = (Page) objs;
      request.setAttribute("pageNo", new Integer(page.getPageNo()));
      request.setAttribute("pageSize", new Integer(page.getPageSize()));
      request.setAttribute("previousPageNo", new Integer(page.getPreviousPageNo()));
      request.setAttribute("nextPageNo", new Integer(page.getNextPageNo()));
      request.setAttribute("maxPageNo", new Integer(page.getMaxPageNo()));
      request.setAttribute("thisPageSize", new Integer(page.size()));
      request.setAttribute("totalSize", new Integer(page.getTotal()));
    }

  }

  public static void addDateIntervalCondition(HttpServletRequest request, EntityQuery query, String attr, String beginOn, String endOn) {
    addDateIntervalCondition(request, query, query.getAlias(), attr, beginOn, endOn);
  }

  public static void addDateIntervalCondition(HttpServletRequest request, EntityQuery query, String alias, String attr, String beginOn, String endOn) {
    String stime = request.getParameter(beginOn);
    String etime = request.getParameter(endOn);
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date sdate = null;
    Date edate = null;
    if (StringUtils.isNotBlank(stime)) {
      try {
        sdate = df.parse(stime);
      } catch (Exception var13) {
        logger.debug("wrong date format:" + stime);
      }
    }

    if (StringUtils.isNotBlank(etime)) {
      try {
        edate = df.parse(etime);
      } catch (Exception var12) {
        logger.debug("wrong date format:" + etime);
      }

      Calendar gc = new GregorianCalendar();
      gc.setTime(edate);
      gc.set(6, gc.get(6) + 1);
      edate = gc.getTime();
    }

    String objAttr = (null == alias ? query.getAlias() : alias) + "." + attr;
    if (null != sdate && null == edate) {
      query.add(new Condition(objAttr + " >=:sdate", sdate));
    } else if (null != sdate && null != edate) {
      query.add(new Condition(objAttr + " >=:sdate and " + objAttr + " <:edate", sdate, edate));
    } else if (null == sdate && null != edate) {
      query.add(new Condition(objAttr + " <:edate", edate));
    }

  }

  static {
    logger = LoggerFactory.getLogger(QueryRequestSupport.class);
    RESERVED_NULL = true;
  }
}
