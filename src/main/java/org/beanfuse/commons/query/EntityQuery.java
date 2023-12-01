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

import org.beanfuse.commons.model.Model;
import org.beanfuse.commons.model.type.EntityType;
import org.apache.commons.lang.StringUtils;

// Referenced classes of package org.beanfuse.commons.query:
//            SqlQuery

public class EntityQuery extends SqlQuery {

  public EntityQuery() {
  }

  public EntityQuery(String hql) {
    queryStr = hql;
  }

  public EntityQuery(String entityName, String alias) {
    EntityType type = Model.context.getEntityType(entityName);
    entityClass = type.getEntityClass();
    this.alias = alias;
    select = "select " + alias + " ";
    from = "from " + entityName + " " + alias;
  }

  public EntityQuery(Class entityClass, String alias) {
    EntityType type = Model.context.getEntityType(entityClass.getName());
    if (null == type)
      type = Model.context.getEntityType(entityClass);
    this.entityClass = type.getEntityClass();
    this.alias = alias;
    select = "select " + alias + " ";
    from = "from " + type.getEntityName() + " " + alias;
  }

  public EntityQuery join(String path, String alias) {
    from += " join " + path + " " + alias;
    return this;
  }

  public EntityQuery join(String joinMode, String path, String alias) {
    from += " " + joinMode + " join " + path + " " + alias;
    return this;
  }

  public String toCountString() {
    if (StringUtils.isNotEmpty(countStr))
      return countStr;
    StringBuffer countString = new StringBuffer("select count(*) ");
    String genQueryStr = genQueryString(false);
    if (StringUtils.isEmpty(genQueryStr))
      return "";
    String lowerCaseQueryStr = genQueryStr.toLowerCase();
    if (StringUtils.contains(lowerCaseQueryStr, " group "))
      return "";
    if (StringUtils.contains(lowerCaseQueryStr, " union "))
      return "";
    int indexOfFrom = lowerCaseQueryStr.indexOf("from");
    String selectWhat = lowerCaseQueryStr.substring(0, indexOfFrom);
    int indexOfDistinct = selectWhat.indexOf("distinct");
    if (-1 != indexOfDistinct) {
      if (StringUtils.contains(selectWhat, ","))
        return null;
      countString = new StringBuffer("select count(");
      countString.append(genQueryStr.substring(indexOfDistinct, indexOfFrom)).append(')');
    }
    countString.append(genQueryStr.substring(indexOfFrom));
    return countString.toString();
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public Class getEntityClass() {
    return entityClass;
  }

  public void setEntityClass(Class entityClass) {
    this.entityClass = entityClass;
  }

  protected Class entityClass;
  protected String alias;
}
