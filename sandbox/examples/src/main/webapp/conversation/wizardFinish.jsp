<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://myfaces.apache.org/sandbox" prefix="s"%>

<!--
/*
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
//-->

<html>
<head>
<meta HTTP-EQUIV="Content-Type" CONTENT="text/html;charset=UTF-8" />
<title>MyFaces - the free JSF Implementation</title>
<link rel="stylesheet" type="text/css" href="css/basic.css" />
</head>
<body>
<f:view>

<t:htmlTag value="h1">Registration Wizard</t:htmlTag>

<h:outputLink value="home.jsf"><h:outputText value="Menu" /></h:outputLink>
<s:separateConversationContext>
	<h:outputLink value="home.jsf"><h:outputText value="Menu (with new conversationContext)" /></h:outputLink>
</s:separateConversationContext>

<h:form>
<h:outputText value="Whatever the page might tell you, no data will ever be saved ;-)." />
<h:panelGrid columns="2">
	<f:facet name="header">
		<h:outputText value="Registration Wizard (Check)" />
	</f:facet>
	<f:facet name="footer">
		<h:panelGroup>
			<h:commandButton value="Save" action="#{wizardController.save}">
				<s:endConversation name="wizard" onOutcome="success"/>
			</h:commandButton>
		</h:panelGroup>
	</f:facet>
	
    <h:outputText value="Edit data on" />
	<h:commandButton value="Page1" action="wizardPage1" />
	
    <h:outputText value="Salutation: " />
    <h:outputText value="#{wizardData.salutation}" />
    
    <h:outputText value="Title: " />
    <h:outputText value="#{wizardData.title}" />
    
    <h:outputText value="Name: " />
    <h:outputText value="#{wizardData.name}" />
    
    <h:outputText value="Surename: " />
    <h:outputText value="#{wizardData.surename}" />
    
    <h:outputText value="Edit data on" />
	<h:commandButton value="Page2" action="wizardPage2" />
	
    <h:outputText value="Street: " />
    <h:outputText value="#{wizardData.street}" />
    
    <h:outputText value="City: " />
    <h:outputText value="#{wizardData.city}" />
    
    <h:outputText value="State: " />
    <h:outputText value="#{wizardData.state}" />

    <h:outputText value="Province: " />
    <h:outputText value="#{wizardData.province}" />
    
    <h:outputText value="Postal: " />
    <h:outputText value="#{wizardData.postal}" />
    
    <h:outputText value="Edit data on" />
	<h:commandButton value="Page3" action="wizardPage3" />
	
    <h:outputText value="Info: " />
    <t:outputText value="#{wizardData.info}" escape="false" />
    
</h:panelGrid>
<h:messages showDetail="true"/>
</h:form>
</f:view>
</body>
</html>
