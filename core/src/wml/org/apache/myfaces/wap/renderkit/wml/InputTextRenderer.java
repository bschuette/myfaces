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
package org.apache.myfaces.wap.renderkit.wml;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.wap.component.InputText;
import org.apache.myfaces.wap.renderkit.Attributes;
import org.apache.myfaces.wap.renderkit.RendererUtils;
import org.apache.myfaces.wap.renderkit.WmlRenderer;

/**
 * @author  <a href="mailto:Jiri.Zaloudek@ivancice.cz">Jiri Zaloudek</a> (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class InputTextRenderer extends WmlRenderer {
    private static Log log = LogFactory.getLog(InputTextRenderer.class);

    /** Creates a new instance of TextRenderer */
    public InputTextRenderer() {
        super();
        log.debug("created object " + this.getClass().getName());
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws java.io.IOException {
        log.debug("encodeBegin(" + component.getId() + ")");
        if (context == null || component == null) {
            throw new NullPointerException();
        }
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws java.io.IOException {
        log.debug("encodeChildren(" + component.getId() + ")");
        if (context == null || component == null) {
            throw new NullPointerException();
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws java.io.IOException {
        log.debug("encodeEnd(" + component.getId() + ")");
        if (context == null || component == null) {
            throw new NullPointerException();
        }

        if (!component.isRendered()) return;

        InputText comp = (InputText)component;

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(Attributes.INPUT, component);

        RendererUtils.writeAttribute(Attributes.ID, comp.getClientId(context), writer);
        RendererUtils.writeAttribute(Attributes.STYLE_CLASS, comp.getStyleClass(), writer);
        RendererUtils.writeAttribute(Attributes.XML_LANG, comp.getXmllang(), writer);

        /* attribute name is not required. If is not set, name value equals component id */
        if (comp.getName() == null) {
            log.debug("getName is null");
            comp.setName(comp.getClientId(context));
        }
        RendererUtils.writeAttribute(Attributes.NAME, comp.getName(), writer);

        /* default value is false, write only if value of attribute emptyok is true */
        if (comp.isEmptyok()) RendererUtils.writeAttribute(Attributes.EMPTY_OK, "true", writer);

        RendererUtils.writeAttribute(Attributes.FORMAT, comp.getFormat(), writer);
        RendererUtils.writeAttribute(Attributes.MAX_LENGTH, comp.getMaxlength(), writer);
        RendererUtils.writeAttribute(Attributes.SIZE, comp.getSize(), writer);
        RendererUtils.writeAttribute(Attributes.TABINDEX, comp.getTabindex(), writer);
        RendererUtils.writeAttribute(Attributes.TITLE, comp.getTitle(), writer);
        RendererUtils.writeAttribute(Attributes.VALUE, comp.getValue(), writer);

        // value "text" is default, don't must be written
        //RenderUtils.writeAttribute(Attributes.TYPE, "text", writer);

        writer.endElement(Attributes.INPUT);
    }

    public void decode(FacesContext context, UIComponent component) {
        log.debug("decode(" + component.getId() + ")");
        if (component == null || context == null) throw new NullPointerException();
        if (!(component instanceof InputText))
            log.error("Component " + component.getClass().getName() + " is no InputText, cannot be converted!");

        InputText comp = (InputText)component;

        Map map = context.getExternalContext().getRequestParameterMap();

        // Set the submitted value of this UIInput component
        if (map.containsKey(comp.getName())){
            log.debug("Parameter:" + comp.getName() + " was found in the request. Value: " + (String)map.get(comp.getName()));
            comp.setSubmittedValue(map.get(comp.getName()));
            //comp.setValue(map.get(comp.getName()));
        }
    }

   /** Overrides method getConvertedValue */
   public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws javax.faces.convert.ConverterException {
        return(RendererUtils.convertToObject(context, component));
    }
}

