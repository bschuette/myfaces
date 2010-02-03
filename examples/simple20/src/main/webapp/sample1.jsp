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

        <t:saveState id="save1" value="#{calcForm.number1}" />
        <t:saveState id="save2" value="#{calcForm.number2}" />
        <t:saveState id="save3" value="#{ucaseForm.text}" />

        <t:messages id="messageList" styleClass="error" summaryFormat="{0} in {1}" />

        <h4>
                <h:outputText value="#{example_messages['sample1_form']}"/>
            </h4>
            <table border="1"><tr><td>

        <h:form id="form1">
            <h:outputLabel for="form1:number1" value="#{example_messages['sample1_number']} 1" />
            <h:outputText value="#{validationController.number1ValidationLabel}: "/>
            <h:inputText id="number1" value="#{calcForm.number1}" maxlength="10" size="25" required="true" >
               <f:validateLongRange minimum="1" maximum="10" />
            </h:inputText>
            <h:message id="number1Error" for="form1:number1" styleClass="error" />
            <br/>

            <h:outputLabel for="form1:number2" value="#{example_messages['sample1_form']} 2" />
            <h:outputText value="#{validationController.number2ValidationLabel}: "/>
            <h:inputText id="number2" value="#{calcForm.number2}" maxlength="10" size="25" required="true" >
               <f:validateLongRange minimum="20" maximum="50" />
            </h:inputText>
            <h:message id="number2Error" for="form1:number2" styleClass="error" />
            <br/>

            <h:outputLabel for="form1:result" value="#{example_messages['sample1_result']}: " />
            <h:outputText id="result" value="#{calcForm.result}" />
            <br/>

            <h:commandButton id="addButton" value="#{example_messages['sample1_add']}" action="none">
                <f:actionListener type="org.apache.myfaces.examples.example1.CalcActionListener" ></f:actionListener>
            </h:commandButton>
            <h:commandButton id="subtractButton" value="#{example_messages['sample1_sub']}" action="none">
                <f:actionListener type="org.apache.myfaces.examples.example1.CalcActionListener" ></f:actionListener>
            </h:commandButton>
            <br/>

            <h:commandLink id="href1" action="none">
                <h:outputText value="#{example_messages['sample1_add_link']}"/>
                <f:actionListener type="org.apache.myfaces.examples.example1.CalcActionListener" ></f:actionListener>
            </h:commandLink>
            <br/>
            <h:commandLink id="href2" action="none">
                <h:outputText value="#{example_messages['sample1_sub_link']}"/>
                <f:actionListener type="org.apache.myfaces.examples.example1.CalcActionListener" ></f:actionListener>
            </h:commandLink>
        </h:form>


        </td></tr></table>
        <h4>
        <h:outputText value="#{example_messages['sample1_another_form']}"/>
        </h4>
        <table border="1"><tr><td>

        <h:form id="form2">
            <h:outputLabel for="form2:text" value="#{example_messages['sample1_text']}" />
            <h:outputText value="#{validationController.textValidationLabel}: "/>
            <h:inputText id="text" value="#{ucaseForm.text}">
                <f:validateLength minimum="3" maximum="7"/>
            </h:inputText>
            <h:message id="textError" for="form2:text" styleClass="error" />
            <br/>
            <h:commandButton id="ucaseButton" value="#{example_messages['sample1_uppercase']}" action="none">
                <f:actionListener type="org.apache.myfaces.examples.example1.UCaseActionListener" />
            </h:commandButton>
            <h:commandButton id="lcaseButton" value="#{example_messages['sample1_lowercase']}" action="none">
                <f:actionListener type="org.apache.myfaces.examples.example1.UCaseActionListener" />
            </h:commandButton>
            <br/>
        </h:form>

        </td></tr></table>
        <h4>
        <h:outputText value="#{example_messages['sample1_validation']}"/>
        </h4>
        <table border="1"><tr><td>

        <h:form id="form3">
            <h:commandButton id="valDisable" value="#{example_messages['sample1_disable_validation']}" action="#{validationController.disableValidation}" />
            <h:commandButton id="valEnable" value="#{example_messages['sample1_enable_validation']}" action="#{validationController.enableValidation}" />
        </h:form>

        </td></tr></table>

        <br/>
        <h:form>
	        <h:commandLink id="jump_home" action="#{ucaseForm.jumpHome}" >Go Home</h:commandLink>
        </h:form>

    </h:panelGroup>
  </ui:define>
 </ui:composition>
</body>
</html>
