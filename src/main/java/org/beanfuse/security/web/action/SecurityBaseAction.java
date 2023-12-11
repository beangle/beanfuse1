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

import org.beanfuse.commons.mvc.struts.action.ExampleAction;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.utils.web.RequestUtils;
import org.beanfuse.security.AuthenticationException;
import org.beanfuse.security.AuthorityException;
import org.beanfuse.security.model.Resource;
import org.beanfuse.security.model.User;
import org.beanfuse.security.restriction.PatternParam;
import org.beanfuse.security.restriction.Restriction;
import org.beanfuse.security.restriction.RestrictionItem;
import org.beanfuse.security.restriction.service.RestrictionService;
import org.beanfuse.security.service.AuthorityService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

public class SecurityBaseAction extends ExampleAction {

  public SecurityBaseAction() {
  }

  protected List getRestrictions(HttpServletRequest request) {
    HttpSession session = request.getSession();
    Map restrictionMap = (Map) session.getAttribute("security.restriction");
    if (null == restrictionMap) {
      restrictionMap = new HashMap();
      session.setAttribute("security.restriction", restrictionMap);
    }
    String resourceName = RequestUtils.getRequestAction(request);
    Resource resource = authorityService.getResource(resourceName);
    if (resource.getPatterns().isEmpty())
      return Collections.EMPTY_LIST;
    List realms = (List) restrictionMap.get(resource.getId());
    User user = getUser(request);
    if (null == realms) {
      realms = restrictionService.getRestrictions(user, resource);
      restrictionMap.put(resource.getId(), realms);
    }
    if (realms.isEmpty())
      throw new AuthorityException(resourceName);
    else
      return realms;
  }

  protected List getRestricitonValues(HttpServletRequest request, String name) {
    List restrictions = getRestrictions(request);
    Set values = new HashSet();
    boolean gotIt = false;
    Iterator iterator = restrictions.iterator();
    do {
      if (!iterator.hasNext())
        break;
      Restriction restiction = (Restriction) iterator.next();
      RestrictionItem item = restiction.getItem(name);
      if (null != item) {
        gotIt = true;
        values.addAll(restrictionService.select(restrictionService.getValues(item.getParam()), item));
      }
    } while (true);
    if (!gotIt) {
      List params = utilService.load(org.beanfuse.security.restriction.PatternParam.class, "name", name);
      if (params.isEmpty()) {
        throw new RuntimeException("bad pattern parameter named :" + name);
      } else {
        PatternParam param = (PatternParam) params.get(0);
        return restrictionService.getValues(param);
      }
    } else {
      return new ArrayList(values);
    }
  }

  protected void applyRestriction(HttpServletRequest request, EntityQuery query) {
    HttpSession session = request.getSession();
    Map restrictionMap = (Map) session.getAttribute("security.restriction");
    if (null == restrictionMap) {
      restrictionMap = new HashMap();
      session.setAttribute("security.restriction", restrictionMap);
    }
    String resourceName = RequestUtils.getRequestAction(request);
    Resource resource = authorityService.getResource(resourceName);
    if (resource.getPatterns().isEmpty())
      return;
    List restrictions = (List) restrictionMap.get(resource.getId());
    if (null == restrictions) {
      restrictions = restrictionService.getRestrictions(getUser(request), resource);
      restrictionMap.put(resource.getId(), restrictions);
    }
    if (restrictions.isEmpty()) {
      throw new AuthorityException(resourceName);
    } else {
      restrictionService.apply(restrictions, query);
    }
  }

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      return super.execute(mapping, form, request, response);
    } catch (Exception var6) {
      if (var6 instanceof AuthenticationException) {
        throw var6;
      } else {
        var6.printStackTrace();
        request.setAttribute("stackTrace", ExceptionUtils.getStackTrace(var6));
        return this.forward(mapping, request, "error.occurred", "error");
      }
    }
  }

  protected static Long getUserId(HttpServletRequest request) {
    Long userId = (Long) request.getSession().getAttribute("security.userId");
    if (null == userId)
      throw new AuthenticationException();
    else
      return userId;
  }

  protected static Long getLoginName(HttpServletRequest request) {
    Long userId = (Long) request.getSession().getAttribute("security.loginName");
    if (null == userId)
      throw new AuthenticationException();
    else
      return userId;
  }

  protected static Long getUserName(HttpServletRequest request) {
    Long userId = (Long) request.getSession().getAttribute("security.userName");
    if (null == userId)
      throw new AuthenticationException();
    else
      return userId;
  }

  protected User getUser(HttpServletRequest request) {
    return (User) utilService.get(User.class, getUserId(request));
  }

  public void setAuthorityService(AuthorityService authorityService) {
    this.authorityService = authorityService;
  }

  public void setRestrictionService(RestrictionService restrictionService) {
    this.restrictionService = restrictionService;
  }

  protected AuthorityService authorityService;
  protected RestrictionService restrictionService;
}
