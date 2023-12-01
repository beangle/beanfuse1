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
package org.beanfuse.commons.model.context;

import org.beanfuse.commons.model.type.CollectionType;
import org.beanfuse.commons.model.type.ComponentType;
import org.beanfuse.commons.model.type.EntityType;
import org.beanfuse.commons.model.type.Type;
import org.hibernate.EntityMode;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

// Referenced classes of package org.beanfuse.commons.model.context:
//            AbstractEntityContext

public class HibernateEntityContext extends AbstractEntityContext {

  public HibernateEntityContext() {
    sessionFactory = null;
    collectionTypes = new HashMap();
  }

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
    if (null != sessionFactory && entityTypes.isEmpty()) {
      Map classMetadatas = sessionFactory.getAllClassMetadata();
      ClassMetadata cm;
      for (Iterator iter = classMetadatas.values().iterator(); iter.hasNext(); buildEntityType(cm.getEntityName()))
        cm = (ClassMetadata) iter.next();

      logger.info("success confiure {} entity types from hibernate!", new Integer(entityTypes.size()));
      logger.info("success confiure {} collection types from hibernate!", new Integer(collectionTypes.size()));
      if (logger.isDebugEnabled())
        loggerTypeInfo();
      collectionTypes.clear();
    }
  }

  private void loggerTypeInfo() {
    List names = new ArrayList(entityTypes.keySet());
    Collections.sort(names);
    EntityType entityType;
    for (Iterator iter = names.iterator(); iter.hasNext(); logger.debug("propertyType size:", new Integer(entityType.getPropertyTypes().size()))) {
      String entityName = (String) iter.next();
      entityType = (EntityType) entityTypes.get(entityName);
      logger.debug("entity:{}", entityType.getEntityName());
      logger.debug("class:{}", entityType.getEntityClass().getName());
    }

    names.clear();
    names.addAll(collectionTypes.keySet());
    Collections.sort(names);
    CollectionType collectionType;
    for (Iterator iter = names.iterator(); iter.hasNext(); logger.debug("elementType:{}", collectionType.getElementType().getReturnedClass())) {
      String entityName = (String) iter.next();
      collectionType = (CollectionType) collectionTypes.get(entityName);
      logger.debug("collection:{}", collectionType.getName());
      logger.debug("class:{}", collectionType.getCollectionClass());
    }

  }

  private EntityType buildEntityType(String entityName) {
    EntityType entityType = (EntityType) entityTypes.get(entityName);
    if (null == entityType) {
      ClassMetadata cm = sessionFactory.getClassMetadata(entityName);
      if (null == cm) {
        logger.error("Cannot find ClassMetadata for {}", entityName);
        return null;
      }
      entityType = new EntityType();
      entityType.setEntityName(cm.getEntityName());
      entityType.setEntityClass(cm.getMappedClass(EntityMode.POJO));
      entityTypes.put(cm.getEntityName(), entityType);
      Map propertyTypes = entityType.getPropertyTypes();
      String ps[] = cm.getPropertyNames();
      for (int i = 0; i < ps.length; i++) {
        org.hibernate.type.Type type = cm.getPropertyType(ps[i]);
        if (type.isEntityType()) {
          propertyTypes.put(ps[i], buildEntityType(type.getName()));
          continue;
        }
        if (type.isComponentType()) {
          propertyTypes.put(ps[i], buildComponentType(entityName, ps[i]));
          continue;
        }
        if (type.isCollectionType())
          propertyTypes.put(ps[i], buildCollectionType(defaultCollectionClass(type), entityName + "." + ps[i]));
      }

    }
    return entityType;
  }

  private CollectionType buildCollectionType(Class collectionClass, String role) {
    CollectionMetadata cm = sessionFactory.getCollectionMetadata(role);
    org.hibernate.type.Type type = cm.getElementType();
    EntityType elementType = null;
    if (type.isEntityType()) {
      elementType = (EntityType) entityTypes.get(type.getName());
      if (null == elementType)
        elementType = buildEntityType(type.getName());
    } else {
      elementType = new EntityType(type.getReturnedClass());
    }
    CollectionType collectionType = new CollectionType();
    collectionType.setElementType(elementType);
    collectionType.setArray(cm.isArray());
    collectionType.setCollectionClass(collectionClass);
    if (!collectionTypes.containsKey(collectionType.getName()))
      collectionTypes.put(collectionType.getName(), collectionType);
    return collectionType;
  }

  private ComponentType buildComponentType(String entityName, String propertyName) {
    EntityType entityType = (EntityType) entityTypes.get(entityName);
    if (null != entityType) {
      Type propertyType = (Type) entityType.getPropertyTypes().get(propertyName);
      if (null != propertyType)
        return (ComponentType) propertyType;
    }
    ClassMetadata cm = sessionFactory.getClassMetadata(entityName);
    org.hibernate.type.ComponentType hcType = (org.hibernate.type.ComponentType) cm.getPropertyType(propertyName);
    String propertyNames[] = hcType.getPropertyNames();
    ComponentType cType = new ComponentType(hcType.getReturnedClass());
    Map propertyTypes = cType.getPropertyTypes();
    for (int j = 0; j < propertyNames.length; j++) {
      org.hibernate.type.Type type = cm.getPropertyType(propertyName + "." + propertyNames[j]);
      if (type.isEntityType()) {
        propertyTypes.put(propertyNames[j], buildEntityType(type.getName()));
        continue;
      }
      if (type.isComponentType()) {
        propertyTypes.put(propertyNames[j], buildComponentType(entityName, propertyName + "." + propertyNames[j]));
        continue;
      }
      if (type.isCollectionType())
        propertyTypes.put(propertyNames[j], buildCollectionType(defaultCollectionClass(type), entityName + "." + propertyName + "." + propertyNames[j]));
    }

    return cType;
  }

  private Class defaultCollectionClass(org.hibernate.type.Type collectionType) {
    if (collectionType.isAnyType())
      return null;
    if ((org.hibernate.type.SetType.class).isAssignableFrom(collectionType.getClass()))
      return java.util.HashSet.class;
    if ((org.hibernate.type.MapType.class).isAssignableFrom(collectionType.getClass()))
      return java.util.HashMap.class;
    else
      return java.util.ArrayList.class;
  }

  private static final Logger logger;
  private SessionFactory sessionFactory;
  private Map collectionTypes;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.commons.model.context.HibernateEntityContext.class);
  }
}
