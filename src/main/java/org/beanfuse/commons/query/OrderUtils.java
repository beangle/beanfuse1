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
import java.util.Iterator;
import java.util.List;

// Referenced classes of package org.beanfuse.commons.query:
//            Order

public final class OrderUtils {

  private OrderUtils() {
  }

  public static String toSortString(List orders) {
    if (null == orders || orders.isEmpty())
      return "";
    StringBuffer buf = new StringBuffer("order by ");
    Iterator iter = orders.iterator();
    do {
      if (!iter.hasNext())
        break;
      Order order = (Order) iter.next();
      switch (order.getDirection()) {
        case 2: // '\002'
          buf.append(order.getProperty()).append(" desc,");
          break;

        default:
          buf.append(order.getProperty()).append(',');
          break;
      }
    } while (true);
    return buf.substring(0, buf.length() - 1).toString();
  }

  public static List parser(String orderString) {
    if (StringUtils.isBlank(orderString))
      return new ArrayList();
    List orders = new ArrayList();
    String orderStrs[] = StringUtils.split(orderString, ",");
    for (int i = 0; i < orderStrs.length; i++) {
      String order = orderStrs[i];
      if (StringUtils.isBlank(order))
        continue;
      order = order.toLowerCase().trim();
      if (order.endsWith(" desc")) {
        orders.add(new Order(orderStrs[i].substring(0, order.indexOf(" desc")), 2));
        continue;
      }
      if (order.endsWith(" asc"))
        orders.add(new Order(orderStrs[i].substring(0, order.indexOf(" asc")), 1));
      else
        orders.add(new Order(orderStrs[i]));
    }

    return orders;
  }
}
