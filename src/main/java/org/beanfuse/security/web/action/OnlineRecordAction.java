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

import org.beanfuse.commons.query.Condition;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.query.OrderUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

// Referenced classes of package org.beanfuse.security.web.action:
//            SecurityBaseAction

public class OnlineRecordAction extends SecurityBaseAction {

  public OnlineRecordAction() {
  }

  protected void indexSetting(HttpServletRequest request)
      throws Exception {
    request.setAttribute("categories", utilService.loadAll(org.beanfuse.security.model.UserCategory.class));
  }

  public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    EntityQuery query = new EntityQuery(org.beanfuse.security.audit.OnlineRecord.class, "onlineRecord");
    populateConditions(request, query);
    Integer category = getInteger(request, "category");
    if (null != category)
      query.add(new Condition("bitand(onlineRecord.user.category,:category)>0", category));
    addTimeCondition(request, query);
    query.setLimit(getPageLimit(request));
    query.addOrder(OrderUtils.parser(request.getParameter("orderBy")));
    addCollection(request, "onlineRecords", utilService.search(query));
    return forward(request);
  }

  private void addTimeCondition(HttpServletRequest request, EntityQuery query)
      throws ParseException {
    String stime = request.getParameter("startTime");
    String etime = request.getParameter("endTime");
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date sdate = null;
    java.util.Date edate = null;
    if (StringUtils.isNotBlank(stime))
      sdate = df.parse(stime);
    if (StringUtils.isNotBlank(etime)) {
      edate = df.parse(etime);
      Calendar gc = new GregorianCalendar();
      gc.setTime(edate);
      gc.set(6, gc.get(6) + 1);
      edate = gc.getTime();
    }
    if (null != sdate && null == edate)
      query.add(new Condition("onlineRecord.loginAt >=:sdate", sdate));
    else if (null != sdate && null != edate)
      query.add(new Condition("onlineRecord.loginAt >=:sdate and onlineRecord.loginAt <:edate", sdate, edate));
    else if (null == sdate && null != edate)
      query.add(new Condition("onlineRecord.loginAt <:edate", edate));
  }

  public ActionForward loginCountStat(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    EntityQuery query = new EntityQuery(org.beanfuse.security.audit.OnlineRecord.class, "onlineRecord");
    query.setSelect("onlineRecord.name,onlineRecord.userName,count(onlineRecord.name)");
    populateConditions(request, query);
    String roleName = request.getParameter("roleName");
    addTimeCondition(request, query);
    if (StringUtils.isNotEmpty(roleName))
      query.add(new Condition("exists( from User u  join u.roles as role where u.name=onlineRecord.name and role.name like :roleName )", "%" + roleName + "%"));
    query.groupBy("onlineRecord.name,onlineRecord.userName");
    query.setLimit(getPageLimit(request));
    query.addOrder(OrderUtils.parser(request.getParameter("orderBy")));
    addCollection(request, "loginCountStats", utilService.search(query));
    return forward(request);
  }

  public ActionForward timeIntervalStat(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    EntityQuery query = new EntityQuery(org.beanfuse.security.audit.OnlineRecord.class, "onlineRecord");
    addTimeCondition(request, query);
    query.setSelect("to_char(onlineRecord.loginAt,'hh24'),count(*)");
    query.groupBy("to_char(onlineRecord.loginAt,'hh24')");
    List datas = (List) utilService.search(query);
    for (int i = 0; i < datas.size(); i++) {
      Object o = ((Object[]) (Object[]) datas.get(i))[0];
      String s2 = (String) o;
      ((Object[]) (Object[]) datas.get(i))[0] = Long.valueOf(s2);
    }

    request.setAttribute("logonStats", datas);
    return forward(request);
  }
}
