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
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

// Referenced classes of package org.beanfuse.commons.transfer.exporter.writer:
//            TemplateWriter

public class ExcelTemplateWriter
    implements TemplateWriter {

  public ExcelTemplateWriter() {
    transformer = new XLSTransformer();
  }

  public ExcelTemplateWriter(OutputStream outputStream) {
    transformer = new XLSTransformer();
    this.outputStream = outputStream;
  }

  public String getFormat() {
    return "excel";
  }

  public String getTemplatePath() {
    return templatePath;
  }

  public void setTemplatePath(String templatePath) {
    this.templatePath = templatePath;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public void write() {
    populateContext();
    try {
      workbook = transformer.transformXLS(new FileInputStream(new File(templatePath)), context.getDatas());
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public void close() {
    try {
      workbook.write(outputStream);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  protected void populateContext() {
    templatePath = (String) context.getDatas().get("templatePath");
    if (StringUtils.isEmpty(templatePath))
      throw new RuntimeException("Empty template path!");
    else
      return;
  }

  public OutputStream getOutputStream() {
    return outputStream;
  }

  public void setOutputStream(OutputStream outputStream) {
    this.outputStream = outputStream;
  }

  protected String templatePath;
  protected XLSTransformer transformer;
  protected Context context;
  protected OutputStream outputStream;
  protected HSSFWorkbook workbook;
}
