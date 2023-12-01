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

import org.beanfuse.commons.model.Entity;
import org.beanfuse.commons.model.EntityUtils;
import org.beanfuse.commons.model.Model;
import org.beanfuse.commons.model.type.EntityType;
import org.beanfuse.commons.mvc.struts.misc.StrutsMessageResource;
import org.beanfuse.commons.query.EntityQuery;
import org.beanfuse.commons.query.limit.Page;
import org.beanfuse.commons.query.limit.PageLimit;
import org.beanfuse.commons.transfer.TransferResult;
import org.beanfuse.commons.transfer.exporter.*;
import org.beanfuse.commons.transfer.exporter.writer.ExcelItemWriter;
import org.beanfuse.commons.transfer.exporter.writer.ExcelTemplateWriter;
import org.beanfuse.commons.utils.persistence.UtilService;
import org.beanfuse.commons.utils.query.QueryRequestSupport;
import org.beanfuse.commons.utils.web.RequestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

// Referenced classes of package org.beanfuse.commons.mvc.struts.action:
//            DispatchAction

public class BaseAction extends DispatchAction {

  public BaseAction() {
  }

  public void remove(Collection arg0) {
    utilService.remove(arg0);
  }

  public void saveOrUpdate(Collection arg0) {
    utilService.saveOrUpdate(arg0);
  }

  public Collection search(EntityQuery query) {
    return utilService.search(query);
  }

  public void setUtilService(UtilService utilService) {
    this.utilService = utilService;
  }

  public static Map getParams(HttpServletRequest request, String prefix) {
    return RequestUtils.getParams(request, prefix, null);
  }

  public static Map getParams(HttpServletRequest request, String prefix, String exclusiveAttrNames) {
    return RequestUtils.getParams(request, prefix, exclusiveAttrNames);
  }

  public static Long getLong(HttpServletRequest request, String name) {
    return RequestUtils.getLong(request, name);
  }

  public static String get(HttpServletRequest request, String name) {
    return RequestUtils.get(request, name);
  }

  public static Integer getInteger(HttpServletRequest request, String name) {
    return RequestUtils.getInteger(request, name);
  }

  public static Float getFloat(HttpServletRequest request, String name) {
    return RequestUtils.getFloat(request, name);
  }

  public static Boolean getBoolean(HttpServletRequest request, String name) {
    return RequestUtils.getBoolean(request, name);
  }

  public static void populate(Map params, Entity entity, String entityName) {
    if (null == entity) {
      throw new RuntimeException("Cannot populate to null.");
    } else {
      Model.populator.populate(params, entity, entityName);
      return;
    }
  }

  public static Object populate(HttpServletRequest request, Class clazz) {
    return populate(request, clazz, EntityUtils.getCommandName(clazz));
  }

  public static Object populate(HttpServletRequest request, Class clazz, String name) {
    EntityType type = null;
    if (clazz.isInterface())
      type = Model.context.getEntityType(clazz.getName());
    else
      type = Model.context.getEntityType(clazz);
    return populate(request, type.newInstance(), type.getEntityName(), name);
  }

  public static Object populate(HttpServletRequest request, String entityName) {
    EntityType type = Model.context.getEntityType(entityName);
    return populate(request, type.newInstance(), type.getEntityName(), EntityUtils.getCommandName(entityName));
  }

  public static Object populate(HttpServletRequest request, String entityName, String name) {
    EntityType type = Model.context.getEntityType(entityName);
    return populate(request, type.newInstance(), type.getEntityName(), name);
  }

  protected static Object populate(HttpServletRequest request, Object obj, String entityName, String name) {
    return Model.populator.populate(RequestUtils.getParams(request, name), obj, entityName);
  }

  public static int getPageNo(HttpServletRequest request) {
    return QueryRequestSupport.getPageNo(request);
  }

  public static int getPageSize(HttpServletRequest request) {
    return QueryRequestSupport.getPageSize(request);
  }

  public static PageLimit getPageLimit(HttpServletRequest request) {
    return QueryRequestSupport.getPageLimit(request);
  }

