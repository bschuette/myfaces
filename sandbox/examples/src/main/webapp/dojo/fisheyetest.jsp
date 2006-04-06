<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://myfaces.apache.org/sandbox" prefix="s"%>

<!--
/*
 * Copyright 2005 The Apache Software Foundation.
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
<%@include file="../inc/head.inc"%>
<body>
<f:view>
	<s:fishEyeList itemWidth="50" itemHeight="50"
		itemMaxWidth="200" itemMaxHeight="200"
		orientation="horizontal" effectUnits="2"
		itemPadding="10" attachEdge="top" labelEdge="bottom">

	<div class="dojo-FisheyeListItem" onClick="load_app(1);"
		dojo:iconsrc="images/icon_browser.png" caption="Web Browser"></div>

	<div class="dojo-FisheyeListItem" onClick="load_app(2);"
		dojo:iconsrc="images/icon_calendar.png" caption="Calendar"></div>

	<div class="dojo-FisheyeListItem" onClick="load_app(3);"
		dojo:iconsrc="images/icon_email.png" caption="Email"></div>

	<div class="dojo-FisheyeListItem" onClick="load_app(4);"
		dojo:iconsrc="images/icon_texteditor.png" caption="Text Editor"></div>

	<div class="dojo-FisheyeListItem" onClick="load_app(5);"
		dojo:iconsrc="images/icon_update.png" caption="Software Update"></div>

	<div class="dojo-FisheyeListItem" onClick="load_app(6);"
		dojo:iconsrc="images/icon_users.png" dojo:caption="Users"></div>
	</s:fishEyeList>


	<%@include file="../inc/page_footer.jsp"%>
</f:view>
</body>
</html>
