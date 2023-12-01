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

package org.beanfuse.commons.utils.persistence.hibernate;

import org.beanfuse.commons.model.Component;
import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.model.predicate.ValidEntityKeyPredicate;
import org.beanfuse.commons.query.Order;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.*;

public class CriterionUtils {
  public static final Logger logger;

  public CriterionUtils() {
  }

  public static void addCriterionsFor(Criteria criteria, List criterions) {
    Iterator iter = criterions.iterator();

    while (iter.hasNext()) {
      criteria.add((Criterion) iter.next());
    }

  }

  public static List getEntityCriterions(Object entity) {
    return getEntityCriterions("", entity, (String[]) null, MatchMode.ANYWHERE, true);
  }

  public static List getEntityCriterions(Object entity, boolean ignoreZero) {
    return getEntityCriterions("", entity, (String[]) null, MatchMode.ANYWHERE, ignoreZero);
  }

  public static List getEntityCriterions(Object entity, String[] excludePropertes) {
    return getEntityCriterions("", entity, excludePropertes, MatchMode.ANYWHERE, true);
  }

  public static List getEntityCriterions(String nestedName, Object entity) {
    return getEntityCriterions(nestedName, entity, (String[]) null, MatchMode.ANYWHERE, true);
  }

  public static List getEntityCriterions(String nestedName, Object entity, String[] excludePropertes) {
    return getEntityCriterions(nestedName, entity, excludePropertes, MatchMode.ANYWHERE, true);
  }

  public static Example getExampleCriterion(Object entity) {
    return getExampleCriterion(entity, (String[]) null, MatchMode.ANYWHERE);
  }

  public static Example getExampleCriterion(Object entity, String[] excludePropertes, MatchMode mode) {
    Example example = Example.create(entity).setPropertySelector(new NotEmptyPropertySelector());
    if (null != mode) {
      example.enableLike(mode);
    }

    if (null != excludePropertes) {
      for (int i = 0; i < excludePropertes.length; ++i) {
        example.excludeProperty(excludePropertes[i]);
      }
    }

    return example;
  }

  public static List getEntityCriterions(String nestedName, Object entity, String[] excludePropertes, MatchMode mode, boolean ignoreZero) {
    if (null == entity) {
      return Collections.EMPTY_LIST;
    } else {
      List criterions = new ArrayList();
      BeanMap map = new BeanMap(entity);
      Set keySet = map.keySet();
      Collection properties = null;
      if (null != excludePropertes) {
        properties = CollectionUtils.subtract(keySet, Arrays.asList(excludePropertes));
      } else {
        List proList = new ArrayList();
        proList.addAll(keySet);
        properties = proList;
      }

      ((Collection) properties).remove("class");
      Iterator iter = ((Collection) properties).iterator();

      while (iter.hasNext()) {
        String propertyName = (String) iter.next();
        if (PropertyUtils.isWriteable(entity, propertyName)) {
          Object value = map.get(propertyName);
          addCriterion(nestedName, entity, excludePropertes, propertyName, value, criterions, mode, ignoreZero);
        }
      }

      return criterions;
    }
  }

  public static List getEqCriterions(Object entity, String[] properties) {
    List criterions = new ArrayList();
    BeanMap map = new BeanMap(entity);

    for (int i = 0; i < properties.length; ++i) {
      criterions.add(Restrictions.eq(properties[i], map.get(properties[i])));
    }

    return criterions;
  }

  public static List getForeignerCriterions(Object entity) {
    BeanMap map = new BeanMap(entity);
    return getForeignerCriterions(entity, (Collection) map.keySet());
  }

  public static List getForeignerCriterions(Object entity, Collection properties) {
    List criterions = new ArrayList();
    BeanMap map = new BeanMap(entity);
    Iterator iter = properties.iterator();

    while (iter.hasNext()) {
      String propertyName = (String) iter.next();
      Object foreigner = map.get(propertyName);
      if (foreigner instanceof Entity) {
        BeanMap foreignerMap = new BeanMap(foreigner);
        Object foreignKey = foreignerMap.get(((Entity) foreigner).key());
        if (ValidEntityKeyPredicate.getInstance().evaluate(foreignKey)) {
          criterions.add(Restrictions.eq(propertyName + "." + ((Entity) foreigner).key(), foreignKey));
        }
      }
    }

    return criterions;
  }

