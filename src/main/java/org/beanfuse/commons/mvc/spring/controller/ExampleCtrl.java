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
package org.beanfuse.commons.mvc.spring.controller;

import org.beanfuse.commons.lang.SeqStringUtil;
import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.model.EntityUtils;
import org.beanfuse.commons.model.Model;
import org.beanfuse.commons.model.type.EntityType;
import org.beanfuse.commons.mvc.spring.ViewSupport;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.query.OrderUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;

// Referenced classes of package org.beanfuse.commons.mvc.spring.controller:
//            BaseCtrl

public class ExampleCtrl extends BaseCtrl {

  public ExampleCtrl() {
  }

  public ModelAndView index(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Map result = new HashMap();
    indexSetting(request, result);
    indexSetting(request);
    return new ModelAndView(ViewSupport.forwardView(request, this), result);
  }

  public ModelAndView search(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    addCollection(request, shortName + "s", search(buildQuery(request)));
    return forward(request);
  }

  protected EntityQuery buildQuery(HttpServletRequest request) {
    EntityQuery query = new EntityQuery(entityName, shortName);
    populateConditions(request, query);
    query.addOrder(OrderUtils.parser(request.getParameter("orderBy")));
    query.setLimit(getPageLimit(request));
    return query;
  }

  protected Collection getExportDatas(HttpServletRequest request) {
    EntityQuery query = buildQuery(request);
    query.setLimit(null);
    return search(query);
  }

  public ModelAndView edit(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long entityId = getEntityId(request, shortName);
    Entity entity = null;
    if (null == entityId)
      entity = populateEntity(request);
    else
      entity = (Entity) getModel(entityId);
    Map result = new HashMap();
    result.put(shortName, entity);
    editSetting(request, entity);
    editSetting(request, result);
    return new ModelAndView(ViewSupport.forwardView(request, this), result);
  }

  public ModelAndView add(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Entity entity = (Entity) super.newCommandObject(commandClass);
    Map result = new HashMap();
    result.put(shortName, entity);
    editSetting(request, result);
    editSetting(request, entity);
    return new ModelAndView(ViewSupport.forwardView(request, this), result);
  }

  public ModelAndView remove(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String entityIdSeq = request.getParameter(shortName + "Id");
    Collection entities = null;
    if (null == entityIdSeq)
      entityIdSeq = request.getParameter(shortName + "Ids");
    entities = getModels(SeqStringUtil.transformToLong(entityIdSeq));
    return removeAndForward(request, entities);
  }

  public ModelAndView save(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Entity entity = populateEntity(request);
    return saveAndForward(request, entity);
  }

  public ModelAndView info(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long entityId = getEntityId(request, shortName);
    Object entity = getModel(entityId);
    Map result = new HashMap();
    result.put(shortName, entity);
    return new ModelAndView(ViewSupport.forwardView(request, this), result);
  }

  protected Entity populateEntity(HttpServletRequest request) {
    Long entityId = getEntityId(request, shortName);
    Entity entity = null;
    try {
      if (null == entityId) {
        entity = (Entity) populate(request, entityName, shortName);
      } else {
        Map params = getParams(request, shortName);
        entity = getModel(entityName, entityId);
        populate(params, entity, entityName);
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    return entity;
  }

  protected void indexSetting(HttpServletRequest httpservletrequest, Map map) {
  }

  protected void indexSetting(HttpServletRequest httpservletrequest) {
  }

  protected void editSetting(HttpServletRequest httpservletrequest, Map map) {
  }

  protected void editSetting(HttpServletRequest httpservletrequest, Object obj) {
  }

  protected void editSetting(HttpServletRequest httpservletrequest, Entity entity1) {
  }

  protected ModelAndView saveAndForward(HttpServletRequest request, Entity entity)
      throws Exception {
    if (entity.isVO())
      EntityUtils.evictEmptyProperty(entity);
    saveOrUpdate(Collections.singletonList(entity));
    return redirect(request, "search", "common.operation.success");
  }

  protected ModelAndView removeAndForward(HttpServletRequest request, Collection models)
      throws Exception {
    String msg = "common.remove.success";
    try {
      remove(models);
    } catch (Exception e) {
      msg = "common.remove.failure";
    }
    return redirect(request, "search", msg);
  }

  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
    if (StringUtils.isEmpty(shortName))
      shortName = EntityUtils.getCommandName(entityName);
    commandClass = Model.context.getEntityType(entityName).getEntityClass();
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String entityName) {
    shortName = entityName;
  }

  protected Long getEntityId(HttpServletRequest request, String shortName) {
    Long entityId = getLong(request, shortName + ".id");
    if (null == entityId)
      entityId = getLong(request, shortName + "Id");
    return entityId;
  }

  protected Object getModel(Serializable id) {
    return utilService.get(entityName, id);
  }

  protected List getModels(Long ids[]) {
    return utilService.load(entityName, "id", ids);
  }

  protected Entity getModel(String entityName, Serializable id) {
    return utilService.get(entityName, id);
  }

  protected Entity getEntity(HttpServletRequest request, String entityName, String name) {
    Long entityId = getEntityId(request, shortName);
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

  protected Entity getEntity(HttpServletRequest request) {
    return getEntity(request, entityName, shortName);
  }

  protected String entityName;
  protected String shortName;
  protected Class commandClass;
}
