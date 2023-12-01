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
package org.beanfuse.commons.encryption;

public class EncryptUtil {
  class Div {

    public int getQuot() {
      return quot;
    }

    public int getRem() {
      return rem;
    }

    private int quot;
    private int rem;

    public Div(int divident, int divisor) {
      rem = divident % divisor;
      if (divident > divisor)
        quot = (divident - quot) / divisor;
      else
        quot = 0;
    }
  }


  public EncryptUtil() {
  }

  private int arrToInt(int arr[])
      throws Exception {
    int value = 0;
    try {
      value = arr[0];
      value += arr[1] * 256;
      value += arr[2] * 256 * 256;
    } catch (Exception e) {
      throw e;
    }
    return value;
  }

  private int[] intToArr(int value)
      throws Exception {
    int val[] = new int[4];
    try {
      val[0] = value % 256;
      val[1] = ((value - val[0]) / 256) % 256;
      val[2] = ((value - val[0] - val[1] * 256) / 256) % 256;
    } catch (Exception e) {
      throw e;
    }
    return val;
  }

  private int getTextWight(String source)
      throws Exception {
    int w = 0;
    try {
      for (int i = 0; i < source.length(); i++)
        w += source.charAt(i);

    } catch (Exception e) {
      throw e;
    }
    return w;
  }

  private String stringOrderSet(int newRow, int newLen, int orderMethod, int arr[])
      throws Exception {
    int newIdx = 0;
    StringBuffer newSet = new StringBuffer();
    try {
      if (orderMethod == 0) {
        for (int i = 0; i < arr.length; i++) {
          newIdx++;
          newSet.append((char) arr[i]);
        }

      } else {
        for (int i = 0; i < orderMethod; i++) {
          for (int j = 0; j < newRow; j++) {
            int pIdx = i + j * orderMethod;
            if (pIdx == 0 || pIdx > arr.length) {
              if (newIdx < newLen) {
                newIdx++;
                newSet.append('~');
              }
            } else if (newIdx < newLen) {
              newIdx++;
              newSet.append((char) arr[pIdx - 1]);
            }
          }

        }

      }
      for (int i = newIdx; i < newLen; i++)
        newSet.append('~');

    } catch (Exception e) {
      throw e;
    }
    return newSet.toString();
  }

  private int[] stringOrderGet(int newRow, int newLen, int orderMethod, String text)
      throws Exception {
    int pIdx = 0;
    int newIdx = 0;
    int newSet[] = new int[newLen];
    try {
      if (orderMethod == 0) {
        for (int i = 0; i < newLen; i++)
          if (text.charAt(i) != '~' && newIdx < newLen)
            newSet[newIdx++] = text.charAt(i);

      } else {
        for (int i = 0; i < newRow; i++) {
          for (int j = 0; j < orderMethod; j++) {
            pIdx = i + j * newRow;
            if (text.charAt(pIdx) != '~' && newIdx < newLen)
              newSet[newIdx++] = text.charAt(pIdx);
          }

        }

      }
      if (newIdx < newLen) {
        int result[] = new int[newIdx];
        for (int i = 0; i < newIdx; i++)
          result[i] = newSet[i];

        newSet = result;
      }
    } catch (Exception e) {
      throw e;
    }
    return newSet;
  }

  private void arrInc(int arr[], int pos)
      throws Exception {
    int idx = 0;
    try {
      do {
        if (++pos >= arr.length)
          pos = 0;
        idx = arr[pos];
        if (idx < 255)
          break;
        arr[pos] = idx - 255;
      } while (true);
      arr[pos] = idx + 1;
    } catch (Exception e) {
      throw e;
    }
  }

  private void arrDec(int arr[], int pos)
      throws Exception {
    int idx = 0;
    try {
      do {
        if (++pos >= arr.length)
          pos = 0;
        idx = arr[pos];
        if (idx != 0)
          break;
        arr[pos] = idx + 255;
      } while (true);
      arr[pos] = idx - 1;
    } catch (Exception e) {
      throw e;
    }
  }

