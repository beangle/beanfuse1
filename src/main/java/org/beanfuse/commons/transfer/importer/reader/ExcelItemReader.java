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
package org.beanfuse.commons.transfer.importer.reader;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package org.beanfuse.commons.transfer.importer.reader:
//            ItemReader

public class ExcelItemReader
    implements ItemReader {

  public ExcelItemReader() {
    attrCount = 0;
  }

  public ExcelItemReader(InputStream is) {
    attrCount = 0;
    try {
      workbook = new HSSFWorkbook(is);
      headIndex = DEFAULT_HEADINDEX;
      indexInSheet = headIndex + 1;
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public ExcelItemReader(HSSFWorkbook workbook, int headIndex) {
    attrCount = 0;
    this.workbook = workbook;
    this.headIndex = headIndex;
    indexInSheet = headIndex + 1;
  }

  public void setWorkbook(HSSFWorkbook wb) {
    workbook = wb;
  }

  public String[] readDescription() {
    HSSFSheet sheet = workbook.getSheetAt(0);
    return readLine(sheet, 0);
  }

  public String[] readTitle() {
    HSSFSheet sheet = workbook.getSheetAt(0);
    String attrs[] = readLine(sheet, headIndex);
    attrCount = attrs.length;
    return attrs;
  }

  protected String[] readLine(HSSFSheet sheet, int rowIndex) {
    HSSFRow row = sheet.getRow(rowIndex);
    if (logger.isDebugEnabled())
      logger.debug("values count:{}" + row.getLastCellNum());
    List attrList = new ArrayList();
    short i = 0;
    do {
      if (i >= row.getLastCellNum())
        break;
      HSSFCell cell = row.getCell(i);
      if (null == cell)
        break;
      String attr = cell.getRichStringCellValue().getString();
      if (StringUtils.isEmpty(attr))
        break;
      attrList.add(attr.trim());
      i++;
    } while (true);
    String attrs[] = new String[attrList.size()];
    attrList.toArray(attrs);
    logger.debug("has attrs {}", attrs);
    return attrs;
  }

  public Object read() {
    HSSFSheet sheet = workbook.getSheetAt(0);
    if (indexInSheet > sheet.getLastRowNum())
      return null;
    HSSFRow row = sheet.getRow(indexInSheet);
    indexInSheet++;
    if (row == null)
      return ((Object) (new Object[attrCount]));
    Object values[] = new Object[attrCount == 0 ? row.getLastCellNum() : attrCount];
    for (short k = 0; k < values.length; k++) {
      String celValue = getCelValue(row.getCell(k));
      if (null != celValue)
        celValue = celValue.trim();
      values[k] = celValue;
    }

    return ((Object) (values));
  }

  private String getCelValue(HSSFCell cell) {
    if (cell == null || cell.getCellType() == 3)
      return "";
    if (cell.getCellType() == 1)
      return cell.getRichStringCellValue().getString();
    if (cell.getCellType() == 0)
      return numberFormat.format(cell.getNumericCellValue());
    if (cell.getCellType() == 4) {
      if (cell.getBooleanCellValue())
        return "true";
      else
        return "false";
    } else {
      return "";
    }
  }

  public String getFormat() {
    return "excel";
  }

  public int getHeadIndex() {
    return headIndex;
  }

  public void setHeadIndex(int headIndex) {
    this.headIndex = headIndex;
  }

  public static Logger logger;
  public static int DEFAULT_HEADINDEX = 0;
  public static NumberFormat numberFormat;
  public static final int sheetNum = 0;
  int headIndex;
  int indexInSheet;
  int attrCount;
  HSSFWorkbook workbook;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.commons.transfer.importer.reader.ExcelItemReader.class);
    numberFormat = NumberFormat.getInstance();
    numberFormat.setGroupingUsed(false);
  }
}
