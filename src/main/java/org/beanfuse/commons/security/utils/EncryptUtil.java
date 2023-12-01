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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;

public class EncryptUtil {

  public EncryptUtil() {
  }

  public static String encodePassword(String password) {
    return encodePassword(password, "MD5");
  }

  public static String encodePassword(String password, String algorithm) {
    byte unencodedPassword[] = password.getBytes();
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance(algorithm);
    } catch (Exception e) {
      log.error("Exception: " + e);
      return password;
    }
    md.reset();
    md.update(unencodedPassword);
    byte encodedPassword[] = md.digest();
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < encodedPassword.length; i++) {
      if ((encodedPassword[i] & 0xff) < 16)
        buf.append("0");
      buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
    }

    return buf.toString();
  }

  private static final Log log;

  static {
    log = LogFactory.getLog(org.beanfuse.commons.security.utils.EncryptUtil.class);
  }
}
