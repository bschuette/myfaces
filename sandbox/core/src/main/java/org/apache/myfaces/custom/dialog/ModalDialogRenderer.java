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

package org.apache.myfaces.custom.dialog;

import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRenderer;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Renderer for the s:modalDialog component.
 * <p>
 * This component works in one of two different ways:
 * <ul>
 * <li>The component can contain child components, in which case the child components
 * are initially hidden but are displayed in a "popup" modal window when the "show"
 * javascript method is invoked. In this mode, a DIV is rendered to wrap the child
 * components. 
 * <li>The component can have a "viewId" property defined, in which case the specified
 * JSF view will be fetched into a "popup" modal window when the "show" javascript
 * method is invoked. In this mode, a DIV containing an IFrame is rendered into the
 * html page; the iframe is used to load the specified view.
 * </ul>
 * It is the page author's responsibility to have some other HTML control on the
 * page whose "onclick" attribute contains javascript to invoke the "show" method
 * of the modal dialog. Component property "dialogVar" specifies the name of a global
 * javascript variable to be created, and its "show" method can be invoked to display
 * the popup.
 * <p>
 * The Dojo library uses css tricks to make the DIV component act like a modal
 * window.
 * 
 * @JSFRenderer
 *   renderKitId = "HTML_BASIC" 
 *   family = "javax.faces.Panel"
 *   type = "org.apache.myfaces.ModalDialog"
 */
public class ModalDialogRenderer extends HtmlRenderer
{
    public static final String RENDERER_TYPE = "org.apache.myfaces.ModalDialog";

    public static final String DIV_ID_PREFIX = "_div";

    /**
     * Writes a DIV opening tag, plus javascript to make sure that the Dojo
     * library is initialised.
     */
    //@Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException
    {
        String javascriptLocation = (String) component.getAttributes().get(JSFAttr.JAVASCRIPT_LOCATION);
        DojoUtils.addMainInclude(context, component, javascriptLocation,
                                 DojoUtils.getDjConfigInstance(context));
        DojoUtils.addRequire(context, component, "dojo.widget.Dialog");

        writeModalDialogBegin(context, (ModalDialog) component, context.getResponseWriter());
    }

    //@Override
    /**
     * Writes a DIV closing tag, plus javascript to declare the global "dialogVar"
     * object that can be invoked to show the modal dialog.
     */
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        ModalDialog dlg = (ModalDialog) component;

        StringBuffer buf = new StringBuffer();

        buf.append("</div>");

        writeDialogLoader(context, dlg, buf);

        context.getResponseWriter().write(buf.toString());

