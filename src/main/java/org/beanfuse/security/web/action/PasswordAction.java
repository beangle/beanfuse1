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

import org.beanfuse.commons.model.predicate.ValidEntityKeyPredicate;
import org.beanfuse.security.User;
import org.beanfuse.security.service.UserService;
import org.beanfuse.security.utils.EncryptUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Referenced classes of package org.beanfuse.security.web.action:
//            SecurityBaseAction

public class PasswordAction extends SecurityBaseAction {

  public PasswordAction() {
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void setMessage(SimpleMailMessage message) {
    this.message = message;
  }

  public ActionForward editUserAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long userId = getLong(request, "user.id");
    request.setAttribute("user", userService.get(userId));
    return forward(request);
  }

  public ActionForward saveOrUpdateAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long userId = getLong(request, "user.id");
    if (ValidEntityKeyPredicate.INSTANCE.evaluate(userId)) {
      return updateAccount(mapping, request, userId);
    } else {
      ActionMessages actionMessages = new ActionMessages();
      actionMessages.add("org.apache.struts.action.GLOBAL_MESSAGE", new ActionMessage("error.parameters.needed"));
      addErrors(request, actionMessages);
      return mapping.findForward("error");
    }
  }

  public ActionForward resetPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return forward(request);
  }

  public ActionForward changePassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    request.setAttribute("user", getUser(request));
    return forward(request);
  }

  public ActionForward updateUserAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long userId = getLong(request, "user.id");
    if (ValidEntityKeyPredicate.INSTANCE.evaluate(userId))
      return updateAccount(mapping, request, userId);
    else
      return null;
  }

  public ActionForward saveChange(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return updateAccount(mapping, request, getUserId(request));
  }

  private ActionForward updateAccount(ActionMapping mapping, HttpServletRequest request, Long userId) {
    String email = request.getParameter("email");
    String pwd = request.getParameter("password");
    Map valueMap = new HashMap(2);
    valueMap.put("password", pwd);
    valueMap.put("email", email);
    utilService.update(org.beanfuse.security.User.class, "id", new Object[]{
        userId
    }, valueMap);
    ActionMessage msg = new ActionMessage("info.password.changed");
    ActionMessages msgs = new ActionMessages();
    msgs.add("org.apache.struts.action.GLOBAL_MESSAGE", msg);
    addErrors(request, msgs);
    return mapping.findForward("actionResult");
  }

  public ActionForward sendPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    if (StringUtils.isEmpty(name) || StringUtils.isEmpty(email)) {
      ActionMessages actionMessages = new ActionMessages();
      actionMessages.add("org.apache.struts.action.GLOBAL_MESSAGE", new ActionMessage("error.parameters.needed"));
      addErrors(request, actionMessages);
      return mapping.findForward("error");
    }
    List userList = utilService.load(org.beanfuse.security.User.class, "name", name);
    User user = null;
    if (userList.isEmpty())
      return goErrorWithMessage(request, "error.user.notExist");
    user = (User) userList.get(0);
    if (!StringUtils.equals(email, user.getEmail()))
      return goErrorWithMessage(request, "error.email.notEqualToOrign");
    String longinName = user.getName();
    String password = RandomStringUtils.randomNumeric(6);
    user.setRemark(password);
    user.setPassword(EncryptUtil.encodePassword(password));
    MessageResources messageResources = getResources(request);
    String title = messageResources.getMessage("user.password.sendmail.title");
    List values = new ArrayList();
    values.add(longinName);
    values.add(password);
    String body = messageResources.getMessage("user.password.sendmail.body", values.toArray());
    try {
      SimpleMailMessage msg = new SimpleMailMessage(message);
      msg.setTo(user.getEmail());
      msg.setSubject(title);
      msg.setText(body.toString());
      mailSender.send(msg);
    } catch (Exception e) {
      e.printStackTrace();
      log.info("reset password error for user:" + user.getName() + " with email :" + user.getEmail());
      return goErrorWithMessage(request, "error.email.sendError");
    }
    utilService.saveOrUpdate(user);
    return forward(request, "sendResult");
  }

  private ActionForward goErrorWithMessage(HttpServletRequest request, String key) {
    ActionMessage msg = new ActionMessage(key);
    ActionMessages msgs = new ActionMessages();
    msgs.add("org.apache.struts.action.GLOBAL_MESSAGE", msg);
    addErrors(request, msgs);
    return forward(request, "resetPassword");
  }

  private UserService userService;
  private MailSender mailSender;
  private SimpleMailMessage message;
}
