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
package org.apache.myfaces.custom.tree2;

import javax.faces.component.NamingContainer;
import java.io.Serializable;


/**
 * Model class for the tree component.  It provides random access to nodes in a tree
 * made up of instances of the {@link TreeNode} class.
 *
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */
public interface TreeModel extends Serializable
{
    /**
     * Separator char to be used in node path generation.
     */
    public final static String SEPARATOR = String.valueOf(NamingContainer.SEPARATOR_CHAR);
    
    /**
     * Gets the current {@link TreeNode} or <code>null</code> if no node ID is selected.
     * @return The current node
     */
    public TreeNode getNode();

    /**
     * Sets the current {@link TreeNode} to the specified node ID.
     * @param nodeId The id of the node to set
     */
    public void setNodeId(String nodeId);

    /**
     * Gets an array of String containing the ID's of all of the {@link TreeNode}s in the path to
     * the specified node.  The path information will be an array of <code>String</code> objects
     * representing node ID's. The array will starting with the ID of the root node and end with
     * the ID of the specified node.
     *
     * @param nodeId The id of the node for whom the path information is needed.
     * @return String[]
     */
    public String[] getPathInformation(String nodeId);

    /**
     * Indicates whether or not the specified {@link TreeNode} is the last child in the <code>List</code>
     * of children.  If the node id provided corresponds to the root node, this returns <code>true</code>.
     *
     * @param nodeId The ID of the node to check
     * @return boolean
     */
    public boolean isLastChild(String nodeId);

    
    public void setTreeState(TreeState state);
    public TreeState getTreeState();
    
    
    
}
