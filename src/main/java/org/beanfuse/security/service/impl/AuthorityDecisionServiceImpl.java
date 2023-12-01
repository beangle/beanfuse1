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
package org.beanfuse.security.service.impl;

import org.beanfuse.commons.utils.persistence.impl.BaseServiceImpl;
import org.beanfuse.security.User;
import org.beanfuse.security.service.AuthorityDecisionService;
import org.beanfuse.security.service.AuthorityService;
import org.beanfuse.security.service.UserService;

import java.util.*;

public class AuthorityDecisionServiceImpl extends BaseServiceImpl
    implements AuthorityDecisionService {

  public AuthorityDecisionServiceImpl() {
    userRoleIds = new HashMap();
    userAuthorities = new HashMap();
    roleAuthorities = new HashMap();
    ignoreResources = new HashSet(0);
  }

  public boolean isIgnored(String actionName) {
    return ignoreResources.contains(actionName);
  }

  public boolean authorized(Long userId, String resourceName) {
    Set actions = (Set) userAuthorities.get(userId);
    boolean success = null != actions && actions.contains(resourceName);
    if (success)
      return success;
    Long roleIds[] = (Long[]) (Long[]) userRoleIds.get(userId);
    if (null == roleIds)
      return false;
    for (int i = 0; i < roleIds.length; i++) {
      Long roleId = roleIds[i];
      actions = (Set) roleAuthorities.get(roleId);
      success = null != actions && actions.contains(resourceName);
      if (success)
        return success;
    }

    return false;
  }

  public void registerAuthorities(Long userId) {
    User user = (User) utilDao.get(org.beanfuse.security.User.class, userId);
    List ua = authorityService.getUserResourceNames(user.getId());
    if (!ua.isEmpty()) {
      userAuthorities.put(userId, new HashSet(ua));
      logger.debug("add authorities for user:{} recource:{}", user.getId(), ua);
    }
    Long roleIds[] = userService.getRoleIds(user);
    for (int i = 0; i < roleIds.length; i++)
      registerRoleAuthorities(roleIds[i]);

    userRoleIds.put(user.getId(), roleIds);
  }

  public void registerRoleAuthorities(Long roleId) {
    List ra = authorityService.getRoleResourceNames(roleId);
    roleAuthorities.put(roleId, new HashSet(ra));
    logger.debug("add authorities for role:{} resource:{}", roleId, ra);
  }

  public void removeAuthorities(Long userId) {
    logger.debug("remove authorities for userId:{}", userId);
    if (null != userId && null != userAuthorities) {
      userAuthorities.remove(userId);
      userRoleIds.remove(userId);
    }
  }

  public void setAuthorityService(AuthorityService authorityService) {
    this.authorityService = authorityService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public Set getIgnoreResources() {
    return ignoreResources;
  }

  public void setIgnoreResources(Set ignoreResources) {
    this.ignoreResources = ignoreResources;
  }

  protected Map userRoleIds;
  protected Map userAuthorities;
  protected Map roleAuthorities;
  protected Set ignoreResources;
  protected AuthorityService authorityService;
  protected UserService userService;
}
