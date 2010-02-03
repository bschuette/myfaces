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

        <h:form id="form" style="display:inline" >

        <t:messages errorClass="error" showSummary="true" showDetail="true" />

        <t:dataTable id="data"
                styleClass="standardTable"
                headerClass="standardTable_Header"
                rowClasses="standardTable_Row1,standardTable_Row2"
                columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                var="country"
                value="#{countryList.countries}"
                preserveDataModel="true">
           <h:column>
               <f:facet name="header">
                  <h:panelGroup>
                    <h:outputText value="#{example_messages['label_country_name']}" />
                    <h:outputText value="*" style="color:red;"/>
                  </h:panelGroup>
               </f:facet>
               <h:inputText id="cname" value="#{country.name}" required="true" />
           </h:column>

           <h:column>
               <f:facet name="header">
                <h:panelGroup>
                    <h:outputText value="#{example_messages['label_country_iso']}" />
                    <h:outputText value="*" style="color:red;"/>
                </h:panelGroup>
               </f:facet>
               <h:inputText id="ciso" value="#{country.isoCode}" required="true" >
                        <f:validateLength maximum="2" minimum="2"/>
               </h:inputText>

           </h:column>

           <h:column>
               <f:facet name="header">
                  <h:outputText value="#{example_messages['label_country_cities']}" />
               </f:facet>
                <t:dataTable id="cities"
                        styleClass="standardTable_Column"
                        var="city"
                        value="#{country.cities}"
                        preserveDataModel="false"
                		>
                   <h:column>
                   	   <f:facet name="header">
	               	     <h:outputText value="#{example_messages['label_city']}"/>
                   	   </f:facet>
                       <h:inputText value="#{city.name}" style="font-size: 11px" >
                         <f:validateLength maximum="20"/>
                       </h:inputText>
                       <f:facet name="footer">
                       	 <h:commandButton value="#{example_messages['label_country_city_add']}" action="#{country.addCity}"/>
                       </f:facet>
                   </h:column>
                   <h:column>
                       	 <h:commandButton value="#{example_messages['label_country_city_delete']}" actionListener="#{country.deleteCity}"/>
                   </h:column>
                </t:dataTable>
           </h:column>

           <h:column>
                <h:selectBooleanCheckbox value="#{country.remove}"/>
           </h:column>

           <h:column>
                <t:commandLink value="#{example_messages['button_delete']}" actionListener="#{countryList.deleteCountry}" />
           </h:column>

           <f:facet name="footer">
                <h:panelGrid columns="1" >
                <t:commandLink value="#{example_messages['new_country']}" action="#{countryList.addCountry}"   />
                <h:panelGroup><f:verbatim>&nbsp;</f:verbatim></h:panelGroup>
                <h:panelGroup>
                    <h:commandButton action="go_back" value="#{example_messages['button_save']}" />
                    <f:verbatim>&nbsp;</f:verbatim>
                    <h:commandButton action="go_back" immediate="true" value="#{example_messages['button_cancel']}" />
                    <f:verbatim>&nbsp;</f:verbatim>
                    <h:commandButton value="#{example_messages['button_apply']}" />
                </h:panelGroup>
                </h:panelGrid>
           </f:facet>

        </t:dataTable>
        </h:form>

    </h:panelGroup>
  </ui:define>
 </ui:composition>
</body>
</html>
