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
package org.beanfuse.security.restriction.model;

import org.beanfuse.commons.model.pojo.LongIdObject;
import org.beanfuse.security.restriction.ParamEditor;

public class PatternParam extends LongIdObject
    implements org.beanfuse.security.restriction.PatternParam {

  public PatternParam() {
  }

  public boolean isMultiValue() {
    return multiValue;
  }

  public void setMultiValue(boolean multiValue) {
    this.multiValue = multiValue;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ParamEditor getEditor() {
    return editor;
  }

  public void setEditor(ParamEditor editor) {
    this.editor = editor;
  }

  private static final long serialVersionUID = 1L;
  private String name;
  private String description;
  private String type;
  private boolean multiValue;
  private ParamEditor editor;
}
