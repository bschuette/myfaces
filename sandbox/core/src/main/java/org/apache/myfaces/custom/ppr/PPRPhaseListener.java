/**
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.custom.ppr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.shared_tomahawk.renderkit.RendererUtils;
import org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlResponseWriterImpl;

import javax.faces.FacesException;
import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Ernst Fastl
 */
public class PPRPhaseListener implements PhaseListener
{
	private static Log log = LogFactory.getLog(PPRPhaseListener.class);
	private static final String PPR_PARAMETER = "org.apache.myfaces.PPRCtrl.ajaxRequest";
	private static final String TRIGGERED_COMPONENTS_PARAMETER = "org.apache.myfaces.PPRCtrl.triggeredComponents";
	private static final String XML_HEADER = "<?xml version=\"1.0\"?>\n";

	public void afterPhase(PhaseEvent phaseEvent)
	{
	}

	public void beforePhase(PhaseEvent event)
	{
		if (log.isDebugEnabled())
		{
			log.debug("In PPRPhaseListener beforePhase");
		}

		FacesContext context = event.getFacesContext();
		Map externalRequestMap = context.getExternalContext().getRequestParameterMap();

		if (externalRequestMap.containsKey(PPR_PARAMETER))
		{
			context.getExternalContext().getRequestMap().put(PPRPanelGroupRenderer.PPR_RESPONSE, Boolean.TRUE);

			ServletResponse response =
				(ServletResponse) context.getExternalContext().getResponse();
			ServletRequest request =
				(ServletRequest) context.getExternalContext().getRequest();

			UIViewRoot viewRoot = context.getViewRoot();
            response.setContentType("text/xml;charset=" + request.getCharacterEncoding());
			response.setLocale(viewRoot.getLocale());
			String triggeredComponents = (String) externalRequestMap.get(TRIGGERED_COMPONENTS_PARAMETER);
			try
			{
				PrintWriter out = response.getWriter();
				context.setResponseWriter(new HtmlResponseWriterImpl(out,
					null,
					request.getCharacterEncoding()));
				out.print(XML_HEADER);
				out.print("<response>\n");
				encodeTriggeredComponents(out, triggeredComponents, viewRoot, context);
				out.print("</response>");
				out.flush();
			}
			catch (IOException e)
			{
				throw new FacesException(e);
			}

			context.responseComplete();
		}                                          
	}

	private void encodeTriggeredComponents(PrintWriter out, String triggeredComponents, UIViewRoot viewRoot, FacesContext context)
	{
		StringTokenizer st = new StringTokenizer(triggeredComponents, ",", false);
		String clientId;
		UIComponent component;
		while (st.hasMoreTokens())
		{
			clientId = st.nextToken();
			component = viewRoot.findComponent(clientId);
			if(component!=null) {
				out.print("<component id=\"" +
					component.getClientId(context) +
					"\"><![CDATA[");
				try
				{
					RendererUtils.renderChildren(context, component);
				}
				catch (IOException e)
				{
					throw new FacesException(e);
				}
				out.print("]]></component>");
			}
			else
			{
				log.debug("PPRPhaseListener component with id" + clientId + "not found!");
			}
		}
        out.print("<state>");
                FacesContext facesContext = FacesContext.getCurrentInstance();
                StateManager stateManager = facesContext.getApplication().getStateManager();
                StateManager.SerializedView serializedView
                        = stateManager.saveSerializedView(facesContext);
                try
                {
                    stateManager.writeState(facesContext, serializedView);
                }
                catch (IOException e)
                {
                    throw new FacesException(e);
                }

        out.print("</state>");

    }

	public PhaseId getPhaseId()
	{
		return PhaseId.RENDER_RESPONSE;
	}
}
