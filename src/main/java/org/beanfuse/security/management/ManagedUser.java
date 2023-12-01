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
package org.beanfuse.security.management;

import org.beanfuse.commons.model.LongIdEntity;

import java.util.Date;
import java.util.Set;

// Referenced classes of package org.beanfuse.security.management:
//            UserManager

public interface ManagedUser
    extends LongIdEntity {

  public abstract UserManager getCreator();

  public abstract void setCreator(UserManager usermanager);

  public abstract Set getManagers();

  public abstract void setManagers(Set set);

  public abstract Date getCreateAt();

  public abstract void setCreateAt(Date date);

  public abstract Date getModifyAt();

  public abstract void setModifyAt(Date date);
}
