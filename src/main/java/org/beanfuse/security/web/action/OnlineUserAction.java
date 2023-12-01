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

import org.beanfuse.commons.bean.comparators.PropertyComparator;
import org.beanfuse.commons.query.limit.PageAdapter;
import org.beanfuse.security.UserCategory;
import org.beanfuse.security.concurrent.category.CategorySessionController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

// Referenced classes of package org.beanfuse.security.web.action:
//            SecurityBaseAction

public class OnlineUserAction extends SecurityBaseAction {

  public OnlineUserAction() {
  }

  protected void indexSetting(HttpServletRequest request) {
    String orderBy = request.getParameter("orderBy");
    if (StringUtils.isEmpty(orderBy))
      orderBy = "principal asc";
    List sessionInfos = sessionController.getSessionRegistry().getSessionInfos();
    Collections.sort(sessionInfos, new PropertyComparator(orderBy));
    addCollection(request, "sessionInfos", new PageAdapter(sessionInfos, getPageLimit(request)));
    request.setAttribute("max", new Integer(sessionController.getMax()));
    request.setAttribute("onlineCount", new Integer(sessionController.getOnlineCount()));
    Map maxMap = new HashMap();
    Map onlineMap = new HashMap();
    List categories = utilService.loadAll(org.beanfuse.security.UserCategory.class);
    Map categoryMap = new HashMap();
    UserCategory category;
    for (Iterator iterator = categories.iterator(); iterator.hasNext(); categoryMap.put(category.getId().toString(), category)) {
      category = (UserCategory) iterator.next();
      String categoryId = category.getId().toString();
      maxMap.put(categoryId, new Integer(sessionController.getMax(category.getId())));
      onlineMap.put(categoryId, new Integer(sessionController.getOnlineCount(category.getId())));
    }

    request.setAttribute("categories", categoryMap);
    request.setAttribute("maxMap", maxMap);
    request.setAttribute("onlineMap", onlineMap);
  }

  public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    List categories = utilService.loadAll(org.beanfuse.security.UserCategory.class);
    Long categoryId;
    int max;
    for (Iterator iterator = categories.iterator(); iterator.hasNext(); sessionController.setMax(categoryId, max)) {
      UserCategory category = (UserCategory) iterator.next();
      categoryId = category.getId();
      max = NumberUtils.toInt(request.getParameter("max_" + categoryId));
    }

    return redirect(request, "index", "info.save.success");
  }

  public ActionForward invalidate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String sessionIds[] = StringUtils.split(request.getParameter("sessionIds"), ",");
    String mySessionId = request.getSession().getId();
    if (null != sessionIds) {
      for (int i = 0; i < sessionIds.length; i++)
        if (!mySessionId.equals(sessionIds[i]))
          sessionController.removeAuthentication(sessionIds[i]);

    }
    return redirect(request, "index", "info.save.success");
  }

  public void setSessionController(CategorySessionController sessionController) {
    this.sessionController = sessionController;
  }

  private CategorySessionController sessionController;
}
