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
import org.beanfuse.commons.transfer.exporter.writer.ItemWriter;
import org.beanfuse.commons.transfer.exporter.writer.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

// Referenced classes of package org.beanfuse.commons.transfer.exporter:
//            Exporter, Context

public class ItemExporter
    implements Exporter {

  public ItemExporter() {
    listeners = new Vector();
    success = 0;
    fail = 0;
    index = -1;
  }

  public Transfer addListener(TransferListener listener) {
    listeners.add(listener);
    listener.setTransfer(this);
    return this;
  }

  public Object getCurrent() {
    return current;
  }

  public int getFail() {
    return fail;
  }

  public Locale getLocale() {
    return Locale.getDefault();
  }

  public int getSuccess() {
    return success;
  }

  public int getTranferIndex() {
    return index;
  }

  public void transfer(TransferResult tr) {
    if (null == titles || titles.length == 0)
      return;
    transferResult = tr;
    tr.setTransfer(this);
    beforeExport();
    TransferListener listener;
    for (Iterator iter = listeners.iterator(); iter.hasNext(); listener.startTransfer(tr))
      listener = (TransferListener) iter.next();

    while (hasNext()) {
      next();
      int errors = tr.errors();
      for (Iterator iter = listeners.iterator(); iter.hasNext(); listener.startTransferItem(tr))
        listener = (TransferListener) iter.next();

      long transferItemStart = System.currentTimeMillis();
      transferItem();
      if (tr.errors() == errors)
        success++;
      else
        fail++;
      logger.debug("tranfer item:{}  take time:{}", String.valueOf(getTranferIndex()), String.valueOf(System.currentTimeMillis() - transferItemStart));
      Iterator iter = listeners.iterator();
      while (iter.hasNext()) {
        listener = (TransferListener) iter.next();
        listener.endTransferItem(tr);
      }
    }
    for (Iterator iter = listeners.iterator(); iter.hasNext(); listener.endTransfer(tr))
      listener = (TransferListener) iter.next();

    writer.close();
  }

  protected void beforeExport() {
    writer.writeTitle(titles);
  }

  public void transferItem() {
    if (null == getCurrent()) {
      return;
    } else {
      writer.write(getCurrent());
      return;
    }
  }

  protected void next() {
    index++;
    current = iter.next();
  }

  public boolean hasNext() {
    return iter.hasNext();
  }

  public String getFormat() {
    return writer.getFormat();
  }

  public String[] getTitles() {
    return titles;
  }

  public void setTitles(String titles[]) {
    this.titles = titles;
  }

  public void setContext(Context context) {
    Collection items = (Collection) context.getDatas().get("items");
    if (null != items) {
      datas = items;
      iter = datas.iterator();
    }
    this.context = context;
  }

  public String getDataName() {
    return null;
  }

  public void setWriter(Writer writer) {
    if (writer instanceof ItemWriter)
      this.writer = (ItemWriter) writer;
  }

  protected static Logger logger;
  protected ItemWriter writer;
  protected TransferResult transferResult;
  protected Vector listeners;
  protected int success;
  protected int fail;
  protected int index;
  protected String titles[];
  protected Collection datas;
  protected Iterator iter;
  private Object current;
  protected Context context;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.commons.transfer.exporter.ItemExporter.class);
  }
}
