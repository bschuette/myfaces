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
package org.apache.myfaces.custom.clientvalidation.validationscript;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.apache.myfaces.custom.clientvalidation.common.CVUtils;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;

/**
 * @author cagatay (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ValidationScriptRenderer extends Renderer {

	public void encodeEnd(FacesContext context, UIComponent component)throws IOException {
		RendererUtils.checkParamValidity(context, component,ValidationScript.class);
		
		if (CVUtils.isCVEnabled()) {
			encodeJavascript(context, component);
			CVUtils.queueCVCalls(context.getViewRoot());
			CVUtils.encodeValidationScript(context);
		}
	}

	private void encodeJavascript(FacesContext context, UIComponent component)throws IOException {
		AddResource addResource = AddResourceFactory.getInstance(context);
		
		//Common
		addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,ValidationScript.class, "common.js");
		
		//MessageBundle
		addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,ValidationScript.class, CVUtils.getJSMessageBundle(context));
		
		//Converters and Validators
		addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,ValidationScript.class, "converters.js");
		addResource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN,ValidationScript.class, "validators.js");
	}
	
}
