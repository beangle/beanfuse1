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
package org.beanfuse.security.restriction.service;

import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.security.model.Resource;
import org.beanfuse.security.model.User;
import org.beanfuse.security.dao.AuthorityDao;
import org.beanfuse.security.restriction.PatternParam;
import org.beanfuse.security.restriction.Restriction;
import org.beanfuse.security.restriction.RestrictionItem;

import java.util.Collection;
import java.util.List;
import java.util.Set;

// Referenced classes of package org.beanfuse.security.restriction.service:
//            RestrictionApply

public interface RestrictionService {

  public abstract List getRestrictions(User user, Resource resource);

  public abstract List getValues(PatternParam patternparam);

  public abstract Set select(Collection collection, RestrictionItem restrictionitem);

  public abstract Set select(Collection collection, List list);

  public abstract void setAuthorityDao(AuthorityDao authoritydao);

  public abstract void apply(List list, EntityQuery entityquery);

  public abstract void apply(Restriction restriction, EntityQuery entityquery);

  public abstract void setRestrictionApply(RestrictionApply restrictionapply);
}
