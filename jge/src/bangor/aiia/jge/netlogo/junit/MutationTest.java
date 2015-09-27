/**
 * Project: jGE NetLogo
 * Author:  Loukas Georgiou
 * Date:	19 March 2008
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

import bangor.aiia.jge.netlogo.Mutation;
import junit.framework.TestCase;

/**
 * The class <code>MutationTest</code> is a JUnit Test Case
 * for the <code>bangor.aiia.jge.netlogo.Mutation</code> class.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 19/03/08
 * @see 	Mutation
 * @since	jGE NetLogo 1.0
 */
public class MutationTest extends TestCase {
	
	private Mutation mutation;
	private Syntax syntax;

	public MutationTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mutation = new Mutation();
		syntax = mutation.getSyntax();	
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.Mutation.getSyntax()'
	 */
	public void testGetSyntax() {
		assertTrue(syntax.getRet() == Syntax.TYPE_STRING);
		assertTrue(syntax.getRight().length == 2);
		assertTrue(syntax.getRight()[0] == Syntax.TYPE_STRING);
		assertTrue(syntax.getRight()[1] == Syntax.TYPE_NUMBER);
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.Mutation.report(Argument[] args, Context context)'
	 */
	public void testReport() {

Exception e = null;
		
		// Create and Open Workspace
		HeadlessWorkspace workspace = HeadlessWorkspace.newInstance();		
        try {workspace.open(AllTests.EmpltyModelPath);}            
        catch(Exception ex) {ex.printStackTrace();}    
        
        
        try {
        	String offspring = (String) workspace.report("jge:mutation \"00000000000000000000\" 1");
       		assertTrue(offspring.indexOf("1") > -1);
       		assertTrue(offspring.indexOf("0") == -1);
       		
       		offspring = (String) workspace.report("jge:mutation \"00000000000000000000\" 18");
       		assertTrue(offspring.indexOf("1") > -1);
       		assertTrue(offspring.indexOf("0") == -1);
       		
       		offspring = (String) workspace.report("jge:mutation \"00000000000000000000\" 0");
       		assertTrue(offspring.indexOf("1") == -1);
       		assertTrue(offspring.indexOf("0") > -1);  
       		
       		offspring = (String) workspace.report("jge:mutation \"00000000000000000000\" -5");
       		assertTrue(offspring.indexOf("1") == -1);
       		assertTrue(offspring.indexOf("0") > -1); 
       		
       		offspring = (String) workspace.report("jge:mutation \"0000000000000000000000000000000000000000\" 0.5");
       		assertTrue(offspring.indexOf("1") > -1);
       		assertTrue(offspring.indexOf("0") > -1); 
       		
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNull(e);
        
        try {
        	workspace.report("jge:mutation \"\" 0.5");      	
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Extension exception: Offspring must be a non-empty binary string");
        e = null;
        
        try {
        	workspace.report("jge:mutation \"222222\" 1");      	
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Input was not binary ('0' or '1')");
        e = null;

        try {
        	workspace.report("jge:mutation \"aaaaaa\" 1");      	
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Input was not binary ('0' or '1')");
        e = null;
        
       
        
        // Dispose Workspace
        try {workspace.dispose();}
        catch(Exception ex) {ex.printStackTrace();} 
		
		
	}

}
