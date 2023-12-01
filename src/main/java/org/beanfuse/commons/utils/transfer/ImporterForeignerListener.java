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

package org.beanfuse.commons.utils.transfer;

import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.model.EntityUtils;
import org.beanfuse.commons.model.Model;
import org.beanfuse.commons.transfer.TransferResult;
import org.beanfuse.commons.transfer.importer.ItemImporterListener;
import org.beanfuse.commons.utils.persistence.UtilDao;
import org.beanfuse.commons.utils.persistence.UtilService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImporterForeignerListener extends ItemImporterListener {
  protected UtilDao utilDao;
  protected Map foreigersMap = new HashMap();
  private static final int CACHE_SIZE = 500;
  private String[] foreigerKeys = new String[]{"code"};

  public ImporterForeignerListener(UtilService utilService) {
    if (null != utilService) {
      this.utilDao = utilService.getUtilDao();
    }

  }

  public ImporterForeignerListener(UtilDao utilDao) {
    this.utilDao = utilDao;
  }

  public void endTransferItem(TransferResult tr) {
    Object[] values = (Object[]) this.importer.getCurData();

    for (int i = 0; i < this.importer.getAttrs().length; ++i) {
      String attr = this.importer.getAttrs()[i];
      int foreigerKeyIndex = 0;

      boolean isforeiger;
      for (isforeiger = false; foreigerKeyIndex < this.foreigerKeys.length; ++foreigerKeyIndex) {
        if (attr.endsWith("." + this.foreigerKeys[foreigerKeyIndex])) {
          isforeiger = true;
          break;
        }
      }

      if (isforeiger) {
        String codeValue = null;
        if (values.length > i) {
          if (null == values[i]) {
            codeValue = null;
          } else {
            codeValue = values[i].toString();
          }

          try {
            Object foreiger = null;
            if (!StringUtils.isEmpty(codeValue)) {
              Object nestedForeigner = PropertyUtils.getProperty(this.importer.getCurrent(), StringUtils.substring(attr, 0, attr.lastIndexOf(".")));
              String className;
              if (nestedForeigner instanceof Entity) {
                className = EntityUtils.getEntityClassName(nestedForeigner.getClass());
                Map foreignerMap = (Map) this.foreigersMap.get(className);
                if (null == foreignerMap) {
                  foreignerMap = new HashMap();
                  this.foreigersMap.put(className, foreignerMap);
                }

                if (((Map) foreignerMap).size() > 500) {
                  ((Map) foreignerMap).clear();
                }

                foreiger = ((Map) foreignerMap).get(codeValue);
                if (foreiger == null) {
                  List foreigners = this.utilDao.load(Class.forName(className), this.foreigerKeys[foreigerKeyIndex], new Object[]{codeValue});
                  if (!foreigners.isEmpty()) {
                    foreiger = foreigners.iterator().next();
                    ((Map) foreignerMap).put(codeValue, foreiger);
                  } else {
                    tr.addFailure("error.model.notExist", codeValue);
                  }
                }
              }

              className = StringUtils.substring(attr, 0, attr.lastIndexOf("."));
              Model.populator.populateValue(className, foreiger, this.importer.getCurrent());
            }
          } catch (Exception var13) {
            var13.printStackTrace();
            throw new RuntimeException(var13.getMessage());
          }
        }
      }
    }

  }

  public void addForeigerKey(String key) {
    String[] foreigers = new String[this.foreigerKeys.length + 1];

    int i;
    for (i = 0; i < this.foreigerKeys.length; ++i) {
      foreigers[i] = this.foreigerKeys[i];
    }

    foreigers[i] = key;
    this.foreigerKeys = foreigers;
  }
}
