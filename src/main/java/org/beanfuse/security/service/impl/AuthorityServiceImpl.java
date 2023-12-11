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

import org.apache.commons.collections.CollectionUtils;
import org.beanfuse.commons.utils.persistence.impl.BaseServiceImpl;
import org.beanfuse.security.AuthorityObject;
import org.beanfuse.security.dao.AuthorityDao;
import org.beanfuse.security.model.*;
import org.beanfuse.security.service.AuthorityService;
import org.beanfuse.security.service.ResourceService;
import org.beanfuse.security.service.UserService;

import java.util.*;

public class AuthorityServiceImpl extends BaseServiceImpl
    implements AuthorityService {

  public AuthorityServiceImpl() {
  }

  public Resource getResource(String name) {
    return resourceService.getResource(name);
  }

  public List getAuthorities(Long userId) {
    User user = userService.get(userId);
    if (null == user)
      return Collections.EMPTY_LIST;
    Map authorities = new HashMap();
    List userAuths = authorityDao.getUserAuthorities(userId);
    Authority userAuth;
    for (Iterator iter = userAuths.iterator(); iter.hasNext(); authorities.put(userAuth.getResource(), userAuth))
      userAuth = (Authority) iter.next();

    if (null != user.getRoles()) {
      for (Iterator it = user.getRoles().iterator(); it.hasNext(); ) {
        List roleAuths = authorityDao.getRoleAuthorities(((Role) it.next()).getId());
        Iterator iter = roleAuths.iterator();
        while (iter.hasNext()) {
          RoleAuthority roleAuth = (RoleAuthority) iter.next();
          if (authorities.containsKey(roleAuth.getResource())) {
            Authority existed = (Authority) authorities.get(roleAuth.getResource());
            existed.merge(roleAuth);
          } else {
            authorities.put(roleAuth.getResource(), roleAuth);
          }
        }
      }

    }
    List authorityList = new ArrayList(authorities.values());
    Collections.sort(authorityList);
    return authorityList;
  }

  public Authority getAuthority(Long userId, Long moduleId) {
    if (null == userId || null == moduleId)
      return null;
    User user = userService.get(userId);
    if (null == user)
      return null;
    Authority au = getUserAuthority(userId, moduleId);
    Set roles = user.getRoles();
    if (null != roles) {
      for (Iterator it = roles.iterator(); it.hasNext(); ) {
        Role one = (Role) it.next();
        Authority ar = getRoleAuthority(one.getId(), moduleId);
        if (null == au)
          au = ar;
        else
          au.merge(ar);
      }

    }
    return au;
  }

  public List getUserAuthorities(Long userId) {
    return authorityDao.getUserAuthorities(userId);
  }

  public Authority getUserAuthority(Long userId, Long moduleId) {
    if (null == userId || null == moduleId)
      return null;
    else
      return authorityDao.getUserAuthority(userId, moduleId);
  }

  public List getResources(Long userId) {
    User user = userService.get(userId);
    Set resources = new HashSet();
    Map params = new HashMap();
    params.put("userId", user.getId());
    String hql = "select distinct r from  User as u  join u.authorities as a join a.resource as r  where  u.id = :userId and r.enabled=true";
    resources.addAll(utilDao.searchHQLQuery(hql, params));
    Long roleIds[] = userService.getRoleIds(user);
    hql = "select distinct m from Role as r join r.authorities as a join a.resource as m where  r.id = :roleId and  m.enabled=true";
    params.clear();
    for (int i = 0; i < roleIds.length; i++) {
      params.put("roleId", roleIds[i]);
      resources.addAll(utilDao.searchHQLQuery(hql, params));
    }

    return new ArrayList(resources);
  }

  public List getRoleAuthorities(Long roleId) {
    return authorityDao.getRoleAuthorities(roleId);
  }

  public Authority getRoleAuthority(Long roleId, Long moduleId) {
    if (roleId == null || null == moduleId)
      return null;
    else
      return authorityDao.getRoleAuthority(roleId, moduleId);
  }

  public List getUserResourceNames(Long userId) {
    return authorityDao.getUserResourceNames(userId);
  }

  public List getRoleResourceNames(Long roleId) {
    return authorityDao.getRoleResourceNames(roleId);
  }

  public List getCategoryResources(Long categoryId) {
    return authorityDao.getCategoryResources(categoryId);
  }

  public void copyAuthority(Role fromRole, Collection toRoles) {
    List fromAuthorities = getRoleAuthorities(fromRole.getId());
    List allAdded = new ArrayList();
    for (Iterator iter = toRoles.iterator(); iter.hasNext(); ) {
      Role toRole = (Role) iter.next();
      List toAuthorities = getRoleAuthorities(toRole.getId());
      Collection newAuthorities = CollectionUtils.subtract(fromAuthorities, toAuthorities);
      Iterator iterator = newAuthorities.iterator();
      while (iterator.hasNext()) {
        RoleAuthority auth = (RoleAuthority) iterator.next();
        allAdded.add(auth.clone());
      }
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
      authority = (Authority) model.clone();
      authority.setResource(element);
    }

    utilService.saveOrUpdate(ao);
  }

  public void remove(Authority authority) {
    if (null != authority)
      utilService.remove(authority);
  }

  public void saveOrUpdate(Authority authority) {
    utilService.saveOrUpdate(authority);
  }

  public void setAuthorityDao(AuthorityDao authorityDao) {
    this.authorityDao = authorityDao;
  }

  public void setResourceService(ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  protected AuthorityDao authorityDao;
  protected UserService userService;
  protected ResourceService resourceService;
}
