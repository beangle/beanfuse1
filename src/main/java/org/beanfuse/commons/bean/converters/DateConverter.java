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
import org.apache.commons.lang.math.NumberUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateConverter
    implements Converter {

  public DateConverter() {
  }

  public Object convert(Class type, Object value) {
    if (value == null)
      return null;
    if (type == (java.util.Date.class))
      return convertToDate(type, value);
    if (type == (java.lang.String.class))
      return convertToString(type, value);
    else
      throw new ConversionException("Could not convert " + value.getClass().getName() + " to " + type.getName());
  }

  protected Object convertToDate(Class type, Object value) {
    if (StringUtils.isEmpty((String) value))
      return null;
    String dateStr = (String) value;
    String times[] = StringUtils.split(dateStr, " ");
    String dateElems[] = null;
    if (!StringUtils.contains(times[0], "-")) {
      dateElems = new String[3];
      dateElems[0] = StringUtils.substring(times[0], 0, 4);
      dateElems[1] = StringUtils.substring(times[0], 4, 6);
      dateElems[2] = StringUtils.substring(times[0], 6, 8);
    } else {
      dateElems = StringUtils.split(times[0], "-");
    }
    Calendar gc = GregorianCalendar.getInstance();
    gc.set(1, NumberUtils.toInt(dateElems[0]));
    gc.set(2, NumberUtils.toInt(dateElems[1]) - 1);
    gc.set(5, NumberUtils.toInt(dateElems[2]));
    if (times.length > 1 && StringUtils.isNotBlank(times[1])) {
      String timeElems[] = StringUtils.split(times[1], ":");
      if (timeElems.length > 0)
        gc.set(11, NumberUtils.toInt(timeElems[0]));
      if (timeElems.length > 1)
        gc.set(12, NumberUtils.toInt(timeElems[1]));
      if (timeElems.length > 2)
        gc.set(13, NumberUtils.toInt(timeElems[2]));
    }
    return gc.getTime();
  }

  protected Object convertToString(Class type, Object value) {
    return value.toString();
  }
}
