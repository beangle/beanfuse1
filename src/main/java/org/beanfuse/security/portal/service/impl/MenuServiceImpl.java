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
package org.beanfuse.security.portal.service.impl;

import org.beanfuse.commons.utils.persistence.impl.BaseServiceImpl;
import org.beanfuse.security.portal.dao.MenuDao;
import org.beanfuse.security.portal.model.Menu;
import org.beanfuse.security.portal.service.MenuService;

import java.util.Collections;
import java.util.List;

public class MenuServiceImpl extends BaseServiceImpl
    implements MenuService {

  public MenuServiceImpl() {
    menuDao = null;
  }

  public List getDescendants(String ancestorCode, int depth) {
    if (null == ancestorCode)
      return null;
    else
      return menuDao.getDescendants(ancestorCode, depth);
  }

  public List getActiveChildren(String parentCode) {
    return menuDao.getActiveChildren(parentCode);
  }

  public List getActiveDescendants(String ancestorCode, int depth) {
    return menuDao.getActiveDescendants(ancestorCode, depth);
  }

  public List getChildren(String parentCode) {
    return menuDao.getChildren(parentCode);
  }

  public Menu get(Long menuId) {
    return menuDao.get(menuId);
  }

  public Menu getByName(String name) {
    return menuDao.getByName(name);
  }

  public Menu getByCode(String code) {
    return menuDao.getByCode(code);
  }

  public List get(Menu menu) {
    return menuDao.get(menu);
  }

  public List get(Long menuIds[]) {
    if (null == menuIds || menuIds.length < 1)
      return Collections.EMPTY_LIST;
    else
      return menuDao.get(menuIds);
  }

  public void saveOrUpdate(Menu menu) {
    menuDao.saveOrUpdate(menu);
  }

  public void updateState(Long ids[], boolean isEnabled) {
    if (null == ids || ids.length < 1)
      return;
    for (int i = 0; i < ids.length; i++) {
      Menu menu = get(ids[i]);
      if (null != menu) {
        menu.setEnabled(isEnabled);
        saveOrUpdate(menu);
      }
    }

  }

  public void setMenuDao(MenuDao menuDao) {
    this.menuDao = menuDao;
  }

  private MenuDao menuDao;
}
