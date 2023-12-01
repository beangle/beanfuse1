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
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.beanfuse.commons.transfer.importer.reader;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.LineNumberReader;

public class CSVReader implements ItemReader {
  protected static Logger logger;
  LineNumberReader reader;

  public CSVReader(LineNumberReader reader) {
    this.reader = reader;
  }

  public String[] readDescription() {
    return null;
  }

  public String[] readTitle() {
    try {
      this.reader.readLine();
      return StringUtils.split(this.reader.readLine(), ",");
    } catch (Exception var2) {
      throw new RuntimeException(var2.getMessage());
    }
  }

  public Object read() {
    String curData = null;

    try {
      curData = this.reader.readLine();
    } catch (IOException var3) {
      logger.error(curData, var3);
    }

    return null == curData ? null : StringUtils.split(curData, ",");
  }

  public String getFormat() {
    return "csv";
  }

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.commons.transfer.importer.reader.CSVReader.class);
  }
}
