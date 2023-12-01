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
package org.beanfuse.commons.transfer.exporter;

import org.beanfuse.commons.transfer.TransferMessage;

// Referenced classes of package org.beanfuse.commons.transfer.exporter:
//            ItemExporter, PropertyExtractor

public class DefaultEntityExporter extends ItemExporter {

  public DefaultEntityExporter() {
  }

  public void transferItem() {
    Object values[] = new Object[attrs.length];
    for (int i = 0; i < values.length; i++)
      try {
        values[i] = propertyExtractor.getPropertyValue(getCurrent(), attrs[i]);
      } catch (Exception e) {
        transferResult.addFailure(TransferMessage.ERROR_ATTRS_EXPORT, "occur in get property :" + attrs[i] + " and exception:" + e.getMessage());
      }

    writer.write(((Object) (values)));
  }

  public PropertyExtractor getPropertyExtractor() {
    return propertyExtractor;
  }

  public void setPropertyExtractor(PropertyExtractor propertyExporter) {
    propertyExtractor = propertyExporter;
  }

  public String[] getAttrs() {
    return attrs;
  }

  public void setAttrs(String attrs[]) {
    this.attrs = attrs;
  }

  protected String attrs[];
  protected PropertyExtractor propertyExtractor;
}
