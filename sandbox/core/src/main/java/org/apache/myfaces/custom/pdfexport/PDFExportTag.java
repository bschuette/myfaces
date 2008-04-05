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
package org.apache.myfaces.custom.pdfexport;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

public class PDFExportTag extends UIComponentTagBase {

	private String _for;
	private String _filename;

	public void release() {
		super.release();
		_for = null;
		_filename = null;
	}

	protected void setProperties(UIComponent component) {
		super.setProperties(component);

		setStringProperty(component, "for", _for);
		setStringProperty(component, "filename", _filename);
	}

	public String getComponentType() {
		return PDFExport.COMPONENT_TYPE;
	}

	public String getRendererType() {
		return "org.apache.myfaces.PDFExportRenderer";
	}

	public String getFor() {
		return _for;
	}
	
	public void setFor(String aFor) {
		_for = aFor;
	}

	public String getFilename() {
		return _filename;
	}
	
	public void setFilename(String filename) {
		this._filename = filename;
	}
}
