<%@ page session="false" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://myfaces.apache.org/sandbox" prefix="s"%>

<html>
	<head>
		<meta HTTP-EQUIV="Content-Type" CONTENT="text/html;charset=UTF-8" />
		<title>MyFaces - the free JSF Implementation</title>

		<link rel="stylesheet" type="text/css" href="css/basic.css" />
		<style type="text/css">
    html, body{	
		width: 100%;	/* make the body expand to fill the visible window */
		height: 100%;
		overflow: hidden;	/* erase window level scrollbars */
		padding: 0 0 0 0;
		margin: 0 0 0 0;
    }
	.dojoSplitPane{
		margin: 5px;
	}
   </style>
	</head>

	<!--
/*
 * Copyright 2004 The Apache Software Foundation.
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


	<body>
		<f:view>
			<h:panelGroup style="width: 100%; height: 100%;">
				<s:dojoInitializer require="dojo.widget.SplitContainer" />
				<s:dojoInitializer require="dojo.widget.ContentPane" />
				<s:layoutingSplitPane id="split" activeSizing="0" 
					style="width: 100%; height: 100%; background: #eeeeee; padding: 10px;" sizerWidth="5">
					<s:layoutingContentPane id="left" sizeShare="20">
						<h:outputFormat value="content1"></h:outputFormat>
						<h:outputFormat value="content2"></h:outputFormat>
					</s:layoutingContentPane>
					<s:layoutingContentPane id="right">
						<h:outputFormat value="contentright1"></h:outputFormat>
						<h:outputFormat value="contentright2"></h:outputFormat>
					</s:layoutingContentPane>
				</s:layoutingSplitPane>
			</h:panelGroup>

		</f:view>
		<%@ include file="../inc/page_footer.jsp"%>
	</body>
</html>
