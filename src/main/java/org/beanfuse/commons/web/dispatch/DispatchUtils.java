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
package org.beanfuse.commons.web.dispatch;

import org.apache.commons.lang.StringUtils;

// Referenced classes of package org.beanfuse.commons.web.dispatch:
//            Conventions, Profile, MatchInfo

public class DispatchUtils {

  public DispatchUtils() {
  }

  public static String getControllerName(Class clazz) {
    Profile profile = Conventions.getProfile(clazz);
    if (Boolean.TRUE.equals(profile.getSimpleURIStyle())) {
      String simpleName = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
      return StringUtils.uncapitalize(simpleName.substring(0, simpleName.length() - profile.getCtlPostfix().length()));
    } else {
      return Conventions.separator + getInfix(clazz, profile);
    }
  }

  public static String getInfix(Class clazz, Profile profile) {
    String className = clazz.getName();
    String postfix = profile.ctlPostfix;
    String simpleName = className.substring(clazz.getName().lastIndexOf('.') + 1);
    if (StringUtils.contains(simpleName, postfix))
      simpleName = StringUtils.uncapitalize(simpleName.substring(0, simpleName.length() - postfix.length()));
    else
      simpleName = StringUtils.uncapitalize(simpleName);
    MatchInfo match = profile.getCtlMatchInfo(className);
    StringBuffer infix = new StringBuffer(match.reserved.toString());
    infix.append(StringUtils.substring(clazz.getPackage().getName(), match.startIndex + 1));
    if (infix.length() == 0)
      return simpleName;
    infix.append("/").append(simpleName);
    for (int i = 0; i < infix.length(); i++)
      if (infix.charAt(i) == '.')
        infix.setCharAt(i, '/');

    return infix.toString();
  }
}
