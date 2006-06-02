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
package org.apache.myfaces.component.html.ext;

import javax.faces.component.UIOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.NewspaperTable;
import org.apache.myfaces.component.UserRoleAware;
import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.custom.crosstable.UIColumns;
import org.apache.myfaces.custom.sortheader.HtmlCommandSortHeader;
import org.apache.myfaces.renderkit.html.ext.HtmlTableRenderer;
import org.apache.myfaces.renderkit.html.util.TableContext;
import org.apache.myfaces.shared_tomahawk.renderkit.JSFAttr;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.DataModel;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.*;
import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import org.apache.myfaces.custom.column.HtmlSimpleColumn;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 */
public class HtmlDataTable extends HtmlDataTableHack implements UserRoleAware, NewspaperTable
{
    private static final Log log = LogFactory.getLog(HtmlDataTable.class);

    private static final int PROCESS_DECODES = 1;
    private static final int PROCESS_VALIDATORS = 2;
    private static final int PROCESS_UPDATES = 3;

    private static final boolean DEFAULT_SORTASCENDING = true;
    private static final boolean DEFAULT_SORTABLE      = false;
    private static final Class OBJECT_ARRAY_CLASS = (new Object[0]).getClass();

    /** the property names */
    public static final String NEWSPAPER_COLUMNS_PROPERTY = "newspaperColumns";
    public static final String SPACER_FACET_NAME = "spacer";
    
    /** the value of the column count property */
    private int _newspaperColumns = 1;

    private _SerializableDataModel _preservedDataModel;

    private String _forceIdIndexFormula = null;
    private String _sortColumn = null;
    private Boolean _sortAscending = null;
    private String _sortProperty = null;
    private Boolean _sortable = null;
    private String _rowOnClick = null;
    private String _rowOnDblClick = null;
    private String _rowOnMouseDown = null;
    private String _rowOnMouseUp = null;
    private String _rowOnMouseOver = null;
    private String _rowOnMouseMove = null;
    private String _rowOnMouseOut = null;
    private String _rowOnKeyPress = null;
    private String _rowOnKeyDown = null;
    private String _rowOnKeyUp = null;
    private String _rowStyleClass = null;
    private String _rowStyle = null;
    private String _rowGroupStyle = null;
    private String _rowGroupStyleClass = null;
    private String _varDetailToggler = null;
    
    private int _sortColumnIndex = -1;
    
    private boolean _isValidChildren = true;

    private Set _expandedNodes = new HashSet();

    private Map _detailRowStates = new HashMap();

    private TableContext _tableContext = null;

    public TableContext getTableContext()
    {
        if ( _tableContext == null )
        {
            _tableContext = new TableContext();
        }
        return _tableContext;
    }

    public String getClientId(FacesContext context)
    {
        String standardClientId = super.getClientId(context);
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
        {
            return standardClientId;
        }

        String forcedIdIndex = getForceIdIndexFormula();
        if( forcedIdIndex == null || forcedIdIndex.length() == 0 )
            return standardClientId;

        // Trick : Remove the last part starting with NamingContainer.SEPARATOR_CHAR that contains the rowIndex.
        // It would be best to not resort to String manipulation,
        // but we can't get super.super.getClientId() :-(
        int indexLast_ = standardClientId.lastIndexOf(NamingContainer.SEPARATOR_CHAR);
        if( indexLast_ == -1 )
        {
            log.info("Could not parse super.getClientId. forcedIdIndex will contain the rowIndex.");
            return standardClientId+NamingContainer.SEPARATOR_CHAR+forcedIdIndex;
        }

        //noinspection UnnecessaryLocalVariable
        String parsedForcedClientId = standardClientId.substring(0, indexLast_+1)+forcedIdIndex;
        return parsedForcedClientId;
    }

    public UIComponent findComponent(String expr)
    {
        if (expr.length()>0 && Character.isDigit(expr.charAt(0)))
        {
            int separatorIndex = expr.indexOf(NamingContainer.SEPARATOR_CHAR);

            String rowIndexStr=expr;
            String remainingPart=null;

            if(separatorIndex!=-1)
            {
                rowIndexStr = expr.substring(0,separatorIndex);
                remainingPart = expr.substring(separatorIndex+1);
            }

            int rowIndex = Integer.valueOf(rowIndexStr).intValue();

            if(remainingPart == null)
            {
                log.error("Wrong syntax of expression : "+expr+
                          " rowIndex was provided, but no component name.");
                return null;
            }

            UIComponent comp = super.findComponent(remainingPart);

            if(comp == null)
                return null;

            //noinspection UnnecessaryLocalVariable
            UIComponentPerspective perspective = new UIComponentPerspective(this,comp,rowIndex);
            return perspective;
        }
        else
        {
            return super.findComponent(expr);
        }
    }

