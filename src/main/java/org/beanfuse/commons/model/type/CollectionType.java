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

import java.lang.reflect.Array;

// Referenced classes of package org.beanfuse.commons.model.type:
//            AbstractType, Type

public class CollectionType extends AbstractType {

  public CollectionType() {
    isArray = false;
  }

  public boolean isCollectionType() {
    return true;
  }

  public String getName() {
    StringBuffer sb = new StringBuffer();
    if (null != collectionClass)
      sb.append(collectionClass.getName());
    sb.append(':');
    if (null != indexClass)
      sb.append(indexClass.getName());
    sb.append(':');
    sb.append(elementType.getName());
    return sb.toString();
  }

  public Type getPropertyType(String property) {
    return elementType;
  }

  public Type getElementType() {
    return elementType;
  }

  public void setElementType(Type elementType) {
    this.elementType = elementType;
  }

  public Class getIndexClass() {
    return indexClass;
  }

  public boolean hasIndex() {
    return null != indexClass && indexClass.equals(Integer.TYPE);
  }

  public Class getReturnedClass() {
    return collectionClass;
  }

  public Class getCollectionClass() {
    return collectionClass;
  }

  public void setCollectionClass(Class collectionClass) {
    this.collectionClass = collectionClass;
  }

  public boolean isArray() {
    return isArray;
  }

  public void setArray(boolean isArray) {
    this.isArray = isArray;
  }

  public Object newInstance() {
    if (isArray)
      return Array.newInstance(elementType.getReturnedClass(), 0);
    else
      return super.newInstance();
  }

  private Class collectionClass;
  private Type elementType;
  private Class indexClass;
  private boolean isArray;
}
