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
package org.beanfuse.commons.detail;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Referenced classes of package org.beanfuse.common.detail:
//            ValueObject, DetailException, Pagination

public class DetailObject extends ValueObject
    implements Cloneable, Serializable {

  public DetailObject() {
  }

  public DetailObject(String nvps)
      throws DetailException {
    super(nvps);
  }

  public void setValue(String key, String value)
      throws DetailException {
    if (key == null && "".equals(key)) {
      throw new DetailException("The key is null");
    } else {
      super.setValue(key, value);
    }
  }

  public String toString() {
    return getDetailString();
  }

  public DetailObject addObject(String name, Object value)
      throws DetailException {
    if (name == null) {
      throw new DetailException("Must give a name to the Object");
    } else {
      setValue(name, value);
      return this;
    }
  }

  public DetailObject addObject(Object obj)
      throws DetailException {
    if (obj == null) {
      throw new DetailException("Obj should not be null");
    } else {
      setValue("_OBJ", obj);
      return this;
    }
  }

  public Set getObjectNames() {
    return getKeys();
  }

  public Object getObject() {
    return getValue("_OBJ");
  }

  public Object getObject(String name) {
    return getValue(name);
  }

  public DetailObject addList(String name, List ls)
      throws DetailException {
    if (name == null) {
      throw new DetailException("Must give a name to the List Object");
    } else {
      setValue(name, ls);
      return this;
    }
  }

  public void addList(List ls)
      throws DetailException {
    if (ls == null) {
      throw new DetailException("Ls should not be null");
    } else {
      setValue("_LS", ls);
      return;
    }
  }

  public List getList() {
    return (List) getValue("_LS");
  }

  public List getList(String rsname) {
    return (List) getValue(rsname);
  }

  public DetailObject addPagination(String name, Pagination pag)
      throws DetailException {
    if (name == null) {
      throw new DetailException("Must give a name to the Pagination");
    } else {
      setValue(name, pag);
      return this;
    }
  }

  public DetailObject addPagination(Pagination pag)
      throws DetailException {
    if (pag == null) {
      throw new DetailException("Obj should not be null");
    } else {
      setValue("_PAG", pag);
      return this;
    }
  }

  public Pagination getPagination() {
    return (Pagination) getValue("_PAG");
  }

  public Pagination getPagination(String name) {
    return (Pagination) getValue(name);
  }

  public Map getDetail() {
    return super.getDetail();
  }

  private static final long serialVersionUID = 0x51363e4d3c8e99d3L;
  public static final String DEFAULT_LS_NAME = "_LS";
  public static final String DEFAULT_PAG_NAME = "_PAG";
  public static final String DEFAULT_OBJ_NAME = "_OBJ";
}
