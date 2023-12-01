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

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package org.beanfuse.commons.model.type:
//            AbstractType, Type, IdentifierType

public class ComponentType extends AbstractType {

  public boolean isComponentType() {
    return true;
  }

  public String getName() {
    return componentClass.toString();
  }

  public Class getReturnedClass() {
    return componentClass;
  }

  public ComponentType() {
    propertyTypes = new HashMap(0);
  }

  public ComponentType(Class componentClass) {
    propertyTypes = new HashMap(0);
    this.componentClass = componentClass;
  }

  public Type getPropertyType(String propertyName) {
    Type type = (Type) propertyTypes.get(propertyName);
    if (null == type) {
      Method getMethod = MethodUtils.getAccessibleMethod(componentClass, "get" + StringUtils.capitalize(propertyName), (Class[]) null);
      if (null != getMethod)
        return new IdentifierType(getMethod.getReturnType());
    }
    return type;
  }

  public Map getPropertyTypes() {
    return propertyTypes;
  }

  private Class componentClass;
  private Map propertyTypes;
}
