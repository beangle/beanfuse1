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
package org.beanfuse.security.restriction.service.impl;

import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.utils.persistence.impl.BaseServiceImpl;
import org.beanfuse.security.model.Resource;
import org.beanfuse.security.model.User;
import org.beanfuse.security.dao.AuthorityDao;
import org.beanfuse.security.restriction.PatternParam;
import org.beanfuse.security.restriction.Restriction;
import org.beanfuse.security.restriction.RestrictionHolder;
import org.beanfuse.security.restriction.RestrictionItem;
import org.beanfuse.security.restriction.service.RestrictionApply;
import org.beanfuse.security.restriction.service.RestrictionService;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class RestrictionServiceImpl extends BaseServiceImpl
    implements RestrictionService {

  public RestrictionServiceImpl() {
  }

  public List getRestrictions(User user, Resource resource) {
    List restrictions = new ArrayList();
    final Set patterns = resource.getPatterns();
    restrictions.addAll(getRoleRestrictions(user, resource));
    RestrictionHolder ua = (RestrictionHolder) authorityDao.getUserAuthority(user.getId(), resource.getId());
    if (null != ua && !ua.getRestrictions().isEmpty())
      restrictions.addAll(ua.getRestrictions());
    RestrictionHolder role;
    for (Iterator iterator = user.getRoles().iterator(); iterator.hasNext(); restrictions.addAll(role.getRestrictions()))
      role = (RestrictionHolder) iterator.next();

    RestrictionHolder userHolder = (RestrictionHolder) user;
    restrictions.addAll(userHolder.getRestrictions());
    return (List) CollectionUtils.select(restrictions, new Predicate() {

          public boolean evaluate(Object obj) {
            Restriction restriciton = (Restriction) obj;
            return restriciton.isEnabled() && patterns.contains(restriciton.getPattern());
          }

        }
    );
  }

  private List getRoleRestrictions(User user, Resource resource) {
    EntityQuery query = new EntityQuery("select restriction from RoleAuthority r join r.role.users as user join r.restrictions as restriction where user=:user and r.resource=:resource and restriction.enabled=true");
    Map params = new HashMap();
    params.put("user", user);
    params.put("resource", resource);
    query.setParams(params);
    return (List) utilService.search(query);
  }

  public List getValues(PatternParam param) {
    if (null == param.getEditor()) {
      return Collections.EMPTY_LIST;
    } else {
      EntityQuery query = new EntityQuery(param.getEditor().getSource());
      List rs = (List) utilService.search(query);
      logger.debug("param size {},source:{} ", new Integer(rs.size()), param.getEditor().getSource());
      return rs;
    }
  }

  public Set select(Collection values, List items) {
    Set selected = new HashSet();
    RestrictionItem item;
    for (Iterator iterator = items.iterator(); iterator.hasNext(); selected.addAll(select(values, item)))
      item = (RestrictionItem) iterator.next();

    return selected;
  }

  public Set select(Collection values, RestrictionItem item) {
    Set selected = new HashSet();
    if (StringUtils.isNotEmpty(item.getValue())) {
      if (item.getValue().equals("*")) {
        selected.addAll(values);
        return selected;
      }

      Set paramValue = (Set) item.getParamValue();
      Iterator iterator = values.iterator();

      while (iterator.hasNext()) {
        Object obj = iterator.next();

        try {
          if (paramValue.contains(PropertyUtils.getProperty(obj, item.getParam().getEditor().getIdProperty()))) {
            selected.add(obj);
          }
        } catch (Exception var8) {
          throw new RuntimeException(var8.getMessage());
        }
      }
    }

    return selected;
  }

  public void setAuthorityDao(AuthorityDao authorityDao) {
    this.authorityDao = authorityDao;
  }

  public void setRestrictionApply(RestrictionApply restrictionApply) {
    this.restrictionApply = restrictionApply;
  }

  public void apply(List restrictions, EntityQuery query) {
    restrictionApply.apply(restrictions, query);
  }

  public void apply(Restriction restriction, EntityQuery query) {
    restrictionApply.apply(restriction, query);
  }

  private AuthorityDao authorityDao;
  private RestrictionApply restrictionApply;
}
