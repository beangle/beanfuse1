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

import org.beanfuse.commons.model.LongIdEntity;
import org.beanfuse.security.AuthorityObject;

public interface Authority
    extends LongIdEntity, Cloneable {

  public abstract Resource getResource();

  public abstract void setResource(Resource resource);

  public abstract void setAuthorityObject(AuthorityObject authorityobject);

  public abstract AuthorityObject getAuthorityObject();

  public abstract boolean isEnabled();

  public abstract void setEnabled(boolean flag);

  public abstract void merge(Authority authority);

  public abstract Object clone();
}
