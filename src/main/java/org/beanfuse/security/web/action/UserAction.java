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
import org.beanfuse.commons.utils.web.RequestUtils;
import org.beanfuse.commons.web.dispatch.Action;
import org.beanfuse.security.User;
import org.beanfuse.security.management.ManagedUser;
import org.beanfuse.security.management.RoleManager;
import org.beanfuse.security.management.UserManager;
import org.beanfuse.security.management.service.UserMngService;
import org.beanfuse.security.model.AbstractAuthorityObject;
import org.beanfuse.security.model.UserPropertyExtractor;
import org.beanfuse.security.service.RoleService;
import org.beanfuse.security.service.UserService;
import org.beanfuse.security.utils.EncryptUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

// Referenced classes of package org.beanfuse.security.web.action:
//            SecurityBaseAction

public class UserAction extends SecurityBaseAction {

  public UserAction() {
  }

  public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    EntityQuery entityQuery = buildUserQuery(request);
    Collection users = utilService.search(entityQuery);
    addCollection(request, "users", users);
    return forward(request);
  }

  protected void indexSetting(HttpServletRequest request)
      throws Exception {
    addCollection(request, "categories", utilService.loadAll(org.beanfuse.security.UserCategory.class));
  }

  private EntityQuery buildUserQuery(HttpServletRequest request) {
    Long managerId = getUserId(request);
    EntityQuery entityQuery = new EntityQuery(entityName, "user");
    entityQuery.join("user.managers", "manager");
    entityQuery.add(new Condition("manager.id=" + managerId));
    String roleName = request.getParameter("roleName");
    if (StringUtils.isNotEmpty(roleName))
      entityQuery.add(new Condition("exists (select u from User u join u.roles as role where u.id=user.id and role.name like :roleName)", "%" + roleName + "%"));
    Long categoryId = RequestUtils.getLong(request, "categoryId");
    if (null != categoryId) {
      entityQuery.join("user.categories", "category");
      entityQuery.add(new Condition("category.id=:categoryId", categoryId));
    }
    populateConditions(request, entityQuery);
    entityQuery.setLimit(getPageLimit(request));
    entityQuery.addOrder(OrderUtils.parser(request.getParameter("orderBy")));
    return entityQuery;
  }

  protected Collection getExportDatas(HttpServletRequest request) {
    EntityQuery entityQuery = buildUserQuery(request);
    entityQuery.setLimit(null);
    return utilService.search(entityQuery);
  }

  protected PropertyExtractor getPropertyExtractor(HttpServletRequest request) {
    return new UserPropertyExtractor();
  }

  protected ActionForward saveAndForward(HttpServletRequest request, Entity entity)
      throws Exception {
    User user = (User) entity;
    String errorMsg = "";
    user.getCategories().clear();
    String categories[] = request.getParameterValues("categoryIds");
    for (int i = 0; i < categories.length; i++) {
      errorMsg = checkCategory(user, Long.valueOf(categories[i]));
      if (StringUtils.isNotEmpty(errorMsg))
        return forward(request, new Action("", "edit"), errorMsg);
    }

    errorMsg = checkUser(user);
    if (StringUtils.isNotEmpty(errorMsg))
      return forward(request, new Action("", "edit"), errorMsg);
    try {
      List roles = new ArrayList();
      String roleIdSeq = request.getParameter("roleIds");
      roles = roleService.get(SeqStringUtil.transformToLong(roleIdSeq));
      if (user.isVO()) {
        User creator = userService.get(getUserId(request));
        user.setPassword(getDefaultPassword(user));
        user.getRoles().clear();
        user.getRoles().addAll(roles);
        userMngService.create((UserManager) creator, (ManagedUser) user);
      } else {
        user.getRoles().clear();
        user.getRoles().addAll(roles);
        userService.saveOrUpdate(user);
      }
    } catch (Exception e) {
      return forward(request, "error");
    }
    return redirect(request, "search", "info.save.success");
  }

  protected void editSetting(HttpServletRequest request, Entity entity)
      throws Exception {
    User user = (User) entity;
    AbstractAuthorityObject authority = (AbstractAuthorityObject) user;
    request.setAttribute("authority", authority);
    User manager = getUser(request);
    Set mngRoles = ((RoleManager) manager).getMngRoles();
    if (null != user.getRoles())
      mngRoles.removeAll(user.getRoles());
    addCollection(request, "mngRoles", mngRoles);
    addCollection(request, "categories", utilService.loadAll(org.beanfuse.security.UserCategory.class));
  }

  public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String userIdSeq = request.getParameter("userIds");
    Long userIds[] = SeqStringUtil.transformToLong(userIdSeq);
    User creator = userService.get(getUserId(request));
    List toBeRemoved = userService.getUsers(userIds);
    try {
      Iterator it = toBeRemoved.iterator();
      do {
        if (!it.hasNext())
          break;
        User one = (User) it.next();
        if (!one.getId().equals(getUserId(request)))
          userMngService.remove((UserManager) creator, (ManagedUser) one);
      } while (true);
    } catch (Exception e) {
      return redirect(request, "search", "info.delete.failure");
    }
    return redirect(request, "search", "info.delete.success");
  }

  public ActionForward activate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String userIdSeq = request.getParameter("userIds");
    Long userIds[] = SeqStringUtil.transformToLong(userIdSeq);
    String isActivate = request.getParameter("isActivate");
    try {
      if (StringUtils.isNotEmpty(isActivate) && "false".equals(isActivate))
        userService.updateState(userIds, User.FREEZE);
      else
        userService.updateState(userIds, User.ACTIVE);
    } catch (Exception e) {
      return forward(mapping, request, "error.occurred", "error");
    }
    String msg = "info.activate.success";
    if (StringUtils.isNotEmpty(isActivate) && "false".equals(isActivate))
      msg = "info.unactivate.success";
    return redirect(request, "search", msg);
  }

  protected String checkCategory(User user, Long categoryId) {
    user.getCategories().add(utilService.get(org.beanfuse.security.UserCategory.class, categoryId));
    return "";
  }

  protected String checkUser(User user) {
    if (user.isVO() && utilService.exist(entityName, "name", user.getName()))
      return "error.model.existed";
    else
      return "";
  }

  protected String getDefaultPassword(User user) {
    return EncryptUtil.encodePassword("1");
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
  }

  public void setUserMngService(UserMngService userMngService) {
    this.userMngService = userMngService;
  }

  private UserService userService;
  private RoleService roleService;
  private UserMngService userMngService;
}
