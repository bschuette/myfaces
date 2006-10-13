/*
 * Copyright 2004-2006 The Apache Software Foundation.
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
package org.apache.myfaces.custom.toggle;

import javax.faces.component.UIComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.shared_tomahawk.taglib.html.HtmlOutputLinkTagBase;

/**
 * JSP tag class for ToggleLink component
 * 
 * @author Sharath Reddy
 */
public class ToggleLinkTag extends HtmlOutputLinkTagBase
{
    private static Log log = LogFactory.getLog(ToggleLinkTag.class);

    private String _for;

    public String getComponentType()
    {
        return ToggleLink.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return ToggleLink.DEFAULT_RENDERER_TYPE;
    }

    public void release()
    {
        super.release();
        _for = null;
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        setStringProperty(component, "for", _for);
    }

    public void setFor(String value)
    {
        this._for = value;
    }
}