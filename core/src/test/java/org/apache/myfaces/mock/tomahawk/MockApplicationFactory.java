/*
 * Copyright 2004-2006 The Apache Software Foundation.
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

package org.apache.myfaces.mock.tomahawk;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

public class MockApplicationFactory extends ApplicationFactory {
	private Application app = null;
	private ApplicationFactory factory;

	public MockApplicationFactory() {
	}

	public MockApplicationFactory(ApplicationFactory factory) {
		this.factory = factory;
	}

	public Application getApplication() {

        if(app==null)
        {
            return factory.getApplication();
        }

        return app;
	}

	public void setApplication(Application application) {
		this.app = application;
	}

}
