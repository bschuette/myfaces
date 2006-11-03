/**
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

package org.apache.myfaces.custom.dojolayouts;

import javax.faces.component.UIComponent;

public class DojoSplitPaneTag extends DojoContentPaneTag {

    private static final String TAG_PARAM_ORIENTATION  = "orientation";

    private static final String TAG_PARAM_SIZERWIDTH   = "sizerWidth";

    private static final String TAG_PARAM_ACTIVESIZING = "activeSizing";

    public static final String  TAG_PARAM_Persist     = "persist";

    public static final String  TAG_PARAM_StartPoint  = "startPoint";

    public static final String  TAG_PARAM_LastPoint   = "lastPoint";

    
    private String              _orientation           = null;

    private String              _sizerWidth            = null;

    private String              _activeSizing          = null;

    private String              _persist               = null;

    private String              _startPoint            = null;

    private String              _lastPoint             = null;

    public void setActiveSizing(String activeSizing) {
        this._activeSizing = activeSizing;
    }

    public void setOrientation(String orientation) {
        this._orientation = orientation;
    }

    public void setSizerWidth(String sizerWidth) {
        this._sizerWidth = sizerWidth;
    }

    public void setPersist(String persist) {
        _persist = persist;
    }

    public void setLastPoint(String lastPoint) {
        _lastPoint = lastPoint;
    }

    public void setStartPoint(String startPoint) {
        _startPoint = startPoint;
    }

    public String getComponentType() {
        return DojoSplitPane.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return DojoSplitPane.DEFAULT_RENDERER_TYPE;
    }

    protected void setProperties(UIComponent component) {

        super.setProperties(component);
        super.setIntegerProperty(component, TAG_PARAM_ACTIVESIZING, _activeSizing);
        super.setIntegerProperty(component, TAG_PARAM_ORIENTATION, _orientation);
        super.setIntegerProperty(component, TAG_PARAM_SIZERWIDTH, _sizerWidth);
        // //setProperties persist begin
        super.setBooleanProperty(component, TAG_PARAM_Persist, _persist);
        // //setProperties persist end

        // // setProperties startPoint begin
        super.setIntegerProperty(component, TAG_PARAM_StartPoint, _startPoint);
        // //setProperties startPoint end

        // //setProperties lastPoint begin
        super.setIntegerProperty(component, TAG_PARAM_LastPoint, _lastPoint);
        // //setProperties lastPoint end

        // //release lastPoint begin
        _lastPoint = null;
        // //release lastPoint end

    }

    public void release() {
        super.release();
        _activeSizing = null;
        _orientation = null;
        _sizerWidth = null;
        // //release persist begin
        _persist = null;
        // //release persist end
        // //release startPoint begin
        _startPoint = null;
        // //release startPoint end
        // //release startPoint begin
        _lastPoint = null;
        // //release startPoint end

    }
}
