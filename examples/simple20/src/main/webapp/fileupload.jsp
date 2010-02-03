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
    <t:saveState value="#{fileUploadForm.name}"/>

    <h:panelGroup id="body">

        <h:messages id="messageList" showSummary="true" />

            <h4>
                <h:outputText value="#{example_messages['fileupload_title']}"/>
            </h4>
            <table border="1"><tr><td>

        <h:form id="form1" enctype="multipart/form-data" >
        <h:outputText value=""/>
            <h:outputText value="#{example_messages['fileupload_gimmeimage']} "/>
            <t:inputFileUpload id="fileupload"
                               accept="image/*"
                               value="#{fileUploadForm.upFile}"
                               storage="file"
                               styleClass="fileUploadInput"
                               required="true"
                               maxlength="200000"/>
            <h:message for="fileupload" showDetail="true" />
            <f:verbatim><br/></f:verbatim>
            <h:outputText value="#{example_messages['fileupload_name']}"/>
            <h:inputText value="#{fileUploadForm.name}"/>
            <h:commandButton value="#{example_messages['fileupload_button']}" action="#{fileUploadForm.upload}" />
        </h:form>

        <h:panelGrid columns="1" rendered="#{fileUploadForm.uploaded}">
            <h:outputText value="#{example_messages['fileupload_msg1']}" />
            <h:graphicImage url="fileupload_showimg.jsf"/>
            <h:outputText value="#{fileUploadForm.name}"/>
            <h:outputText value="#{example_messages['fileupload_msg2']}" />
            <h:outputLink value="fileupload_showimg.jsf">
                <f:param name="allowCache" value="true"/>
                <f:param name="openDirectly" value="false"/>
                <h:outputText value="#{example_messages['fileupload_dlimg']}"/>
            </h:outputLink>
            <h:outputText value="#{example_messages['fileupload_msg3']}" />
            <h:outputLink value="fileupload_showimg.jsf">
                <f:param name="allowCache" value="true"/>
                <f:param name="openDirectly" value="true"/>
                <h:outputText value="#{example_messages['fileupload_dlimg']}"/>
            </h:outputLink>
        </h:panelGrid>

        </td></tr></table>


    </h:panelGroup>
  </ui:define>
 </ui:composition>
</body>
</html>
