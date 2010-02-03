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
        <h:outputText value="#{example_messages['new_country']}" styleClass="standard"/>
    </h:commandLink>
    <h:commandLink action="go_edit_table" immediate="true">
        <h:outputText value="#{example_messages['country_edit_table']}" styleClass="standard"/>
    </h:commandLink>
</h:panelGrid>

    <p>
    A table rendering the details using an independent table within an cell of the master table.<br/>
    As a resulting effect the each child table will have its own cell width.
    </p>

    <t:dataTable id="data" styleClass="standardTable" headerClass="standardTable_Header" footerClass="standardTable_Header"
             rowClasses="standardTable_Row1,standardTable_Row2"
             columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column" var="currentCountry"
             value="#{countryList.countries}" preserveDataModel="true" varDetailToggler="detailToggler">
    <h:column>
        <f:facet name="header">
            <h:outputText value="#{example_messages['label_country_name']}"/>
        </f:facet>
        <t:commandLink action="go_country" immediate="true">
            <h:outputText value="#{currentCountry.name}"/>
            <!-- for convenience: MyFaces extension. sets id of current row in countryForm -->
            <!-- you don't have to implement a custom action! -->
            <t:updateActionListener property="#{countryForm.id}" value="#{currentCountry.id}"/>
        </t:commandLink>
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:outputText value="#{example_messages['label_country_iso']}"/>
        </f:facet>
        <h:outputText value="#{currentCountry.isoCode}"/>
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:outputText value="#{example_messages['label_country_cities']}"/>
        </f:facet>
        <h:commandLink rendered="#{detailToggler.currentDetailExpanded}" action="#{detailToggler.toggleDetail}">
            <h:outputText value="Hide"/>
        </h:commandLink>
        <h:commandLink rendered="#{!detailToggler.currentDetailExpanded}" action="#{detailToggler.toggleDetail}">
            <h:outputText value="Show"/>
        </h:commandLink>
    </h:column>
    <f:facet name="detailStamp">
        <t:dataTable id="cities" styleClass="standardTable_Column" var="city" value="#{currentCountry.cities}">
            <h:column>
                <h:outputText value="#{city}" style="font-size: 11px"/>
            </h:column>
            <h:column>
                <h:selectBooleanCheckbox value="#{city.selected}"></h:selectBooleanCheckbox>
            </h:column>
            <h:column>
                <h:commandLink action="#{city.unselect}" value="Unselect"/>
            </h:column>
        </t:dataTable>
    </f:facet>
</t:dataTable>

 <p>
    A table rendering the details using an "embedded" table. (embedded="true" at the child datatable).
    In contrast to the above example this will result in a combined table sharing the same layout.
    All cells in on collumn will have the same width.<br />
    To achive this the child table will not renter a table start/end tag and, also the rendering of
    any thead/tfoot/th will be supressed. You have to take care about that fact in your stylesheet(s).
    </p>

<t:dataTable id="data2" styleClass="standardTable" detailStampExpandedDefault="true"
             headerClass="standardTable_Header"
             footerClass="standardTable_Header" rowClasses="standardTable_Row1,standardTable_Row2"
             columnClasses="standardTable_Column,standardTable_ColumnCentered,standardTable_Column"
             var="currentCountry"
             value="#{countryList.countries}" preserveDataModel="true" varDetailToggler="detailToggler"
    rowId="#{currentCountry.isoCode}">
    <t:column columnId="countryName">
        <f:facet name="header">
            <h:outputText value="#{example_messages['label_country_name']}"/>
        </f:facet>
        <t:commandLink action="go_country" immediate="true">
            <h:outputText value="#{currentCountry.name}"/>
            <!-- for convenience: MyFaces extension. sets id of current row in countryForm -->
            <!-- you don't have to implement a custom action! -->
            <t:updateActionListener property="#{countryForm.id}" value="#{currentCountry.id}"/>
        </t:commandLink>
    </t:column>
    <t:column columnId="uselesColumn">
        <f:facet name="header">
            <h:outputText value="useles column"/>
        </f:facet>
        <h:outputText value="-"/>
    </t:column>
    <t:column columnId="countryIso">
        <f:facet name="header">
            <h:outputText value="#{example_messages['label_country_iso']}"/>
        </f:facet>
        <h:outputText value="#{currentCountry.isoCode}"/>
    </t:column>

    <t:column columnId="countryCities">
        <f:facet name="header">
            <h:outputText value="#{example_messages['label_country_cities']}"/>
        </f:facet>
        <h:commandLink rendered="#{detailToggler.currentDetailExpanded}" action="#{detailToggler.toggleDetail}">
            <h:outputText value="Hide"/>
        </h:commandLink>
        <h:commandLink rendered="#{!detailToggler.currentDetailExpanded}" action="#{detailToggler.toggleDetail}">
            <h:outputText value="Show"/>
        </h:commandLink>
    </t:column>

    <f:facet name="detailStamp">
        <t:dataTable id="cities2" embedded="true" var="city" value="#{currentCountry.cities}"
            rowId="#{city.name}">
            <t:column colspan="2" headercolspan="2">
                <f:facet name="header">
                    <h:outputText value="city"/>
                </f:facet>
                <h:outputText value="#{city}" style="font-size: 11px"/>
            </t:column>
            <t:column></t:column>
            <t:column>
                <f:facet name="header">
                    <h:outputText value="selected"/>
                </f:facet>
                <h:selectBooleanCheckbox value="#{city.selected}"></h:selectBooleanCheckbox>
            </t:column>
            <t:column>
                <f:facet name="header">
                    <h:outputText value="unselect"/>
                </f:facet>
                <h:commandLink action="#{city.unselect}" value="Unselect"/>
            </t:column>
        </t:dataTable>
    </f:facet>
</t:dataTable>

</h:panelGroup>

</h:form>
  </ui:define>
 </ui:composition>
</body>
</html>
