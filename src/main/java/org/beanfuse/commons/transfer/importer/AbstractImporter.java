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

import org.beanfuse.commons.transfer.Transfer;
import org.beanfuse.commons.transfer.TransferListener;
import org.beanfuse.commons.transfer.TransferResult;
import org.beanfuse.commons.transfer.importer.reader.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

// Referenced classes of package org.beanfuse.commons.transfer.importer:
//            Importer

public abstract class AbstractImporter
    implements Importer {

  public AbstractImporter() {
    listeners = new Vector();
    success = 0;
    fail = 0;
    index = 0;
  }

  public void transfer(TransferResult tr) {
    transferResult = tr;
    transferResult.setTransfer(this);
    long transferStartAt = System.currentTimeMillis();
    try {
      beforeImport();
    } catch (Exception e) {
      return;
    }
    TransferListener listener;
    for (Iterator iter = listeners.iterator(); iter.hasNext(); listener.startTransfer(tr))
      listener = (TransferListener) iter.next();

    do {
      if (!read())
        break;
      long transferItemStart = System.currentTimeMillis();
      index++;
      beforeImportItem();
      if (isDataValid()) {
        int errors = tr.errors();
        for (Iterator iter = listeners.iterator(); iter.hasNext(); listener.startTransferItem(tr))
          listener = (TransferListener) iter.next();

        if (tr.errors() <= errors) {
          transferItem();
          for (Iterator iter = listeners.iterator(); iter.hasNext(); listener.endTransferItem(tr))
            listener = (TransferListener) iter.next();

          if (tr.errors() == errors)
            success++;
          else
            fail++;
          if (logger.isDebugEnabled())
            logger.debug("importer item:" + getTranferIndex() + " take time:" + (System.currentTimeMillis() - transferItemStart));
        }
      }
    } while (true);
    for (Iterator iter = listeners.iterator(); iter.hasNext(); listener.endTransfer(tr))
      listener = (TransferListener) iter.next();

    if (logger.isDebugEnabled())
      logger.debug("importer elapse:" + (System.currentTimeMillis() - transferStartAt));
  }

  public int getFail() {
    return fail;
  }

  public int getSuccess() {
    return success;
  }

  public Reader getReader() {
    return reader;
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }

  public boolean ignoreNull() {
    return true;
  }

  public Locale getLocale() {
    return Locale.getDefault();
  }

  public String getFormat() {
    return reader.getFormat();
  }

  public int getTranferIndex() {
    return index;
  }

  public Transfer addListener(TransferListener listener) {
    listeners.add(listener);
    listener.setTransfer(this);
    return this;
  }

  protected static Logger logger;
  protected Reader reader;
  protected TransferResult transferResult;
  protected Vector listeners;
  protected int success;
  protected int fail;
  protected int index;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.commons.transfer.importer.AbstractImporter.class);
  }
}
