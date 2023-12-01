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
package org.beanfuse.commons.mvc.struts.misc;

import org.beanfuse.commons.transfer.TransferResult;
import org.beanfuse.commons.transfer.importer.DefaultEntityImporter;
import org.beanfuse.commons.transfer.importer.EntityImporter;
import org.beanfuse.commons.transfer.importer.reader.CSVReader;
import org.beanfuse.commons.transfer.importer.reader.ExcelItemReader;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.List;

public class ImporterServletSupport {

  public ImporterServletSupport() {
  }

  public static EntityImporter buildEntityImporter(HttpServletRequest request, Class clazz, TransferResult tr) {
    try {
      ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
      upload.setHeaderEncoding("utf-8");
      List items = upload.parseRequest(request);
      Iterator iter = items.iterator();

      while (iter.hasNext()) {
        FileItem element = (FileItem) iter.next();
        if (!element.isFormField()) {
          String fileName = element.getName();
          if (!StringUtils.isEmpty(fileName)) {
            InputStream is = element.getInputStream();
            DefaultEntityImporter importer;
            if (fileName.endsWith(".xls")) {
              HSSFWorkbook wb = new HSSFWorkbook(is);
              if (wb.getNumberOfSheets() >= 1 && wb.getSheetAt(0).getLastRowNum() != 0) {
                importer = new DefaultEntityImporter(clazz);
                importer.setReader(new ExcelItemReader(wb, 1));
                request.setAttribute("importer", importer);
                request.setAttribute("importResult", tr);
                return importer;
              }

              return null;
            }

            LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));
            if (null == reader.readLine()) {
              return null;
            }

            reader.reset();
            importer = new DefaultEntityImporter(clazz);
            importer.setReader(new CSVReader(reader));
            return importer;
          }
        }
      }

      return null;
    } catch (Exception var11) {
      tr.addFailure("error.parameters.illegal", var11.getMessage());
      return null;
    }
  }
}
