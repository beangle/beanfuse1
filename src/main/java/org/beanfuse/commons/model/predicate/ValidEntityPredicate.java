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
package org.beanfuse.commons.model.predicate;

import org.beanfuse.commons.model.Entity;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Predicate;

import java.io.Serializable;

// Referenced classes of package org.beanfuse.commons.model.predicate:
//            ValidEntityKeyPredicate

public class ValidEntityPredicate
    implements Predicate {

  public ValidEntityPredicate() {
  }

  public boolean evaluate(Object value) {
    if (null == value) {
      return false;
    } else {
      try {
        Serializable key = (Serializable) PropertyUtils.getProperty(value, ((Entity) value).key());
        return (new ValidEntityKeyPredicate()).evaluate(key);
      } catch (Exception var3) {
        return false;
      }
    }
  }


  public static ValidEntityPredicate getInstance() {
    return INSTANCE;
  }

  public static ValidEntityPredicate INSTANCE = new ValidEntityPredicate();

}
