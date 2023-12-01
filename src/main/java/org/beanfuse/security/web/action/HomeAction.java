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

import org.beanfuse.commons.query.Condition;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.security.AuthenticationException;
import org.beanfuse.security.User;
import org.beanfuse.security.portal.model.MenuProfile;
import org.beanfuse.security.portal.service.MenuAuthorityService;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;

// Referenced classes of package org.beanfuse.security.web.action:
//            SecurityBaseAction

public class HomeAction extends SecurityBaseAction {

  public HomeAction() {
  }

  public ActionForward index(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long userId = getUserId(request);
    if (null == userId) {
      throw new AuthenticationException("without login");
    } else {
      User user = getUser(request);
      EntityQuery query = new EntityQuery(org.beanfuse.security.portal.model.MenuProfile.class, "mp");
      query.add(new Condition("category.id=:categoryId", user.getDefaultCategory().getId()));
      List mps = (List) utilService.search(query);
      MenuProfile mp = (MenuProfile) mps.get(0);
      List dd = menuAuthorityService.getMenuAuthorities(mp, user, 1, "");
      request.setAttribute("menus", dd);
      request.setAttribute("user", user);
      request.setAttribute("categories", utilService.loadAll(org.beanfuse.security.UserCategory.class));
      return forward(request);
    }
  }

  public ActionForward moduleList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    String parentCode = request.getParameter("parentCode");
    if (StringUtils.isEmpty(parentCode))
      parentCode = "0";
    Long menuProfileId = getLong(request, "menuProfileId");
    MenuProfile mp = null;
    if (null != menuProfileId)
      mp = (MenuProfile) utilService.get(org.beanfuse.security.portal.model.MenuProfile.class, menuProfileId);
    List modulesTree = menuAuthorityService.getMenus(mp, getUser(request), -1, parentCode);
    request.setAttribute("moduleTree", modulesTree);
    request.setAttribute("parentCode", parentCode);
    return forward(request);
  }

  public ActionForward welcome(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    request.setAttribute("date", new Date(System.currentTimeMillis()));
    return forward(request);
  }

  public void setMenuAuthorityService(MenuAuthorityService menuAuthorityService) {
    this.menuAuthorityService = menuAuthorityService;
  }

  private MenuAuthorityService menuAuthorityService;
}
