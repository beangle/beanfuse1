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
package org.beanfuse.security.concurrent.category;

import org.beanfuse.security.Authentication;
import org.beanfuse.security.AuthenticationException;
import org.beanfuse.security.UserDetails;
import org.beanfuse.security.concurrent.SessionInfo;
import org.beanfuse.security.concurrent.SessionInfoService;
import org.beanfuse.security.concurrent.impl.ConcurrentSessionControllerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;

public class CategorySessionControllerImpl extends ConcurrentSessionControllerImpl
    implements CategorySessionController, InitializingBean {
  protected static Logger logger = LoggerFactory.getLogger(CategorySessionControllerImpl.class);

  /**
   * 各类监测类型的监测数据
   */
  protected Map profileMap = new HashMap();

  protected CategoryProfileProvider profileProvider;

  private SessionInfoService sessionInfoService;

  public void changeCategory(String sessionId, Long category) {
    SessionInfo record = (SessionInfo) sessionRegistry.getSessionInfo(sessionId);
    if (!record.getDetails().getCategory().equals(category)) {
      ((CategoryProfile) profileMap.get(record.getDetails().getCategory())).decrease();
      ((CategoryProfile) profileMap.get(category)).increase();
      record.getDetails().setCategory(category);
    }
  }

  public int getMax() {
    int max = 0;
    for (Iterator iter = profileMap.values().iterator(); iter.hasNext(); ) {
      CategoryProfile element = (CategoryProfile) iter.next();
      max += element.getMax();
    }
    return max;
  }

  public int getMax(Long category) {
    return ((CategoryProfile) profileMap.get(category)).getMax();
  }

  public int getOnlineCount() {
    int online = 0;
    for (Iterator iter = profileMap.values().iterator(); iter.hasNext(); ) {
      CategoryProfile element = (CategoryProfile) iter.next();
      online += element.getOnline();
    }
    return online;
  }

  public int getOnlineCount(Long category) {
    return ((CategoryProfile) profileMap.get(category)).getOnline();
  }

  public Collection getSessionInfos(Long category) {
    ArrayList sessionInfos = new ArrayList();
    for (Iterator iterator = sessionRegistry.getSessionInfos().iterator(); iterator.hasNext(); ) {
      SessionInfo record = (SessionInfo) iterator.next();
      if (record.getDetails().getCategory().equals(category)) {
        sessionInfos.add(record);
      }
    }
    return sessionInfos;
  }

  public void removeAuthentication(String sessionId) {
    SessionInfo info = sessionRegistry.getSessionInfo(sessionId);
    sessionRegistry.remove(sessionId);
    if (null != info) {
      Long category = info.getDetails().getCategory();
      CategoryProfile profile = ((CategoryProfile) profileMap.get(category));
      profile.decrease();
      info.setRemark(info.getRemark());
      sessionInfoService.save(info);
    }
  }

  public void registerAuthentication(Authentication authentication) {
    UserDetails details = (UserDetails) authentication.getDetails();
    CategoryProfile profile = (CategoryProfile) profileMap.get(details.getCategory());
    boolean existed = false;
    if (null != sessionRegistry.getSessionInfo(details.getSessionId())) {
      existed = true;
    }
    synchronized (profile) {
      if (profile.hasCapacity()) {
        sessionRegistry.register(details.getSessionId(), authentication);
        if (!existed) {
          profile.increase();
        }
        System.out.println("***************calculateOnline**************");
        calculateOnline();
      } else {
        throw new AuthenticationException(Authentication.ERROR_OVERMAX);
      }
    }
  }

  public void calculateOnline() {
    int all = 0;
    for (Iterator iterator = profileMap.keySet().iterator(); iterator.hasNext(); ) {
      Long category = (Long) iterator.next();
      CategoryProfile p = (CategoryProfile) profileMap.get(category);
      all += p.getOnline();
    }
    if (true || all != sessionRegistry.count()) {
      synchronized (profileMap) {
        logger.debug("start calculate...registry {} profile {}", new Integer(sessionRegistry.count()),
            new Integer(all));
        Map newProfileMap = new HashMap();
        for (Iterator iterator = profileMap.keySet().iterator(); iterator.hasNext(); ) {
          Long category = (Long) iterator.next();
          CategoryProfile profile = (CategoryProfile) profileMap.get(category);
          CategoryProfile newProfile = new CategoryProfile();
          newProfile.setCategory(category);
          newProfile.setMax(profile.getMax());
          newProfile.setInactiveInterval(profile.getInactiveInterval());
          newProfile.setOnline(0);
          newProfileMap.put(category, newProfile);
        }
        List infos = sessionRegistry.getSessionInfos();
        for (Iterator iterator = infos.iterator(); iterator.hasNext(); ) {
          SessionInfo info = (SessionInfo) iterator.next();
          CategoryProfile profile = (CategoryProfile) newProfileMap.get(info.getDetails().getCategory());
          profile.increase();
        }
        profileMap.putAll(newProfileMap);
      }
    }
  }

  public boolean isMaxArrived() {
    return getOnlineCount() >= getMax();
  }

  public boolean isMaxArrived(Long category) {
    return getOnlineCount(category) >= getMax(category);
  }

  public void setMax(Long category, int count) {
    ((CategoryProfile) profileMap.get(category)).setMax(count);
  }

  public SessionInfoService getSessionInfoService() {
    return sessionInfoService;
  }

  public void setSessionInfoService(SessionInfoService sessionInfoService) {
    this.sessionInfoService = sessionInfoService;
  }

  public CategoryProfileProvider getProfileProvider() {
    return profileProvider;
  }

  public void afterPropertiesSet() throws Exception {
    if (null != profileProvider) {
      for (Iterator iterator = profileProvider.getProfiles().iterator(); iterator.hasNext(); ) {
        CategoryProfile profile = (CategoryProfile) iterator.next();
        profileMap.put(profile.getCategory(), profile);
      }
      // 日志输出
      logger.info("profiles:" + profileMap);
    }
  }

  public void setProfileProvider(CategoryProfileProvider profileProvider) {
    this.profileProvider = profileProvider;
  }

}
