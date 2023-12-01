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
package org.beanfuse.commons.detail;

public class Converter {

  public Converter() {
  }

  public static String decode(String str) {
    if (str == null)
      return null;
    StringBuffer resultStr = new StringBuffer(str);
    byte ch[] = new byte[1];
    for (int i = 0; i < resultStr.length(); i++)
      try {
        if (resultStr.charAt(i) == '%') {
          ch[0] = Byte.parseByte(resultStr.substring(i + 1, i + 3), 16);
          resultStr.replace(i, i + 3, new String(ch));
        }
      } catch (NumberFormatException numberformatexception) {
      }

    return new String(resultStr);
  }

  public static String encode(String str, String replacer) {
    if (str == null || replacer == null)
      return str;
    if (replacer.length() == 0)
      return str;
    if (replacer.indexOf('%') == -1)
      replacer = replacer + '%';
    StringBuffer resultStr = new StringBuffer("");
    StringBuffer mapper = new StringBuffer("");
    int strCount = replacer.length();
    for (int i = 0; i < strCount; i++) {
      int x = replacer.charAt(i);
      String tmpStr = Integer.toHexString(x).toUpperCase();
      if (tmpStr.length() == 1)
        tmpStr = "0" + tmpStr;
      mapper.append(tmpStr.substring(tmpStr.length() - 2, 2));
    }

    strCount = str.length();
    for (int i = 0; i < strCount; i++) {
      char ch = str.charAt(i);
      int pos = replacer.indexOf(ch);
      if (pos != -1)
        resultStr.append('%').append(mapper.substring(pos * 2, pos * 2 + 2));
      else
        resultStr.append(ch);
    }

    return new String(resultStr);
  }

  public static String padding(String s, int length) {
    String spaces = "                                                        ";
    if (s == null)
      return null;
    StringBuffer sb;
    for (sb = new StringBuffer(s); sb.length() < length; sb.append("                                                        "))
      ;
    return sb.substring(0, length);
  }

  public static final char SYM_PERCENTAGE = 37;
}
