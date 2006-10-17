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
package org.apache.myfaces.custom.dynaForm.annot.jsf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * setup a converter. <br />
 * converterId and converterClass are mutually exclusive
 */
@Target(value={ElementType.METHOD, ElementType.FIELD})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Converter
{
	/**
	 * attach a converter by its id
	 */
	String converterId() default "";

	/**
	 * attach a converter by its class
	 */
	Class converterClass() default Void.class;
}