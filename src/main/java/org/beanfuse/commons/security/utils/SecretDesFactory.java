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
package org.beanfuse.commons.security.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class SecretDesFactory {

  public SecretDesFactory(byte encKey[], byte desKey[])
      throws Exception {
    SecureRandom sr = new SecureRandom();
    byte rawKeyData[] = encKey;
    DESKeySpec dks = new DESKeySpec(rawKeyData);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    javax.crypto.SecretKey key = keyFactory.generateSecret(dks);
    enCipher = Cipher.getInstance("DES");
    enCipher.init(1, key, sr);
    rawKeyData = desKey;
    dks = new DESKeySpec(rawKeyData);
    key = keyFactory.generateSecret(dks);
    deCipher = Cipher.getInstance("DES");
    deCipher.init(2, key, sr);
  }

  public static String toHexString(byte value[]) {
    String newString = "";
    for (int i = 0; i < value.length; i++) {
      byte b = value[i];
      String str = Integer.toHexString(b);
      if (str.length() > 2)
        str = str.substring(str.length() - 2);
      if (str.length() < 2)
        str = "0" + str;
      newString = newString + str;
    }

    return newString.toUpperCase();
  }

  public static byte[] fromHexString(String value) {
    byte rs[] = new byte[value.length() / 2];
    for (int i = 0; i < value.length(); i += 2) {
      String b = value.substring(i, i + 2);
      rs[i / 2] = Integer.valueOf(b, 16).byteValue();
    }

    return rs;
  }

  public byte[] encypt(byte value[]) {
    try {
      return enCipher.doFinal(value);
    } catch (Exception e) {
      return new byte[0];
    }
  }

  public byte[] decrypt(byte value[]) {
    try {
      return deCipher.doFinal(value);
    } catch (Exception e) {
      return new byte[0];
    }
  }

  public static void main(String args[])
      throws Exception {
    String value = "2323";
    SecretDesFactory f = new SecretDesFactory("ABCDEFGH".getBytes(), "ABCDEFGH".getBytes());
    String encryptText = toHexString(f.encypt(value.getBytes()));
    System.out.println("doEncrypt - " + encryptText);
    System.out.println("doDecrypt - " + new String(f.decrypt(fromHexString(encryptText))));
  }

  private Cipher enCipher;
  private Cipher deCipher;
}
