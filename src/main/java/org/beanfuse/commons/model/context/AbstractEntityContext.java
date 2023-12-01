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

import org.beanfuse.commons.model.type.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

// Referenced classes of package org.beanfuse.commons.model.context:
//            EntityContext

public abstract class AbstractEntityContext
    implements EntityContext {

  public AbstractEntityContext() {
    entityTypes = new HashMap();
    classEntityTypes = new HashMap();
  }

  public String[] getEntityNames(Class clazz) {
    return null;
  }

  public EntityType getEntityType(Class entityClass) {
    String className = entityClass.getName();
    EntityType type = (EntityType) entityTypes.get(className);
    if (null != type)
      return type;
    type = (EntityType) classEntityTypes.get(className);
    if (null == type) {
      List matched = new ArrayList();
      Iterator iter = entityTypes.values().iterator();
      do {
        if (!iter.hasNext())
          break;
        EntityType entityType = (EntityType) iter.next();
        if (className.equals(entityType.getEntityName()) || className.equals(entityType.getEntityClass().getName()))
          matched.add(entityType);
      } while (true);
      if (matched.size() > 1)
        throw new RuntimeException("multi-entityName for class:" + className);
      if (matched.isEmpty()) {
        EntityType tmp = new EntityType(entityClass);
        classEntityTypes.put(className, tmp);
        return tmp;
      }
      classEntityTypes.put(className, matched.get(0));
      type = (EntityType) matched.get(0);
    }
    return type;
  }

  public EntityType getEntityType(String entityName) {
    EntityType type = (EntityType) this.entityTypes.get(entityName);
    if (null != type) {
      return type;
    } else {
      type = (EntityType) this.classEntityTypes.get(entityName);
      if (null != type) {
        return type;
      } else {
        try {
          return new EntityType(Class.forName(entityName));
        } catch (ClassNotFoundException var4) {
          logger.error("system doesn't contains entity {}", entityName);
          return null;
        }
      }
    }
  }

  protected Map entityTypes;
  protected Map classEntityTypes;
  protected static final Logger logger;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.commons.model.context.AbstractEntityContext.class);
  }
}
