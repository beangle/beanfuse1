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
import java.util.*;

public abstract class ValueObject
    implements Cloneable, Serializable {

  public ValueObject() {
    detail = new HashMap();
  }

  public ValueObject(String nvps)
      throws DetailException {
    detail = new Hashtable();
    setDetailString(nvps);
  }

  protected Object getCopy() {
    try {
      return clone();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  protected void setDetailString(String nvps)
      throws DetailException {
    StringTokenizer aStringList = new StringTokenizer(nvps, "&");
    String errorList = "";
    while (aStringList.hasMoreTokens()) {
      String nvp = aStringList.nextToken();
      int i = nvp.indexOf("=");
      if (i == -1) {
        errorList.concat("Not NVP:" + nvp + "\n");
      } else {
        String key = nvp.substring(0, i);
        String value = nvp.substring(i + 1);
        setValue(Converter.decode(key), Converter.decode(value));
      }
    }
    if (errorList.length() > 0)
      throw new DetailException(errorList);
    else
      return;
  }

  protected String getDetailString() {
    return getDetailString(true);
  }

  protected String getDetailString(boolean trimSpace) {
    StringBuffer detailString = new StringBuffer();
    String tmpValue;
    String tmpKey;
    for (Iterator keySet = detail.keySet().iterator(); keySet.hasNext(); detailString.append(Converter.encode(tmpKey, "&=")).append("=").append(Converter.encode(tmpValue, "&="))) {
      tmpKey = (String) keySet.next();
      tmpValue = (String) detail.get(tmpKey);
      if (tmpValue == null)
        tmpValue = "";
      if (trimSpace) {
        tmpKey = tmpKey.trim();
        tmpValue = tmpValue.trim();
      }
      if (detailString.length() > 0)
        detailString.append("&");
    }

    return detailString.toString();
  }

  protected Set getKeys() {
    return detail.keySet();
  }

  protected void setValue(String key, Object value) {
    detail.put(key, value);
  }

  protected void setValue(Map value) {
    detail.putAll(value);
  }

  protected Object getValue(String key) {
    return detail.get(key);
  }

  protected Object getValue(String key, String defaultValue) {
    if (getValue(key) == null)
      return defaultValue;
    else
      return getValue(key);
  }

  protected void clear() {
    detail.clear();
  }

  protected Object clone()
      throws CloneNotSupportedException {
    return super.clone();
  }

  protected void delKey(String key) {
    detail.remove(key);
  }

  protected static ValueObject merge(ValueObject vo1, ValueObject vo2) {
    ValueObject resultvo = (ValueObject) vo1.getCopy();
    resultvo.setValue(vo2.getDetail());
    return resultvo;
  }

  protected void removeKey(String key) {
    detail.remove(key);
  }

  protected int size() {
    return detail.size();
  }

  public String toString() {
    return getDetailString();
  }

  protected Map getDetail() {
    return detail;
  }

  protected Map detail;
}
