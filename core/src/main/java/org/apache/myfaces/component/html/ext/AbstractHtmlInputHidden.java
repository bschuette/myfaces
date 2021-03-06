/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.component.html.ext;

import org.apache.myfaces.component.ForceIdAware;
import org.apache.myfaces.component.html.util.HtmlComponentUtils;

import javax.faces.context.FacesContext;


/**
 * Extended version of {@link javax.faces.component.html.HtmlInputHidden} 
 * that provides additional MyFaces functionality.
 *
 * @JSFComponent
 *   name = "t:inputHidden"
 *   class = "org.apache.myfaces.component.html.ext.HtmlInputHidden"
 *   tagClass = "org.apache.myfaces.generated.taglib.html.ext.HtmlInputHiddenTag"
 * @since 1.1.7
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */
public abstract class AbstractHtmlInputHidden
    extends javax.faces.component.html.HtmlInputHidden
    implements ForceIdAware
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlInputHidden";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Hidden";

    public AbstractHtmlInputHidden()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }
    
    public String getClientId(FacesContext context)
    {
        String clientId = HtmlComponentUtils.getClientId(this, getRenderer(context), context);
        if (clientId == null)
        {
            clientId = super.getClientId(context);
        }

        return clientId;
    }
}

