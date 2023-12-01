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
package org.beanfuse.commons.transfer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class TransferMessage {

  public TransferMessage(int index, String message, Object value) {
    values = new ArrayList();
    this.index = index;
    this.message = message;
    values.add(value);
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List getValues() {
    return values;
  }

  public void setValues(List values) {
    this.values = values;
  }

  public String toString() {
    return (new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)).append("index", index).append("message", message).append("values", values).toString();
  }

  public static String ERROR_ATTRS = "error.transfer.attrs";
  public static String ERROR_ATTRS_EXPORT = "error.transfer.attrs.export";
  int index;
  String message;
  List values;

}
