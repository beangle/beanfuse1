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
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.*;

import java.io.OutputStream;

// Referenced classes of package org.beanfuse.commons.transfer.exporter.writer:
//            AbstractItemWriter

public class ExcelItemWriter extends AbstractItemWriter {

  public ExcelItemWriter() {
    countPerSheet = 50000;
    workbook = new HSSFWorkbook();
    index = 0;
    sheet = workbook.createSheet((index + 1) + "-" + countPerSheet);
  }

  public ExcelItemWriter(OutputStream outputStream) {
    countPerSheet = 50000;
    workbook = new HSSFWorkbook();
    index = 0;
    sheet = workbook.createSheet((index + 1) + "-" + countPerSheet);
    this.outputStream = outputStream;
  }

  public int getCountPerSheet() {
    return countPerSheet;
  }

  public void setCountPerSheet(int dataNumPerSheet) {
    countPerSheet = dataNumPerSheet;
  }

  public void write(Object obj) {
    if (index + 1 >= countPerSheet)
      sheet = workbook.createSheet((index + 1) + "-" + (index + countPerSheet));
    writeExcel(obj);
    index++;
  }

  public void writeTitle(Object title) {
    write(title);
  }

  public String getFormat() {
    return "excel";
  }

  public void writeExcel(Object datas) {
    HSSFRow row = sheet.createRow(index);
    if (datas != null)
      if (datas.getClass().isArray()) {
        Object values[] = (Object[]) (Object[]) datas;
        for (int i = 0; i < values.length; i++) {
          HSSFCell cell = row.createCell((short) i);
          if (values[i] instanceof Number)
            cell.setCellType(0);
          cell.setCellValue(new HSSFRichTextString(values[i] != null ? values[i].toString() : ""));
        }

      } else {
        HSSFCell cell = row.createCell((short) 0);
        if (datas instanceof Number)
          cell.setCellType(0);
        cell.setCellValue(new HSSFRichTextString(datas.toString()));
      }
  }

  public void close() {
    try {
      workbook.write(outputStream);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public void setContext(Context context) {
    super.setContext(context);
    if (null != context) {
      Object count = context.getDatas().get("countPerSheet");
      if (null != count && NumberUtils.isNumber(count.toString())) {
        int countParam = NumberUtils.toInt(count.toString());
        if (countParam > 0)
          countPerSheet = countParam;
      }
    }
  }

  protected int countPerSheet;
  protected HSSFWorkbook workbook;
  protected int index;
  protected HSSFSheet sheet;
}
