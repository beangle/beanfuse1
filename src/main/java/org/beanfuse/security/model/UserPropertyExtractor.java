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
package org.beanfuse.security.model;

import org.beanfuse.commons.transfer.exporter.DefaultPropertyExtractor;

import java.util.Collection;
import java.util.Iterator;

// Referenced classes of package org.beanfuse.security.model:
//            User

public class UserPropertyExtractor extends DefaultPropertyExtractor {

  public UserPropertyExtractor() {
  }

  public Object getPropertyValue(Object target, String property)
      throws Exception {
    User user = (User) target;
    if ("roles".equals(property))
      return getPropertyIn(user.getRoles(), "name");
    if ("managers".equals(property))
      return getUserNames(user.getManagers());
    if ("mngRoles".equals(property))
      return getPropertyIn(user.getMngRoles(), "name");
    if ("mngUsers".equals(property))
      return getUserNames(user.getMngUsers());
    else
      return super.getPropertyValue(target, property);
  }

  public static final StringBuffer getUserNames(Collection users) {
    StringBuffer sb = new StringBuffer();
    Iterator iter = users.iterator();
    do {
      if (!iter.hasNext())
        break;
      User user = (User) iter.next();
      sb.append(user.getUserName()).append('(').append(user.getName()).append(')');
      if (iter.hasNext())
        sb.append(' ');
    } while (true);
    return sb;
  }
}
