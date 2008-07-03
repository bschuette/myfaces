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
package org.apache.myfaces.custom.conversation;

import java.util.Comparator;

import javax.faces.el.ValueBinding;

/**
 * Compre the expression string of two {@link ValueBinding}s
 * 
 * @author imario@apache.org
 */
public class ValueBindingKey implements Comparator
{
    public int compare(Object o1, Object o2)
    {
        ValueBinding vb1 = (ValueBinding) o1;
        ValueBinding vb2 = (ValueBinding) o2;
        
        return vb1.getExpressionString().compareTo(vb2.getExpressionString());
    }
}
