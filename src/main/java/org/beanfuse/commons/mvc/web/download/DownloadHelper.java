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
package org.beanfuse.commons.mvc.web.download;

import org.beanfuse.commons.utils.web.RequestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DownloadHelper {

  public DownloadHelper() {
  }

  public static void download(HttpServletRequest request, HttpServletResponse response, File file) {
    download(request, response, file, file.getName());
  }

  public static void download(HttpServletRequest request, HttpServletResponse response, File file, String displayFileName) {
    String attch_name = "";
    byte b[] = new byte[100];
    int len = 0;
    try {
      if (StringUtils.isBlank(displayFileName))
        attch_name = getAttachName(file.getAbsolutePath());
      else
        attch_name = displayFileName;
      attch_name = RequestUtils.encodeAttachName(request, attch_name);
      InputStream inStream = new FileInputStream(file);
      response.reset();
      String ext = StringUtils.substringAfterLast(file.getAbsolutePath(), ".");
      if (StringUtils.equalsIgnoreCase("doc", ext))
        response.setContentType("application/msword");
      else if (StringUtils.equalsIgnoreCase("xls", ext))
        response.setContentType("application/vnd.ms-excel");
      else
        response.setContentType("application/x-msdownload");
      response.addHeader("Content-Disposition", "attachment; filename=\"" + attch_name + "\"");
      while ((len = inStream.read(b)) > 0)
        response.getOutputStream().write(b, 0, len);
      inStream.close();
    } catch (Exception e) {
      logger.warn("client abort download file:{}", attch_name);
    }
  }

  public static String getAttachName(String file_name) {
    if (file_name == null)
      return "";
    file_name = file_name.trim();
    int iPos = 0;
    iPos = file_name.lastIndexOf("\\");
    if (iPos > -1)
      file_name = file_name.substring(iPos + 1);
    iPos = file_name.lastIndexOf("/");
    if (iPos > -1)
      file_name = file_name.substring(iPos + 1);
    iPos = file_name.lastIndexOf(File.separator);
    if (iPos > -1)
      file_name = file_name.substring(iPos + 1);
    return file_name;
  }

  private static Logger logger;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.commons.mvc.web.download.DownloadHelper.class);
  }
}