  private int[] uuEncode6bit(int enLen, int arr[])
      throws Exception {
    int idx = 0;
    int uuCrt = 0;
    int enIdx = 0;
    int p1 = 0;
    int p2 = 0;
    int uuLen = arr.length;
    int enArr[] = new int[enLen];
    try {
      Div v = new Div(uuLen, 3);
      uuCrt = v.getQuot() - (v.getRem() == 0 ? 1 : 0);
      enIdx = 0;
      for (int i = 0; i <= uuCrt; i++) {
        for (int j = 0; j < 3; j++) {
          idx = i * 3 + j;
          if (idx < uuLen)
            p1 = arr[idx];
          else
            p1 = 0;
          switch (j) {
            case 0: // '\0'
              if (enIdx < enLen)
                enArr[enIdx++] = p1 / 4 + 33;
              p2 = (p1 & 3) * 16;
              break;

            case 1: // '\001'
              if (enIdx < enLen)
                enArr[enIdx++] = p2 + p1 / 16 + 33;
              p2 = (p1 & 0xf) * 4;
              break;

            case 2: // '\002'
              if (enIdx < enLen)
                enArr[enIdx++] = p2 + p1 / 64 + 33;
              if (enIdx < enLen)
                enArr[enIdx++] = (p1 & 0x3f) + 33;
              break;
          }
          if (idx > uuLen)
            break;
        }

      }

      if (enIdx < enLen) {
        int res[] = new int[enIdx];
        for (int i = 0; i < enIdx; i++)
          res[i] = enArr[i];

        enArr = res;
      }
    } catch (Exception e) {
      throw e;
    }
    return enArr;
  }

  private int[] uuDecode6bit(int deLen, int arr[])
      throws Exception {
    int deIdx = 0;
    int n = 0;
    int n1 = 0;
    int n2 = 0;
    int n3 = 0;
    int enArr[] = new int[deLen];
    int uuLen = arr.length;
    try {
      Div v = new Div(uuLen, 4);
      int uuCrt = v.getQuot() - (v.getRem() == 0 ? 1 : 0);
      for (int i = 0; i <= uuCrt; i++) {
        for (int j = 0; j < 4; j++) {
          int idx = i * 4 + j;
          if (idx < uuLen) {
            n = arr[idx] - 33;
          } else {
            n = 0;
            break;
          }
          switch (j) {
            case 0: // '\0'
              n1 = n;
              break;

            case 1: // '\001'
              n2 = n;
              if (deIdx < deLen)
                enArr[deIdx++] = (n1 & 0x3f) * 4 + (n2 & 0x30) / 16;
              break;

            case 2: // '\002'
              n3 = n;
              if (deIdx < deLen)
                enArr[deIdx++] = (n2 & 0xf) * 16 + (n3 & 0x3c) / 4;
              break;

            case 3: // '\003'
              if (deIdx < deLen)
                enArr[deIdx++] = (n3 & 3) * 64 + n;
              break;
          }
          if (idx > uuLen)
            break;
        }

      }

      if (deIdx < deLen) {
        int res[] = new int[deIdx];
        for (int i = 0; i < deIdx; i++)
          res[i] = enArr[i];

        enArr = res;
      }
    } catch (Exception e) {
      throw e;
    }
    return enArr;
  }

