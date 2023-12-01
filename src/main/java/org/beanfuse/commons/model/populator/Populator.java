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
package org.beanfuse.commons.model.populator;

import java.util.Map;

public interface Populator {

  public abstract Object populate(Map map, Object obj);

  public abstract Object populate(Map map, Class class1);

  public abstract Object populate(Map map, Object obj, String s);

  public abstract Object populate(Map map, String s);

  public abstract void populateValue(String s, Object obj, Object obj1);

  public abstract void populateValue(String s, Object obj, Object obj1, String s1);

  public abstract Object initPropertyPath(String s, Object obj, String s1);
}
