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
package org.beanfuse.security.concurrent.category;


public class CategoryProfile {

  public CategoryProfile() {
  }

  public CategoryProfile(Long category, int max, int inactiveInterval) {
    this.category = category;
    this.max = max;
    this.inactiveInterval = inactiveInterval;
  }

  public boolean hasCapacity() {
    return online < max;
  }

  public boolean isFull() {
    return online >= max;
  }

  public int getOnline() {
    return online;
  }

  public void setOnline(int online) {
    this.online = online;
  }

  public Long getCategory() {
    return category;
  }

  public void setCategory(Long category) {
    this.category = category;
  }

  public int getMax() {
    return max;
  }

  public void setMax(int max) {
    this.max = max;
  }

  public int getInactiveInterval() {
    return inactiveInterval;
  }

  public void setInactiveInterval(int inactiveInterval) {
    this.inactiveInterval = inactiveInterval;
  }

  public void increase() {
    online++;
  }

  public void decrease() {
    online--;
  }

  public String toString() {
    return "{category=" + category + ";max=" + max + "}";
  }

  Long category;
  int online;
  int max;
  int inactiveInterval;
}
