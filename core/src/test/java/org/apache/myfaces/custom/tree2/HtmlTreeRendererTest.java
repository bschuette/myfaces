/*
 * Copyright 2006 The Apache Software Foundation.
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

import org.apache.myfaces.renderkit.JSFAttr;

import junit.framework.Test;
import junit.framework.TestSuite;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Stack;

/**
 * - .. Root (0)
 * |
 * - .. A (0:0)
 * |    |
 * |    + .. AA (0:0:0)
 * |    |
 * |    + .. AB (0:0:1)
 * |    |
 * |    + .. AC (0:0:2)
 * |         | .. aca (0:0:2:0)
 * |         | .. acb (0:0:2:1)
 * |
 * + .. B (0:1)
 * |    |
 * |    + .. BA (0:1:0)
 * |    |
 * |    + .. BB (0:1:1)
 * |
 * | .. C (0:2)
 * |
 * | .. d (0:3)
 */

public class HtmlTreeRendererTest extends AbstractTreeTestCase
{
    private NodeSimulator nodeSim;

    /**
     * Constructor
     * @param name String
     */
    public HtmlTreeRendererTest(String name)
    {
        super(name);
    }

    /**
     * See abstract class
     */
    public void setUp()
    {
        super.setUp();

        nodeSim = new NodeSimulator();

        // the tree needs a default facet - use a node simulator instead of the usual components
        // so we can detect when a node is rendered
        tree.getFacets().put(DEFAULT_NODE_TYPE, nodeSim);

        // set up the renderer for the component.  with mock stuff we don't have faces config doing this for us
        renderKit.addRenderer(HtmlTree.COMPONENT_FAMILY, HtmlTree.COMPONENT_TYPE, new HtmlTreeRenderer());
    }

    /**
     * Tests the option of hiding the root node (with server side toggling option)
     * @throws Exception
     */
    public void testHideRootNodeServer() throws Exception
    {
        tree.getAttributes().put(JSFAttr.CLIENT_SIDE_TOGGLE, Boolean.FALSE);
        tree.getAttributes().put(JSFAttr.SHOW_ROOT_NODE, Boolean.FALSE);

        HtmlTreeRenderer treeRenderer = new HtmlTreeRenderer();
        treeRenderer.encodeChildren(facesContext, tree);

        // we expect the nodes to come off the stack in the reverse order in which they were added
        Stack nodeStack = nodeSim.getNodeStack();
        int numRendered = nodeStack.size();
        assertEquals("Unexpected number of nodes rendered", 4, numRendered);

        TreeNode node = (TreeNode)nodeStack.pop();
        assertEquals("d", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("C", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("B", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("A", node.getIdentifier());
    }

    /**
     * Tests the option of hiding the root node (with client side toggling option)
     * @throws Exception
     */
    public void testHideRootNodeClient() throws Exception
    {
        tree.getAttributes().put(JSFAttr.CLIENT_SIDE_TOGGLE, Boolean.TRUE);
        tree.getAttributes().put(JSFAttr.SHOW_ROOT_NODE, Boolean.FALSE);

        HtmlTreeRenderer treeRenderer = new HtmlTreeRenderer();
        treeRenderer.encodeChildren(facesContext, tree);

        // we expect the nodes to come off the stack in the reverse order in which they were added
        Stack nodeStack = nodeSim.getNodeStack();
        int numRendered = nodeStack.size();
        assertEquals("Unexpected number of nodes rendered", 11, numRendered);

        TreeNode node = (TreeNode)nodeStack.pop();
        assertEquals("d", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("C", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("BB", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("BA", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("B", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("acb", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("aca", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("AC", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("AB", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("AA", node.getIdentifier());

        node = (TreeNode)nodeStack.pop();
        assertEquals("A", node.getIdentifier());
    }

    public static Test suite()
    {
        return new TestSuite(HtmlTreeRendererTest.class);
    }
}