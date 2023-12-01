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

package org.beanfuse.security.restriction.model;

import org.beanfuse.commons.lang.SeqStringUtil;
import org.beanfuse.commons.model.pojo.LongIdObject;
import org.beanfuse.security.restriction.PatternParam;
import org.beanfuse.security.restriction.Restriction;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RestrictionItem extends LongIdObject implements org.beanfuse.security.restriction.RestrictionItem {
  private static final long serialVersionUID = -6244689987796389613L;
  protected PatternParam param;
  protected String value;
  protected Restriction restriction;

  public RestrictionItem() {
  }

  public RestrictionItem(PatternParam param, String value) {
    this.param = param;
    this.value = value;
  }

  public Restriction getRestriction() {
    return this.restriction;
  }

  public void setRestriction(Restriction restriction) {
    this.restriction = restriction;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public PatternParam getParam() {
    return this.param;
  }

  public void setParam(PatternParam param) {
    this.param = param;
  }

  protected Object clone() throws CloneNotSupportedException {
    RestrictionItem item = new RestrictionItem();
    item.setParam(this.getParam());
    item.setValue(this.getValue());
    return item;
  }

  public String toString() {
    return "key:[" + this.param.getName() + "] value:[" + this.value + "]";
  }

  public Object getParamValue() {
    if (ObjectUtils.equals("*", this.value)) {
      return "*";
    } else if (null != this.param) {
      try {
        Constructor con = Class.forName(this.param.getType()).getConstructor(java.lang.String.class);
        if (StringUtils.isEmpty(this.value)) {
          return null;
        } else if (!this.param.isMultiValue()) {
          return con.newInstance(this.value);
        } else {
          Set valueSet = new HashSet();
          String[] values = StringUtils.split(this.value, ",");

          for (int i = 0; i < values.length; ++i) {
            valueSet.add(con.newInstance(values[i]));
          }

          return valueSet;
        }
      } catch (Exception var5) {
        throw new RuntimeException("exception with param type:" + this.param.getType() + " value:" + this.getValue());
      }
    } else {
      return this.value;
    }
  }

  public static RestrictionItem mergeAll(List realms) {
    RestrictionItem item = new RestrictionItem();
    if (CollectionUtils.isEmpty(realms)) {
      return item;
    } else {
      Iterator iter = realms.iterator();

      while (iter.hasNext()) {
        RestrictionItem thisItem = (RestrictionItem) iter.next();
        item.merge(thisItem);
      }

      return item;
    }
  }

  public RestrictionItem merge(RestrictionItem other) {
    if (null == other) {
      return this;
    } else {
      this.setValue(evictComma(SeqStringUtil.mergeSeq(this.getValue(), other.getValue())));
      return this;
    }
  }

  public RestrictionItem shrink(RestrictionItem other) {
    if (null == other) {
      return this;
    } else {
      this.setValue(evictComma(SeqStringUtil.subtractSeq(this.getValue(), other.getValue())));
      return this;
    }
  }

  private static String evictComma(String str) {
    if (StringUtils.isEmpty(str)) {
      return str;
    } else if (str.startsWith(",") && str.endsWith(",")) {
      return str.substring(1, str.length() - 1);
    } else if (str.startsWith(",")) {
      return str.substring(1);
    } else {
      return str.endsWith(",") ? str.substring(0, str.length() - 1) : str;
    }
  }
}
