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
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.beanfuse.security.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class SymmetricEncryptHelper {
  private Cipher enCipher;
  private Cipher deCipher;
  private String algorithm;

  public SymmetricEncryptHelper(byte[] key) {
    this(key, "DES");
  }

  public SymmetricEncryptHelper(byte[] key, String algorithm) {
    this.algorithm = algorithm;
    SecureRandom sr = new SecureRandom();

    try {
      DESKeySpec dks = new DESKeySpec(key);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
      SecretKey secretKey = keyFactory.generateSecret(dks);
      this.enCipher = Cipher.getInstance(algorithm);
      this.deCipher = Cipher.getInstance(algorithm);
      this.enCipher.init(1, secretKey, sr);
      this.deCipher.init(2, secretKey, sr);
    } catch (Exception var7) {
      throw new RuntimeException(var7.getMessage());
    }
  }

  public byte[] encypt(byte[] value) {
    try {
      return this.enCipher.doFinal(value);
    } catch (Exception var3) {
      return new byte[0];
    }
  }

  public byte[] decrypt(byte[] value) {
    try {
      return this.deCipher.doFinal(value);
    } catch (Exception var3) {
      return new byte[0];
    }
  }

  public String getAlgorithm() {
    return this.algorithm;
  }

  public static String toHexString(byte[] value) {
    String newString = "";

    for (int i = 0; i < value.length; ++i) {
      byte b = value[i];
      String str = Integer.toHexString(b);
      if (str.length() > 2) {
        str = str.substring(str.length() - 2);
      }

      if (str.length() < 2) {
        str = "0" + str;
      }

      newString = newString + str;
    }

    return newString.toUpperCase();
  }

  public static byte[] fromHexString(String value) {
    byte[] rs = new byte[value.length() / 2];

    for (int i = 0; i < value.length(); i += 2) {
      String b = value.substring(i, i + 2);
      rs[i / 2] = Integer.valueOf(b, 16).byteValue();
    }

    return rs;
  }

  public static byte[] encypt(byte[] key, byte[] value) {
    return (new SymmetricEncryptHelper(key)).encypt(value);
  }

  public static byte[] decrypt(byte[] key, byte[] value) {
    return (new SymmetricEncryptHelper(key)).decrypt(value);
  }
}
