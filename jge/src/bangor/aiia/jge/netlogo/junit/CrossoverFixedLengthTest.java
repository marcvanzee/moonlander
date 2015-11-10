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

import org.nlogo.api.LogoList;
import org.nlogo.api.Syntax;
import org.nlogo.headless.HeadlessWorkspace;

import bangor.aiia.jge.netlogo.CrossoverFixedLength;
import junit.framework.TestCase;

/**
 * The class <code>CrossoverFixedLengthTest</code> is a JUnit Test Case
 * for the <code>bangor.aiia.jge.netlogo.CrossoverFixedLength</code> class.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 19/07/08
 * @see 	CrossoverFixedLength
 * @since	jGE NetLogo 2.0
 */
public class CrossoverFixedLengthTest extends TestCase {
	
	private CrossoverFixedLength crossover;
	private Syntax syntax;

	public CrossoverFixedLengthTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		crossover = new CrossoverFixedLength();
		syntax = crossover.getSyntax();		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.CrossoverFixedLength.getSyntax()'
	 */
	public void testGetSyntax() {
		assertTrue(syntax.getRet() == Syntax.TYPE_LIST);
		assertTrue(syntax.getRight().length == 3);
		assertTrue(syntax.getRight()[0] == Syntax.TYPE_STRING);
		assertTrue(syntax.getRight()[1] == Syntax.TYPE_STRING);	
		assertTrue(syntax.getRight()[2] == Syntax.TYPE_NUMBER);
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.CrossoverFixedLength.report(Argument[] args, Context context)'
	 */
	public void testReport() {

		Exception e = null;
		
		// Create and Open Workspace
		HeadlessWorkspace workspace = HeadlessWorkspace.newInstance();		
        try {workspace.open(AllTests.EmpltyModelPath);}            
        catch(Exception ex) {ex.printStackTrace();}    
        
        
        try {
        	LogoList offspring = (LogoList) workspace.report("jge:crossover-fixed-length \"00000000000000000000000000000000000000000000000000000000000000000000000000000000\" \"11111111111111111111111111111111111111111111111111111111111111111111111111111111\" 1");
        	assertTrue(offspring.size() == 2);
       		assertTrue(offspring.get(0).toString().indexOf("1") > -1);
       		assertTrue(offspring.get(1).toString().indexOf("0") > -1);
       		
       		offspring = (LogoList) workspace.report("jge:crossover-fixed-length \"00000000000000000000000000000000000000000000000000000000000000000000000000000000\" \"11111111111111111111111111111111111111111111111111111111111111111111111111111111\" 0");
        	assertTrue(offspring.size() == 2);
       		assertTrue(offspring.get(0).toString().indexOf("1") == -1);
       		assertTrue(offspring.get(1).toString().indexOf("0") == -1);  
       		
       		offspring = (LogoList) workspace.report("jge:crossover-fixed-length \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\" \"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\" 10");
        	assertTrue(offspring.size() == 2);
       		assertTrue(offspring.get(0).toString().indexOf("b") > -1);
       		assertTrue(offspring.get(1).toString().indexOf("a") > -1);
       		
       		offspring = (LogoList) workspace.report("jge:crossover-fixed-length \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\" \"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\" -5");
        	assertTrue(offspring.size() == 2);
       		assertTrue(offspring.get(0).toString().indexOf("b") == -1);
       		assertTrue(offspring.get(1).toString().indexOf("a") == -1);       		
       		
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNull(e);
        
        try {
        	workspace.report("jge:crossover-fixed-length \"\" \"1111\" 0.5");      	
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Extension exception: ParentA must be a non-empty binary string");
        e = null;
        
        try {
        	workspace.report("jge:crossover-fixed-length \"1111\" \"\" 0.5");      	
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Extension exception: ParentB must be a non-empty binary string");
        e = null;
        
        try {
        	workspace.report("jge:crossover-fixed-length \"0000\" \"111111\" 0.5");      	
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Extension exception: ParentA and ParentB must have the same length");
        e = null;
        
        
        // Dispose Workspace
        try {workspace.dispose();}
        catch(Exception ex) {ex.printStackTrace();} 		
		
	}

}
