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

<s:startConversation name="page" />
<s:conversation name="page" value="#{convData}" />

<h:outputLink value="home.jsf"><h:outputText value="Menu" /></h:outputLink>

<h:form>
<h:panelGrid columns="2">
    <h:outputText value="Enter something into this field: " />
    <h:inputText value="#{convData.input}" />
    
    <h:commandLink value="check value"/>
    <h:commandLink value="save value">
        <s:endConversation name="page" />
    </h:commandLink>
    <h:commandLink value="save value - action" action="saveMe">
        <s:endConversation name="page" />
    </h:commandLink>
    <h:commandLink value="save value - actionMethod" action="#{convData.save}">
        <s:endConversation name="page" />
    </h:commandLink>
    <h:commandLink value="save value - end conv onOutcome" action="saveLocal">
        <s:endConversation name="page" onOutcome="saveLocal"/>
    </h:commandLink>
    <h:commandLink value="save value - without end conv onOutcome" action="saveLocal">
        <s:endConversation name="page" onOutcome="NONO_saveLocal"/>
    </h:commandLink>
</h:panelGrid>
<h:panelGrid columns="1">
    <h:outputText value="Press 'check value' to simulate a server action witout ending the conversation" />
    <h:outputText value="Press 'save value' to simulate a server action AND END the conversation" />
</h:panelGrid>
</h:form>
</f:view>
</body>
</html>
