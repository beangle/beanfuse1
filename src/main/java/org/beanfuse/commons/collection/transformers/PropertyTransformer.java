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
package org.beanfuse.commons.collection.transformers;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Transformer;

public class PropertyTransformer
    implements Transformer {

  public PropertyTransformer(String property) {
    this.property = property;
  }

  public PropertyTransformer() {
  }

  public Object transform(Object arg0) {
    try {
      return PropertyUtils.getProperty(arg0, this.property);
    } catch (Exception var3) {
      return null;
    }
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  private String property;
}
