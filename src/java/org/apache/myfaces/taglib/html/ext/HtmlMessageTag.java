/*
 * Copyright 2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.taglib.html.ext;

import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.component.html.ext.HtmlMessage;
import org.apache.myfaces.taglib.html.HtmlMessageTagBase;
import org.apache.myfaces.renderkit.JSFAttr;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlMessageTag
        extends HtmlMessageTagBase
{
    private static final Log log = LogFactory.getLog(HtmlMessageTag.class);

    public String getComponentType()
    {
        return HtmlMessage.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return "org.apache.myfaces.Message";
    }

    private String _summaryFormat;
    private String _detailFormat;
    private String _enabledOnUserRole;
    private String _visibleOnUserRole;
    private String _replaceIdWithLabel;

    public void release()
    {
        super.release();

        _summaryFormat = null;
        _detailFormat = null;
        _enabledOnUserRole = null;
        _visibleOnUserRole = null;
        _replaceIdWithLabel = null;
    }

    protected void setProperties(UIComponent component)
    {
        // setting message properties
        super.setProperties(component);

        setStringProperty(component, "summaryFormat", _summaryFormat);
        setStringProperty(component, "detailFormat", _detailFormat);
        setStringProperty(component, UserRoleAware.ENABLED_ON_USER_ROLE_ATTR, _enabledOnUserRole);
        setStringProperty(component, UserRoleAware.VISIBLE_ON_USER_ROLE_ATTR, _visibleOnUserRole);
        setBooleanProperty(component, "replaceIdWithLabel", _replaceIdWithLabel == null ? Boolean.TRUE.toString() : _replaceIdWithLabel);

    }

    protected String getOrCreateUniqueId(FacesContext context)
    {
        String id = getId();
        if (id == null)
        {
            // default id so client side scripts can use this (ie: ajax), this will obviously break things if someone specifies an id, so please don't specify an id if using Ajax components!
            id = "msgFor_" + getFor();
            log.debug("Setting id on MessageTag: " + id);
            setForceId("true");
        }
        //String id = super.getOrCreateUniqueId(context);
        //log.debug("ID after getorcreate: " + id);
        return id;
    }

    public void setSummaryFormat(String summaryFormat)
    {
        _summaryFormat = summaryFormat;
    }

    public void setDetailFormat(String detailFormat)
    {
        _detailFormat = detailFormat;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public void setReplaceIdWithLabel(String replaceIdWithLabel)
    {
        _replaceIdWithLabel = replaceIdWithLabel;
    }
}
