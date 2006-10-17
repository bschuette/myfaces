<%--
  Copyright 2006 The Apache Software Foundation.

  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy of
  the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  License for the specific language governing permissions and limitations under
  the License.
  --%>

<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
<t:document>
<t:documentHead>
    <%@include file="/inc/head.inc" %>
</t:documentHead>
<t:documentBody>

    <h:form>
        <%@include file="/inc/page_header.jsp" %>

        <h:panelGrid>

            <h:outputLink value="simpleBean.jsf" ><f:verbatim>Show a simple bean as form and list</f:verbatim></h:outputLink>
            <h:outputLink value="customizedSimpleBean.jsf" ><f:verbatim>Show a simple bean as form with customized property</f:verbatim></h:outputLink>
            <h:outputLink value="relationBean.jsf" ><f:verbatim>Show a bean which as a relation to another bean</f:verbatim></h:outputLink>

		</h:panelGrid>

        <%@include file="/inc/page_footer.jsp" %>
    </h:form>

</t:documentBody>
</t:document>
</f:view>