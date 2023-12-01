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


// Referenced classes of package org.beanfuse.commons.transfer:
//            AbstractTransferListener, Transfer, TransferResult

public class TransferDebugListener extends AbstractTransferListener {

  public TransferDebugListener() {
  }

  public void startTransfer(TransferResult tr) {
    tr.addMessage("start", transfer.getDataName());
  }

  public void endTransfer(TransferResult tr) {
    tr.addMessage("end", transfer.getDataName());
  }

  public void startTransferItem(TransferResult tr) {
    tr.addMessage("start Item", transfer.getTranferIndex() + "");
  }

  public void endTransferItem(TransferResult tr) {
    tr.addMessage("end Item", transfer.getCurrent());
  }

  public void setTransfer(Transfer transfer) {
    this.transfer = transfer;
  }

  Transfer transfer;
}