    public void setRowIndex(int rowIndex)
    {
    	FacesContext facesContext = FacesContext.getCurrentInstance();

        if (rowIndex < -1)
        {
            throw new IllegalArgumentException("rowIndex is less than -1");
        }

        UIComponent facet = getFacet(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME);
        /*Just for obtaining an iterator which must be passed to saveDescendantComponentStates()*/
        Set set = new HashSet();
        set.add(facet);
        if(rowIndex!=-1 && facet!=null){

            _detailRowStates.put(getClientId(facesContext),saveDescendantComponentStates(set.iterator(),false));
        }

        String rowIndexVar = getRowIndexVar();
        String rowCountVar = getRowCountVar();
        String previousRowDataVar = getPreviousRowDataVar();
        if (rowIndexVar != null || rowCountVar != null || previousRowDataVar != null)
        {
            Map requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();

            if (previousRowDataVar != null && rowIndex >= 0) //we only need to provide the previousRowDataVar for a valid rowIndex
            {
                if (isRowAvailable())
                {
                    //previous row is available
                    requestMap.put(previousRowDataVar, getRowData());
                }
                else
                {
                    //no previous row available
                    requestMap.put(previousRowDataVar, null);
                }
            }

            super.setRowIndex(rowIndex);

            if (rowIndex >= 0)
            {
                //regular row index, update request scope variables
                if (rowIndexVar != null)
                {
                    requestMap.put(rowIndexVar, new Integer(rowIndex));
                }

                if (rowCountVar != null)
                {
                    requestMap.put(rowCountVar, new Integer(getRowCount()));
                }
            }
            else
            {
                //rowIndex == -1 means end of loop --> remove request scope variables
                if (rowIndexVar != null)
                {
                    requestMap.remove(rowIndexVar);
                }

                if (rowCountVar != null)
                {
                    requestMap.remove(rowCountVar);
                }

                if (previousRowDataVar != null)
                {
                    requestMap.remove(previousRowDataVar);
                }
            }
        }
        else
        {
            // no extended var attributes defined, no special treatment
            super.setRowIndex(rowIndex);
        }

        if (rowIndex!=-1 && facet!=null)
        {
            Object rowState = _detailRowStates.get(getClientId(facesContext));

            restoreDescendantComponentStates(set.iterator(),
                                rowState, false);

        }

        if (_varDetailToggler != null)
        {
            Map requestMap = getFacesContext().getExternalContext().getRequestMap();
            requestMap.put(_varDetailToggler, this);
        }
    }       

    public void processDecodes(FacesContext context)
    {
        if (!isRendered())
        {
            return;
        }
        super.processDecodes(context);
        setRowIndex(-1);
        processColumns(context, PROCESS_DECODES);
        setRowIndex(-1);
        processDetails(context,PROCESS_DECODES);
        setRowIndex(-1);
    }

    /**
     *
     * @param context
     * @param processAction
     */
    private void processDetails(FacesContext context, int processAction)
    {
    	UIComponent facet = getFacet(HtmlTableRenderer.DETAIL_STAMP_FACET_NAME);

    	if (facet!=null) 
        {
            int first = getFirst();
            int rows = getRows();
            int last;
            if (rows == 0)
            {
                last = getRowCount();
            }
            else
            {
                last = first + rows;
            }
            for (int rowIndex = first; last==-1 || rowIndex < last; rowIndex++)
            {
                setRowIndex(rowIndex);

                if(!isCurrentDetailExpanded()){
                    continue;
                }

                //scrolled past the last row
                if (!isRowAvailable())
                    break;

                process(context,facet,processAction);
            }
    	}
    }

    /**
     * @param context
     * @param processAction
     */
    private void processColumns(FacesContext context, int processAction)
    {
        for (Iterator it = getChildren().iterator(); it.hasNext();)
        {
            UIComponent child = (UIComponent) it.next();
            if (child instanceof UIColumns)
            {
                process(context, child, processAction);
            }
        }
    }

    private void process(FacesContext context, UIComponent component, int processAction)
    {
        switch (processAction)
        {
            case PROCESS_DECODES:
                    component.processDecodes(context);
                    break;
            case PROCESS_VALIDATORS:
                    component.processValidators(context);
                    break;
            case PROCESS_UPDATES:
                    component.processUpdates(context);
                    break;
        }
    }

    public void processValidators(FacesContext context)
    {
        if (!isRendered())
        {
            return;
        }
        super.processValidators(context);
        processColumns(context, PROCESS_VALIDATORS);
        setRowIndex(-1);
        processDetails(context,PROCESS_VALIDATORS);
        setRowIndex(-1);

        if (context.getRenderResponse())
        {
            _isValidChildren = false;
        }
    }

    public void processUpdates(FacesContext context)
    {
        if (!isRendered())
        {
            return;
        }
        super.processUpdates(context);
        processColumns(context, PROCESS_UPDATES);
        setRowIndex(-1);
        processDetails(context,PROCESS_UPDATES);
        setRowIndex(-1);

        if (isPreserveDataModel())
        {
            updateModelFromPreservedDataModel(context);
        }

        if (context.getRenderResponse())
        {
            _isValidChildren = false;
        }
    }

