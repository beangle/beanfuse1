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
package org.beanfuse.security.restriction.service.impl;

import org.beanfuse.commons.query.Condition;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.security.AuthorityException;
import org.beanfuse.security.restriction.Restriction;
import org.beanfuse.security.restriction.RestrictionItem;
import org.beanfuse.security.restriction.service.RestrictionApply;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RestrictionEntityQueryApply
    implements RestrictionApply {

  public RestrictionEntityQueryApply() {
  }

  public void apply(List restrictions, EntityQuery query) {
    String prefix = "(";
    StringBuffer conBuffer = new StringBuffer();
    List paramValues = new ArrayList();
    int index = 0;
    for (Iterator iterator = restrictions.iterator(); iterator.hasNext(); index++) {
      Restriction restriction = (Restriction) iterator.next();
      String patternContent = restriction.getPattern().getContent();
      patternContent = StringUtils.replace(patternContent, "{alias}", query.getAlias());
      String contents[] = StringUtils.split(StringUtils.replace(patternContent, " and ", "$"), "$");
      StringBuffer singleConBuf = new StringBuffer(prefix);
      for (int i = 0; i < contents.length; i++) {
        String content = contents[i];
        Condition c = new Condition(content);
        List params = c.getNamedParams();
        for (Iterator iter = params.iterator(); iter.hasNext(); ) {
          String param = (String) iter.next();
          RestrictionItem item = restriction.getItem(param);
          if (null != item && StringUtils.isNotEmpty(item.getValue())) {
            if (item.getValue().equals("*")) {
              content = "";
            } else {
              content = StringUtils.replace(content, ":" + item.getParam().getName(), ":" + item.getParam().getName() + index);
              paramValues.add(item.getParamValue());
            }
          } else {
            throw new AuthorityException(param + " had not been initialized");
          }
        }

        if (singleConBuf.length() > 1 && content.length() > 0)
          singleConBuf.append(" and ");
        singleConBuf.append(content);
      }

      if (singleConBuf.length() <= 1)
        continue;
      singleConBuf.append(')');
      if (conBuffer.length() > 0)
        conBuffer.append(" or ");
      conBuffer.append(singleConBuf.toString());
    }

    if (conBuffer.length() > 0) {
      Condition con = new Condition(conBuffer.toString());
      con.setValues(paramValues);
      query.add(con);
    }
  }

  public void apply(Restriction restriction, EntityQuery query) {
    apply(Collections.singletonList(restriction), query);
  }
}