        if (dlg.getViewId() != null)
        {
            RendererUtils.renderChildren(context, component);
            HtmlRendererUtils.writePrettyLineSeparator(context);
        }
    }

    private void appendHiderIds(StringBuffer buf, ModalDialog dlg)
    {
        List hiders = new ArrayList();

        if (dlg.getHiderIds() != null)
        {
            hiders.addAll(Arrays.asList(dlg.getHiderIds().split(",")));
        }

        if (isRenderCloseButton(dlg) && dlg.getDialogTitle() != null)
        {
            hiders.add(dlg.getDialogVar() + "Closer");
        }

        for (int i = 0; i < hiders.size(); i++)
        {
            String varName = "btn" + i;
            buf
                .append("var ")
                .append(varName)
                .append(" = document.getElementById(\"")
                .append(((String) hiders.get(i)).trim())
                .append("\");")

                .append(dlg.getDialogVar())
                .append(".setCloseControl(")
                .append(varName)
                .append(");");
        }
    }

    private void appendDialogAttributes(StringBuffer buf, ModalDialog dlg)
    {
        if(dlg.getDialogAttr() == null)
        {
            return;
        }

        StringTokenizer it = new StringTokenizer(dlg.getDialogAttr(), " ");
        while(it.hasMoreElements())
        {
            String[] pair = it.nextToken().split("=");
            String attribute = pair[0];
            String value = pair[1].replaceAll("'", "");
            try
            {
                Double number = new Double(value);
            }
            catch(NumberFormatException e)
            {
                value = new StringBuffer("\"").append(value).append("\"").toString();
            }
            buf
                .append(", ")
                .append(attribute)
                .append(":")
                .append(value);
        }
    }

    /**
     * Write an html DIV whose css style the Dojo library will manipulate to make it
     * look like a popup window. 
     * <p>
     * The child components (or the nested iframe) will be rendered inside this div.
     */
    private void writeModalDialogBegin(FacesContext context, ModalDialog dlg, ResponseWriter writer)
    throws IOException
    {
        StringBuffer buf = new StringBuffer();

        String dlgId = getDialogId(dlg);
        buf.append("<div id=\"").append(dlgId).append("\"");
        if(dlg.getStyle() != null)
        {
            buf.append(" style=\"").append(dlg.getStyle()).append("\"");
        }
        if(dlg.getStyleClass() != null)
        {
            buf.append(" class=\"").append(dlg.getStyleClass()).append("\"");
        }
        buf.append(">");

        writer.write(buf.toString());
    }

    private String getDialogId(ModalDialog dlg)
    {
        String dlgId = dlg.getId() != null ?
                       dlg.getId() :
                       new StringBuffer(dlg.getDialogId()).append(DIV_ID_PREFIX).toString();
        return dlgId;
    }

    /**
     * Write a javascript block that declares a single global variable of type Object that
     * provides methods for showing and hiding the modal dialog.
     * <p>
     * The name of the javascript variable is specified by component property "dialogVar".
     */
    private String writeDialogLoader(FacesContext context, ModalDialog dlg, StringBuffer buf)
    {
        String dlgId = getDialogId(dlg);
        String dialogVar = dlg.getDialogVar();
        buf.append("<script type=\"text/javascript\">");

        // Declare a global variable whose name is specified by dialogVar.
        // This variable will be initialized to point to a Dojo Widget object
        // when page load is complete.
        buf.append("var ").append(dialogVar).append(";");

        // Declare a function that will be called on page load to initialize dialogVar
        buf .append("function " + dialogVar + "_loader(e) {")
            .append(dialogVar)
            .append(" = dojo.widget.createWidget(\"dialog\", {id:")
            .append("\"")
            .append(dlg.getDialogId())
            .append("\"");

        appendDialogAttributes(buf, dlg);

        buf.append("}, dojo.byId(\"").append(dlgId).append("\"));");

        appendHiderIds(buf, dlg);

        if (dlg.getViewId() != null)
        {
            appendShowHideView(context, buf, dlg);
        }

        buf.append("}");

        // Emit some global javascript that causes the initialization function
        // defined above.
        //
        // We cannot use a standard javascript setTimeout call, as this breaks the
        // submitOnEvent component.
        //
        // We cannot call the loader function immediately, as it appears that this
        // breaks IE sometimes (always?).
        //
        // So it looks like using dojo's addOnLoad function is the best solution.. 
        buf.append("dojo.addOnLoad(function() {" + dialogVar + "_loader();});");

        buf.append("</script>");
        return dlgId;
    }

    /**
     * This is invoked only when the ModalDialog component has a viewId property
     * defined, ie the dialog should automatically load a specific JSF view when
     * it is shown.
     * <p>
     * Javascript is generated which override the standard Dojo modal dialog
     * show and hide methods to do some initialisation, including loading the
     * required view into the iframe.
     */
    private void appendShowHideView(FacesContext context, StringBuffer buf, ModalDialog dlg)
    {
        StringBuffer sbUrl = new StringBuffer();
        sbUrl.append(context.getExternalContext().getRequestContextPath());
        sbUrl.append("/");
        sbUrl.append(dlg.getViewId());
        String encodedUrl = context.getExternalContext().encodeActionURL(sbUrl.toString());

        String dialogVar = dlg.getDialogVar();

        // save original onShow function (the standard dojo widget implementation)
        buf .append(dialogVar)
            .append(".oldOnShow=")
            .append(dialogVar)
            .append(".onShow;");

        // Define a new onShow function which first shows the modal window then
        // causes it to do a GET to the server to fetch a specific page that is
        // defined by the "viewId" property on the JSF component.
        //
        // TODO: What is the purpose of variable window._myfaces_currentModal?
        // There doesn't appear to be anything that *reads* it...
        //
        // TODO: What is the purpose of ${dialogVar}._myfaces_ok? Nothing appears
        // to read it. 
        buf .append(dialogVar)
            .append(".onShow = function() {")
            .append("this.oldOnShow();")
            .append("var content = document.getElementById(\"modalDialogContent")
            .append(dialogVar)
            .append("\"); ")
            .append("window._myfaces_currentModal=")
            .append(dialogVar)
            .append("; ")
            .append(dialogVar)
            .append("._myfaces_ok=false; ")
            .append("content.contentWindow.location.replace('")
            .append(encodedUrl)
            .append("'); ")
            .append("}; ");

        // save original onHide function (the standard dojo widget implementation)
        buf .append(dialogVar)
            .append(".oldOnHide=")
            .append(dialogVar)
            .append(".onHide;");

        // Define a new onHide function which first shows the modal window then
        // causes it to do a GET to the server to fetch a specific page that is
        // defined by the "viewId" property on the JSF component.
        buf .append(dialogVar)
            .append(".onHide = function() {")
            .append("this.oldOnHide();")
            .append("window._myfaces_currentModal=null;")
            .append("var content = document.getElementById(\"modalDialogContent")
            .append(dialogVar)
            .append("\"); ")
            .append("content.contentWindow.location.replace('javascript:false;'); ")
            .append("}; ");
    }

    public boolean getRendersChildren()
    {
        return true;
    }

    /**
     * Override normal "encodeChildren" method to either render the children in
     * normal manner, or to render an IFrame instead.
     * <p>
     * When the ModalDialog component has its "viewId" property set, then all
     * children (except facets) are ignored, and instead an IFrame is rendered
     * into which a specific JSF viewId will later be loaded.
     *
     * @see javax.faces.render.Renderer#encodeChildren(javax.faces.context.FacesContext,
     *      javax.faces.component.UIComponent)
     */
    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException
    {
        ModalDialog dlg = (ModalDialog) uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();

        UIComponent titleFacet = dlg.getFacet("titleBar");
        if (titleFacet != null)
        {
            RendererUtils.renderChild(facesContext, titleFacet);
        }
        else if (dlg.getDialogTitle() != null)
        {
            AddResourceFactory.getInstance(facesContext).addStyleSheet(facesContext, AddResource.HEADER_BEGIN,  ModalDialog.class, "modalDialog.css");

            writer.startElement(HTML.TABLE_ELEM, dlg);
            writer.writeAttribute(HTML.CLASS_ATTR, "modalDialogDecoration " + getStyleName(dlg, "Decoration") , null);
            writer.writeAttribute(HTML.CELLPADDING_ATTR, "2", null);
            writer.writeAttribute(HTML.CELLSPACING_ATTR, "0", null);

            writer.startElement(HTML.TR_ELEM, dlg);
            writer.writeAttribute(HTML.CLASS_ATTR, "modalDialogTitle " + getStyleName(dlg, "Title"), null);

            writer.startElement(HTML.TD_ELEM, dlg);
            writer.writeAttribute(HTML.CLASS_ATTR, "modalDialogTitleLeft " + getStyleName(dlg, "TitleLeft"), null);
            writer.writeText(dlg.getDialogTitle(), null);
            writer.endElement(HTML.TD_ELEM);

            writer.startElement(HTML.TD_ELEM, dlg);
            writer.writeAttribute(HTML.CLASS_ATTR, "modalDialogTitleRight " + getStyleName(dlg, "TitleRight"), null);
            if (isRenderCloseButton(dlg))
            {
                String imageUri = AddResourceFactory.getInstance(facesContext).getResourceUri(facesContext, ModalDialog.class, "close.gif");
                writer.startElement(HTML.IMG_ELEM, dlg);
                writer.writeAttribute(HTML.ID_ATTR, dlg.getDialogVar() + "Closer", null);
                writer.writeAttribute(HTML.SRC_ATTR, imageUri, null);
                writer.writeAttribute(HTML.CLASS_ATTR, "modalDialogCloser " + getStyleName(dlg, "Closer"), null);
                writer.endElement(HTML.IMG_ELEM);
            }
            writer.endElement(HTML.TD_ELEM);

            writer.endElement(HTML.TR_ELEM);
            writer.endElement(HTML.TABLE_ELEM);
        }

        if (dlg.getViewId() != null)
        {
            renderDialogViewFrame(facesContext, dlg);
        }
        else
        {
            RendererUtils.renderChildren(facesContext, uiComponent);
            HtmlRendererUtils.writePrettyLineSeparator(facesContext);
        }
    }

    protected boolean isRenderCloseButton(ModalDialog dlg)
    {
        return !Boolean.FALSE.equals(dlg.getCloseButton());
    }

    private String getStyleName(ModalDialog dlg, String suffix)
    {
        if (dlg.getStyleClass() != null)
        {
            return dlg.getStyleClass() + suffix;
        }

        return "";
    }

    /**
     * Invoked only when the ModalDialog component has property viewId defined.
     */
    private void renderDialogViewFrame(FacesContext facesContext, ModalDialog dlg) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.IFRAME_ELEM, dlg);
        writer.writeAttribute(HTML.ID_ATTR, "modalDialogContent" + dlg.getDialogVar(), null);
        writer.writeAttribute(HTML.CLASS_ATTR, "modalDialogContent " + getStyleName(dlg, "Content"), null);
        writer.writeAttribute(HTML.SCROLLING_ATTR, "auto", null);
        writer.writeAttribute(HTML.FRAMEBORDER_ATTR, "0", null);
        writer.endElement(HTML.IFRAME_ELEM);
    }
}
