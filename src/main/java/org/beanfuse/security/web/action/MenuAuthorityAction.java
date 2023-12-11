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
package org.beanfuse.security.web.action;

import org.beanfuse.commons.lang.SeqStringUtil;
import org.beanfuse.commons.query.Condition;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.web.dispatch.Action;
import org.beanfuse.security.*;
import org.beanfuse.security.model.*;
import org.beanfuse.security.portal.model.*;
import org.beanfuse.security.portal.service.MenuAuthorityService;
import org.beanfuse.security.service.AuthorityDecisionService;
import org.beanfuse.security.service.RoleService;
import org.beanfuse.security.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

// Referenced classes of package org.beanfuse.security.web.action:
//            SecurityBaseAction

public class MenuAuthorityAction extends SecurityBaseAction {

  public MenuAuthorityAction() {
  }

  public ActionForward index(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    User curUser = userService.get(getUserId(request));
    request.setAttribute("manager", curUser);
    return forward(request);
  }

  public ActionForward editAuthority(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    MenuAuthorityObject ao = getAuthorityObject(request);
    User user = getUser(request);
    List categories = new ArrayList();
    if (ao instanceof User)
      categories.addAll(((User) ao).getCategories());
    else
      categories.add(((Role) ao).getCategory());
    EntityQuery query = new EntityQuery(org.beanfuse.security.portal.model.MenuProfile.class, "menuProfile");
    query.add(new Condition("menuProfile.category in(:categories)", categories));
    List menuProfiles = (List) utilService.search(query);
    request.setAttribute("menuProfiles", menuProfiles);
    Long menuProfileId = getLong(request, "menuProfileId");
    if (null == menuProfileId && !menuProfiles.isEmpty())
      menuProfileId = ((MenuProfile) (MenuProfile) menuProfiles.get(0)).getId();
    if (null != menuProfileId) {
      MenuProfile menuProfile = (MenuProfile) utilService.get(org.beanfuse.security.portal.model.MenuProfile.class, menuProfileId);
      List menus = null;
      java.util.Collection resources = null;
      Long adminId = getUserId(request);
      if (adminId.longValue() == User.ADMIN_ID.longValue()) {
        menus = menuProfile.getMenus();
        resources = utilService.loadAll(Resource.class);
      } else {
        menus = menuAuthorityService.getMenus(menuProfile, user);
        resources = authorityService.getResources(adminId);
      }
      request.setAttribute("resources", new HashSet(resources));
      request.setAttribute("menus", menus);
      java.util.Collection aoMenus = null;
      Set aoResources = new HashSet();
      Map aoResourceAuthorityMap = new HashMap();
      List authorities = null;
      if (ao instanceof User) {
        aoMenus = menuAuthorityService.getUserMenus(menuProfile, (User) ao);
        authorities = authorityService.getUserAuthorities(ao.getId());
      } else {
        aoMenus = menuAuthorityService.getRoleMenus(menuProfile, (Role) ao);
        authorities = authorityService.getRoleAuthorities(ao.getId());
      }
      Authority authority;
      for (Iterator iter = authorities.iterator(); iter.hasNext(); aoResourceAuthorityMap.put(authority.getResource().getId().toString(), authority.getId())) {
        authority = (Authority) iter.next();
        aoResources.add(authority.getResource());
      }

      request.setAttribute("aoMenus", new HashSet(aoMenus));
      request.setAttribute("aoResources", aoResources);
      request.setAttribute("aoResourceAuthorityMap", aoResourceAuthorityMap);
    }
    request.setAttribute("ao", ao);
    return forward(request);
  }

