/**
 * Project: jGE NetLogo
 * Author:  Loukas Georgiou
 * Date:	29 March 2008
 * 
 * Copyright 2008 Loukas Georgiou.
 * This file is part of jGE NetLogo extension.
 * 
 * jGE NetLogo extension is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * jGE NetLogo extension is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with jGE NetLogo extension.  If not, see <http://www.gnu.org/licenses/>.
 */ 
 
package bangor.aiia.jge.netlogo.junit;

import org.nlogo.api.Syntax;
import org.nlogo.headless.HeadlessWorkspace;
import bangor.aiia.jge.netlogo.Binary;
import junit.framework.TestCase;

/**
 * The class <code>BinaryTest</code> is a JUnit Test Case
 * for the <code>bangor.aiia.jge.netlogo.Binary</code> class.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 29/03/08
 * @see 	Binary
 * @since	jGE NetLogo 2.0
 */
public class BinaryTest extends TestCase {

	private Binary binary;
	private Syntax syntax;
	
	public BinaryTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		binary = new Binary();
		syntax = binary.getSyntax();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.Binary.getSyntax()'
	 */
	public void testGetSyntax() {		
		assertTrue(syntax.getRet() == Syntax.TYPE_BOOLEAN);
		assertTrue(syntax.getRight().length == 1);
		assertTrue(syntax.getRight()[0] == Syntax.TYPE_STRING);		
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.Binary.report(Argument[] args, Context context)'
	 */
	public void testReport() {		

		Exception e = null;
		
		// Create and Open Workspace
		HeadlessWorkspace workspace = HeadlessWorkspace.newInstance();		
        try {workspace.open(AllTests.EmpltyModelPath);}            
        catch(Exception ex) {ex.printStackTrace();}    
        
        
        try {        	
       		assertEquals((Boolean) workspace.report("jge:is-binary? \"01010101\""), new Boolean(true));
       		assertEquals((Boolean) workspace.report("jge:is-binary? \"\""), new Boolean(false));
       		assertEquals((Boolean) workspace.report("jge:is-binary? \" \""), new Boolean(false));
       		assertEquals((Boolean) workspace.report("jge:is-binary? \"aaaabbbb\""), new Boolean(false));
       		assertEquals((Boolean) workspace.report("jge:is-binary? \"232323232\""), new Boolean(false));
       		assertEquals((Boolean) workspace.report("jge:is-binary? \"a010101010\""), new Boolean(false));
       		assertEquals((Boolean) workspace.report("jge:is-binary? \"010101010a\""), new Boolean(false));
       		assertEquals((Boolean) workspace.report("jge:is-binary? \"2010101010\""), new Boolean(false));
       		assertEquals((Boolean) workspace.report("jge:is-binary? \"0101010102\""), new Boolean(false));
       		assertEquals((Boolean) workspace.report("jge:is-binary? \"1111111111\""), new Boolean(true));
       		assertEquals((Boolean) workspace.report("jge:is-binary? \"0000000000\""), new Boolean(true));
        	        	
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNull(e);       
        
            
        // Dispose Workspace
        try {workspace.dispose();}
        catch(Exception ex) {ex.printStackTrace();}		
		
	}

}