  public String encryText(String pLainText, String keyString) {
    int i = 0;
    int j = 0;
    int idx = 0;
    int arr[] = new int[4];
    String encryString = null;
    try {
      int kLen;
      int kMod;
      if ("".equals(keyString)) {
        kLen = 0;
        kMod = 3;
      } else {
        kLen = keyString.length();
        kMod = getTextWight(keyString) % 4 + 3;
      }
      int eLen = (8 + pLainText.length()) * 8;
      Div dv = new Div(eLen, 6);
      eLen = dv.getQuot() + (dv.getRem() == 0 ? 0 : 1);
      dv = new Div(eLen + 1, kMod);
      int mRow = dv.getQuot() + (dv.getRem() > 0 ? 1 : 0);
      int mLen = mRow * kMod;
      int dest[] = new int[8 + pLainText.length()];
      arr = intToArr(getTextWight(pLainText));
      for (i = 0; i < 4; i++)
        dest[i] = arr[i];

      arr = intToArr(pLainText.length());
      for (i = 4; i < 8; i++)
        dest[i] = arr[i - 4];

      for (i = 8; i < dest.length; i++)
        dest[i] = pLainText.charAt(i - 8);

      int pLen = dest.length;
      if (kLen <= 0) {
        dest = uuEncode6bit(eLen, dest);
        if (dest.length > 0) {
          encryString = stringOrderSet(mRow, mLen, 0, dest);
          return encryString;
        }
      }
      for (i = 0; i < pLen; i++)
        for (j = 0; j < kLen; j++) {
          idx = i + j;
          if (idx < pLen) {
            int ano = dest[idx];
            ano += keyString.charAt(j);
            if (ano > 255) {
              ano = ano - 255 - 1;
              arrInc(dest, idx);
            }
            dest[idx] = ano;
          }
        }


      dest = uuEncode6bit(eLen, dest);
      encryString = stringOrderSet(mRow, mLen, kMod, dest);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return encryString;
  }

  public String decryText(String enCodeText, String keyString) {
    int i = 0;
    int j = 0;
    int idx = 0;
    int arr[] = new int[4];
    String encryString = null;
    try {
      int eLen = enCodeText.length();
      int dest[] = new int[eLen + 1];
      int kLen;
      int kMod;
      if ("".equals(keyString)) {
        kLen = 0;
        kMod = 3;
      } else {
        kLen = keyString.length();
        kMod = getTextWight(keyString) % 4 + 3;
      }
      Div dv = new Div(eLen, kMod);
      int mRow = dv.getQuot() + (dv.getRem() > 0 ? 1 : 0);
      int mLen = mRow * kMod;
      if (kLen <= 0)
        kMod = 0;
      int tmp[] = stringOrderGet(mRow, mLen, kMod, enCodeText);
      dest = uuDecode6bit(eLen, tmp);
      eLen = dest.length;
      if (kLen > 0)
        for (i = 0; i < eLen; i++)
          for (j = 0; j < kLen; j++) {
            idx = i + j;
            if (idx < eLen) {
              int ano = dest[idx];
              if (ano < keyString.charAt(j)) {
                ano = (ano + 255 + 1) - keyString.charAt(j);
                arrDec(dest, idx);
              } else {
                ano -= keyString.charAt(j);
              }
              dest[idx] = ano;
            }
          }


      for (int k = 0; k < 4; k++)
        arr[k] = dest[k];

      i = arrToInt(arr);
      for (int k = 4; k < 8; k++)
        arr[k - 4] = dest[k];

      j = arrToInt(arr);
      eLen = j;
      char ch[] = new char[eLen];
      for (int k = 0; k < eLen; k++)
        ch[k] = (char) dest[k + 8];

      encryString = new String(ch);
      j = getTextWight(encryString);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (i == j)
      return encryString;
    else
      return null;
  }

  public String usrDecryGroupRight(String encryGroupRights, String groupID)
      throws Exception {
    if ("".equals(encryGroupRights)) {
      throw new Exception("Decry content is empty.");
    } else {
      StringBuffer sb = new StringBuffer("Grp");
      sb.append(groupID);
      return decryText(encryGroupRights, sb.toString());
    }
  }

  public String usrEncryGroupRight(String plainGroupRights, String groupID)
      throws Exception {
    if ("".equals(plainGroupRights)) {
      throw new Exception("Encry content is empty.");
    } else {
      StringBuffer sb = new StringBuffer("Grp");
      sb.append(groupID);
      return encryText(plainGroupRights, sb.toString());
    }
  }

  public String usrDecryUserName(String encryUserName)
      throws Exception {
    if ("".equals(encryUserName)) {
      throw new Exception("Decry content is empty.");
    } else {
      String sb = new String("LoGin");
      return decryText(encryUserName, sb);
    }
  }

  public String usrEncryUserName(String plainUserName)
      throws Exception {
    if ("".equals(plainUserName)) {
      throw new Exception("Encry content is empty.");
    } else {
      String sb = new String("LoGin");
      return encryText(plainUserName, sb);
    }
  }

  public String usrDesryRight(String encryRights, String userName)
      throws Exception {
    if ("".equals(encryRights)) {
      throw new Exception("Decry content is empty.");
    } else {
      StringBuffer sb = new StringBuffer("uRg");
      sb.append(userName);
      return decryText(encryRights, sb.toString());
    }
  }

  public String usrEncryRight(String plainRights, String userName)
      throws Exception {
    if ("".equals(plainRights)) {
      throw new Exception("Encry content is empty.");
    } else {
      StringBuffer sb = new StringBuffer("uRg");
      sb.append(userName);
      return encryText(plainRights, sb.toString());
    }
  }

  public String usrDesryLocation(String encryLocation, String userName)
      throws Exception {
    if ("".equals(encryLocation)) {
      throw new Exception("Decry content is empty.");
    } else {
      StringBuffer sb = new StringBuffer("uLo");
      sb.append(userName);
      return decryText(encryLocation, sb.toString());
    }
  }

  public String usrEncryLocation(String plainLocation, String userName)
      throws Exception {
    if ("".equals(plainLocation)) {
      throw new Exception("Encry content is empty.");
    } else {
      StringBuffer sb = new StringBuffer("uLo");
      sb.append(userName);
      return encryText(plainLocation, sb.toString());
    }
  }

  public String usrEncryPassword(String plainPassword, String userName)
      throws Exception {
    if ("".equals(plainPassword)) {
      throw new Exception("Encry content is empty.") {

      }
          ;
    } else {
      StringBuffer sb = new StringBuffer("PwD");
      sb.append(userName);
      return encryText(plainPassword, sb.toString());
    }
  }

  public static void main(String args[]) {
    EncryptUtil en = new EncryptUtil();
  }

  private static final int CHARBASE = 33;
  private static final int MINCHARRANGE = 0;
  private static final int MAXCHARRANGE = 255;
  private static final int BITOFBYTE = 8;
  private static final int CHARORDERBASE = 3;
  private static final int CHARORDERMODE = 4;
}
