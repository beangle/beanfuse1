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

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.beanfuse.commons.lang.SeqStringUtil;
import org.beanfuse.commons.web.dispatch.Action;
import org.beanfuse.security.management.RoleManager;
import org.beanfuse.security.management.UserManager;
import org.beanfuse.security.model.Role;
import org.beanfuse.security.model.User;
import org.beanfuse.security.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package org.beanfuse.security.web.action:
//            SecurityBaseAction

public class ManagementAction extends SecurityBaseAction {

  public ManagementAction() {
  }

  public ActionForward editManagement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    Long userId = getLong(request, "user.id");
    User manager = userService.get(getUserId(request));
    User user = null;
    if (null != userId && userId.intValue() != 0)
      user = userService.get(userId);
    if (null == user) {
      return forward(request, new Action(org.beanfuse.security.web.action.UserAction.class, "search"));
    } else {
      request.setAttribute("manager", manager);
      request.setAttribute("user", user);
      return forward(request);
    }
  }

  public ActionForward saveManagement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long userId = getLong(request, "user.id");
    User user = null;
    if (null != userId)
      user = userService.get(userId);
    User manager = getUser(request);
    manageUser(manager, user, request.getParameter("userIds"));
    manageRole(manager, user, request.getParameter("roleIds"));
    userService.saveOrUpdate(user);
    return redirect(request, new Action(org.beanfuse.security.web.action.UserAction.class, "search"), "info.save.success");
  }

  private void manageUser(UserManager manager, UserManager user, String userIdSeq) {
    List users = new ArrayList();
    if (StringUtils.isNotEmpty(userIdSeq))
      users = userService.getUsers(SeqStringUtil.transformToLong(userIdSeq));
    user.getMngUsers().removeAll(manager.getMngUsers());
    user.getMngUsers().addAll(users);
  }

  private void manageRole(RoleManager manager, RoleManager user, String roleIdSeq) {
    List roles = new ArrayList();
    if (StringUtils.isNotEmpty(roleIdSeq))
      roles = utilService.load(Role.class, "id", SeqStringUtil.transformToLong(roleIdSeq));
    user.getMngRoles().removeAll(manager.getMngRoles());
    user.getMngRoles().addAll(roles);
  }

  public ActionForward removeManagement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String userIdSeq = request.getParameter("userIds");
    if (StringUtils.isEmpty(userIdSeq))
      return forward(request, new Action(org.beanfuse.security.web.action.UserAction.class, "index"));
    Long userIds[] = SeqStringUtil.transformToLong(userIdSeq);
    User manager = userService.get(getUserId(request));
    List users = userService.getUsers(userIds);
    String removeWhat = request.getParameter("kind");
    if (removeWhat.equals("mngUsers") || removeWhat.equals("both"))
      removeManageUser(manager, users);
    else if (removeWhat.equals("mngRoles") || removeWhat.equals("both"))
      removeManageRole(manager, users);
    return redirect(request, new Action(org.beanfuse.security.web.action.UserAction.class, "search"), "info.save.success");
  }

  private void removeManageUser(UserManager manager, List users) {
    UserManager user;
    for (Iterator it = users.iterator(); it.hasNext(); utilService.saveOrUpdate(user)) {
      user = (UserManager) it.next();
      if (null != user.getMngUsers())
        user.getMngUsers().removeAll(manager.getMngUsers());
    }

  }

  private void removeManageRole(RoleManager manager, List users) {
    RoleManager user;
    for (Iterator it = users.iterator(); it.hasNext(); utilService.saveOrUpdate(user)) {
      user = (RoleManager) it.next();
      if (null != user.getMngRoles())
        user.getMngRoles().removeAll(manager.getMngRoles());
    }

  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  private UserService userService;
}
