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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.*;

public class SeqStringUtil {

  public SeqStringUtil() {
  }

  public static String mergeSeq(String first, String second, String delimiter) {
    if (StringUtils.isNotEmpty(second) && StringUtils.isNotEmpty(first)) {
      List firstSeq = Arrays.asList(StringUtils.split(first, delimiter));
      List secondSeq = Arrays.asList(StringUtils.split(second, delimiter));
      Collection rs = CollectionUtils.union(firstSeq, secondSeq);
      StringBuffer buf = new StringBuffer();
      String ele;
      for (Iterator iter = rs.iterator(); iter.hasNext(); buf.append(delimiter).append(ele))
        ele = (String) iter.next();

      if (buf.length() > 0)
        buf.append(delimiter);
      return buf.toString();
    } else {
      return (first != null ? first : "") + (second != null ? second : "");
    }
  }

  public static String mergeSeq(String first, String second) {
    return mergeSeq(first, second, defualtDelimiter);
  }

  public static boolean isEqualSeq(String first, String second, String delimiter) {
    if (StringUtils.isNotEmpty(first) && StringUtils.isNotEmpty(second)) {
      String firstWords[] = StringUtils.split(first, delimiter);
      Set firstSet = new HashSet();
      for (int i = 0; i < firstWords.length; i++)
        firstSet.add(firstWords[i]);

      String secondWords[] = StringUtils.split(second, delimiter);
      Set secondSet = new HashSet();
      for (int i = 0; i < secondWords.length; i++)
        secondSet.add(secondWords[i]);

      return firstSet.equals(secondSet);
    } else {
      return StringUtils.isEmpty(first) & StringUtils.isEmpty(second);
    }
  }

  public static String subtractSeq(String first, String second, String delimiter) {
    if (StringUtils.isEmpty(first))
      return "";
    if (StringUtils.isEmpty(second)) {
      if (!first.startsWith(delimiter))
        first = delimiter + first;
      if (!first.endsWith(delimiter))
        first = first + delimiter;
      return first;
    }
    List firstSeq = Arrays.asList(StringUtils.split(first, delimiter));
    List secondSeq = Arrays.asList(StringUtils.split(second, delimiter));
    Collection rs = CollectionUtils.subtract(firstSeq, secondSeq);
    StringBuffer buf = new StringBuffer();
    String ele;
    for (Iterator iter = rs.iterator(); iter.hasNext(); buf.append(delimiter).append(ele))
      ele = (String) iter.next();

    if (buf.length() > 0)
      buf.append(delimiter);
    return buf.toString();
  }

  public static String subtractSeq(String first, String second) {
    return subtractSeq(first, second, defualtDelimiter);
  }

  public static String intersectSeq(String first, String second, String delimiter) {
    if (StringUtils.isEmpty(first) || StringUtils.isEmpty(second))
      return "";
    List firstSeq = Arrays.asList(StringUtils.split(first, ","));
    List secondSeq = Arrays.asList(StringUtils.split(second, ","));
    Collection rs = CollectionUtils.intersection(firstSeq, secondSeq);
    StringBuffer buf = new StringBuffer();
    String ele;
    for (Iterator iter = rs.iterator(); iter.hasNext(); buf.append(",").append(ele))
      ele = (String) iter.next();

    if (buf.length() > 0)
      buf.append(",");
    return buf.toString();
  }

  public static String intersectSeq(String first, String second) {
    return intersectSeq(first, second, defualtDelimiter);
  }

  public static boolean isEqualSeq(String first, String second) {
    return isEqualSeq(first, second, defualtDelimiter);
  }

  public static String removeWord(String host, String word) {
    return removeWord(host, word, defualtDelimiter);
  }

  public static String removeWord(String host, String word, String delimiter) {
    if (host.indexOf(word) == -1)
      return host;
    int beginIndex = host.indexOf(word);
    int endIndex = beginIndex + word.length();
    if (beginIndex == 0)
      return host.substring(endIndex + 1);
    if (endIndex == host.length()) {
      return host.substring(0, beginIndex - delimiter.length());
    } else {
      String before = host.substring(0, beginIndex);
      String after = host.substring(endIndex + 1);
      return before + after;
    }
  }

  public static boolean isIn(String hostSeq, String word) {
    return hostSeq.indexOf(word) != -1;
  }

  public static String transformToSeq(Object seq[], String delimiter) {
    if (null == seq || seq.length < 1)
      return "";
    StringBuffer aim = new StringBuffer();
    for (int i = 0; i < seq.length; i++) {
      if (StringUtils.isNotEmpty(aim.toString()))
        aim.append(delimiter);
      aim.append(seq[i]);
    }

    return aim.toString();
  }

  public static String transformToSeq(String seq[]) {
    return transformToSeq(((Object[]) (seq)), defualtDelimiter);
  }

  public static Long[] transformToLong(String Ids) {
    if (StringUtils.isEmpty(Ids))
      return null;
    else
      return transformToLong(StringUtils.split(Ids, ","));
  }

  public static Long[] transformToLong(String Ids[]) {
    if (null == Ids)
      return null;
    Long idsOfLong[] = new Long[Ids.length];
    for (int i = 0; i < Ids.length; i++)
      idsOfLong[i] = new Long(Ids[i]);

    return idsOfLong;
  }

  public static Integer[] transformToInteger(String Ids) {
    if (StringUtils.isEmpty(Ids))
      return null;
    else
      return transformToInteger(StringUtils.split(Ids, ","));
  }

  public static Integer[] transformToInteger(String Ids[]) {
    Integer idsOfInteger[] = new Integer[Ids.length];
    for (int i = 0; i < Ids.length; i++)
      idsOfInteger[i] = new Integer(Ids[i]);

    return idsOfInteger;
  }

  public static Integer[] splitNumSeq(String numSeq) {
    if (StringUtils.isEmpty(numSeq))
      return null;
    String numArray[] = StringUtils.split(numSeq, ",");
    Set numSet = new HashSet();
    for (int i = 0; i < numArray.length; i++) {
      String num = numArray[i];
      if (StringUtils.contains(num, "-")) {
        String termFromTo[] = StringUtils.split(num, "-");
        int from = NumberUtils.toInt(termFromTo[0]);
        int to = NumberUtils.toInt(termFromTo[1]);
        for (int j = from; j <= to; j++)
          numSet.add(new Integer(j));

      } else {
        numSet.add(new Integer(num));
      }
    }

    Integer nums[] = new Integer[numSet.size()];
    numSet.toArray(nums);
    return nums;
  }

  public static String keepUnique(String keys) {
    String keysArray[] = StringUtils.split(keys, ",");
    List keyList = new ArrayList();
    for (int i = 0; i < keysArray.length; i++)
      if (!keyList.contains(keysArray[i]))
        keyList.add(keysArray[i]);

    StringBuffer keyBuf = new StringBuffer();
    Iterator iter = keyList.iterator();
    do {
      if (!iter.hasNext())
        break;
      keyBuf.append(iter.next());
      if (iter.hasNext())
        keyBuf.append(",");
    } while (true);
    return keyBuf.toString();
  }

  public static String defualtDelimiter = ",";

}
