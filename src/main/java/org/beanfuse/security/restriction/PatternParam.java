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
package org.beanfuse.security.restriction;

import org.beanfuse.commons.model.LongIdEntity;

// Referenced classes of package org.beanfuse.security.restriction:
//            ParamEditor

public interface PatternParam
    extends LongIdEntity {

  public abstract String getName();

  public abstract void setName(String s);

  public abstract String getType();

  public abstract void setType(String s);

  public abstract String getDescription();

  public abstract void setDescription(String s);

  public abstract ParamEditor getEditor();

  public abstract void setEditor(ParamEditor parameditor);

  public abstract void setMultiValue(boolean flag);

  public abstract boolean isMultiValue();
}
