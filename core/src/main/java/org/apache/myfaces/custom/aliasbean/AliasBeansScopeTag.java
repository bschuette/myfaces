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
package org.apache.myfaces.custom.aliasbean;

import org.apache.myfaces.taglib.UIComponentTagBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.faces.component.UIComponent;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class AliasBeansScopeTag extends UIComponentTagBase
{
    private Log log = LogFactory.getLog(AliasBeansScopeTag.class);

    public String getComponentType()
    {
        return AliasBeansScope.COMPONENT_TYPE;
    }

    public String getRendererType()
    {
        return null;
    }


    public int doStartTag() throws JspException
    {
        int retVal = super.doStartTag();

        UIComponent comp = getComponentInstance();

        if(comp instanceof AliasBeansScope)
        {
            ((AliasBeansScope) comp).makeAliases(getFacesContext());
        }
        else
        {
            log.warn("associated component is no aliasBeansScope");
        }

        return retVal;
    }

    public int doEndTag() throws JspException
    {
        int retVal =  super.doEndTag();

        UIComponent comp = getComponentInstance();

        if(comp instanceof AliasBeansScope)
        {
            ((AliasBeansScope) comp).removeAliases(getFacesContext());
        }
        else
        {
            log.warn("associated component is no aliasBeansScope");
        }

        return retVal;
    }

}