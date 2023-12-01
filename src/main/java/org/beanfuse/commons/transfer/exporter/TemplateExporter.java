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

import org.beanfuse.commons.transfer.Transfer;
import org.beanfuse.commons.transfer.TransferListener;
import org.beanfuse.commons.transfer.TransferResult;
import org.beanfuse.commons.transfer.exporter.writer.TemplateWriter;
import org.beanfuse.commons.transfer.exporter.writer.Writer;

import java.util.Locale;

// Referenced classes of package org.beanfuse.commons.transfer.exporter:
//            Exporter, Context

public class TemplateExporter
    implements Exporter {

  public TemplateExporter() {
  }

  public void setContext(Context context) {
    writer.setContext(context);
  }

  public Transfer addListener(TransferListener listener) {
    return this;
  }

  public Object getCurrent() {
    return null;
  }

  public String getDataName() {
    return null;
  }

  public int getFail() {
    return 0;
  }

  public String getFormat() {
    return writer.getFormat();
  }

  public Locale getLocale() {
    return null;
  }

  public int getSuccess() {
    return 0;
  }

  public int getTranferIndex() {
    return 0;
  }

  public void transfer(TransferResult tr) {
    writer.write();
    writer.close();
  }

  public void transferItem() {
  }

  public void setWriter(Writer writer) {
    if (writer instanceof TemplateWriter)
      this.writer = (TemplateWriter) writer;
  }

  protected TemplateWriter writer;
}