    private void updateModelFromPreservedDataModel(FacesContext context)
    {
        ValueBinding vb = getValueBinding("value");
        if (vb != null && !vb.isReadOnly(context))
        {
            _SerializableDataModel dm = (_SerializableDataModel) getDataModel();
            Class type = vb.getType(context);
            if (DataModel.class.isAssignableFrom(type))
            {
                vb.setValue(context, dm);
            }
            else if (List.class.isAssignableFrom(type))
            {
                vb.setValue(context, dm.getWrappedData());
            }
            else if (OBJECT_ARRAY_CLASS.isAssignableFrom(type))
            {
                List lst = (List) dm.getWrappedData();
                vb.setValue(context, lst.toArray(new Object[lst.size()]));
            }
            else if (ResultSet.class.isAssignableFrom(type))
            {
                throw new UnsupportedOperationException(this.getClass().getName()
                                                        + " UnsupportedOperationException");
            }
            else
            {
                //Assume scalar data model
                List lst = (List) dm.getWrappedData();
                if (lst.size() > 0)
                {
                    vb.setValue(context, lst.get(0));
                }
                else
                {
                    vb.setValue(context, null);
                }
            }
        }
        _preservedDataModel = null;
    }

    public void encodeBegin(FacesContext context) throws IOException
    {
        if (!isRendered())
            return;

        if (_isValidChildren && !hasErrorMessages(context))
        {
            _preservedDataModel = null;
        }

        for (Iterator iter = getChildren().iterator(); iter.hasNext();)
        {
            UIComponent component = (UIComponent) iter.next();
            if (component instanceof UIColumns)
            {
                // Merge the columns from the tomahawk dynamic component
                // into this object.
                ((UIColumns) component).encodeTableBegin(context);
            }
        }                
               
        //replace facet header content component of the columns, with a new command sort header component
        //if sortable=true, replace it for all, if not just for the columns marked as sortable
        for (Iterator iter = getChildren().iterator(); iter.hasNext();)
        {
            UIComponent component = (UIComponent) iter.next();
            if (component instanceof UIColumn)
            {
                UIColumn aColumn = (UIColumn)component;                                        
                UIComponent headerFacet = aColumn.getHeader();                
         
                boolean replaceHeaderFacets = isSortable(); //if the table is sortable, all 
                                                            //header facets can be changed if needed                        
                String columnName = null;
                String propertyName = null;
                boolean defaultSorted = false;

                if (aColumn instanceof HtmlSimpleColumn)
                {
                    HtmlSimpleColumn asColumn = (HtmlSimpleColumn)aColumn;
                    propertyName = asColumn.getSortPropertyName();
                    defaultSorted = asColumn.isDefaultSorted();
                    
                    if (asColumn.isSortable())
                        replaceHeaderFacets = true;
                }
                
                //replace header facet with a sortable header component if needed
                if (replaceHeaderFacets && isSortHeaderNeeded(aColumn, headerFacet))
                {                    
                    propertyName = propertyName != null ? propertyName : getSortPropertyFromEL(aColumn);
                    
                    HtmlCommandSortHeader sortHeader = createSortHeaderComponent(context, aColumn, headerFacet, propertyName);                        
                    columnName = sortHeader.getColumnName();

                    aColumn.setHeader(sortHeader);
                    sortHeader.setParent(aColumn);                                                
                }                                                                   
                else if (headerFacet instanceof HtmlCommandSortHeader)
                {                    
                    HtmlCommandSortHeader sortHeader = (HtmlCommandSortHeader)headerFacet;
                    columnName = sortHeader.getColumnName();
                    propertyName = sortHeader.getPropertyName();
                }
                
                //make the column marked as default sorted be the current sorted column
                if (defaultSorted && getSortColumn() == null)
                {                    
                    setSortColumn(columnName);
                    setSortProperty(propertyName);
                }
            }
        }        

        // Now invoke the superclass encodeBegin, which will eventually
        // execute the encodeBegin for the associated renderer.
        super.encodeBegin(context);
    }           
    
