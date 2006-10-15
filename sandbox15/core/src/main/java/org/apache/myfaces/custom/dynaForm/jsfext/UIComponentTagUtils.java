/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.myfaces.custom.dynaForm.jsfext;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class UIComponentTagUtils
{
	public static boolean isValueReference(String v)
	{
		return UIComponentTag.isValueReference(v);
	}

	@SuppressWarnings("unchecked")
	public static void setObjectProperty(FacesContext context,
			UIComponent component, String propName, Object value)
	{
		if (value != null)
		{
			if (value instanceof String
					&& UIComponentTagUtils.isValueReference(value.toString()))
			{
				ValueBinding vb = context.getApplication().createValueBinding(
					value.toString());
				component.setValueBinding(propName, vb);
			}
			else
			{
				component.getAttributes().put(propName, value);
			}
		}
	}
}
