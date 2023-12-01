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
package org.beanfuse.security;

import org.beanfuse.commons.model.LongIdEntity;

import java.util.Set;

public interface Resource
    extends LongIdEntity {

  public abstract String getName();

  public abstract String getTitle();

  public abstract String getDescription();

  public abstract boolean isEnabled();

  public abstract void setEnabled(boolean flag);

  public abstract Set getCategories();

  public abstract void setCategories(Set set);

  public abstract Set getPatterns();

  public abstract void setPatterns(Set set);
}
