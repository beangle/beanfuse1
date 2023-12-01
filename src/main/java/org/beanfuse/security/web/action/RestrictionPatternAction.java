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
import org.beanfuse.security.restriction.RestrictionPattern;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForward;

import javax.servlet.http.HttpServletRequest;

// Referenced classes of package org.beanfuse.security.web.action:
//            SecurityBaseAction

public class RestrictionPatternAction extends SecurityBaseAction {

  public RestrictionPatternAction() {
  }

  protected void editSetting(HttpServletRequest request, Entity entity)
      throws Exception {
    request.setAttribute("patternParams", utilService.loadAll(org.beanfuse.security.restriction.PatternParam.class));
  }

  protected ActionForward saveAndForward(HttpServletRequest request, Entity entity)
      throws Exception {
    RestrictionPattern pattern = (RestrictionPattern) entity;
    String paramIds = request.getParameter("paramIds");
    pattern.getParams().clear();
    if (StringUtils.isNotEmpty(paramIds))
      pattern.getParams().addAll(utilService.load(org.beanfuse.security.restriction.PatternParam.class, "id", SeqStringUtil.transformToLong(paramIds)));
    utilService.saveOrUpdate(pattern);
    return redirect(request, "search", "info.save.success");
  }
}
