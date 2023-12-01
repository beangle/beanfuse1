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

import org.beanfuse.commons.transfer.ItemTransfer;
import org.beanfuse.commons.transfer.importer.reader.ItemReader;
import org.beanfuse.commons.transfer.importer.reader.Reader;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

// Referenced classes of package org.beanfuse.commons.transfer.importer:
//            AbstractImporter, Importer

public abstract class ItemImporter extends AbstractImporter
    implements Importer, ItemTransfer {

  public ItemImporter() {
  }

  public void beforeImport() {
    descriptions = ((ItemReader) reader).readDescription();
    attrs = ((ItemReader) reader).readTitle();
  }

  public void setReader(Reader reader) {
    if (reader instanceof ItemReader)
      this.reader = (ItemReader) reader;
    else
      throw new RuntimeException("Expected LineReader but\uFF1A" + reader.getClass().getName());
  }

  public Map curDataMap() {
    Map params = new HashMap();
    for (int i = 0; i < attrs.length && i < values.length; i++)
      params.put(attrs[i], values[i]);

    return params;
  }

  public boolean changeCurValue(String attr, Object value) {
    for (int i = 0; i < attrs.length; i++)
      if (attrs[i].equals(attr)) {
        values[i] = value;
        return true;
      }

    return false;
  }

  public boolean read() {
    Object curData[] = (Object[]) (Object[]) reader.read();
    if (null == curData) {
      setCurrent(null);
      setCurData(null);
      return false;
    }
    if (curData.length < getAttrs().length) {
      Object newValues[] = new Object[getAttrs().length];
      System.arraycopy(((Object) (curData)), 0, ((Object) (newValues)), 0, curData.length);
      curData = newValues;
    }
    setCurData(((Object) (curData)));
    return true;
  }

  public boolean isDataValid() {
    boolean valid = false;
    int i = 0;
    do {
      if (i >= values.length)
        break;
      if (values[i] instanceof String) {
        String tt = (String) values[i];
        if (StringUtils.isNotBlank(tt)) {
          valid = true;
          break;
        }
      } else {
        if (null != values[i])
          valid = true;
        break;
      }
      i++;
    } while (true);
    return valid;
  }

  public Object getCurData() {
    return ((Object) (values));
  }

  public void setCurData(Object curData) {
    values = (Object[]) (Object[]) curData;
  }

  public String[] getAttrs() {
    return attrs;
  }

  public void setAttrs(String attrs[]) {
    this.attrs = attrs;
  }

  public String[] getDescriptions() {
    return descriptions;
  }

  public void setDescriptions(String descriptions[]) {
    this.descriptions = descriptions;
  }

  protected String descriptions[];
  protected String attrs[];
  protected Object values[];
}
