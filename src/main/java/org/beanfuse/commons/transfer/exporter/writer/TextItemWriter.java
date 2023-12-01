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
package org.beanfuse.commons.transfer.exporter.writer;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

// Referenced classes of package org.beanfuse.commons.transfer.exporter.writer:
//            AbstractItemWriter

public class TextItemWriter extends AbstractItemWriter {

  public String getDelimiter() {
    return delimiter;
  }

  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  public TextItemWriter(OutputStream outputStream) {
    delimiter = ",";
    setOutputStream(outputStream);
  }

  public void write(Object obj) {
    if (null == obj)
      return;
    try {
      if (obj.getClass().isArray()) {
        Object values[] = (Object[]) (Object[]) obj;
        for (int i = 0; i < values.length; i++) {
          if (null == values[i])
            osw.write("");
          else
            osw.write(values[i].toString());
          if (i < values.length - 1)
            osw.write(delimiter);
          else
            osw.write("\r\n");
        }

      } else {
        osw.write(obj.toString());
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public void writeTitle(Object title) {
    write(title);
  }

  public void close() {
    try {
      osw.close();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public String getFormat() {
    return "txt";
  }

  public void setOutputStream(OutputStream outputStream) {
    this.outputStream = outputStream;
    osw = new OutputStreamWriter(outputStream);
  }

  private String delimiter;
  protected OutputStreamWriter osw;
}
