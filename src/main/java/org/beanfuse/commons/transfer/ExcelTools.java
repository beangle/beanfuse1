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

import org.beanfuse.commons.transfer.exporter.DefaultPropertyExtractor;
import org.beanfuse.commons.transfer.exporter.PropertyExtractor;
import org.apache.poi.hssf.usermodel.*;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class ExcelTools {

  public ExcelTools() {
    format = new DecimalFormat("#");
    intoExcelNumberformat = new DecimalFormat("#0.00");
  }

  public HSSFWorkbook toExcel(Collection datas, String propertyShowKeys)
      throws Exception {
    HSSFWorkbook wb = new HSSFWorkbook();
    return toExcel(wb, "export data", datas, propertyShowKeys);
  }

  public HSSFWorkbook toExcel(HSSFWorkbook wb, String sheetName, Collection datas, String propertyShowKeys)
      throws Exception {
    HSSFSheet sheet = wb.createSheet(sheetName);
    HSSFRow row = null;
    HSSFCell cell = null;
    String pShowKeys[] = Tokenizer2StringArray(propertyShowKeys, ",");
    row = sheet.createRow(0);
    for (int i = 0; i < pShowKeys.length; i++) {
      cell = row.createCell((short) i);
      cell.setCellValue(new HSSFRichTextString(pShowKeys[i]));
    }

    int rowId = 1;
    for (Iterator iter = datas.iterator(); iter.hasNext(); ) {
      row = sheet.createRow(rowId);
      Object objs[] = (Object[]) (Object[]) iter.next();
      for (int j = 0; j < objs.length; j++) {
        cell = row.createCell((short) j);
        cell.setCellValue(new HSSFRichTextString(objs[j] != null ? objs[j].toString() : ""));
      }

      rowId++;
    }

    return wb;
  }

  public HSSFWorkbook object2Excel(Collection list, String propertyKeys, String propertyShowKeys, PropertyExtractor exporter)
      throws Exception {
    HSSFWorkbook wb = new HSSFWorkbook();
    object2Excel(wb, "export result", list, propertyKeys, propertyShowKeys, exporter);
    return wb;
  }

  public HSSFWorkbook object2Excel(HSSFWorkbook wb, String sheetName, Collection list, String propertyKeys, String propertyShowKeys, PropertyExtractor exporter)
      throws Exception {
    HSSFSheet sheet = wb.createSheet(sheetName);
    HSSFRow row = null;
    HSSFCell cell = null;
    Object cellVal = null;
    String pKeys[] = Tokenizer2StringArray(propertyKeys, ",");
    String pShowKeys[] = Tokenizer2StringArray(propertyShowKeys, ",");
    row = sheet.createRow(0);
    for (int i = 0; i < pShowKeys.length; i++) {
      cell = row.createCell((short) i);
      cell.setCellValue(new HSSFRichTextString(pShowKeys[i]));
    }

    int rowId = 1;
    for (Iterator iter = list.iterator(); iter.hasNext(); ) {
      row = sheet.createRow(rowId);
      Object obj = iter.next();
      for (int i = 0; i < pKeys.length; i++) {
        cell = row.createCell((short) i);
        cellVal = exporter.getPropertyValue(obj, pKeys[i]);
        String cellValue = "";
        if (null != cellVal)
          cellValue = cellVal.toString();
        if (cellVal instanceof Float)
          cellValue = intoExcelNumberformat.format(cellVal);
        cell.setCellValue(new HSSFRichTextString(cellValue));
      }

      rowId++;
    }

    return wb;
  }

  public HSSFWorkbook object2Excel(List list, String propertyKeys, String propertyShowKeys)
      throws Exception {
    return object2Excel(((Collection) (list)), propertyKeys, propertyShowKeys, ((PropertyExtractor) (new DefaultPropertyExtractor())));
  }

  private String[] Tokenizer2StringArray(String sourceStr, String strDot) {
    StringTokenizer st = new StringTokenizer(sourceStr, strDot);
    int size = st.countTokens();
    String strArray[] = new String[size];
    for (int i = 0; i < size; i++)
      strArray[i] = st.nextToken();

    return strArray;
  }

  DecimalFormat format;
  DecimalFormat intoExcelNumberformat;
}
