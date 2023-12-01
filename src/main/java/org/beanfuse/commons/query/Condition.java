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
package org.beanfuse.commons.query;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Condition {

  public Condition() {
    values = new ArrayList(1);
  }

  public Condition(String content) {
    this(content, null, null);
  }

  public Condition(String content, Object param) {
    this(content, param, null);
  }

  public Condition(String content, Object param1, Object param2) {
    this(content, param1, param2, null);
  }

  public Condition(String content, Object param1, Object param2, Object param3) {
    values = new ArrayList(1);
    this.content = content;
    if (null != param1)
      values.add(param1);
    if (null != param2)
      values.add(param2);
    if (null != param3)
      values.add(param3);
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public List getValues() {
    return values;
  }

  public void setValues(List values) {
    this.values = values;
  }

  public boolean isNamed() {
    return !StringUtils.contains(content, "?");
  }

  public List getNamedParams() {
    if (!StringUtils.contains(content, ":"))
      return Collections.EMPTY_LIST;
    List params = new ArrayList();
    int index = 0;
    do {
      int colonIndex = content.indexOf(':', index);
      if (-1 == colonIndex)
        break;
      index = colonIndex + 1;
      do {
        if (index >= content.length())
          break;
        char c = content.charAt(index);
        if (!isValidIdentifierStarter(c))
          break;
        index++;
      } while (true);
      String paramName = content.substring(colonIndex + 1, index);
      if (!params.contains(paramName))
        params.add(paramName);
    } while (index < content.length());
    return params;
  }

  public boolean isValidIdentifierStarter(char ch) {
    return 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' || ch == '_' || '0' <= ch && ch <= '9';
  }

  public Condition addValue(Object value) {
    getValues().add(value);
    return this;
  }

  public String toString() {
    StringBuffer str = (new StringBuffer(content)).append(" ");
    for (Iterator iter = values.iterator(); iter.hasNext(); str.append(iter.next())) ;
    return str.toString();
  }

  public boolean equals(Object obj) {
    if (null == getContent() || !(obj instanceof Condition))
      return false;
    else
      return getContent().equals(((Condition) obj).getContent());
  }

  public int hashCode() {
    if (null == content)
      return 0;
    else
      return content.hashCode();
  }

  public static Condition eq(String content, Number value) {
    return new Condition(content + " = " + value);
  }

  public static Condition eq(String content, String value) {
    return new Condition(content + " = '" + value + "'");
  }

  public static Condition le(String content, Number value) {
    return new Condition(content + " <= " + value);
  }

  public static Condition ge(String content, Number value) {
    return new Condition(content + " >= " + value);
  }

  public static Condition ne(String content, Number value) {
    return new Condition(content + " <> " + value);
  }

  public static Condition like(String content, String value) {
    return new Condition(content + " like '%" + value + "%'");
  }

  private String content;
  private List values;
}
