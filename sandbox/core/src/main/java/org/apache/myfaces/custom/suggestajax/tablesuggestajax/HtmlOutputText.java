package org.apache.myfaces.custom.suggestajax.tablesuggestajax;


import org.apache.myfaces.custom.inputAjax.Listener;
import org.apache.myfaces.custom.util.ComponentUtils;
import org.apache.myfaces.shared_tomahawk.util._ComponentUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_impl.renderkit.html.HTML;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Gerald M�llan
 *         Date: 15.02.2006
 *         Time: 13:30:43
 */
public class HtmlOutputText extends org.apache.myfaces.component.html.ext.HtmlOutputText
{
    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlOutputTextFor";

    private String _for;
    private String _forValue;
    private String _label;

    public HtmlOutputText()
    {
    }

    public void encodeBegin(FacesContext facesContext) throws IOException
    {
        super.encodeBegin(facesContext);
    }

    public void encodeEnd(FacesContext facesContext) throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.SPAN_ELEM, null);
        writer.writeAttribute("id", this.getClientId(facesContext) + "_span", null);
        super.encodeEnd(facesContext);
        writer.endElement(HTML.SPAN_ELEM);
    }

    public boolean getRendersChildren()
    {
        return true;
    }

    public void encodeChildren(FacesContext facesContext) throws IOException
    {
        checkForListeners(facesContext, this);
        super.encodeChildren(facesContext);
    }

    private void checkForListeners(FacesContext context, UIComponent component)
    {
        // TODO: MOVE THIS UP THE TREE OR TO SOME OTHER LEVEL SO IT CAN WORK ON ANY COMPONENT
        List children = component.getChildren();
        if(children != null){
            for (int i=0; i<children.size(); i++)
            {
                UIComponent child = (UIComponent) children.get(i);
                if(child instanceof Listener){
                    Listener listener = (Listener) child;
                    Map rmap = context.getExternalContext().getRequestMap();
                    List listeners = (List) rmap.get(Listener.LISTENER_MAP_ENTRY);
                    if(listeners == null){
                        listeners = new ArrayList();
                        rmap.put(Listener.LISTENER_MAP_ENTRY, listeners);
                    }
                    // find component
                    UIViewRoot root = context.getViewRoot();
                    UIComponent ajaxComponent; //= component.findComponent(listener.getOn());
                    //System.out.println("FINDING COMPONENT TO LISTEN ON: " + ajaxComponent);
                    ajaxComponent = ComponentUtils.findComponentById(context, root, listener.getOn());
                    if(ajaxComponent != null){
                        //System.out.println("FINDING COMPONENT TO LISTEN ON: " + ajaxComponent);
                        Map listenerItem = new HashMap();
                        listenerItem.put("listenOn", ajaxComponent.getClientId(context));
                        listenerItem.put("listenerId", component.getClientId(context));
                        listenerItem.put("action", "update");
                        listeners.add(listenerItem);
                    }
                }
            }
        }
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = _for;
        values[2] = _label;
        values[3] = _forValue;

        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _for = (String) values[1];
        _label = (String) values[2];
        _forValue = (String) values[3];
    }

    public String getFor()
    {
         if (_for != null)
            return _for;
        ValueBinding vb = getValueBinding("for");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public void setFor(String aFor)
    {
        _for = aFor;
    }


    public String getForValue()
    {
         if (_forValue != null)
            return _forValue;
        ValueBinding vb = getValueBinding("forValue");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public void setForValue(String forValue)
    {
        _forValue = forValue;
    }

    public String getLabel()
    {
        if (_label != null)
            return _label;
        ValueBinding vb = getValueBinding("label");
        return vb != null ? vb.getValue(getFacesContext()).toString() : null;
    }

    public void setLabel(String label)
    {
        _label = label;
    }
}