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

import org.beanfuse.commons.transfer.exporter.Context;

import java.io.OutputStream;

// Referenced classes of package org.beanfuse.commons.transfer.exporter.writer:
//            ItemWriter

public abstract class AbstractItemWriter
    implements ItemWriter {

  public AbstractItemWriter() {
  }

  public void setOutputStream(OutputStream outputStream) {
    this.outputStream = outputStream;
  }

  public OutputStream getOutputStream() {
    return outputStream;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  protected OutputStream outputStream;
  protected Context context;
}
