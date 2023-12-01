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

public class DESEncrypt {

  public DESEncrypt(byte desKey[]) {
    this.desKey = desKey;
  }

  public byte[] doEncrypt(byte plainText[])
      throws Exception {
    SecureRandom sr = new SecureRandom();
    byte rawKeyData[] = desKey;
    DESKeySpec dks = new DESKeySpec(rawKeyData);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    javax.crypto.SecretKey key = keyFactory.generateSecret(dks);
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(1, key, sr);
    byte data[] = plainText;
    byte encryptedData[] = cipher.doFinal(data);
    return encryptedData;
  }

  private byte desKey[];
}