  public ActionForward prompt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return forward(request);
  }

  public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    MenuAuthorityObject mao = getAuthorityObject(request);
    boolean forUser = mao instanceof User;
    MenuProfile menuProfile = (MenuProfile) utilService.get(MenuProfile.class, getLong(request, "menuProfileId"));
    Set newMenus = new HashSet(utilService.load(Menu.class, "id", SeqStringUtil.transformToLong(request.getParameterValues("menuId"))));
    Set newResources = new HashSet(utilService.load(Resource.class, "id", SeqStringUtil.transformToLong(request.getParameterValues("resourceId"))));
    User manager = getUser(request);
    Set mngMenus = null;
    Set mngResources = new HashSet();
    if (manager.getId().longValue() == User.ADMIN_ID.longValue())
      mngMenus = new HashSet(menuProfile.getMenus());
    else
      mngMenus = new HashSet(menuAuthorityService.getMenus(menuProfile, manager));
    Menu m;
    for (Iterator iter = mngMenus.iterator(); iter.hasNext(); mngResources.addAll(m.getResources()))
      m = (Menu) iter.next();

    Set removedMenus = new HashSet();
    Iterator iter = mao.getMenuAuthorities().iterator();
    do {
      if (!iter.hasNext())
        break;
      MenuAuthority ma = (MenuAuthority) iter.next();
      if (mngMenus.contains(ma.getMenu()) && ma.getMenu().getProfile().equals(menuProfile))
        if (!newMenus.contains(ma.getMenu()))
          removedMenus.add(ma);
        else
          newMenus.remove(ma.getMenu());
    } while (true);

    AuthorityObject ao = (AuthorityObject) mao;
    Set removedResources = new HashSet();
    iter = ao.getAuthorities().iterator();
    do {
      if (!iter.hasNext())
        break;
      Authority au = (Authority) iter.next();
      if (mngResources.contains(au.getResource()))
        if (!newResources.contains(au.getResource()))
          removedResources.add(au);
        else
          newResources.remove(au.getResource());
    } while (true);

    mao.getMenuAuthorities().removeAll(removedMenus);
    ao.getAuthorities().removeAll(removedResources);
    MenuAuthority authority = null;
    for (Iterator iterator = newMenus.iterator(); iterator.hasNext(); mao.getMenuAuthorities().add(authority)) {
      Menu menu = (Menu) iterator.next();
      authority = null;
      if (forUser)
        authority = new UserMenuAuthority((User) mao, menu);
      else
        authority = new RoleMenuAuthority((Role) mao, menu);
    }

    for (Iterator iterator = newResources.iterator(); iterator.hasNext(); ) {
      Resource resource = (Resource) iterator.next();
      Authority a = null;
      if (forUser)
        a = new UserAuthority((User) ao, resource);
      else
        a = new RoleAuthority((Role) ao, resource);

      ao.getAuthorities().add(a);
    }

    utilService.saveOrUpdate(mao);
    if (!forUser)
      authorityDecisionService.registerRoleAuthorities(ao.getId());
    return redirect(request, new Action(getClass(), "editAuthority", "&who=" + ((mao instanceof User) ? "userAuthority" : "roleAuthority") + "&id=" + mao.getId() + "&menuProfileId=" + menuProfile.getId()), "info.save.success");
  }

  private MenuAuthorityObject getAuthorityObject(HttpServletRequest request) {
    String who = request.getParameter("who");
    Long id = getLong(request, "id");
    if (StringUtils.isEmpty(who) || !who.equals("userAuthority") && !who.equals("roleAuthority") || null == id)
      return null;
    if (who.equals("userAuthority"))
      return (MenuAuthorityObject) userService.get(id);
    else
      return (MenuAuthorityObject) roleService.get(id);
  }

  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public void setMenuAuthorityService(MenuAuthorityService menuAuthorityService) {
    this.menuAuthorityService = menuAuthorityService;
  }

  public AuthorityDecisionService getAuthorityDecisionService() {
    return authorityDecisionService;
  }

  public void setAuthorityDecisionService(AuthorityDecisionService authorityDecisionService) {
    this.authorityDecisionService = authorityDecisionService;
  }

  private UserService userService;
  private RoleService roleService;
  private MenuAuthorityService menuAuthorityService;
  private AuthorityDecisionService authorityDecisionService;
}
