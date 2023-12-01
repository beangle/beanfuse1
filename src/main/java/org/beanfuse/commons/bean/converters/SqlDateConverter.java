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
package org.beanfuse.commons.bean.converters;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;

import java.sql.Date;

public class SqlDateConverter
    implements Converter {

  public SqlDateConverter() {
  }

  public Object convert(Class type, Object value) {
    if (value == null)
      return null;
    if (type == (java.sql.Date.class))
      return convertToDate(value);
    if (type == (java.lang.String.class))
      return convertToString(value);
    else
      throw new ConversionException("Could not convert " + value.getClass().getName() + " to " + type.getName());
  }

  protected Object convertToDate(Object value) {
    if (StringUtils.isEmpty((String) value))
      return null;
    String dateStr = (String) value;
    if (!StringUtils.contains(dateStr, "-")) {
      StringBuffer dateBuf = new StringBuffer(dateStr);
      dateBuf.insert(6, '-');
      dateBuf.insert(4, '-');
      dateStr = dateBuf.toString();
    }
    return Date.valueOf(dateStr);
  }

  protected Object convertToString(Object value) {
    return value.toString();
  }
}
