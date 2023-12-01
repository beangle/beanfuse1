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
package org.beanfuse.commons.mvc.struts.action;

import org.beanfuse.commons.lang.SeqStringUtil;
import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.model.EntityUtils;
import org.beanfuse.commons.model.Model;
import org.beanfuse.commons.model.type.EntityType;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.query.OrderUtils;
import org.beanfuse.commons.web.dispatch.Conventions;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// Referenced classes of package org.beanfuse.commons.mvc.struts.action:
//            BaseAction

public abstract class ExampleAction extends BaseAction {

  public ExampleAction() {
  }

  public ActionForward index(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    indexSetting(request);
    return forward(request);
  }

  public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    addCollection(request, shortName + "s", search(buildQuery(request)));
    return forward(request);
  }

  protected Collection getExportDatas(HttpServletRequest request) {
    EntityQuery query = buildQuery(request);
    query.setLimit(null);
    return search(query);
  }

  public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long entityId = getEntityId(request, shortName);
    Entity entity = null;
    if (null == entityId)
      entity = populateEntity(request);
    else
      entity = getModel(entityName, entityId);
    request.setAttribute(shortName, entity);
    editSetting(request, entity);
    return forward(request);
  }

  public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long entityId = getLong(request, shortName + "Id");
    Collection entities = null;
    if (null == entityId) {
      String entityIdSeq = request.getParameter(shortName + "Ids");
      entities = getModels(entityName, SeqStringUtil.transformToLong(entityIdSeq));
    } else {
      Entity entity = getModel(entityName, entityId);
      entities = Collections.singletonList(entity);
    }
    return removeAndForward(request, entities);
  }

  public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    return saveAndForward(request, populateEntity(request));
  }

  protected Entity populateEntity(HttpServletRequest request) {
    return populateEntity(request, entityName, shortName);
  }

  protected Long getEntityId(HttpServletRequest request, String shortName) {
    Long entityId = getLong(request, shortName + ".id");
    if (null == entityId)
      entityId = getLong(request, shortName + "Id");
    return entityId;
  }

  protected Entity populateEntity(HttpServletRequest request, String entityName, String shortName) {
    Long entityId = getEntityId(request, shortName);
    Entity entity = null;
    try {
      if (null == entityId) {
        entity = (Entity) populate(request, entityName, shortName);
      } else {
        java.util.Map params = getParams(request, shortName);
        entity = getModel(entityName, entityId);
        populate(params, entity, entityName);
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    return entity;
  }

  protected Entity populateEntity(HttpServletRequest request, Class entityClass, String shortName) {
    EntityType type = null;
    if (entityClass.isInterface())
      type = Model.context.getEntityType(entityClass.getName());
    else
      type = Model.context.getEntityType(entityClass);
    return populateEntity(request, type.getEntityName(), shortName);
  }

  protected Entity getEntity(HttpServletRequest request) {
    return getEntity(request, entityName, shortName);
  }

  protected Entity getEntity(HttpServletRequest request, String entityName, String name) {
    Long entityId = getEntityId(request, name);
    Entity entity = null;
    try {
      EntityType type = Model.context.getEntityType(entityName);
      if (null == entityId)
        entity = (Entity) populate(request, type.newInstance(), type.getEntityName(), name);
      else
        entity = getModel(entityName, entityId);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    return entity;
  }

  protected Entity getEntity(HttpServletRequest request, Class entityClass, String shortName) {
    EntityType type = null;
    if (entityClass.isInterface())
      type = Model.context.getEntityType(entityClass.getName());
    else
      type = Model.context.getEntityType(entityClass);
    return getEntity(request, type.getEntityName(), shortName);
  }

  public ActionForward info(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long entityId = getEntityId(request, shortName);
    Entity entity = getModel(entityName, entityId);
    request.setAttribute(shortName, entity);
    return forward(request);
  }

  protected void indexSetting(HttpServletRequest httpservletrequest)
      throws Exception {
  }

  protected void editSetting(HttpServletRequest httpservletrequest, Entity entity1)
      throws Exception {
  }

  protected ActionForward saveAndForward(HttpServletRequest request, Entity entity) throws Exception {
    try {
      this.saveOrUpdate(Collections.singletonList(entity));
      return this.redirect(request, "search", "info.save.success");
    } catch (Exception var4) {
      log.info("saveAndForwad failure for:" + var4.getMessage());
      return this.redirect(request, "search", "info.save.failure");
    }
  }

  protected ActionForward removeAndForward(HttpServletRequest request, Collection entities) throws Exception {
    try {
      this.remove(entities);
    } catch (Exception var4) {
      log.info("removeAndForwad failure for:" + var4.getMessage());
      return this.redirect(request, "search", "info.delete.failure");
    }

    return this.redirect(request, "search", "info.delete.success");
  }

  protected EntityQuery buildQuery(HttpServletRequest request) {
    EntityQuery query = new EntityQuery(entityName, shortName);
    populateConditions(request, query);
    query.addOrder(OrderUtils.parser(request.getParameter("orderBy")));
    query.setLimit(getPageLimit(request));
    return query;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
    if (StringUtils.isEmpty(shortName))
      shortName = EntityUtils.getCommandName(entityName);
  }

  protected Entity getModel(String entityName, Serializable id) {
    return utilService.get(entityName, id);
  }

  protected List getModels(String entityName, Long ids[]) {
    return utilService.load(entityName, "id", ids);
  }

  protected List getModels(Class modelClass, Long ids[]) {
    return utilService.load(modelClass, "id", ids);
  }

  protected String getMethodName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String parameter)
      throws Exception {
    String method = request.getParameter(parameter);
    if (StringUtils.isNotEmpty(method))
      return method;
    else
      return Conventions.getProfile(getClass()).getDefaultMethod();
  }

  protected String entityName;
  protected String shortName;
}