    /**
     *
     */
    protected boolean isSortHeaderNeeded(UIColumn parentColumn, UIComponent headerFacet)    
    {
        return !(headerFacet instanceof HtmlCommandSortHeader);        
    }
    /**
     *
     */
    protected HtmlCommandSortHeader createSortHeaderComponent(FacesContext context, UIColumn parentColumn, UIComponent initialHeaderFacet, String propertyName)
    {
        Application application = context.getApplication();
        
        HtmlCommandSortHeader sortHeader = (HtmlCommandSortHeader)application.createComponent(HtmlCommandSortHeader.COMPONENT_TYPE);                                
        String id = context.getViewRoot().createUniqueId();
        sortHeader.setId(id);
        sortHeader.setColumnName(id);
        sortHeader.setPropertyName(propertyName);
        sortHeader.setArrow(true);          
        sortHeader.setImmediate(true); //needed to work when dataScroller is present
        sortHeader.getChildren().add(initialHeaderFacet);
        initialHeaderFacet.setParent(sortHeader);
        
        return sortHeader;
    }
    /**
     *
     */
    protected String getSortPropertyFromEL(UIComponent component)
    {        
        if (getVar() == null)
            return null;               
        
        for (Iterator iter = component.getChildren().iterator(); iter.hasNext();)
        {
            UIComponent aChild = (UIComponent) iter.next();            
            if (aChild.isRendered() && aChild instanceof UIOutput)
            {
                UIOutput output = (UIOutput)aChild;
                
                ValueBinding vb = output.getValueBinding("value");                 
                if (vb != null)
                {
                    String expressionString = vb.getExpressionString();
                    
                    int varIndex = expressionString.indexOf(getVar()+".");
                    if (varIndex != -1)
                    {
                        int varEndIndex = varIndex + getVar().length();
                        String tempProp = expressionString.substring(varEndIndex + 1, expressionString.length()); 
                        
                        StringTokenizer tokenizer = new StringTokenizer(tempProp, " +[]{}-/*|?:&<>!=()%");
                        if (tokenizer.hasMoreTokens())                        
                            return tokenizer.nextToken();                                                    
                    }
                }
            }
            else return getSortPropertyFromEL(aChild);
        }
        
        return null;
    }
    /**
     * @return the index coresponding to the given column name.
     */
    protected int columnNameToIndex(String columnName)
    {        
        int index = 0;
        for (Iterator iter = getChildren().iterator(); iter.hasNext();)
        {
            UIComponent aChild = (UIComponent)iter.next();              
            if (aChild instanceof UIColumn)
            {
                UIComponent headerFacet = ((UIColumn)aChild).getHeader();
                if (headerFacet != null && headerFacet instanceof HtmlCommandSortHeader)
                {
                    HtmlCommandSortHeader sortHeader = (HtmlCommandSortHeader)headerFacet;                    
                    if (columnName != null && columnName.equals(sortHeader.getColumnName()))
                        return index;
                }                 
            }
            
            index += 1;
        }
        
        return -1;
    }
    
    /**
     * @see javax.faces.component.UIData#encodeEnd(javax.faces.context.FacesContext)
     */
    public void encodeEnd(FacesContext context) throws IOException
    {
        super.encodeEnd(context);
        for (Iterator iter = getChildren().iterator(); iter.hasNext();)
        {
            UIComponent component = (UIComponent) iter.next();
            if (component instanceof UIColumns)
            {
                ((UIColumns) component).encodeTableEnd(context);
            }
        }
    }

    public int getFirst()
    {
        if (_preservedDataModel != null)
        {
            //Rather get the currently restored DataModel attribute
            return _preservedDataModel.getFirst();
        }
        else
        {
            return super.getFirst();
        }
    }

    public void setFirst(int first)
    {
        if (_preservedDataModel != null)
        {
            //Also change the currently restored DataModel attribute
            _preservedDataModel.setFirst(first);
        }
        super.setFirst(first);
    }

    public int getRows()
    {
        if (_preservedDataModel != null)
        {
            //Rather get the currently restored DataModel attribute
            return _preservedDataModel.getRows();
        }
        else
        {
            return super.getRows();
        }
    }

    public void setRows(int rows)
    {
        if (_preservedDataModel != null)
        {
            //Also change the currently restored DataModel attribute
            _preservedDataModel.setRows(rows);
        }
        super.setRows(rows);
    }

    public Object saveState(FacesContext context)
    {
        boolean preserveSort = isPreserveSort();
        boolean sortable     = isSortable();
        
        Object values[] = new Object[33];
        values[0] = super.saveState(context);
        values[1] = _preserveDataModel;
        if (isPreserveDataModel())
        {
            values[2] = saveAttachedState(context, getSerializableDataModel());
        }
        else
        {
            values[2] = null;
        }
        values[3] = _preserveSort;
        values[4] = _forceIdIndexFormula;
        values[5] = _sortColumn;
        values[6] = _sortAscending;
        values[7] = _sortProperty;
        values[8] = _sortable;
        values[9] = _renderedIfEmpty;
        values[10] = _rowCountVar;
        values[11] = _rowIndexVar;

        values[12] = _rowOnClick;
        values[13] = _rowOnDblClick;
        values[14] = _rowOnMouseDown;
        values[15] = _rowOnMouseUp;
        values[16] = _rowOnMouseOver;
        values[17] = _rowOnMouseMove;
        values[18] = _rowOnMouseOut;
        values[19] = _rowOnKeyPress;
        values[20] = _rowOnKeyDown;
        values[21] = _rowOnKeyUp;

        values[22] = _rowStyleClass;
        values[23] = _rowStyle;

        values[24] = preserveSort ? getSortColumn() : null;
        values[25] = preserveSort ? Boolean.valueOf(isSortAscending()) : null;
        
        values[26] = _varDetailToggler;
        values[27] = _expandedNodes;
        values[28] = _rowGroupStyle;
        values[29] = _rowGroupStyleClass;
        values[30] = _sortedColumnVar;
        values[31] = new Integer(_sortColumnIndex);        

        values[32] = new Integer(_newspaperColumns);

        return values;
    }

