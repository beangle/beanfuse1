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

import org.apache.commons.lang.StringUtils;

public class BitStringUtil {

  public BitStringUtil() {
  }

  public static String and(String first, String second) {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < first.length(); i++)
      if (first.charAt(i) != '0' && second.charAt(i) != '0')
        buffer.append('1');
      else
        buffer.append('0');

    return buffer.toString();
  }

  public static String or(String first, String second) {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < first.length(); i++)
      if (first.charAt(i) != '0' || second.charAt(i) != '0')
        buffer.append('1');
      else
        buffer.append('0');

    return buffer.toString();
  }

  public static String andWith(String str, String boolString) {
    if (StringUtils.isEmpty(str))
      return null;
    if (StringUtils.isEmpty(boolString))
      return str;
    if (str.length() < boolString.length())
      return str;
    StringBuffer buffer = new StringBuffer(str);
    for (int i = 0; i < buffer.length(); i++)
      if (boolString.charAt(i) == '0')
        buffer.setCharAt(i, '0');

    return buffer.toString();
  }

  public static String convertToBoolStr(String first) {
    StringBuffer occupyBuffer = new StringBuffer(first.length());
    for (int i = 0; i < first.length(); i++)
      if (first.charAt(i) != '0')
        occupyBuffer.append('1');
      else
        occupyBuffer.append('0');

    return occupyBuffer.toString();
  }

  public static Long BinValueOf(String binaryStr) {
    if (StringUtils.isEmpty(binaryStr))
      return new Long(0L);
    long value = 0L;
    long height = 1L;
    for (int i = binaryStr.length() - 1; i >= 0; i--) {
      if (binaryStr.charAt(i) == '1')
        value += height;
      height *= 2L;
    }

    return new Long(value);
  }

  public static String reverse(String binaryStr) {
    StringBuffer occupyBuffer = new StringBuffer(binaryStr.length());
    for (int i = 0; i < binaryStr.length(); i++)
      if (binaryStr.charAt(i) == '0')
        occupyBuffer.append('1');
      else
        occupyBuffer.append('0');

    return occupyBuffer.toString();
  }
}
