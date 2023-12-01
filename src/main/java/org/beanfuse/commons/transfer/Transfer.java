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
package org.beanfuse.commons.transfer;

import java.util.Locale;

// Referenced classes of package org.beanfuse.commons.transfer:
//            TransferResult, TransferListener

public interface Transfer {

  public abstract void transfer(TransferResult transferresult);

  public abstract void transferItem();

  public abstract Transfer addListener(TransferListener transferlistener);

  public abstract String getFormat();

  public abstract Locale getLocale();

  public abstract String getDataName();

  public abstract int getFail();

  public abstract int getSuccess();

  public abstract int getTranferIndex();

  public abstract Object getCurrent();

  public static final String EXCEL = "excel";
  public static final String CSV = "csv";
  public static final String TXT = "txt";
  public static final String DBF = "dbf";
  public static final String PDF = "pdf";
  public static final String HTML = "html";
}
