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
package org.apache.myfaces.custom.dynaForm.metadata.impl.jsf;


import org.apache.myfaces.custom.dynaForm.component.dynaForm.DynaConfig;
import org.apache.myfaces.custom.dynaForm.metadata.MetaData;

/**
 * Extract metadata from jsf form.<br />
 * This will read all facets with "ff_" name prefix
 */
public class JsfExtractor extends AbstractJsfExtractor
{
	public JsfExtractor()
	{
	}

	protected void initFromConfig(MetaData.FieldImpl field, DynaConfig config)
	{
		config.configureMetaData(field);
	}
}
