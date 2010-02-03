<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
        xmlns:f="http://java.sun.com/jsf/core"
        xmlns:h="http://java.sun.com/jsf/html"
        xmlns:ui="http://java.sun.com/jsf/facelets"
        xmlns:t="http://myfaces.apache.org/tomahawk">
<!--
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
//-->
<body>
 <ui:composition template="/META-INF/templates/template.xhtml">
  <ui:define name="body">
    <h:panelGroup id="body">

        <t:messages showDetail="true" showSummary="false"/>

        <h:form id="form1">
            <h:panelGrid columns="3">

                <h:outputLabel for="email" value="#{example_messages['validate_email']}"/>
                <h:inputText id="email" value="#{validateForm.email}" required="true">
                    <f:validator validatorId="org.apache.myfaces.validator.Email"/>
                </h:inputText>
                <t:message id="emailError" for="email" styleClass="error"/>

                <h:outputLabel for="email2" value="#{example_messages['validate_email']}2"/>
                <h:inputText id="email2" value="#{validateForm.email2}" required="true">
                    <t:validateEmail detailMessage="Not a valid email address."/>
                </h:inputText>
                <t:message id="emailError2" for="email2" styleClass="error"/>

                <h:outputLabel for="creditCardNumber" value="#{example_messages['validate_credit']}"/>
                <h:inputText id="creditCardNumber" value="#{validateForm.creditCardNumber}" required="true">
                    <t:validateCreditCard detailMessage='#{"{0} is not a valid credit card number."}'/>
                </h:inputText>
                <t:message id="creditCardNumberError" for="creditCardNumber" styleClass="error"/>

                <h:outputLabel for="regExprValue" value="#{example_messages['validate_regexp']}"/>
                <h:inputText id="regExprValue" value="#{validateForm.regExpr}" required="true">
                    <t:validateRegExpr pattern='\d{5}' detailMessage='#{"{0} is not valid in this field." }'/>
                </h:inputText>
                <t:message id="regExprValueError" for="regExprValue" styleClass="error"/>

                <h:outputLabel for="equal" value="#{example_messages['validate_equal']}"/>
                <h:inputText id="equal" value="#{validateForm.equal}" required="true"/>
                <t:message id="equalError" for="equal" styleClass="error"/>

                <h:outputLabel for="equal2" value="#{example_messages['validate_equal']}2"/>
                <h:inputText id="equal2" value="#{validateForm.equal2}" required="true">
                    <t:validateEqual for="equal"
                                     summaryMessage='#{"Value {0} should equal {1}"}'
                                     detailMessage='#{"The value of this field, {0}, should equal the value of that other field, {1}"}'/>
                </h:inputText>
                <t:message id="equal2Error" for="equal2" styleClass="error"/>

                <h:panelGroup/>
                <h:commandButton id="validateButton" value="#{example_messages['button_submit']}"
                                 action="#{validateForm.submit}"/>
                <h:panelGroup/>

            </h:panelGrid>
        </h:form>

    </h:panelGroup>
  </ui:define>
 </ui:composition>
</body>
</html>
