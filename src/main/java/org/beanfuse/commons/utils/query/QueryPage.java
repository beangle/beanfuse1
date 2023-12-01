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
package org.beanfuse.commons.utils.query;

import org.beanfuse.commons.query.AbstractQuery;
import org.beanfuse.commons.query.limit.AbstractQueryPage;
import org.beanfuse.commons.query.limit.Page;
import org.beanfuse.commons.query.limit.SinglePage;
import org.beanfuse.commons.utils.persistence.UtilService;

public class QueryPage extends AbstractQueryPage {

  public QueryPage(AbstractQuery query, UtilService utilService) {
    super(query);
    this.utilService = utilService;
    next();
  }

  public void setUtilService(UtilService utilService) {
    this.utilService = utilService;
  }

  public Page moveTo(int pageNo) {
    query.getLimit().setPageNo(pageNo);
    setPageData((SinglePage) utilService.search(query));
    return this;
  }

  private UtilService utilService;
}
