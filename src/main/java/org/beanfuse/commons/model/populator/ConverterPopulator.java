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

package org.beanfuse.commons.model.populator;

import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.model.Model;
import org.beanfuse.commons.model.predicate.ValidEntityKeyPredicate;
import org.beanfuse.commons.model.type.EntityType;
import org.beanfuse.commons.model.type.Type;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

public class ConverterPopulator implements Populator {
  protected static final Logger logger;
  public static final boolean TRIM_STR = true;

  public ConverterPopulator() {
  }

  public Object initPropertyPath(String attr, Object object, String entityName) {
    try {
      String[] attrs = StringUtils.split(attr, ".");
      if (attrs.length < 2) {
        return object;
      } else {
        int index = 0;
        Object property = null;
        Object propObj = object;

        Type propertyType;
        for (Type type = Model.context.getEntityType(entityName); index < attrs.length - 1; type = propertyType) {
          property = PropertyUtils.getProperty(propObj, attrs[index]);
          propertyType = ((Type) type).getPropertyType(attrs[index]);
          if (null == propertyType) {
            logger.error("Cannot find property type [{}] of {}", attrs[index], propObj.getClass());
            break;
          }

          if (null == property) {
            property = propertyType.newInstance();
            PropertyUtils.setProperty(propObj, attrs[index], property);
          }

          if (null == property) {
            break;
          }

          ++index;
          propObj = property;
        }

        return property;
      }
    } catch (Exception var10) {
      var10.printStackTrace();
      logger.error(object.toString(), var10);
      return null;
    }
  }

  public void populateValue(String attr, Object value, Object target, String entityName) {
    try {
      if (null != this.initPropertyPath(attr, target, entityName)) {
        BeanUtils.copyProperty(target, attr, value);
      }
    } catch (Exception var6) {
      logger.error("copy property failure:[class:" + entityName + " attr:" + attr + " value:" + value + "]:", var6);
    }

  }

  public void populateValue(String attr, Object value, Object target) {
    EntityType em = Model.context.getEntityType(target.getClass());
    this.populateValue(attr, value, target, em.getEntityName());
  }

  public Object populate(Map params, Object target) {
    return target instanceof HibernateProxy ? this.populate(params, target, ((HibernateProxy) target).getHibernateLazyInitializer().getEntityName()) : this.populate(params, target, Model.context.getEntityType(target.getClass()).getEntityName());
  }

  public Object populate(Map params, String entityName) {
    EntityType em = Model.context.getEntityType(entityName);
    if (null != em) {
      return this.populate(params, em.newInstance(), em.getEntityName());
    } else {
      throw new RuntimeException(entityName + " was not configured!");
    }
  }

  public Object populate(Map params, Class entityClass) {
    EntityType em = Model.context.getEntityType(entityClass);
    return this.populate(params, em.newInstance(), em.getEntityName());
  }

  public Object populate(Map params, Object entity, String entityName) {
    String key = null;
    if (entity instanceof Entity) {
      key = ((Entity) entity).key();
    }

    Iterator iter = params.keySet().iterator();

    while (true) {
      while (iter.hasNext()) {
        String attr = (String) iter.next();
        Object value = params.get(attr);
        if (value instanceof String) {
          if (StringUtils.isEmpty((String) value)) {
            value = null;
          } else {
            value = ((String) value).trim();
          }
        }

        if (null != key && key.equals(attr)) {
          if (ValidEntityKeyPredicate.INSTANCE.evaluate(value)) {
            this.setValue(key, value, entity);
          } else {
            try {
              PropertyUtils.setProperty(entity, key, (Object) null);
            } catch (Exception var12) {
              throw new RuntimeException(var12.getMessage());
            }
          }
        } else {
          if (-1 == attr.indexOf(46)) {
            this.setValue(attr, value, entity);
          } else {
            String parentAttr = StringUtils.substring(attr, 0, attr.lastIndexOf("."));

            try {
              Object parentEntity = this.initPropertyPath(attr, entity, entityName);
              if (null == parentEntity) {
                logger.error("error attr:[" + attr + "] value:[" + value + "]");
                continue;
              }

              if (parentEntity instanceof Entity) {
                String foreignKey = ((Entity) parentEntity).key();
                if (attr.endsWith("." + foreignKey)) {
                  if (null == value) {
                    this.setValue(parentAttr, (Object) null, entity);
                  } else {
                    Object foreignValue = BeanUtils.getProperty(entity, attr);
                    if (null != foreignValue) {
                      if (!foreignValue.toString().equals(value.toString())) {
                        this.setValue(parentAttr, (Object) null, entity);
                        this.initPropertyPath(attr, entity, entityName);
                        this.setValue(attr, value, entity);
                      }
                    } else {
                      this.setValue(attr, value, entity);
                    }
                  }
                } else {
                  this.setValue(attr, value, entity);
                }
              } else {
                this.setValue(attr, value, entity);
              }
            } catch (Exception var13) {
              logger.error("error attr:[" + attr + "] value:[" + value + "]", var13);
            }
          }

          if (logger.isDebugEnabled()) {
            logger.debug("populate attr:[" + attr + "] value:[" + value + "]");
          }
        }
      }

      return entity;
    }
  }

  private void setValue(String attr, Object value, Object target) {
    try {
      BeanUtils.copyProperty(target, attr, value);
    } catch (Exception var5) {
      logger.error("copy property failure:[class:" + target.getClass().getName() + " attr:" + attr + " value:" + value + "]:", var5);
    }

  }

  static {
    logger = LoggerFactory.getLogger(ConverterPopulator.class);
  }
}
