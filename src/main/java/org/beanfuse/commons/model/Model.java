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
package org.beanfuse.commons.model;

import org.beanfuse.commons.model.context.EntityContext;
import org.beanfuse.commons.model.populator.Populator;

import java.util.Properties;

public class Model {

  public static final void init() {
    try {
      Properties props = new Properties();
      java.io.InputStream is = (org.beanfuse.commons.model.context.AbstractEntityContext.class).getResourceAsStream("/model-default.properties");
      if (null != is)
        props.load(is);
      is = (org.beanfuse.commons.model.context.AbstractEntityContext.class).getResourceAsStream("/model.properties");
      if (null != is)
        props.load(is);
      Class populatorClass = Class.forName((String) props.get("model.populatorClass"));
      populator = (Populator) populatorClass.newInstance();
      Class contextClass = Class.forName((String) props.get("model.contextClass"));
      context = (EntityContext) contextClass.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Model() {
  }

  public EntityContext getContext() {
    return context;
  }

  public void setContext(EntityContext context) {
    context = context;
  }

  public Populator getPopulator() {
    return populator;
  }

  public void setPopulator(Populator populator) {
    populator = populator;
  }

  public static final String NULL = "null";
  public static EntityContext context;
  public static Populator populator;
  public static final String POPULATOR_CLASS_NAME = "model.populatorClass";
  public static final String CONTEXT_CLASS_NAME = "model.contextClass";

  static {
    init();
  }
}