  public static void addCollection(HttpServletRequest request, String name, Collection objs) {
    if (objs instanceof Page)
      QueryRequestSupport.addPage(request, objs);
    request.setAttribute(name, objs);
  }

  public static void populateConditions(HttpServletRequest request, EntityQuery entityQuery) {
    QueryRequestSupport.populateConditions(request, entityQuery);
  }

  public static void populateConditions(HttpServletRequest request, EntityQuery entityQuery, String exclusiveAttrNames) {
    QueryRequestSupport.populateConditions(request, entityQuery, exclusiveAttrNames);
  }

  public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String format = request.getParameter("format");
    String fileName = request.getParameter("fileName");
    String template = request.getParameter("template");
    if (StringUtils.isBlank(format))
      format = "excel";
    if (StringUtils.isEmpty(fileName))
      fileName = "exportResult";
    Context context = new Context();
    context.getDatas().put("format", format);
    context.getDatas().put("exportFile", fileName);
    if (StringUtils.isNotBlank(template))
      template = resolveTemplatePath(template);
    context.getDatas().put("templatePath", template);
    configExportContext(request, context);
    Collection datas = (Collection) context.getDatas().get("items");
    boolean isArray = false;
    if (!CollectionUtils.isEmpty(datas)) {
      Object first = datas.iterator().next();
      if (first.getClass().isArray())
        isArray = true;
    }
    Exporter exporter;
    if (isArray)
      exporter = new ItemExporter();
    else if (StringUtils.isNotBlank(template)) {
      exporter = new TemplateExporter();
    } else {
      exporter = new DefaultEntityExporter();
      ((DefaultEntityExporter) exporter).setAttrs(StringUtils.split(getExportKeys(request), ","));
      ((DefaultEntityExporter) exporter).setPropertyExtractor(getPropertyExtractor(request));
    }
    if (exporter instanceof ItemExporter) {
      ((ItemExporter) exporter).setTitles(StringUtils.split(getExportTitles(request), ","));
      exporter.setWriter(new ExcelItemWriter(response.getOutputStream()));
    } else {
      exporter.setWriter(new ExcelTemplateWriter(response.getOutputStream()));
    }
    if (format.equals("excel")) {
      response.setContentType("application/vnd.ms-excel;charset=GBK");
      response.setHeader("Content-Disposition", "attachment;filename=" + RequestUtils.encodeAttachName(request, context.getDatas().get("exportFile").toString()) + ".xls");
    } else {
      throw new RuntimeException("Exporter is not supported for other format:" + exporter.getFormat());
    }
    exporter.setContext(context);
    exporter.transfer(new TransferResult());
    return null;
  }

  protected String resolveTemplatePath(String template) {
    return template;
  }

  protected void configExportContext(HttpServletRequest request, Context context) {
    Collection datas = getExportDatas(request);
    context.getDatas().put("items", datas);
  }

  protected PropertyExtractor getPropertyExtractor(HttpServletRequest request) {
    return new DefaultPropertyExtractor(getLocale(request), new StrutsMessageResource(getResources(request)));
  }

  protected Collection getExportDatas(HttpServletRequest request) {
    return Collections.EMPTY_LIST;
  }

  protected String getExportKeys(HttpServletRequest request) {
    String messageKey = request.getParameter("messageKey");
    if (StringUtils.isNotBlank(messageKey)) {
      MessageResources message = getResources(request, "excelconfig");
      return message.getMessage(messageKey);
    } else {
      return request.getParameter("keys");
    }
  }

  protected String getExportTitles(HttpServletRequest request) {
    String messageTitle = request.getParameter("messageTitle");
    if (StringUtils.isNotBlank(messageTitle)) {
      MessageResources message = getResources(request, "excelconfig");
      return message.getMessage(messageTitle);
    } else {
      String titles = request.getParameter("titles");
      return titles;
    }
  }

  protected UtilService utilService;
  protected static final String errorForward = "error";
}
