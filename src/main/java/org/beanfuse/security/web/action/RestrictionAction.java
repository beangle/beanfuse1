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
import org.beanfuse.commons.bean.comparators.PropertyComparator;
import org.beanfuse.commons.lang.SeqStringUtil;
import org.beanfuse.security.model.*;
import org.beanfuse.security.restriction.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

// Referenced classes of package org.beanfuse.security.web.action:
//            SecurityBaseAction

public class RestrictionAction extends SecurityBaseAction {

  public RestrictionAction() {
  }

  public ActionForward tip(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return forward(request);
  }

  public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Restriction restriction = getRestriction(request);
    RestrictionHolder holer = getHolder(request);
    holer.getRestrictions().remove(restriction);
    utilService.saveOrUpdate(holer);
    return redirect(request, "info", "info.delete.success");
  }

  private RestrictionHolder getHolder(HttpServletRequest request) {
    Long restrictionHolderId = getLong(request, "restriction.holder.id");
    String restrictionType = get(request, "restrictionType");
    RestrictionHolder holer = null;
    if ("user".equals(restrictionType))
      holer = (RestrictionHolder) utilService.get(User.class, restrictionHolderId);
    else if ("role".equals(restrictionType))
      holer = (RestrictionHolder) utilService.get(Role.class, restrictionHolderId);
    else if ("userAuthority".equals(restrictionType))
      holer = (RestrictionHolder) utilService.get(UserAuthority.class, restrictionHolderId);
    else
      holer = (RestrictionHolder) utilService.get(RoleAuthority.class, restrictionHolderId);
    return holer;
  }

  public ActionForward info(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    RestrictionHolder holder = getHolder(request);
    List restrictions = new ArrayList(holder.getRestrictions());
    Collections.sort(restrictions, new PropertyComparator("pattern.name"));
    Map paramMaps = new HashMap();
    Restriction restriction;
    Map aoParams;
    label0:
    for (Iterator iterator = restrictions.iterator(); iterator.hasNext(); paramMaps.put(restriction.getId().toString(), aoParams)) {
      restriction = (Restriction) iterator.next();
      aoParams = new HashMap();
      Iterator iter = restriction.getItems().iterator();
      do {
        if (!iter.hasNext())
          continue label0;
        RestrictionItem item = (RestrictionItem) iter.next();
        if (StringUtils.isNotEmpty(item.getValue()))
          if (null == item.getParam().getEditor())
            aoParams.put(item.getParam().getName(), item.getValue());
          else
            aoParams.put(item.getParam().getName(), restrictionService.select(restrictionService.getValues(item.getParam()), item));
      } while (true);
    }

    String forEdit = request.getParameter("forEdit");
    if (StringUtils.isNotEmpty(forEdit)) {
      List patterns = new ArrayList();
      if (holder instanceof Authority) {
        Authority au = (Authority) holder;
        patterns = new ArrayList(au.getResource().getPatterns());
      } else {
        patterns = utilService.loadAll(org.beanfuse.security.restriction.RestrictionPattern.class);
      }
      request.setAttribute("patterns", patterns);
    }
    request.setAttribute("paramMaps", paramMaps);
    request.setAttribute("restrictions", restrictions);
    return forward(request);
  }

  public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Restriction restriction = getRestriction(request);
    for (Iterator iter = restriction.getPattern().getParams().iterator(); iter.hasNext(); ) {
      PatternParam param = (PatternParam) iter.next();
      RestrictionItem item = restriction.getItem(param);
      if (null == item)
        item = restriction.addItem(param);
      String values[] = request.getParameterValues(param.getName());
      if (null == values || values.length == 0)
        item.setValue(null);
      else if (values.length == 1)
        item.setValue(values[0]);
      else
        item.setValue(SeqStringUtil.transformToSeq(values));
    }

    RestrictionHolder holder = getHolder(request);
    if (restriction.isVO()) {
      holder.getRestrictions().add(restriction);
      utilService.saveOrUpdate(holder);
    } else {
      utilService.saveOrUpdate((String) restrictionTypeMap.get(get(request, "restrictionType")), restriction);
    }
    return redirect(request, "info", "info.save.success");
  }

  public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Restriction restriction = getRestriction(request);
    Map mngParams = new HashMap();
    Map aoParams = new HashMap();
    PatternParam param;
    List mngParam;
    for (Iterator iter = restriction.getPattern().getParams().iterator(); iter.hasNext(); mngParams.put(param.getName(), mngParam)) {
      param = (PatternParam) iter.next();
      mngParam = restrictionService.getValues(param);
    }

    for (Iterator iter = restriction.getItems().iterator(); iter.hasNext(); ) {
      RestrictionItem item = (RestrictionItem) iter.next();
      if (null == item.getParam().getEditor()) {
        aoParams.put(item.getParam().getName(), item.getValue());
      } else {
        Set aoParam = restrictionService.select(restrictionService.getValues(item.getParam()), item);
        aoParams.put(item.getParam().getName(), aoParam);
      }
    }

    request.setAttribute("mngParams", mngParams);
    request.setAttribute("aoParams", aoParams);
    request.setAttribute("restriction", restriction);
    return forward(request);
  }

  private Restriction getRestriction(HttpServletRequest request) {
    Long restrictionId = getLong(request, "restriction.id");
    Restriction restriction = null;
    String entityName = (String) restrictionTypeMap.get(get(request, "restrictionType"));
    if (null == restrictionId)
      restriction = new org.beanfuse.security.restriction.model.Restriction();
    else
      restriction = (Restriction) utilService.get(entityName, restrictionId);
    Map params = getParams(request, "restriction");
    populate(params, restriction, entityName);
    if (null == restrictionId)
      restriction.setPattern((RestrictionPattern) utilService.get(org.beanfuse.security.restriction.RestrictionPattern.class, restriction.getPattern().getId()));
    return restriction;
  }

  public static final Map restrictionTypeMap;

  static {
    restrictionTypeMap = new HashMap();
    restrictionTypeMap.put("user", "org.beanfuse.security.restriction.UserRestriction");
    restrictionTypeMap.put("role", "org.beanfuse.security.restriction.RoleRestriction");
    restrictionTypeMap.put("userAuthority", "org.beanfuse.security.restriction.UserAuthorityRestriction");
    restrictionTypeMap.put("roleAuthority", "org.beanfuse.security.restriction.RoleAuthorityRestriction");
  }
}
