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
import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.query.Condition;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.query.OrderUtils;
import org.beanfuse.commons.transfer.exporter.PropertyExtractor;
import org.beanfuse.security.Role;
import org.beanfuse.security.management.ManagedRole;
import org.beanfuse.security.management.RoleManager;
import org.beanfuse.security.management.service.RoleMngService;
import org.beanfuse.security.model.AbstractAuthorityObject;
import org.beanfuse.security.model.RolePropertyExtractor;
import org.beanfuse.security.service.AuthorityService;
import org.beanfuse.security.service.RoleService;
import org.beanfuse.security.service.UserService;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

// Referenced classes of package org.beanfuse.security.web.action:
//            SecurityBaseAction

public class RoleAction extends SecurityBaseAction {

  public RoleAction() {
  }

  protected void indexSetting(HttpServletRequest request)
      throws Exception {
    addCollection(request, "categories", utilService.loadAll(org.beanfuse.security.UserCategory.class));
  }

  protected void editSetting(HttpServletRequest request, Entity entity)
      throws Exception {
    request.setAttribute("categories", utilService.loadAll(org.beanfuse.security.UserCategory.class));
    AbstractAuthorityObject authority = (AbstractAuthorityObject) entity;
    request.setAttribute("authority", authority);
  }

  public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    EntityQuery entityQuery = buildQuery(request);
    addCollection(request, "roles", utilService.search(entityQuery));
    return forward(request);
  }

  protected EntityQuery buildQuery(HttpServletRequest request) {
    Long userId = getUserId(request);
    EntityQuery entityQuery = new EntityQuery(entityName, "role");
    entityQuery.join("role.managers", "user");
    entityQuery.add(new Condition("user.id =:user_id", userId));
    populateConditions(request, entityQuery);
    entityQuery.setLimit(getPageLimit(request));
    entityQuery.addOrder(OrderUtils.parser(request.getParameter("orderBy")));
    return entityQuery;
  }

  protected Collection getExportDatas(HttpServletRequest request) {
    EntityQuery entityQuery = buildQuery(request);
    entityQuery.setLimit(null);
    return utilService.search(entityQuery);
  }

  protected PropertyExtractor getPropertyExtractor(HttpServletRequest request) {
    return new RolePropertyExtractor();
  }

  protected ActionForward saveAndForward(HttpServletRequest request, Entity entity)
      throws Exception {
    Role role = (Role) entity;
    if (null != role) {
      List list = utilService.load(org.beanfuse.security.Role.class, "name", role.getName());
      int isUniqueFlag = 0;
      if (null != role.getId())
        isUniqueFlag = 1;
      if (null != list && list.size() > isUniqueFlag)
        return redirect(request, "edit", "info.save.error.unique.name");
    }
    if (null == role.getId()) {
      org.beanfuse.security.User creator = userService.get(getUserId(request));
      roleMngService.create((RoleManager) creator, (ManagedRole) role);
    } else {
      roleService.saveOrUpdate(role);
    }
    return redirect(request, "search", "info.save.success");
  }

  public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String roleIdSeq = request.getParameter("roleIds");
    org.beanfuse.security.User curUser = userService.get(getUserId(request));
    List toBeRemoved = roleService.get(SeqStringUtil.transformToLong(roleIdSeq));
    roleMngService.remove((RoleManager) curUser, toBeRemoved);
    return redirect(request, "search", "info.delete.success");
  }

  public ActionForward copyAuthSetting(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long fromRoleId = getLong(request, "roleId");
    Role fromRole = roleService.get(fromRoleId);
    request.setAttribute("fromRole", fromRole);
    request.setAttribute("toRoles", ((RoleManager) getUser(request)).getMngRoles());
    return forward(request);
  }

  public ActionForward copyAuth(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long fromRoleId = getLong(request, "fromRoleId");
    Long toRoleIds[] = SeqStringUtil.transformToLong(request.getParameter("toRoleIds"));
    Role fromRole = roleService.get(fromRoleId);
    List toRoles = roleService.get(toRoleIds);
    authorityService.copyAuthority(fromRole, toRoles);
    return redirect(request, "search", "info.set.success");
  }

  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
  }

  public void setAuthorityService(AuthorityService authorityService) {
    this.authorityService = authorityService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public void setRoleMngService(RoleMngService roleMngService) {
    this.roleMngService = roleMngService;
  }

  private RoleService roleService;
  private UserService userService;
  private RoleMngService roleMngService;
}
