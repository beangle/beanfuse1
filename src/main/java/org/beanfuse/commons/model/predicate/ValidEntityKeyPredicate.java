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

import org.beanfuse.commons.collection.predicates.NotEmptyStringPredicate;
import org.beanfuse.commons.collection.predicates.NotZeroNumberPredicate;
import org.apache.commons.collections.Predicate;

public class ValidEntityKeyPredicate
    implements Predicate {

  public ValidEntityKeyPredicate() {
  }

  public boolean evaluate(Object value) {
    return NotEmptyStringPredicate.INSTANCE.evaluate(value) || NotZeroNumberPredicate.INSTANCE.evaluate(value);
  }

  public static ValidEntityKeyPredicate getInstance() {
    return INSTANCE;
  }

  public static ValidEntityKeyPredicate INSTANCE = new ValidEntityKeyPredicate();

}
