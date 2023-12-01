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

package org.beanfuse.commons.query;

import org.beanfuse.commons.model.Component;
import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.model.predicate.ValidEntityKeyPredicate;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class ConditionUtils {
  private static final Logger logger;

  private ConditionUtils() {
  }

  public static String toQueryString(List conditions) {
    if (null != conditions && !conditions.isEmpty()) {
      StringBuffer buf = new StringBuffer(" (1=1) ");
      Iterator iter = conditions.iterator();

      while (iter.hasNext()) {
        Condition conditioin = (Condition) iter.next();
        buf.append(" and (").append(conditioin.getContent()).append(')');
      }

      return buf.toString();
    } else {
      return "";
    }
  }

  public static List extractConditions(String alias, Entity entity) {
    if (null == entity) {
      return Collections.EMPTY_LIST;
    } else {
      List conditions = new ArrayList();
      if (StringUtils.isEmpty(alias)) {
        alias = "";
      } else {
        alias = alias + ".";
      }

      String attr = "";

      try {
        Map beanMap = PropertyUtils.describe(entity);
        Iterator iter = beanMap.keySet().iterator();

        while (iter.hasNext()) {
          attr = (String) iter.next();
          if (PropertyUtils.isWriteable(entity, attr)) {
            Object value = PropertyUtils.getProperty(entity, attr);
            if (null != value && !(value instanceof Collection)) {
              addAttrCondition(conditions, alias + attr, value);
            }
          }
        }
      } catch (Exception var7) {
        logger.debug("error occur in extractConditions for  bean {} with attr named {}", entity, attr);
      }

      return conditions;
    }
  }

  public static Map getParamMap(List conditions) {
    Map params = new HashMap();
    Iterator iter = conditions.iterator();

    while (iter.hasNext()) {
      Condition condition = (Condition) iter.next();
      params.putAll(getParamMap(condition));
    }

    return params;
  }

  public static Map getParamMap(Condition condition) {
    Map params = new HashMap();
    if (!StringUtils.contains(condition.getContent(), "?")) {
      List paramNames = condition.getNamedParams();
      if (paramNames.size() > condition.getValues().size()) {
        throw new RuntimeException("condition params not setted [" + condition.getContent() + "] with value:" + condition.getValues());
      }

      for (int i = 0; i < paramNames.size(); ++i) {
        params.put((String) paramNames.get(i), condition.getValues().get(i));
      }
    }

    return params;
  }

  private static void addAttrCondition(List conditions, String name, Object value) {
    if (value instanceof String) {
      if (StringUtils.isBlank((String) value)) {
        return;
      }

      Object v = "%" + value + "%";
      conditions.add(new Condition(name + " like :" + name.replace('.', '_'), v));
    } else {
      if (value instanceof Component) {
        conditions.addAll(extractComponent(name, (Component) value));
        return;
      }

      if (value instanceof Entity) {
        try {
          String key = ((Entity) value).key();
          value = PropertyUtils.getProperty(value, key);
          if (ValidEntityKeyPredicate.getInstance().evaluate(value)) {
            conditions.add(new Condition(name + "." + key + " = :" + name.replace('.', '_') + "_" + key, value));
          }
        } catch (Exception var4) {
          logger.warn("getProperty " + value + "error", var4);
        }
      } else {
        conditions.add(new Condition(name + " = :" + name.replace('.', '_'), value));
      }
    }

  }

  private static List extractComponent(String prefix, Component component) {
    if (null == component) {
      return Collections.EMPTY_LIST;
    } else {
      List conditions = new ArrayList();
      String attr = "";

      try {
        Map beanMap = PropertyUtils.describe(component);
        Iterator iter = beanMap.keySet().iterator();

        while (iter.hasNext()) {
          attr = (String) iter.next();
          if (!"class".equals(attr) && PropertyUtils.isWriteable(component, attr)) {
            Object value = PropertyUtils.getProperty(component, attr);
            if (value != null) {
              if (value instanceof Collection) {
                if (((Collection) value).isEmpty()) {
                }
              } else {
                addAttrCondition(conditions, prefix + "." + attr, value);
              }
            }
          }
        }
      } catch (Exception var7) {
        logger.debug("[extractComponent]:error occur in extractComponent of component:" + component + "with attr named :" + attr);
      }

      return conditions;
    }
  }

  static {
    logger = LoggerFactory.getLogger(ConditionUtils.class);
  }
}
