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
package org.beanfuse.commons.model.type;

import org.beanfuse.commons.model.Model;
import org.beanfuse.commons.model.ReflectHelper;

import java.util.HashMap;
import java.util.Map;

// Referenced classes of package org.beanfuse.commons.model.type:
//            AbstractType, Type, IdentifierType

public class EntityType extends AbstractType {

  public EntityType() {
    propertyTypes = new HashMap(0);
  }

  public EntityType(String entityName, Class entityClass) {
    propertyTypes = new HashMap(0);
    this.entityName = entityName;
    this.entityClass = entityClass;
  }

  public EntityType(Class entityClass) {
    propertyTypes = new HashMap(0);
    this.entityClass = entityClass;
    entityName = entityClass.getName();
  }

  public boolean isEntityType() {
    return true;
  }

  public Class getEntityClass() {
    return entityClass;
  }

  public void setEntityClass(Class entityClass) {
    this.entityClass = entityClass;
  }

  public Map getPropertyTypes() {
    return propertyTypes;
  }

  public void setPropertyTypes(Map propertyTypes) {
    this.propertyTypes = propertyTypes;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  public Type getPropertyType(String property) {
    Type type = (Type) propertyTypes.get(property);
    if (null == type) {
      Class propertyType = ReflectHelper.getProperty(entityClass, property);
      if (null != propertyType) {
        if (propertyType.isInterface())
          return Model.context.getEntityType(propertyType.getName());
        type = new IdentifierType(propertyType);
      }
    }
    if (null == type)
      logger.error("{} doesn't contains property {}", entityName, property);
    return type;
  }

  public String getEntityName() {
    return entityName;
  }

  public String getName() {
    return entityName;
  }

  public Class getReturnedClass() {
    return entityClass;
  }

  private String entityName;
  private Class entityClass;
  private Map propertyTypes;
}
