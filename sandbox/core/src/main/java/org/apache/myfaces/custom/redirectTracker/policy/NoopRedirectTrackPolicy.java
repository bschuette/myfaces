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
package org.apache.myfaces.custom.redirectTracker.policy;

import org.apache.myfaces.custom.redirectTracker.RedirectTrackerPolicy;
import org.apache.myfaces.custom.redirectTracker.RedirectTrackerContext;

/**
 * This implementation does nothing (for backward compatibility with JSF) - the default
 */
public class NoopRedirectTrackPolicy implements RedirectTrackerPolicy
{
	public String trackRedirect(RedirectTrackerContext redirectContext, String redirectPath)
	{
		return redirectPath;
	}
}
