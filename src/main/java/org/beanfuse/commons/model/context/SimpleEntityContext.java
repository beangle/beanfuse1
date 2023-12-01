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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class SimpleEntityContext extends AbstractEntityContext {

  public SimpleEntityContext() {
    Properties props = new Properties();
    try {
      java.io.InputStream is = (org.beanfuse.commons.model.context.AbstractEntityContext.class).getResourceAsStream("/model.properties");
      if (null != is)
        props.load(is);
      else
        logger.warn("cannot find model.properties using model-default.properties");
    } catch (IOException e) {
      logger.error("read error model.properties");
    }
    Set keys = props.keySet();
    String key;
    EntityType entityType;
    for (Iterator iterator = keys.iterator(); iterator.hasNext(); entityTypes.put(key, entityType)) {
      key = (String) iterator.next();
      String value = props.getProperty(key);
      entityType = new EntityType();
      entityType.setEntityName(key);
      try {
        entityType.setEntityClass(Class.forName(value));
      } catch (ClassNotFoundException e) {
        logger.error(value + " was not correct class name", e);
      }
      entityType.setPropertyTypes(new HashMap());
    }

  }

  private static final Logger logger;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.commons.model.context.SimpleEntityContext.class);
  }
}
