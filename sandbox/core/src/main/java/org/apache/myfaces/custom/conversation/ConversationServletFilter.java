package org.apache.myfaces.custom.conversation;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ConversationServletFilter implements Filter
{
	public final static String CONVERSATION_FILTER_CALLED = "org.apache.myfaces.conversation.ConversationServletFilter.CALLED";
	
	public void init(FilterConfig arg0) throws ServletException
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		ConversationManager conversationManager = null;
		
		if (!Boolean.TRUE.equals(request.getAttribute(CONVERSATION_FILTER_CALLED)))
		{
			if (request instanceof HttpServletRequest)
			{
				conversationManager = ConversationManager.getInstance(((HttpServletRequest) request).getSession(false));
				if (conversationManager != null)
				{
					conversationManager.attachPersistence();
				}
			}
			
			request.setAttribute(CONVERSATION_FILTER_CALLED, Boolean.TRUE);
		}
		
		try
		{
			chain.doFilter(request, response);
		}
		finally
		{
			if (conversationManager != null)
			{
				conversationManager.detachPersistence();
			}
		}
	}

	public void destroy()
	{
	}
}