    /**
     * @see org.apache.myfaces.component.html.ext.HtmlDataTableHack#getDataModel()
     */
    protected DataModel getDataModel()
    {        
        if (_preservedDataModel != null)
        {
            setDataModel(_preservedDataModel);
            _preservedDataModel = null;
        }
        
        return super.getDataModel();               
    }
    /**
     * @see org.apache.myfaces.component.html.ext.HtmlDataTableHack#createDataModel()
     */
    protected DataModel createDataModel()
    {
        DataModel dataModel = super.createDataModel();               
                         
        boolean isSortable = isSortable();
        
        if (!(dataModel instanceof SortableModel))
        {                                
            //if sortable=true make each column sortable
            //if sortable=false, check to see if at least one column sortable                       
            for (Iterator iter = getChildren().iterator(); iter.hasNext();)
            {
                UIComponent component = (UIComponent) iter.next();
                if (component instanceof HtmlSimpleColumn)
                {
                    HtmlSimpleColumn aColumn = (HtmlSimpleColumn)component;
                    if (isSortable())
                        aColumn.setSortable(true);
                    
                    if (aColumn.isSortable())
                        isSortable = true;
                    
                    if (aColumn.isDefaultSorted() && getSortColumn() == null)
                        setSortProperty(aColumn.getSortPropertyName());
                }
            }
                    
            if (isSortable)
                dataModel = new SortableModel(dataModel);                            
        }
        
        if (isSortable && getSortProperty() != null)
        {         
            SortCriterion criterion = new SortCriterion(getSortProperty(), isSortAscending());
            List criteria = new ArrayList();
            criteria.add(criterion);

            ((SortableModel)dataModel).setSortCriteria(criteria);
        }        
                
        return dataModel;
    }       

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _preserveDataModel = (Boolean) values[1];
        if (isPreserveDataModel())
        {
            _preservedDataModel = (_SerializableDataModel) restoreAttachedState(context, values[2]);
        }
        else
        {
            _preservedDataModel = null;
        }
        _preserveSort = (Boolean) values[3];
        _forceIdIndexFormula = (String) values[4];
        _sortColumn = (String) values[5];
        _sortAscending = (Boolean) values[6];
        _sortProperty = (String) values[7];        
        _sortable = (Boolean) values[8];
        _renderedIfEmpty = (Boolean) values[9];
        _rowCountVar = (String) values[10];
        _rowIndexVar = (String) values[11];

        _rowOnClick = (String) values[12];
        _rowOnDblClick = (String) values[13];
        _rowOnMouseDown = (String) values[14];
        _rowOnMouseUp = (String) values[15];
        _rowOnMouseOver = (String) values[16];
        _rowOnMouseMove = (String) values[17];
        _rowOnMouseOut = (String) values[18];
        _rowOnKeyPress = (String) values[19];
        _rowOnKeyDown = (String) values[20];
        _rowOnKeyUp = (String) values[21];

        _rowStyleClass = (String) values[22];
        _rowStyle = (String) values[23];

        if (isPreserveSort())
        {
            String sortColumn = (String) values[24];
            Boolean sortAscending = (Boolean) values[25];
            if (sortColumn != null && sortAscending != null)
            {
                ValueBinding vb = getValueBinding("sortColumn");
                if (vb != null && !vb.isReadOnly(context))
                {
                    vb.setValue(context, sortColumn);
                }

                vb = getValueBinding("sortAscending");
                if (vb != null && !vb.isReadOnly(context))
                {
                    vb.setValue(context, sortAscending);
                }
            }
        }
        