  public static List getForeignerCriterions(Object entity, String[] properties) {
    return getForeignerCriterions(entity, (Collection) Arrays.asList(properties));
  }

  public static List getLikeCriterions(Object entity, String[] Properties) {
    return getLikeCriterions(entity, Properties, MatchMode.ANYWHERE);
  }

  public static List getLikeCriterions(Object entity, String[] properties, MatchMode mode) {
    List criterions = new ArrayList();
    BeanMap map = new BeanMap(entity);

    for (int i = 0; i < properties.length; ++i) {
      Object value = map.get(properties[i]);
      if (value instanceof String && StringUtils.isNotEmpty((String) value)) {
        criterions.add(Restrictions.like(properties[i], (String) value, mode));
      }
    }

    return criterions;
  }

  public static Map getParamsMap(Entity entity) {
    return null == entity ? Collections.EMPTY_MAP : getParamsMap(entity, MatchMode.ANYWHERE);
  }

  public static Map getParamsMap(Entity entity, MatchMode mode) {
    if (null == entity) {
      return Collections.EMPTY_MAP;
    } else {
      Map datas = new HashMap();
      String attr = "";

      try {
        Map beanMap = PropertyUtils.describe(entity);
        Iterator iter = beanMap.keySet().iterator();

        while (iter.hasNext()) {
          attr = (String) iter.next();
          Object value = PropertyUtils.getProperty(entity, attr);
          if (value != null) {
            addTrivialAttr(datas, attr, value, mode);
            if (value instanceof Entity) {
              String key = ((Entity) value).key();
              value = PropertyUtils.getProperty(entity, attr + "." + key);
              if (ValidEntityKeyPredicate.getInstance().evaluate(value)) {
                datas.put(attr + "." + key, value);
              }
            }
          }
        }

        return datas;
      } catch (Exception var8) {
        System.out.println("[converToMap]:error occur in converToMap of bean" + entity + "with attr named " + attr);
        var8.printStackTrace();
        return Collections.EMPTY_MAP;
      }
    }
  }

  static Criterion eqCriterion(String name, Object value) {
    logger.debug("[CriterionUtils]:" + name + ":" + value);
    return Restrictions.eq(name, value);
  }

  static Criterion likeCriterion(String name, String value, MatchMode mode) {
    logger.debug("[CriterionUtils]:" + name + ":" + value);
    return Restrictions.like(name, value, mode);
  }

  private static void addCriterion(String nestedName, Object entity, String[] excludePropertes, String path, Object value, List criterions, MatchMode mode, boolean ignoreZero) {
    if (null != value) {
      addPrimativeCriterion(nestedName + path, value, criterions, ignoreZero);
      if (value instanceof String) {
        if (StringUtils.isNotEmpty((String) value)) {
          criterions.add(likeCriterion(nestedName + path, (String) value, mode));
        }
      } else if (value instanceof Entity) {
        BeanMap foreignerMap = new BeanMap(value);
        Object foreignKey = foreignerMap.get(((Entity) value).key());
        if (ValidEntityKeyPredicate.getInstance().evaluate(foreignKey)) {
          criterions.add(eqCriterion(nestedName + path + "." + ((Entity) value).key(), foreignKey));
        }
      } else if (value instanceof Component) {
        criterions.addAll(getComponentCriterions(nestedName, entity, path, excludePropertes, mode, ignoreZero));
      }

    }
  }

