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
package org.beanfuse.commons.transfer.importer;

import org.beanfuse.commons.model.populator.Populator;

// Referenced classes of package org.beanfuse.commons.transfer.importer:
//            Importer

public interface EntityImporter
    extends Importer {

  public abstract Class getEntityClass();

  public abstract void setEntityClass(Class class1);

  public abstract String[] getForeignerKeys();

  public abstract void addForeignedKeys(String s);

  public abstract void setPopulator(Populator populator);
}
