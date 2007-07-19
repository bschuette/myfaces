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
package org.apache.myfaces.custom.passwordStrength;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.taglib.html.ext.HtmlInputTextTag;

public class PasswordStrengthTag extends HtmlInputTextTag {

	/**
	 * return the component class ...
	 */
	public String getComponentType() {
		return PasswordStrengthComponent.COMPONENT_TYPE;
	}

	/**
	 * Returns the component renderer ...
	 */
	public String getRendererType() {
		return PasswordStrengthComponent.RENDERER_TYPE;
	}

	/**
	 * release the used resources ...
	 */
	public void release() {
		super.release();
		//_text = null;
		_preferredPasswordLength = null;
		_prefixText = null;
		_textStrengthDescriptions = null;
	}

	protected void setProperties(UIComponent component) {
		super.setProperties(component);
	//	setStringProperty(component, "text", _text);
		setStringProperty(component, "preferredPasswordLength",
				_preferredPasswordLength);
		setStringProperty(component, "textStrengthDescriptions",
				_textStrengthDescriptions);
		setStringProperty(component, "prefixText", _prefixText);
	}

	//Getters & Setters ...

	public String getPreferredPasswordLength() {
		return _preferredPasswordLength;
	}

	public void setPreferredPasswordLength(String preferredPasswordLength) {
		this._preferredPasswordLength = preferredPasswordLength;
	}

	public String getPrefixText() {
		return _prefixText;
	}

	public void setPrefixText(String prefixText) {
		_prefixText = prefixText;
	}

	public String getTextStrengthDescriptions() {
		return _textStrengthDescriptions;
	}

	public void setTextStrengthDescriptions(String strengthDescriptions) {
		_textStrengthDescriptions = strengthDescriptions;
	}	
	
	//Attributes ...
	private String _preferredPasswordLength;
	private String _textStrengthDescriptions;
	private String _prefixText;
}