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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.beanfuse.commons.utils.persistence.impl.BaseServiceImpl;
import org.beanfuse.security.AuthorityObject;
import org.beanfuse.security.model.*;
import org.beanfuse.security.portal.dao.MenuAuthorityDao;
import org.beanfuse.security.portal.model.Menu;
import org.beanfuse.security.portal.model.MenuAuthority;
import org.beanfuse.security.portal.model.MenuProfile;
import org.beanfuse.security.portal.service.MenuAuthorityService;
import org.beanfuse.security.service.UserService;

import java.util.*;

public class MenuAuthorityServiceImpl extends BaseServiceImpl
    implements MenuAuthorityService {

  public MenuAuthorityServiceImpl() {
  }

  public MenuAuthority getUserMenuAuthority(User user, Menu menu) {
    return menuAuthorityDao.getUserMenuAuthority(user, menu);
  }

  public List getMenuAuthorities(MenuProfile profile, User user) {
    return getMenuAuthorities(profile, user, -1, "0");
  }

  public List getMenuAuthorities(MenuProfile profile, User user, int depth, String ancestorCode) {
    if (null == user || StringUtils.isEmpty(ancestorCode))
      return Collections.EMPTY_LIST;
    Map authorities = new HashMap();
    List userAuths = menuAuthorityDao.getUserMenuAuthorities(profile, user, depth, ancestorCode);
    Authority userAuth;
    for (Iterator iter = userAuths.iterator(); iter.hasNext(); authorities.put(userAuth.getResource(), userAuth))
      userAuth = (Authority) iter.next();

    if (null != user.getRoles()) {
      for (Iterator it = user.getRoles().iterator(); it.hasNext(); ) {
        List roleAuths = menuAuthorityDao.getRoleMenuAuthorities(profile, (Role) it.next(), depth, ancestorCode);
        Iterator iter = roleAuths.iterator();
        while (iter.hasNext()) {
          RoleAuthority roleAuth = (RoleAuthority) iter.next();
          if (!authorities.containsKey(roleAuth.getResource()))
            authorities.put(roleAuth.getResource(), roleAuth);
        }
      }

    }
    List authorityList = new ArrayList(authorities.values());
    Collections.sort(authorityList);
    return authorityList;
  }

  public List getUserMenuAuthorities(MenuProfile profile, User user) {
    return getUserMenuAuthorities(profile, user, -1, null);
  }

  public List getUserMenuAuthorities(MenuProfile profile, User user, int depth, String ancestorCode) {
    if (null == user || StringUtils.isEmpty(ancestorCode)) {
      return null;
    } else {
      List userAuthorities = menuAuthorityDao.getUserMenuAuthorities(profile, user, depth, ancestorCode);
      return userAuthorities;
    }
  }

  public List getRoleMenuAuthorities(MenuProfile profile, Role role) {
    return getRoleMenuAuthorities(profile, role, -1);
  }

  public List getRoleMenuAuthorities(MenuProfile profile, Role role, int depth) {
    return getRoleMenuAuthorities(profile, role, depth, null);
  }

  public List getRoleMenuAuthorities(MenuProfile profile, Role role, int depth, String ancestorCode) {
    if (null == role || StringUtils.isEmpty(ancestorCode)) {
      return Collections.EMPTY_LIST;
    } else {
      List roleAuthorities = menuAuthorityDao.getRoleMenuAuthorities(profile, role, depth, ancestorCode);
      return roleAuthorities;
    }
  }

  public MenuAuthority getRoleMenuAuthority(Role role, Menu menu) {
    if (role == null || null == menu)
      return null;
    else
      return menuAuthorityDao.getRoleMenuAuthority(role, menu);
  }

  public List getMenus(MenuProfile profile, User user) {
    return getMenus(profile, user, -1, null);
  }

  public List getMenus(MenuProfile profile, User user, int depth, String ancestorCode) {
    Set modules = new HashSet();
    modules.addAll(menuAuthorityDao.getUserMenus(profile, user, depth, ancestorCode));
    Set roles = userService.getRoles(user);
    Role role;
    for (Iterator iterator = roles.iterator(); iterator.hasNext(); modules.addAll(menuAuthorityDao.getRoleMenus(profile, role, depth, ancestorCode)))
      role = (Role) iterator.next();

    if (null == modules) {
      return Collections.EMPTY_LIST;
    } else {
      List moduleList = new ArrayList(modules);
      Collections.sort(moduleList);
      return moduleList;
    }
  }

  public List getUserMenus(MenuProfile profile, User user) {
    return menuAuthorityDao.getUserMenus(profile, user, 0, null);
  }

  public List getRoleMenus(MenuProfile profile, Role role) {
    return menuAuthorityDao.getRoleMenus(profile, role, 0, null);
  }

  /**
   * @deprecated Method copyAuthority is deprecated
   */

  public void copyAuthority(MenuProfile profile, Role fromRole, Collection toRoles) {
    List fromAuthorities = getRoleMenuAuthorities(profile, fromRole);
    List allAdded = new ArrayList();
    for (Iterator iter = toRoles.iterator(); iter.hasNext(); ) {
      Role toRole = (Role) iter.next();
      List toAuthorities = getRoleMenuAuthorities(profile, toRole);
      Collection newAuthorities = CollectionUtils.subtract(fromAuthorities, toAuthorities);
      Iterator iterator = newAuthorities.iterator();
      while (iterator.hasNext()) ;
    }

    utilService.saveOrUpdate(allAdded);
  }

  public void authorize(AuthorityObject ao, Set resources, Class authorityClass) {
    Set reserved = new HashSet();
    Iterator iterator = ao.getAuthorities().iterator();
    do {
      if (!iterator.hasNext())
        break;
      Authority authority = (Authority) iterator.next();
      if (resources.contains(authority.getResource())) {
        reserved.add(authority);
        resources.remove(authority.getResource());
      }
    } while (true);
    ao.getAuthorities().clear();
    ao.getAuthorities().addAll(reserved);
    Authority model = null;
    try {
      model = (Authority) authorityClass.newInstance();
      model.setEnabled(true);
    } catch (Exception e) {
      throw new RuntimeException("cannot init authroity by class:" + authorityClass);
    }
    model.setAuthorityObject(ao);
    Authority authority;
    for (Iterator iter = resources.iterator(); iter.hasNext(); ao.getAuthorities().add(authority)) {
      Resource element = (Resource) iter.next();
      authority = null;
      authority.setResource(element);
    }

    utilService.saveOrUpdate(ao);
  }

  public static Set distillResources(Collection menus) {
    if (null == menus || menus.size() < 0)
      return Collections.EMPTY_SET;
    Set actionNames = new HashSet();
    Iterator it = menus.iterator();
    do {
      if (!it.hasNext())
        break;
      Set actions = ((Menu) it.next()).getResources();
      if (null != actions) {
        Iterator ot = actions.iterator();
        while (ot.hasNext())
          actionNames.add(((Resource) ot.next()).getName());
      }
    } while (true);
    return actionNames;
  }

  public void saveOrUpdate(MenuAuthority o) {
    utilService.saveOrUpdate(o);
  }

  public void setMenuAuthorityDao(MenuAuthorityDao menuAuthorityDao) {
    this.menuAuthorityDao = menuAuthorityDao;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  protected MenuAuthorityDao menuAuthorityDao;
  protected UserService userService;
}
