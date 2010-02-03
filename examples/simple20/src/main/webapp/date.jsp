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

        <h:messages id="messageList" showSummary="true" showDetail="true" />


            <h4>
                <h:outputText value="#{example_messages['date_comp_header']}"/>
            </h4>
        

            <h:form>
                <p>
                        <h:outputText value="#{example_messages['date_comp_text1']}"/>
                        <h:message for="date1"/>
                        <t:inputDate id="date1" value="#{date1}" popupCalendar="true"/>
                        <br/>
                        <h:outputText value="#{example_messages['date_comp_text2']} #{date1}"/>
                </p>

                <p>
                <h:outputText value="#{example_messages['date_comp_text3']}"/>
                        <t:inputDate id="date2" value="#{date2}" type="time"/>
                        <h:message for="date2"/>
                        <br/>
                        <h:outputText value="#{example_messages['date_comp_text4']} #{date2}"/>
                </p>

                <p>
                <h:outputText value="#{example_messages['date_comp_text5']}"/>
                        <t:inputDate id="date3" value="#{date3}" type="both"/>
                        <h:message for="date3"/>
                        <br/>
                        <h:outputText value="#{example_messages['date_comp_text6']} #{date3}"/>
                </p>

                <h:commandButton value="#{example_messages['date_comp_button']}"/>
            </h:form>

    </h:panelGroup>
  </ui:define>
 </ui:composition>
</body>
</html>
