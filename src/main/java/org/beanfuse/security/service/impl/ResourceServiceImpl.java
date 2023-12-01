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
package org.beanfuse.security.service.impl;

import org.beanfuse.commons.query.Condition;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.utils.persistence.impl.BaseServiceImpl;
import org.beanfuse.security.Resource;
import org.beanfuse.security.service.ResourceService;

import java.util.Iterator;
import java.util.List;

public class ResourceServiceImpl extends BaseServiceImpl
    implements ResourceService {

  public ResourceServiceImpl() {
  }

  public void updateState(Long resourceIds[], boolean isEnabled) {
    EntityQuery query = new EntityQuery(org.beanfuse.security.Resource.class, "resource");
    query.add(new Condition("resource.id in (:ids)", resourceIds));
    List resources = (List) utilDao.search(query);
    Resource resource;
    for (Iterator iterator = resources.iterator(); iterator.hasNext(); resource.setEnabled(isEnabled))
      resource = (Resource) iterator.next();

    utilDao.saveOrUpdate(resources);
  }

  public Resource getResource(String name) {
    EntityQuery query = new EntityQuery(org.beanfuse.security.Resource.class, "resource");
    query.add(new Condition("resource.name=:name", name));
    query.setCacheable(true);
    List resources = (List) utilDao.search(query);
    if (resources.isEmpty())
      return null;
    else
      return (Resource) resources.get(0);
  }
}
