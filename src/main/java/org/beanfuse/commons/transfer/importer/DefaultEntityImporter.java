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
package org.beanfuse.commons.transfer.importer;

import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.model.Model;
import org.beanfuse.commons.model.populator.EntityPopulator;
import org.beanfuse.commons.model.populator.Populator;
import org.beanfuse.commons.model.type.EntityType;
import org.beanfuse.commons.transfer.TransferMessage;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

// Referenced classes of package org.beanfuse.commons.transfer.importer:
//            ItemImporter, EntityImporter

public class DefaultEntityImporter extends ItemImporter
    implements EntityImporter {

  public DefaultEntityImporter(Class entityClass) {
    populator = new EntityPopulator();
    EntityType type = null;
    if (entityClass.isInterface())
      type = Model.context.getEntityType(entityClass.getName());
    else
      type = Model.context.getEntityType(entityClass);
    this.entityClass = type.getEntityClass();
    entityName = type.getEntityName();
  }

  public Class getEntityClass() {
    return entityClass;
  }

  public void setEntityClass(Class entityClass) {
    this.entityClass = entityClass;
  }

  public String getDataName() {
    return entityName;
  }

  public void beforeImport() {
    super.beforeImport();
    List errorAttrs = checkAttrs();
    if (!errorAttrs.isEmpty()) {
      transferResult.addFailure(TransferMessage.ERROR_ATTRS, errorAttrs.toString());
      throw new RuntimeException("error attrs:" + errorAttrs);
    } else {
      return;
    }
  }

  public void beforeImportItem() {
    entity = null;
  }

  public void transferItem() {
    int i = 0;
    try {
      if (null == entity)
        entity = (Entity) entityClass.newInstance();
      if (logger.isDebugEnabled())
        logger.debug("tranfer index:" + getTranferIndex() + ":" + ArrayUtils.toString(((Object) (values))));
      for (i = 0; i < attrs.length && i < values.length; i++)
        if (!StringUtils.isBlank(attrs[i])) {
          if (values[i] instanceof String) {
            String strValue = (String) values[i];
            if (StringUtils.isBlank(strValue))
              values[i] = null;
            else
              values[i] = StringUtils.trim(strValue);
          }
          if (null != values[i]) {
            if (values[i].equals("null"))
              values[i] = null;
            if (StringUtils.contains(attrs[i], '.') && null != foreignerKeys) {
              boolean isForeigner = false;
              for (int j = 0; j < foreignerKeys.length; j++)
                if (attrs[i].endsWith("." + foreignerKeys[j]))
                  isForeigner = true;

              if (isForeigner) {
                String parentPath = StringUtils.substringBeforeLast(attrs[i], ".");
                Object property = populator.initPropertyPath(parentPath, entity, entityClass.getName());
                if ((property instanceof Entity) && ((Entity) property).isPO()) {
                  populator.populateValue(parentPath, null, entity);
                  populator.initPropertyPath(attrs[i], entity, entityName);
                }
              }
            }
            populator.populateValue(attrs[i], values[i], entity);
          }
        }

    } catch (Exception e) {
      if (logger.isDebugEnabled())
        logger.error("error:", e);
      transferResult.addFailure(e.getMessage(), values[i]);
    }
  }

  protected List checkAttrs() {
    List errorAttrs = new ArrayList();
    try {
      Entity example = (Entity) entityClass.newInstance();
      for (int i = 0; i < attrs.length; i++)
        if (StringUtils.isNotBlank(attrs[i]))
          try {
            populator.initPropertyPath(attrs[i], example, entityName);
          } catch (Exception e) {
            errorAttrs.add(attrs[i]);
          }

    } catch (Exception e) {
      if (logger.isDebugEnabled())
        logger.error(entityClass.toString(), e);
    }
    return errorAttrs;
  }

  public Object getCurrent() {
    return entity;
  }

  public void setCurrent(Object object) {
    entity = (Entity) object;
  }

  public String[] getForeignerKeys() {
    return foreignerKeys;
  }

  public void setForeignerKeys(String foreignerKeys[]) {
    this.foreignerKeys = foreignerKeys;
  }

  public void addForeignedKeys(String foreignerKey) {
    if (null == foreignerKeys) {
      foreignerKeys = (new String[]{
          foreignerKey
      });
    } else {
      String newForeignerKeys[] = new String[foreignerKeys.length + 1];
      for (int i = 0; i < foreignerKeys.length; i++)
        newForeignerKeys[i] = foreignerKeys[i];

      newForeignerKeys[foreignerKeys.length] = foreignerKey;
      foreignerKeys = newForeignerKeys;
    }
  }

  public void setPopulator(Populator populator) {
    this.populator = populator;
  }

  protected Class entityClass;
  protected String entityName;
  protected Entity entity;
  protected String foreignerKeys[] = {
      "code"
  };
  protected Populator populator;
}
