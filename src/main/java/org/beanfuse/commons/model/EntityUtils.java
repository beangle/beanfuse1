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

package org.beanfuse.commons.model;

import org.beanfuse.commons.model.predicate.EmptyKeyPredicate;
import org.beanfuse.commons.model.predicate.ValidEntityKeyPredicate;
import org.beanfuse.commons.model.predicate.ValidEntityPredicate;
import org.beanfuse.commons.model.type.EntityType;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

public class EntityUtils {
  protected static final Logger logger = LoggerFactory.getLogger(EntityUtils.class);

  public EntityUtils() {
  }

  /**
   * @deprecated
   */
  public static void merge(Object dest, Object orig) {
    String attr = "";

    try {
      Set attrs = PropertyUtils.describe(orig).keySet();
      attrs.remove("class");
      Iterator it = attrs.iterator();

      while (it.hasNext()) {
        attr = (String) it.next();
        if (PropertyUtils.isWriteable(orig, attr)) {
          Object value = PropertyUtils.getProperty(orig, attr);
          if (null != value) {
            if (value instanceof Component) {
              Object savedValue = PropertyUtils.getProperty(dest, attr);
              if (null != savedValue) {
                merge(savedValue, value);
              } else {
                PropertyUtils.setProperty(dest, attr, value);
              }
            } else if (!(value instanceof Collection)) {
              if (value instanceof Entity) {
                Serializable key = (Serializable) PropertyUtils.getProperty(value, ((Entity) value).key());
                if (null != key) {
                  if ((new EmptyKeyPredicate()).evaluate(key)) {
                    PropertyUtils.setProperty(dest, attr, (Object) null);
                  } else {
                    PropertyUtils.setProperty(dest, attr, value);
                  }
                }
              } else {
                PropertyUtils.setProperty(dest, attr, value);
              }
            }
          }
        }
      }

    } catch (Exception var7) {
      var7.printStackTrace();
      if (logger.isDebugEnabled()) {
        logger.debug("error occur in reflection of attr:" + attr + " of entity " + dest.getClass().getName());
      }

      logger.debug(ExceptionUtils.getStackTrace(var7));
    }
  }

  public static void evictEmptyProperty(Object entity) {
    if (null != entity) {
      boolean isEntity = false;
      if (entity instanceof Entity) {
        isEntity = true;
      }

      BeanMap map = new BeanMap(entity);
      List attList = new ArrayList();
      attList.addAll(map.keySet());
      attList.remove("class");
      Iterator iter = attList.iterator();

      while (true) {
        while (true) {
          String attr;
          Object value;
          do {
            do {
              if (!iter.hasNext()) {
                return;
              }

              attr = (String) iter.next();
            } while (!PropertyUtils.isWriteable(entity, attr));

            value = map.get(attr);
          } while (null == value);

          if (isEntity && attr.equals(((Entity) entity).key()) && !ValidEntityKeyPredicate.getInstance().evaluate(value)) {
            map.put(attr, (Object) null);
          }

          if (value instanceof Entity && !ValidEntityPredicate.getInstance().evaluate(value)) {
            map.put(attr, (Object) null);
          } else if (value instanceof Component) {
            evictEmptyProperty(value);
          }
        }
      }
    }
  }

  public static List extractIds(Collection entities) {
    List IdList = new ArrayList();
    Iterator iter = entities.iterator();

    while (iter.hasNext()) {
      Object element = iter.next();
      if (element instanceof Entity) {
        try {
          IdList.add(PropertyUtils.getProperty(element, ((Entity) element).key()));
        } catch (Exception var5) {
          var5.printStackTrace();
        }
      }
    }

    return IdList;
  }

  public static String getCommandName(Class clazz) {
    String name = clazz.getName();
    return StringUtils.uncapitalize(name.substring(name.lastIndexOf(46) + 1));
  }

  public static String getCommandName(String entityName) {
    return StringUtils.uncapitalize(StringUtils.substringAfterLast(entityName, "."));
  }

  public static String getCommandName(Object obj) {
    String name = obj.getClass().getName();
    int dollar = name.indexOf(36);
    if (-1 != dollar) {
      name = name.substring(name.lastIndexOf(46) + 1, dollar);
    } else {
      name = name.substring(name.lastIndexOf(46) + 1);
    }

    return StringUtils.uncapitalize(name);
  }

  public static Object getInstance(Class clazz) {
    return getEntityType(clazz).newInstance();
  }

  public static Entity getEntity(Class clazz) {
    return (Entity) getEntityType(clazz).newInstance();
  }

  public static Entity getEntity(Class clazz, Serializable id) {
    Entity entity = (Entity) getEntityType(clazz).newInstance();

    try {
      PropertyUtils.setProperty(entity, "id", id);
    } catch (Exception var4) {
      logger.error("initialize {} with id {} error", clazz, id);
    }

    return entity;
  }

  public static EntityType getEntityType(Class clazz) {
    EntityType type = null;
    if (clazz.isInterface()) {
      type = Model.context.getEntityType(clazz.getName());
    } else {
      type = Model.context.getEntityType(clazz);
    }

    return type;
  }

  public static String extractIdSeq(Collection entities) {
    if (null != entities && !entities.isEmpty()) {
      StringBuffer idBuf = new StringBuffer(",");
      Iterator iter = entities.iterator();

      while (iter.hasNext()) {
        Object element = iter.next();
        if (element instanceof Entity) {
          try {
            idBuf.append(PropertyUtils.getProperty(element, ((Entity) element).key()));
            idBuf.append(",");
          } catch (Exception var5) {
            throw new RuntimeException(var5.getMessage());
          }
        }
      }

      return idBuf.toString();
    } else {
      return "";
    }
  }

  public static boolean isEmpty(Entity entity, boolean ignoreDefault) {
    BeanMap map = new BeanMap(entity);
    List attList = new ArrayList();
    attList.addAll(map.keySet());
    attList.remove("class");

    try {
      Iterator iter = attList.iterator();

      while (iter.hasNext()) {
        String attr = (String) iter.next();
        if (PropertyUtils.isWriteable(entity, attr)) {
          Object value = map.get(attr);
          if (null != value) {
            if (!ignoreDefault) {
              return false;
            }

            if (value instanceof Number) {
              if (((Number) ((Number) value)).intValue() != 0) {
                return false;
              }
            } else {
              if (!(value instanceof String)) {
                return false;
              }

              String str = (String) value;
              if (StringUtils.isNotEmpty(str)) {
                return false;
              }
            }
          }
        }
      }
    } catch (Exception var8) {
      var8.printStackTrace();
    }

    return true;
  }

  /**
   * @deprecated
   */
  public static void populate(Map params, Object entity) {
    Model.populator.populate(params, entity);
  }

  public static String getEntityClassName(Class clazz) {
    String name = clazz.getName();
    int dollar = name.indexOf(36);
    return -1 != dollar ? name.substring(0, dollar) : name;
  }

}
