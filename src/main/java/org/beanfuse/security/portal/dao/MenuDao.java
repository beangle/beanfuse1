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
package org.beanfuse.security.portal.dao;

import org.beanfuse.security.portal.model.Menu;

import java.util.List;

public interface MenuDao {

  public abstract Menu get(Long long1);

  public abstract Menu getByName(String s);

  public abstract Menu getByCode(String s);

  public abstract List get(Menu menu);

  public abstract List get(Long along[]);

  public abstract List getChildren(String s);

  public abstract List getActiveChildren(String s);

  public abstract List getDescendants(String s, int i);

  public abstract List getActiveDescendants(String s, int i);

  public abstract void saveOrUpdate(Menu menu);
}
