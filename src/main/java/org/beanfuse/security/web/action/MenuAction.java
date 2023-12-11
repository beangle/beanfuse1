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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.beanfuse.commons.lang.SeqStringUtil;
import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.query.OrderUtils;
import org.beanfuse.security.model.Resource;
import org.beanfuse.security.portal.model.Menu;
import org.beanfuse.security.portal.service.MenuService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

// Referenced classes of package org.beanfuse.security.web.action:
//            SecurityBaseAction

public class MenuAction extends SecurityBaseAction {

  public MenuAction() {
  }

  protected void indexSetting(HttpServletRequest request)
      throws Exception {
    request.setAttribute("profiles", utilService.loadAll(org.beanfuse.security.portal.model.MenuProfile.class));
  }

  protected void editSetting(HttpServletRequest request, Entity entity)
      throws Exception {
    request.setAttribute("profiles", utilService.loadAll(org.beanfuse.security.portal.model.MenuProfile.class));
    Menu menu = (Menu) entity;
    List resurces = utilService.loadAll(Resource.class);
    Set existResources = menu.getResources();
    if (null != resurces)
      resurces.removeAll(existResources);
    request.setAttribute("resources", resurces);
  }

  protected ActionForward saveAndForward(HttpServletRequest request, Entity entity)
      throws Exception {
    Menu menu = (Menu) entity;
    try {
      List resources = new ArrayList();
      String resourceIdSeq = request.getParameter("resourceIds");
      if (null != resourceIdSeq && resourceIdSeq.length() > 0)
        resources = utilService.load(Resource.class, "id", SeqStringUtil.transformToLong(resourceIdSeq));
      menu.getResources().clear();
      menu.getResources().addAll(resources);
      menuService.saveOrUpdate(menu);
    } catch (Exception e) {
      return forward(request, "error");
    }
    return redirect(request, "search", "info.save.success");
  }

  public ActionForward activate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String menuIdSeq = request.getParameter("menuIds");
    Long menuIds[] = SeqStringUtil.transformToLong(menuIdSeq);
    Boolean isActivate = getBoolean(request, "isActivate");
    if (null != isActivate && Boolean.FALSE.equals(isActivate))
      menuService.updateState(menuIds, false);
    else
      menuService.updateState(menuIds, true);
    return redirect(request, "search", "info.save.success");
  }

  public ActionForward preview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    EntityQuery query = new EntityQuery(org.beanfuse.security.portal.model.Menu.class, "menu");
    populateConditions(request, query);
    query.addOrder(OrderUtils.parser("menu.code asc"));
    addCollection(request, "menus", utilService.search(query));
    query.setQueryStr(null);
    query.setOrders(Collections.EMPTY_LIST);
    query.setSelect("max(length(menu.code)/2)");
    List rs = (List) utilService.search(query);
    request.setAttribute("depth", rs.get(0));
    return forward(request);
  }

  public void setmenuService(MenuService menuService) {
    this.menuService = menuService;
  }

  private MenuService menuService;
}