  private static void addPrimativeCriterion(String name, Object value, List criterions, boolean ignoreZero) {
    Criterion criterion = null;
    if (value instanceof Number) {
      if (ignoreZero) {
        if (0 != ((Number) value).intValue()) {
          criterion = eqCriterion(name, value);
        }
      } else {
        criterion = eqCriterion(name, value);
      }
    }

    if (value instanceof Character || value instanceof Boolean) {
      criterion = eqCriterion(name, value);
    }

    if (value instanceof Date) {
      criterion = eqCriterion(name, value);
    }

    if (null != criterion) {
      criterions.add(criterion);
    }

  }

  private static void addTrivialAttr(Map datas, String name, Object value, MatchMode mode) {
    if (value instanceof Number && ((Number) value).intValue() != 0) {
      datas.put(name, value);
    }

    String key;
    if (value instanceof String && StringUtils.isNotBlank((String) value)) {
      key = (String) value;
      if (mode.equals(MatchMode.ANYWHERE)) {
        value = "%" + key + "%";
      } else if (mode.equals(MatchMode.START)) {
        value = key + "%";
      } else if (mode.equals(MatchMode.END)) {
        value = "%" + key;
      }

      datas.put(name, value);
    }

    if (value instanceof Component) {
      datas.putAll(converToMap(name, (Component) value, mode));
    }

    if (value instanceof Entity) {
      try {
        key = ((Entity) value).key();
        value = PropertyUtils.getProperty(value, key);
        if (ValidEntityKeyPredicate.getInstance().evaluate(value)) {
          datas.put(name + "." + key, value);
        }
      } catch (Exception var5) {
      }
    }

  }

  private static Map converToMap(String prefix, Component component, MatchMode mode) {
    if (null == component) {
      return Collections.EMPTY_MAP;
    } else {
      Map datas = new HashMap();
      String attr = "";

      try {
        Map beanMap = PropertyUtils.describe(component);
        Iterator iter = beanMap.keySet().iterator();

        while (iter.hasNext()) {
          attr = (String) iter.next();
          Object value = PropertyUtils.getProperty(component, attr);
          if (value != null) {
            addTrivialAttr(datas, prefix + "." + attr, value, mode);
          }
        }

        return datas;
      } catch (Exception var8) {
        System.out.println("[converToMap]:error occur in converToMap of component" + component + "with attr named " + attr);
        var8.printStackTrace();
        return Collections.EMPTY_MAP;
      }
    }
  }

  private static List getComponentCriterions(String nestedName, Object entity, String property, String[] excludePropertes, MatchMode mode, boolean ignoreZero) {
    List criterions = new ArrayList();
    Component component = null;

    try {
      component = (Component) PropertyUtils.getProperty(entity, property);
    } catch (Exception var15) {
      return Collections.EMPTY_LIST;
    }

    if (null == component) {
      return Collections.EMPTY_LIST;
    } else {
      BeanMap map = new BeanMap(component);
      Set properties = map.keySet();
      Set excludeSet = null;
      if (null != excludePropertes) {
        excludeSet = new HashSet();
        ((Set) excludeSet).addAll(Arrays.asList(excludePropertes));
      } else {
        excludeSet = Collections.EMPTY_SET;
      }

      Iterator iter = properties.iterator();

      while (iter.hasNext()) {
        String propertyName = (String) iter.next();
        String cascadeName = property + "." + propertyName;
        if (!((Set) excludeSet).contains(cascadeName) && !"class".equals(propertyName) && PropertyUtils.isWriteable(component, propertyName)) {
          Object value = map.get(propertyName);
          addCriterion(nestedName, entity, excludePropertes, cascadeName, value, criterions, mode, ignoreZero);
        }
      }

      return criterions;
    }
  }

  public static void addSortListFor(Criteria criteria, List sortList) {
    if (null != sortList) {
      Iterator iter = sortList.iterator();

      while (iter.hasNext()) {
        Order order = (Order) iter.next();
        switch (order.getDirection()) {
          case 2:
            criteria.addOrder(org.hibernate.criterion.Order.desc(order.getProperty()));
            break;
          default:
            criteria.addOrder(org.hibernate.criterion.Order.asc(order.getProperty()));
        }
      }
    }

  }

  static {
    logger = LoggerFactory.getLogger(CriterionUtils.class);
  }
}
