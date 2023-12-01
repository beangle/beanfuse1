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
package org.beanfuse.commons.lang;


public class StringUtil {

  public StringUtil() {
  }

  public static int countChar(String host, char charactor) {
    int count = 0;
    for (int i = 0; i < host.length(); i++)
      if (host.charAt(i) == charactor)
        count++;

    return count;
  }

  public static int countStr(String host, String searchStr) {
    int count = 0;
    int startIndex = 0;
    do {
      if (startIndex >= host.length())
        break;
      int findLoc = host.indexOf(searchStr, startIndex);
      if (findLoc == -1)
        break;
      count++;
      startIndex = (findLoc + searchStr.length()) - 1;
      startIndex++;
    } while (true);
    return count;
  }

  public static String replace(String str, String c, int pos) {
    if (str.length() < pos)
      return "";
    else
      return str.substring(0, pos - 1) + c + str.substring(pos);
  }

  public static String replace(String str, String given, int begin, int end) {
    if (begin < 1 || end > str.length() || end < begin)
      return str;
    else
      return str.substring(0, begin - 1) + given + str.substring(end);
  }

  public static String defualtDelimiter = ",";

}
