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

package org.apache.myfaces.custom.suggestajax.inputsuggestajax;

import org.apache.commons.collections.map.HashedMap;
import org.apache.myfaces.custom.ajax.api.AjaxRenderer;
import org.apache.myfaces.custom.dojo.DojoConfig;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.custom.suggestajax.SuggestAjaxRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.util.UnicodeEncoder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Gerald Müllan
 * @author Martin Marinschek
 * @version $Revision: 177984 $ $Date: 2005-05-23 19:39:37 +0200 (Mon, 23 May 2005) $
 */
public class InputSuggestAjaxRenderer extends SuggestAjaxRenderer implements AjaxRenderer
{
   /**
     * Encodes any stand-alone javascript functions that are needed.
     * Uses either the extension filter, or a
     * user-supplied location for the javascript files.
     *
     * @param context FacesContext
     * @param component UIComponent
     * @throws java.io.IOException if base-transport layer was not available
     */
    private void encodeJavascript(FacesContext context, UIComponent component)
                                                                        throws IOException
    {
        String javascriptLocation = (String)component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);

        DojoUtils.addMainInclude(context, component, javascriptLocation, new DojoConfig());
        DojoUtils.addRequire(context, component, "extensions.FacesIO");
        DojoUtils.addRequire(context, component, "dojo.widget.ComboBox");
        DojoUtils.addRequire(context, component, "dojo.event.*");
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        RendererUtils.checkParamValidity(context, component, InputSuggestAjax.class);

        InputSuggestAjax inputSuggestAjax = (InputSuggestAjax) component;

        encodeJavascript(context,component);

        String clientId = component.getClientId(context);
        String actionURL = getActionUrl(context);

        String charset = (inputSuggestAjax.getCharset() != null ? inputSuggestAjax.getCharset() : "");

        String ajaxUrl = context.getExternalContext().encodeActionURL(addQueryString(actionURL, "affectedAjaxComponent=" + clientId +
                "&charset=" + charset + "&" + clientId + "=%{searchString}"));

        ResponseWriter out = context.getResponseWriter();

        Object valueObject = inputSuggestAjax.getValue();
        String label = null;
        String value = null;

        String valueToRender = null;
        
        String idToRender = null;

		/* check if the user supplied a label method */
		if (inputSuggestAjax.getItemLabelMethod() == null)
		{
			if (valueObject instanceof String)
			{
				valueToRender = (String) valueObject;

				idToRender = clientId;
			}
			else if (valueObject == null)
			{
				valueToRender = "";

				idToRender = clientId;
			}
		}
		else
        {
            MethodBinding labelMethod = inputSuggestAjax.getItemLabelMethod();

            if (labelMethod != null)
            {
				Converter converter = getRequiredConverter(context, inputSuggestAjax);

                label = (String) labelMethod.invoke(context, new Object[]{valueObject});

				value = converter.getAsString(context, inputSuggestAjax, valueObject);

                idToRender = clientId + "_valueFake";
            }
        }

        out.startElement(HTML.DIV_ELEM, component);
        out.writeAttribute(HTML.ID_ATTR, clientId , null);
        out.endElement(HTML.DIV_ELEM);

        String inputSuggestComponentVar = DojoUtils.calculateWidgetVarName(clientId);

        Map attributes = new HashedMap();

        attributes.put("dataUrl", ajaxUrl);
        attributes.put("mode", "remote");

        if (label != null)
        {
            valueToRender = label;
        }
        else if (valueToRender != null)
        {
            valueToRender = escapeQuotes(valueToRender);
        }

        DojoUtils.renderWidgetInitializationCode(context, component, "ComboBox", attributes);

