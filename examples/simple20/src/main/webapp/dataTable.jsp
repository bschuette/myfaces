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
      <h:form>
           <h:panelGroup id="body">

               <h:panelGrid columns="1">
                   <h:commandLink action="go_country" immediate="true">
                        <h:outputText value="#{example_messages['new_country']}" styleClass="standard" />
                   </h:commandLink>
                   <h:commandLink action="go_edit_table" immediate="true">
                        <h:outputText value="#{example_messages['country_edit_table']}" styleClass="standard" />
                   </h:commandLink>
               </h:panelGrid>
               <f:verbatim><br/></f:verbatim>

                <t:dataTable id="data"
                        styleClass="standardTable"
                        headerClass="standardTable_Header"
                        footerClass="standardTable_Header"
                        rowClasses="standardTable_Row1,standardTable_Row2"
                        columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
                        var="country"
                        value="#{countryList.countries}"
                        preserveDataModel="true" >
                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{example_messages['label_country_name']}" />
                       </f:facet>
                       <t:commandLink action="go_country" immediate="true" >
                            <h:outputText value="#{country.name}" />
                            <!-- for convenience: MyFaces extension. sets id of current row in countryForm -->
                            <!-- you don't have to implement a custom action! -->
                            <t:updateActionListener property="#{countryForm.id}" value="#{country.id}" />
                       </t:commandLink>
                   </h:column>

                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{example_messages['label_country_iso']}" />
                       </f:facet>
                       <h:outputText value="#{country.isoCode}" />
                   </h:column>

                   <h:column>
                       <f:facet name="header">
                          <h:outputText value="#{example_messages['label_country_cities']}" />
                       </f:facet>
                        <t:dataTable id="cities"
                                styleClass="standardTable_Column"
                                var="city"
                                value="#{country.cities}"
                                preserveDataModel="false">
                           <h:column>
                               <h:outputText value="#{city}" style="font-size: 11px" />
                           </h:column>
                        </t:dataTable>
                   </h:column>

                </t:dataTable>

                <f:verbatim><br/></f:verbatim>

            </h:panelGroup>
      </h:form>
  </ui:define>
 </ui:composition>
</body>
</html>
