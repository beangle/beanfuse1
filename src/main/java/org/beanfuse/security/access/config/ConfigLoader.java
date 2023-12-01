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
package org.beanfuse.security.access.config;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Properties;

// Referenced classes of package org.beanfuse.security.access.config:
//            AccessConfig

public class ConfigLoader {

  public static ConfigLoader getInstance() {
    if (singleton == null)
      singleton = new ConfigLoader();
    return singleton;
  }

  private ConfigLoader() {
  }

  public synchronized AccessConfig getConfig() {
    if (null != config)
      return config;
    Properties props = new Properties();
    java.io.InputStream is = (org.beanfuse.security.access.config.ConfigLoader.class).getResourceAsStream("/access.properties");
    if (null == is)
      is = (org.beanfuse.security.access.config.ConfigLoader.class).getResourceAsStream("/org.beanfuse.security/access/access-default.properties");
    try {
      logger.debug("Loading config...");
      props.load(is);
      config = new AccessConfig();
      String name;
      for (Iterator iterator = props.keySet().iterator(); iterator.hasNext(); BeanUtils.copyProperty(config, name, props.getProperty(name)))
        name = (String) iterator.next();

      logger.info(config.toString());
    } catch (Exception e) {
      logger.error("Exception", e);
      throw new RuntimeException(e.getMessage());
    }
    return config;
  }

  private static final Logger logger;
  private AccessConfig config;
  private static ConfigLoader singleton;

  static {
    logger = LoggerFactory.getLogger(org.beanfuse.security.access.config.ConfigLoader.class);
  }
}
