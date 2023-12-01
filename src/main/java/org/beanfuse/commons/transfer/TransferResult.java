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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package org.beanfuse.commons.transfer:
//            TransferMessage, Transfer

public class TransferResult {

  public TransferResult() {
    msgs = new ArrayList();
    errs = new ArrayList();
  }

  public void addFailure(String message, Object value) {
    errs.add(new TransferMessage(transfer.getTranferIndex(), message, value));
  }

  public void addMessage(String message, Object value) {
    msgs.add(new TransferMessage(transfer.getTranferIndex(), message, value));
  }

  public boolean hasErrors() {
    return !errs.isEmpty();
  }

  public void printResult() {
    TransferMessage msg;
    for (Iterator iter = msgs.iterator(); iter.hasNext(); System.out.println(msg))
      msg = (TransferMessage) iter.next();

  }

  public int errors() {
    return errs.size();
  }

  public List getMsgs() {
    return msgs;
  }

  public void setMsgs(List msgs) {
    this.msgs = msgs;
  }

  public List getErrs() {
    return errs;
  }

  public void setErrs(List errs) {
    this.errs = errs;
  }

  public Transfer getTransfer() {
    return transfer;
  }

  public void setTransfer(Transfer transfer) {
    this.transfer = transfer;
  }

  List msgs;
  List errs;
  Transfer transfer;
}
