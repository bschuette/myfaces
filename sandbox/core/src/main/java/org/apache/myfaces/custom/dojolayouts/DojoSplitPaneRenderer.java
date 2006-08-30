package org.apache.myfaces.custom.dojolayouts;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.apache.myfaces.custom.dojo.DojoUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HTML;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlRendererUtils;

public class DojoSplitPaneRenderer extends DojoContentPaneRenderer {
    protected void encodeJavascriptBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeJavascriptBegin(context, component);
        DojoUtils.addRequire(context, component, "dojo.widget.SplitContainer");
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }

        Boolean rendered = (Boolean) component.getAttributes().get("rendered");

        if ((rendered != null) && (!rendered.booleanValue()))
            return;

        super.encodeBegin(context, component);
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        RendererUtils.renderChildren(context, component);
        HtmlRendererUtils.writePrettyLineSeparator(context);
        Stack stack = getChildsStack(context, component);
        List children = component.getChildren();

        for (Iterator cit = children.iterator(); cit.hasNext();) {
            UIComponent child = (UIComponent) cit.next();
            if (!child.isRendered())
                continue;
            if (child instanceof DojoContentPane) {
                stack.push(DojoUtils.calculateWidgetVarName(child.getClientId(context)));
            }
        }
    }

    private Stack getChildsStack(FacesContext context, UIComponent component) {
        Stack menuStack = (Stack) ((HttpServletRequest) context.getExternalContext().getRequest()).getAttribute(component.getClientId(context)
                + DojoSplitPaneRenderer.class.toString());
        if (menuStack != null)
            return menuStack;

        menuStack = new Stack();
        ((HttpServletRequest) context.getExternalContext().getRequest()).setAttribute(component.getClientId(context) + DojoSplitPaneRenderer.class.toString(),
                menuStack);
        return menuStack;
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);
    }

    protected void encodeJavascriptEnd(FacesContext context, UIComponent component) throws IOException {
        // called by super.encodeEnd
        DojoSplitPane pane = (DojoSplitPane) component;
        Map attributes = new HashedMap();

        attributes.put("sizeShare", pane.getSizeShare());
        attributes.put("activeSizing", pane.getActiveSizing());
        attributes.put("orientation", pane.getSplitOrientationation());
        attributes.put("sizerWidth", pane.getSizerWidth());
     
        DojoUtils.renderWidgetInitializationCode(context, component, "SplitContainer", attributes);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT, null);

        Stack stack = this.getChildsStack(context, component);
        String panelComponentVar = DojoUtils.calculateWidgetVarName(component.getClientId(context));
        while (!stack.isEmpty()) {
            String javascriptVar = (String) stack.pop();
            writer.write(panelComponentVar + ".addChild(" + javascriptVar + ");\n");
        }
        ;
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    public boolean getRendersChildren() {
        return true;
    }

}
