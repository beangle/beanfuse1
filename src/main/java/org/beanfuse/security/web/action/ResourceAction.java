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
import org.beanfuse.commons.query.Condition;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.query.Order;
import org.beanfuse.security.model.Resource;
import org.beanfuse.security.service.ResourceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

// Referenced classes of package org.beanfuse.security.web.action:
//            SecurityBaseAction

public class ResourceAction extends SecurityBaseAction {

  public ResourceAction() {
  }

  public ActionForward activate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long resourceIds[] = SeqStringUtil.transformToLong(request.getParameter("resourceIds"));
    Boolean enabled = getBoolean(request, "enabled");
    if (null == enabled)
      enabled = Boolean.FALSE;
    resourceService.updateState(resourceIds, enabled.booleanValue());
    return redirect(request, "search", "info.save.success");
  }

  protected void editSetting(HttpServletRequest request, Entity entity)
      throws Exception {
    Resource resource = (Resource) entity;
    List patterns = utilService.loadAll(org.beanfuse.security.restriction.RestrictionPattern.class);
    patterns.removeAll(resource.getPatterns());
    request.setAttribute("patterns", patterns);
    request.setAttribute("categories", utilService.loadAll(org.beanfuse.security.model.UserCategory.class));
  }

  protected ActionForward saveAndForward(HttpServletRequest request, Entity entity)
      throws Exception {
    Resource resource = (Resource) entity;
    if (null != resource) {
      List list = utilService.load(Resource.class, "name", resource.getName());
      int isUniqueFlag = 0;
      if (null != resource.getId())
        isUniqueFlag = 1;
      if (null != list && list.size() > isUniqueFlag)
        return redirect(request, "edit", "info.save.error.unique.name");
    }
    Long patternIds[] = SeqStringUtil.transformToLong(get(request, "patternIds"));
    List patterns = Collections.EMPTY_LIST;
    if (null != patternIds)
      patterns = utilService.load(org.beanfuse.security.restriction.RestrictionPattern.class, "id", patternIds);
    resource.getPatterns().clear();
    resource.getPatterns().addAll(patterns);
    String categoryIds[] = request.getParameterValues("categoryIds");
    List categories = utilService.load(Resource.class, "id", SeqStringUtil.transformToLong(categoryIds));
    resource.getCategories().clear();
    resource.getCategories().addAll(categories);
    utilService.saveOrUpdate(resource);
    return redirect(request, "search", "info.save.success");
  }

  public ActionForward info(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long entityId = getEntityId(request, shortName);
    Entity entity = getModel(entityName, entityId);
    EntityQuery query = new EntityQuery(org.beanfuse.security.portal.model.Menu.class, "menu");
    query.join("menu.resources", "r");
    query.add(new Condition("r.id=:resourceId", entity.getEntityId()));
    query.addOrder(new Order("menu.profile.id,menu.code"));
    List menus = (List) utilService.search(query);
    request.setAttribute(shortName, entity);
    request.setAttribute("menus", menus);
    request.setAttribute("categories", utilService.loadAll(org.beanfuse.security.model.UserCategory.class));
    return forward(request);
  }

  public void setResourceService(ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  private ResourceService resourceService;
}