        _varDetailToggler = (String)values[26];
        _expandedNodes = (Set)values[27];
        _rowGroupStyle = (String)values[28];
        _rowGroupStyleClass = (String)values[29];
        _sortedColumnVar = (String)values[30];
        _sortColumnIndex = values[31] != null ? ((Integer)values[31]).intValue() : -1;
        _newspaperColumns = ((Integer)values[32]).intValue();
    }

    public _SerializableDataModel getSerializableDataModel()
    {
        DataModel dm = getDataModel();
        if (dm instanceof _SerializableDataModel)
        {
            return (_SerializableDataModel) dm;
        }
        return createSerializableDataModel();
    }

    /**
     * @return _SerializableDataModel
     */
    private _SerializableDataModel createSerializableDataModel()
    {
        Object value = getValue();
        if (value == null)
        {
            return null;
        }
        else if (value instanceof DataModel)
        {
            return new _SerializableDataModel(getFirst(), getRows(), (DataModel) value);
        }
        else if (value instanceof List)
        {
            return new _SerializableListDataModel(getFirst(), getRows(), (List) value);
        }
        // accept a Collection is not supported in the Spec
        else if (value instanceof Collection)
        {
            return new _SerializableListDataModel(getFirst(), getRows(), new ArrayList((Collection) value));
        }
        else if (OBJECT_ARRAY_CLASS.isAssignableFrom(value.getClass()))
        {
            return new _SerializableArrayDataModel(getFirst(), getRows(), (Object[]) value);
        }
        else if (value instanceof ResultSet)
        {
            return new _SerializableResultSetDataModel(getFirst(), getRows(), (ResultSet) value);
        }
        else if (value instanceof javax.servlet.jsp.jstl.sql.Result)
        {
            return new _SerializableResultDataModel(getFirst(), getRows(),
                                                    (javax.servlet.jsp.jstl.sql.Result) value);
        }
        else
        {
            return new _SerializableScalarDataModel(getFirst(), getRows(), value);
        }
    }

    public boolean isRendered()
    {
        if (!UserRoleUtils.isVisibleOnUserRole(this))
            return false;
        return super.isRendered();
    }

    public void setForceIdIndexFormula(String forceIdIndexFormula)
    {
        _forceIdIndexFormula = forceIdIndexFormula;
        ValueBinding vb = getValueBinding("forceIdIndexFormula");
        if (vb != null)
        {
            vb.setValue(getFacesContext(), _forceIdIndexFormula);
            _forceIdIndexFormula = null;
        }
    }

    public String getForceIdIndexFormula()
    {
        if (_forceIdIndexFormula != null)
            return _forceIdIndexFormula;
        ValueBinding vb = getValueBinding("forceIdIndexFormula");
        if( vb == null )
            return null;
        Object eval = vb.getValue(getFacesContext());
        return eval == null ? null : eval.toString();
    }

    /**
     * Specify what column the data should be sorted on.
     * <p>
     * Note that calling this method <i>immediately</i> stores the value
     * via any value-binding with name "sortColumn". This is done because
     * this method is called by the HtmlCommandSortHeader component when
     * the user has clicked on a column's sort header. In this case, the
     * the model getter method mapped for name "value" needs to read this
     * value in able to return the data in the desired order - but the
     * HtmlCommandSortHeader component is usually "immediate" in order to
     * avoid validating the enclosing form. Yes, this is rather hacky -
     * but it works.
     */
    public void setSortColumn(String sortColumn)
    {        
        _sortColumn = sortColumn;
        // update model is necessary here, because processUpdates is never called
        // reason: HtmlCommandSortHeader.isImmediate() == true
        ValueBinding vb = getValueBinding("sortColumn");
        if (vb != null)
        {
            vb.setValue(getFacesContext(), _sortColumn);
            _sortColumn = null;
        }       
        
        setSortColumnIndex(columnNameToIndex(sortColumn));
    }

    public String getSortColumn()
    {
        if (_sortColumn != null) return _sortColumn;        
        ValueBinding vb = getValueBinding("sortColumn");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setSortAscending(boolean sortAscending)
    {
        _sortAscending = Boolean.valueOf(sortAscending);
        // update model is necessary here, because processUpdates is never called
        // reason: HtmlCommandSortHeader.isImmediate() == true
        ValueBinding vb = getValueBinding("sortAscending");
        if (vb != null)
        {
            vb.setValue(getFacesContext(), _sortAscending);
            _sortAscending = null;
        }
    }

    public boolean isSortAscending()
    {
        if (_sortAscending != null)
            return _sortAscending.booleanValue();
        ValueBinding vb = getValueBinding("sortAscending");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_SORTASCENDING;
    }
    
    public void setSortProperty(String sortProperty)
    {        
        _sortProperty = sortProperty;           
    }

    public String getSortProperty()
    {
        return _sortProperty;
    }

    public void setSortable(boolean sortable)
    {
         _sortable = sortable ? Boolean.TRUE : Boolean.FALSE;        
    }

    public boolean isSortable()
    {        
        if (_sortable != null) return _sortable.booleanValue();
        ValueBinding vb = getValueBinding("sortable");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;               
        return v != null ? v.booleanValue() : DEFAULT_SORTABLE;
    }
    
    public void setRowOnMouseOver(String rowOnMouseOver)
    {
        _rowOnMouseOver = rowOnMouseOver;
    }

    public String getRowOnMouseOver()
    {
        if (_rowOnMouseOver != null)
            return _rowOnMouseOver;
        ValueBinding vb = getValueBinding("rowOnMouseOver");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setRowOnMouseOut(String rowOnMouseOut)
    {
        _rowOnMouseOut = rowOnMouseOut;
    }

    public String getRowOnMouseOut()
    {
        if (_rowOnMouseOut != null)
            return _rowOnMouseOut;
        ValueBinding vb = getValueBinding("rowOnMouseOut");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setRowOnClick(String rowOnClick)
    {
        _rowOnClick = rowOnClick;
    }

    public String getRowOnClick()
    {
        if (_rowOnClick != null)
            return _rowOnClick;
        ValueBinding vb = getValueBinding("rowOnClick");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setRowOnDblClick(String rowOnDblClick)
    {
        _rowOnDblClick = rowOnDblClick;
    }

    public String getRowOnDblClick()
    {
        if (_rowOnDblClick != null)
            return _rowOnDblClick;
        ValueBinding vb = getValueBinding("rowOnDblClick");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public String getRowOnKeyDown()
    {
        if (_rowOnKeyDown != null)
            return _rowOnKeyDown;
        ValueBinding vb = getValueBinding("rowOnKeyDown");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

  public void setRowOnKeyDown(String rowOnKeyDown)
  {
    _rowOnKeyDown = rowOnKeyDown;
  }

  public String getRowOnKeyPress()
  {
    if (_rowOnKeyPress != null)
      return _rowOnKeyPress;
    ValueBinding vb = getValueBinding("rowOnKeyPress");
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
  }

  public void setRowOnKeyPress(String rowOnKeyPress)
  {
    _rowOnKeyPress = rowOnKeyPress;
  }

  public String getRowOnKeyUp()
  {
    if (_rowOnKeyUp != null)
      return _rowOnKeyUp;
    ValueBinding vb = getValueBinding("rowOnKeyUp");
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
  }

  public void setRowOnKeyUp(String rowOnKeyUp)
  {
    _rowOnKeyUp = rowOnKeyUp;
  }

  public String getRowStyleClass()
  {
    if (_rowStyleClass != null)
      return _rowStyleClass;
    ValueBinding vb = getValueBinding(JSFAttr.ROW_STYLECLASS_ATTR);
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
  }

  public void setRowStyleClass(String rowStyleClass)
  {
    _rowStyleClass = rowStyleClass;
  }

  public String getRowStyle()
  {
    if (_rowStyle != null)
      return _rowStyle;
    ValueBinding vb = getValueBinding(JSFAttr.ROW_STYLE_ATTR);
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
  }

  public void setRowStyle(String rowStyle)
  {
    _rowStyle = rowStyle;
  }

    public String getRowOnMouseDown()
    {
        if (_rowOnMouseDown != null)
            return _rowOnMouseDown;
        ValueBinding vb = getValueBinding("rowOnMouseDown");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setRowOnMouseDown(String rowOnMouseDown)
    {
        _rowOnMouseDown = rowOnMouseDown;
    }

    public String getRowOnMouseMove()
    {
        if (_rowOnMouseMove != null)
            return _rowOnMouseMove;
        ValueBinding vb = getValueBinding("rowOnMouseMove");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setRowOnMouseMove(String rowOnMouseMove)
    {
        _rowOnMouseMove = rowOnMouseMove;
    }

    public String getRowOnMouseUp()
    {
        if (_rowOnMouseUp != null)
            return _rowOnMouseUp;
        ValueBinding vb = getValueBinding("rowOnMouseUp");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setRowOnMouseUp(String rowOnMouseUp)
    {
        _rowOnMouseUp = rowOnMouseUp;
    }

    protected boolean isValidChildren() {
        return _isValidChildren;
    }

    protected void setIsValidChildren(boolean isValidChildren) {
        _isValidChildren = isValidChildren;
    }

    protected _SerializableDataModel getPreservedDataModel() {
        return _preservedDataModel;
    }

    protected void setPreservedDataModel(_SerializableDataModel preservedDataModel) {
        _preservedDataModel = preservedDataModel;
    }


    public boolean isCurrentDetailExpanded(){
        return _expandedNodes.contains(new Integer(getRowIndex()));
    }

    public void setVarDetailToggler(String varDetailToggler)
    {
        _varDetailToggler = varDetailToggler;
    }

    public String getVarDetailToggler()
    {
        return _varDetailToggler;
    }

    public String getRowGroupStyle()
    {
        if (_rowGroupStyle != null)
      return _rowGroupStyle;
    ValueBinding vb = getValueBinding("rowGroupStyle");
    return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setRowGroupStyle(String rowGroupStyle)
    {
        _rowGroupStyle = rowGroupStyle;
    }

    public String getRowGroupStyleClass()
    {
        return _rowGroupStyleClass;
    }

    public void setRowGroupStyleClass(String rowGroupStyleClass)
    {
        _rowGroupStyleClass = rowGroupStyleClass;
    }

    public HtmlDataTable()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    /**
     * Change the status of the current detail row from collapsed to expanded or
     * viceversa.
     *
     */
    public void toggleDetail()
    {
        Integer rowIndex = new Integer(getRowIndex());

        if(_expandedNodes.contains(rowIndex))
        {
            _expandedNodes.remove(rowIndex);
        } 
        else 
        {
            _expandedNodes.add(rowIndex);
        }
    }

    /**
     * Return true if the current detail row is expanded.
     *
     * @return true if the current detail row is expanded.
     */
    public boolean isDetailExpanded()
    {
        Integer rowIndex = new Integer(getRowIndex());

        return _expandedNodes.contains(rowIndex);
    }

    public int getSortColumnIndex()
    {
        if (_sortColumnIndex == -1)
            _sortColumnIndex = columnNameToIndex(getSortColumn());
        
        return _sortColumnIndex;
    }
    
    public void setSortColumnIndex(int sortColumnIndex)
    {        
        _sortColumnIndex = sortColumnIndex;               
    }       
    
    /**
     * Set the number of columns the table will be divided over.
     */
    public int getNewspaperColumns() {
        return _newspaperColumns;
    }
    public void setNewspaperColumns(int newspaperColumns) {
        this._newspaperColumns = newspaperColumns;
    }
    
    /**
     * Gets the spacer facet, between each pair of newspaper columns.
     */
    public UIComponent getSpacer() {
        return (UIComponent)getFacets().get(SPACER_FACET_NAME);
    }
    public void setSpacer(UIComponent spacer) {
        getFacets().put(SPACER_FACET_NAME, spacer);
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "org.apache.myfaces.HtmlDataTable";
    public static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.Table";

    private static final boolean DEFAULT_PRESERVEDATAMODEL = false;
    private static final boolean DEFAULT_PRESERVESORT = true;
    private static final boolean DEFAULT_RENDEREDIFEMPTY = true;

    private Boolean _preserveDataModel = null;
    private Boolean _preserveSort = null;
    private String _enabledOnUserRole = null;
    private String _visibleOnUserRole = null;
    private Boolean _renderedIfEmpty = null;
    private String _rowIndexVar = null;
    private String _rowCountVar = null;
    private String _sortedColumnVar = null;
    private String _previousRowDataVar = null;

    public void setPreserveDataModel(boolean preserveDataModel)
    {
        _preserveDataModel = Boolean.valueOf(preserveDataModel);
    }

    public boolean isPreserveDataModel()
    {
        if (_preserveDataModel != null)
            return _preserveDataModel.booleanValue();
        ValueBinding vb = getValueBinding("preserveDataModel");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_PRESERVEDATAMODEL;
    }

    public void setPreserveSort(boolean preserveSort)
    {
        _preserveSort = Boolean.valueOf(preserveSort);
    }

    public boolean isPreserveSort()
    {
        if (_preserveSort != null)
            return _preserveSort.booleanValue();
        ValueBinding vb = getValueBinding("preserveSort");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_PRESERVESORT;
    }

    public void setEnabledOnUserRole(String enabledOnUserRole)
    {
        _enabledOnUserRole = enabledOnUserRole;
    }

    public String getEnabledOnUserRole()
    {
        if (_enabledOnUserRole != null)
            return _enabledOnUserRole;
        ValueBinding vb = getValueBinding("enabledOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setVisibleOnUserRole(String visibleOnUserRole)
    {
        _visibleOnUserRole = visibleOnUserRole;
    }

    public String getVisibleOnUserRole()
    {
        if (_visibleOnUserRole != null)
            return _visibleOnUserRole;
        ValueBinding vb = getValueBinding("visibleOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setRenderedIfEmpty(boolean renderedIfEmpty)
    {
        _renderedIfEmpty = Boolean.valueOf(renderedIfEmpty);
    }

    public boolean isRenderedIfEmpty()
    {
        if (_renderedIfEmpty != null)
            return _renderedIfEmpty.booleanValue();
        ValueBinding vb = getValueBinding("renderedIfEmpty");
        Boolean v = vb != null ? (Boolean) vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_RENDEREDIFEMPTY;
    }

    public void setRowIndexVar(String rowIndexVar)
    {
        _rowIndexVar = rowIndexVar;
    }

    public String getRowIndexVar()
    {
        if (_rowIndexVar != null)
            return _rowIndexVar;
        ValueBinding vb = getValueBinding("rowIndexVar");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setRowCountVar(String rowCountVar)
    {
        _rowCountVar = rowCountVar;
    }

    public String getRowCountVar()
    {
        if (_rowCountVar != null)
            return _rowCountVar;
        ValueBinding vb = getValueBinding("rowCountVar");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    public void setPreviousRowDataVar(String previousRowDataVar)
    {
        _previousRowDataVar = previousRowDataVar;
    }

    public String getPreviousRowDataVar()
    {
        if (_previousRowDataVar != null)
            return _previousRowDataVar;
        ValueBinding vb = getValueBinding("previousRowDataVar");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }      
    
    public void setSortedColumnVar(String sortedColumnVar)
    {
        _sortedColumnVar = sortedColumnVar;
    }

    public String getSortedColumnVar()
    {
        if (_sortedColumnVar != null) return _sortedColumnVar;
        ValueBinding vb = getValueBinding("sortedColumnVar");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    //------------------ GENERATED CODE END ---------------------------------------
}