        out.startElement(HTML.SCRIPT_ELEM, null);
        out.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);

        StringBuffer buffer = new StringBuffer();

        buffer.append("dojo.addOnLoad(function() {\n")
                .append(inputSuggestComponentVar).append(".textInputNode.name=\"").append(idToRender).append("\";\n");

        /** todo: cbTableNode doesn't exist anymore in dojo 0.4 - what to do?
        if (inputSuggestAjax.getStyle() != null)
        {
            buffer.append(inputSuggestComponentVar).append(".cbTableNode.style.cssText=\"").append(inputSuggestAjax.getStyle()).append("\";\n");
        }
        if (inputSuggestAjax.getStyleClass() != null)
        {
            buffer.append(inputSuggestComponentVar).append(".cbTableNode.className=\"").append(inputSuggestAjax.getStyleClass()).append("\";\n");
        }
        */

        buffer.append(inputSuggestComponentVar).append(".textInputNode.value = \"").append(valueToRender).append("\";\n")
              .append(inputSuggestComponentVar).append(".comboBoxValue.value = \"").append(valueToRender).append("\";\n")
              .append("});\n");

        out.write(buffer.toString());

        out.endElement(HTML.SCRIPT_ELEM);

        if (value != null)
        {
            out.startElement(HTML.INPUT_ELEM, inputSuggestAjax);
            out.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_HIDDEN, null);
            out.writeAttribute(HTML.ID_ATTR, clientId, null);
            out.writeAttribute(HTML.NAME_ATTR, clientId, null);
            out.writeAttribute(HTML.VALUE_ATTR, value, null);
            out.endElement(HTML.INPUT_ELEM);

            out.startElement(HTML.SCRIPT_ELEM, null);
            out.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);

            StringBuffer script = new StringBuffer();

             script.append("dojo.event.connect("+inputSuggestComponentVar+", \"selectOption\", function(evt) { \n"
                   + "dojo.byId('"+ clientId +"').value = ").append(inputSuggestComponentVar).append(".comboBoxSelectionValue.value; });\n");

            out.write(script.toString());

            out.endElement(HTML.SCRIPT_ELEM);
        }
    }

	protected Converter getRequiredConverter(FacesContext context, InputSuggestAjax inputSuggestAjax)
	{
		Converter converter = inputSuggestAjax.getConverter();
		if (converter != null)
		{
			return converter;
		}
		
		Class type = inputSuggestAjax.getValueBinding("value").getType(context);
		if (type != null)
		{
			converter = context.getApplication().createConverter(type);
			if (converter != null)
			{
				return converter;
			}
		}

		throw new IllegalStateException("There must be a converter if " +
														  "attribute \"labelMethod\" is used");
	}

	public void encodeAjax(FacesContext context, UIComponent uiComponent)
                                                                    throws IOException
    {
        InputSuggestAjax inputSuggestAjax = (InputSuggestAjax) uiComponent;

        Collection suggesteds = getSuggestedItems(context, uiComponent);

        MethodBinding labelMethod = inputSuggestAjax.getItemLabelMethod();

        StringBuffer buf = new StringBuffer();

        buf.append("[");

        if (labelMethod != null)
        {
			Converter converter = getRequiredConverter(context, inputSuggestAjax);

            for (Iterator iterator = suggesteds.iterator(); iterator.hasNext();)
            {
                Object suggestedItemObject = iterator.next();

                String label = (String) labelMethod.invoke(context, new Object[]{suggestedItemObject});
                String value = converter.getAsString(context, inputSuggestAjax, suggestedItemObject);

                buf.append("[\"").append(label).append("\",\"").append(value).append("\"],");
            }
        }
        else
        {
            int suggestedCount=0;

            //writing the suggested list
            for (Iterator suggestedItem = suggesteds.iterator() ; suggestedItem.hasNext() ; suggestedCount++)
            {
                if( suggestedCount > DEFAULT_MAX_SUGGESTED_ITEMS)
                    break;

                Object item = suggestedItem.next();

                String prefix = escapeQuotes(encodeSuggestString(item.toString()).substring(0, 1)).toUpperCase();

                buf.append("[\"").append(encodeSuggestString(escapeQuotes(item.toString()))).append("\",\"")
                   .append(prefix).append("\"],");
            }
        }

        buf.append("]");

        context.getResponseWriter().write(buf.toString());
    }

    protected String encodeSuggestString(String str)
    {
        //If you want UTF-8 and we don't do it, you can enable it here with UnicodeEncoder.encode()
        return str;
    }

    public void decode(FacesContext facesContext, UIComponent component)
    {
        super.decode(facesContext, component);
    }

    private String escapeQuotes(String input)
    {
   	    return input != null ? input.replaceAll("\"", "\\\\\"") : "";
    }

}
