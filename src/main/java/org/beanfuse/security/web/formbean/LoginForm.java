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
package org.beanfuse.security.web.formbean;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.http.HttpServletRequest;

public class LoginForm extends ActionForm {

  public LoginForm() {
  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();
    if (name == null || name == "" || name.trim().length() < 1)
      addError(errors, "usrName", "userName.null.error");
    if (password == null || password == "" || password.trim().length() < 1)
      addError(errors, "password", "password.null.error");
    if (null != request.getSession().getAttribute("randomCode"))
      if (StringUtils.isEmpty(randomCode))
        addError(errors, "randomCode", "security.error.emptyAuthenticationCode");
      else if (!request.getSession().getAttribute("randomCode").equals(randomCode))
        addError(errors, "randomCode", "security.error.authenticationCode");
    return errors;
  }

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    name = null;
    password = null;
    randomCode = null;
  }

  protected void addError(ActionErrors errors, String targetName, String resourceName) {
    errors.add(targetName, new ActionMessage(resourceName));
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRandomCode() {
    return randomCode;
  }

  public void setRandomCode(String randomCode) {
    this.randomCode = randomCode;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  private static final long serialVersionUID = 0x21f92c82bcf85981L;
  private String name;
  private String password;
  private String randomCode;
  private String lang;
}
