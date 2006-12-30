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
package org.apache.myfaces.test;

import org.apache.myfaces.test.utils.TestUtils;
import org.apache.shale.test.base.AbstractJsfTestCase;

/**
 * Abstract Shale Test base class, which sets up the JSF environment.  If the test
 * overrides <code>setUp()</code> and/or <code>tearDown()</code>, then those
 * methods but call the overwitten method to insure a valid test environment.
 */
public class AbstractTomahawkJsfTestCase extends AbstractJsfTestCase
{

    /**
     * Construct a new instance of the test.
     * 
     * @param name Name of the test.
     */
    public AbstractTomahawkJsfTestCase(String name)
    {
        super(name);
    }

    /**
     *  Setup the test environment, including the following:
     *  <ul>
     *  <li>Add Tomahawk's renderers to the faces context.</li>
     *  </ul> 
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        TestUtils.addDefaultRenderers(facesContext);
    }

    /**
     * Tear down the test environment.
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

}
