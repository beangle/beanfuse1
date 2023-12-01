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
package org.beanfuse.security.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class AuthenticationCodeGenerator {

  public AuthenticationCodeGenerator() {
  }

  public static String genCodeImage(OutputStream os)
      throws IOException {
    return genCodeImage(os, 45, 18, 0);
  }

  public static String genCodeImage(OutputStream os, int width, int height, int interferePoints) {
    BufferedImage image = new BufferedImage(width, height, 1);
    Graphics g = image.getGraphics();
    g.setColor(Color.white);
    g.fillRect(0, 0, width, height);
    g.setColor(Color.black);
    g.drawRect(0, 0, width - 1, height - 1);
    String arr[] = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y", "z", "A", "B", "C", "D",
        "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
        "Y", "Z"
    };
    Random rm = new Random();
    String rand = arr[rm.nextInt(arr.length)] + arr[rm.nextInt(arr.length)] + arr[rm.nextInt(arr.length)] + arr[rm.nextInt(arr.length)];
    rand = "" + rm.nextInt(10) + rm.nextInt(10) + rm.nextInt(10) + rm.nextInt(10);
    g.setColor(Color.black);
    g.setFont(CODE_FONT);
    g.drawString(rand, 2, 15);
    Random random = new Random();
    for (int iIndex = 0; iIndex < interferePoints; iIndex++) {
      int x = random.nextInt(width);
      int y = random.nextInt(height);
      g.drawLine(x, y, x, y);
    }

    g.dispose();
    try {
      ImageIO.write(image, "JPEG", os);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
    return rand;
  }

  public static Font CODE_FONT = new Font("Times New Roman", 0, 18);

}
