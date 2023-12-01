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
package org.beanfuse.commons.bean.comparators;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Comparator;

// Referenced classes of package org.beanfuse.commons.bean.comparators:
//            CollatorStringComparator, StringComparator

public class PropertyComparator
    implements Comparator {

  public PropertyComparator(String cmpStr) {
    index = -1;
    if (StringUtils.isEmpty(cmpStr))
      return;
    if (StringUtils.contains(cmpStr, ','))
      throw new RuntimeException("PropertyComparator don't suport comma based order by.Use MultiPropertyComparator ");
    if (cmpStr.startsWith("[")) {
      index = NumberUtils.toInt(StringUtils.substringBetween(cmpStr, "[", "]"));
      cmpWhat = StringUtils.substringAfter(cmpStr, "]");
      if (cmpWhat.startsWith("."))
        cmpWhat = cmpWhat.substring(1);
    } else {
      cmpWhat = cmpStr;
    }
    isAsc = true;
    if (StringUtils.contains(cmpWhat, ' ')) {
      if (StringUtils.contains(cmpWhat, " desc"))
        isAsc = false;
      cmpWhat = cmpWhat.substring(0, cmpWhat.indexOf(' '));
    }
    stringComparator = new CollatorStringComparator(isAsc);
  }

  public PropertyComparator(String cmpWhat, boolean isAsc) {
    this(cmpWhat + " " + (isAsc ? "" : "desc"));
  }

  public int compare(Object arg0, Object arg1) {
    Object what0 = null;
    Object what1 = null;
    try {
      if (index > -1) {
        arg0 = ((Object[]) (Object[]) arg0)[index];
        arg1 = ((Object[]) (Object[]) arg1)[index];
        if (StringUtils.isEmpty(cmpWhat)) {
          what0 = arg0;
          what1 = arg1;
        }
      }
      if (StringUtils.isNotEmpty(cmpWhat)) {
        what0 = PropertyUtils.getProperty(arg0, cmpWhat);
        what1 = PropertyUtils.getProperty(arg1, cmpWhat);
      }
    } catch (Exception e) {
    }
    if (what0 == null && null == what1)
      return 0;
    if (null == comparator) {
      if ((what0 instanceof String) || (what1 instanceof String))
        return stringComparator.compare(what0, what1);
      if (what0 == null && null != what1)
        return isAsc ? -1 : 1;
      if (what0 != null && null == what1)
        return isAsc ? 1 : -1;
      if (isAsc)
        return ((Comparable) what0).compareTo(what1);
      else
        return ((Comparable) what1).compareTo(what0);
    } else {
      return comparator.compare(what0, what1);
    }
  }

  public Comparator getComparator() {
    return comparator;
  }

  public void setComparator(Comparator comparator) {
    this.comparator = comparator;
  }

  public StringComparator getStringComparator() {
    return stringComparator;
  }

  public void setStringComparator(StringComparator stringComparator) {
    this.stringComparator = stringComparator;
  }

  private String cmpWhat;
  private int index;
  private boolean isAsc;
  private Comparator comparator;
  private StringComparator stringComparator;
}
