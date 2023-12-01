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

import java.util.Date;
import java.util.Set;

public interface AuthorityObject
    extends LongIdEntity {

  public abstract String getName();

  public abstract void setName(String s);

  public abstract Set getAuthorities();

  public abstract void setAuthorities(Set set);

  public abstract boolean isEnabled();

  public abstract Date getCreateAt();

  public abstract void setCreateAt(Date date);

  public abstract Date getModifyAt();

  public abstract void setModifyAt(Date date);

  public abstract String getRemark();

  public abstract void setRemark(String s);

  public static final Integer FREEZE = new Integer(0);
  public static final Integer ACTIVE = new Integer(1);

}
