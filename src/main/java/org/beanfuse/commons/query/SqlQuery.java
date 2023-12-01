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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

// Referenced classes of package org.beanfuse.commons.query:
//            AbstractQuery, ConditionUtils, OrderUtils, Condition, 
//            Order

public class SqlQuery extends AbstractQuery {

  public SqlQuery() {
    conditions = new ArrayList();
    orders = new ArrayList();
    groups = new ArrayList();
  }

  public SqlQuery(String queryStr) {
    conditions = new ArrayList();
    orders = new ArrayList();
    groups = new ArrayList();
    this.queryStr = queryStr;
  }

  public SqlQuery add(Condition condition) {
    conditions.add(condition);
    return this;
  }

  public SqlQuery add(Collection cons) {
    conditions.addAll(cons);
    return this;
  }

  public SqlQuery addOrder(Order order) {
    if (null != order)
      orders.add(order);
    return this;
  }

  public SqlQuery addOrder(List orders) {
    if (null != orders)
      this.orders.addAll(orders);
    return this;
  }

  public String getSelect() {
    return select;
  }

  public void setSelect(String select) {
    if (null == select)
      this.select = select;
    else if (StringUtils.contains(select.toLowerCase(), "select"))
      this.select = select;
    else
      this.select = "select " + select;
  }

  public List getConditions() {
    return conditions;
  }

  public void setConditions(List conditions) {
    this.conditions = conditions;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    if (null == from)
      this.from = from;
    else if (StringUtils.contains(from.toLowerCase(), "from"))
      this.from = from;
    else
      this.from = " from " + from;
  }

  public List getOrders() {
    return orders;
  }

  public void setOrders(List orders) {
    this.orders = orders;
  }

  public List getGroups() {
    return groups;
  }

  public void setGroups(List groups) {
    this.groups = groups;
  }

  public SqlQuery groupBy(String what) {
    if (StringUtils.isNotEmpty(what))
      groups.add(what);
    return this;
  }

  public String toQueryString() {
    if (StringUtils.isNotEmpty(queryStr))
      return queryStr;
    else
      return genQueryString(true);
  }

  public String toCountString() {
    if (StringUtils.isNotEmpty(countStr))
      return countStr;
    else
      return "select count(*) from (" + genQueryString(false) + ")";
  }

  protected String genQueryString(boolean hasOrder) {
    if (null == from)
      return queryStr;
    StringBuffer buf = new StringBuffer(50);
    buf.append(select != null ? select : "").append(' ').append(from);
    if (!conditions.isEmpty())
      buf.append(" where ").append(ConditionUtils.toQueryString(conditions));
    if (!groups.isEmpty()) {
      buf.append(" group by ");
      String groupBy;
      for (Iterator iter = groups.iterator(); iter.hasNext(); buf.append(groupBy).append(','))
        groupBy = (String) iter.next();

      buf.deleteCharAt(buf.length() - 1);
    }
    if (hasOrder && !CollectionUtils.isEmpty(orders))
      buf.append(' ').append(OrderUtils.toSortString(orders));
    return buf.toString();
  }

  public Map getParams() {
    if (null == params)
      return ConditionUtils.getParamMap(conditions);
    else
      return params;
  }

  public static final String INNER_JOIN = " left join ";
  public static final String OUTER_JOIN = " outer join ";
  public static final String LEFT_OUTER_JOIN = " left outer join ";
  public static final String RIGHT_OUTER_JOIN = " right outer join ";
  protected String select;
  protected String from;
  protected List conditions;
  protected List orders;
  protected List groups;
}
