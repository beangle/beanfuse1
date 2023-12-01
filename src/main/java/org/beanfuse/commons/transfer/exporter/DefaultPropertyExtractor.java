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
package org.beanfuse.commons.transfer.exporter;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

// Referenced classes of package org.beanfuse.commons.transfer.exporter:
//            PropertyExtractor, MessageResourceBuddle

public class DefaultPropertyExtractor
    implements PropertyExtractor {

  public DefaultPropertyExtractor() {
    buddle = null;
    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    numberFormat = NumberFormat.getInstance();
  }

  public DefaultPropertyExtractor(Locale locale, MessageResourceBuddle buddle) {
    this.buddle = null;
    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    numberFormat = NumberFormat.getInstance();
    setLocale(locale);
    setBuddle(buddle);
  }

  protected Object extract(Object target, String property)
      throws Exception {
    String subProperties[] = StringUtils.split(property, '.');
    StringBuffer passedProperty = new StringBuffer(subProperties[0]);
    for (int i = 0; i < subProperties.length - 1; i++)
      if (null != PropertyUtils.getProperty(target, passedProperty.toString()))
        passedProperty.append(".").append(subProperties[i + 1]);
      else
        return "";

    Object value = PropertyUtils.getProperty(target, property);
    if (value instanceof Number)
      return numberFormat.format(value);
    if (value instanceof Date)
      return dateFormat.format(value);
    if (value instanceof Boolean) {
      if (null == locale)
        return value;
      if (Boolean.TRUE.equals(value))
        return getMessage(locale, "yes");
      else
        return getMessage(locale, "no");
    } else {
      return value;
    }
  }

  public Object getPropertyValue(Object target, String property)
      throws Exception {
    return extract(target, property);
  }

  public String getPropertyIn(Collection collection, String property)
      throws Exception {
    StringBuffer sb = new StringBuffer();
    if (null != collection) {
      Object one;
      for (Iterator iter = collection.iterator(); iter.hasNext(); sb.append(extract(one, property)).append(","))
        one = iter.next();

    }
    if (sb.length() > 0)
      sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public DateFormat getDateFormat() {
    return dateFormat;
  }

  public void setDateFormat(DateFormat dateFormat) {
    this.dateFormat = dateFormat;
  }

  public NumberFormat getNumberFormat() {
    return numberFormat;
  }

  public void setNumberFormat(NumberFormat numberFormat) {
    this.numberFormat = numberFormat;
  }

  protected String getMessage(Locale locale, String key) {
    if (null != buddle)
      return buddle.getMessage(locale, key);
    else
      return key;
  }

  public MessageResourceBuddle getBuddle() {
    return buddle;
  }

  public void setBuddle(MessageResourceBuddle buddle) {
    this.buddle = buddle;
  }

  public Locale getLocale() {
    return locale;
  }

  protected Locale locale;
  protected MessageResourceBuddle buddle;
  protected DateFormat dateFormat;
  protected NumberFormat numberFormat;
}
