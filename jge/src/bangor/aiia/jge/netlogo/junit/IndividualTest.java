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
import bangor.aiia.jge.netlogo.Individual;
import junit.framework.TestCase;

/**
 * The class <code>IndividualTest</code> is a JUnit Test Case
 * for the <code>bangor.aiia.jge.netlogo.Individual</code> class.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 19/03/08
 * @see 	Individual
 * @since	jGE NetLogo 1.0
 */
public class IndividualTest extends TestCase {

	private Individual individual;
	private Syntax syntax;
	
	public IndividualTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		individual = new Individual();
		syntax = individual.getSyntax();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.Individual.getSyntax()'
	 */
	public void testGetSyntax() {		
		assertTrue(syntax.getRet() == Syntax.TYPE_STRING);
		assertTrue(syntax.getRight().length == 3);
		assertTrue(syntax.getRight()[0] == Syntax.TYPE_NUMBER);
		assertTrue(syntax.getRight()[1] == Syntax.TYPE_NUMBER);
		assertTrue(syntax.getRight()[2] == Syntax.TYPE_NUMBER);
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.Individual.report(Argument[] args, Context context)'
	 */
	public void testReport() {		

		Exception e = null;
		
		// Create and Open Workspace
		HeadlessWorkspace workspace = HeadlessWorkspace.newInstance();		
        try {workspace.open(AllTests.EmpltyModelPath);}            
        catch(Exception ex) {ex.printStackTrace();}    
        
        
        try {
        	String individual = (String) workspace.report("jge:individual 2 3 3");
       		assertTrue(individual.length() == 6);
        	for (int i = 0; i < 20; i++) {
           		individual = (String) workspace.report("jge:individual 4 4 8");
           		assertTrue(individual.length() >= 16);
           		assertTrue(individual.length() <= 32);
        	}        	
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNull(e);
                        
        try {
        	workspace.report("jge:individual 0 3 3");      	
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Extension exception: Codonsize value must be greater than zero (0)");
        e = null;
        
        try {
        	workspace.report("jge:individual 2 0 3");      	
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Extension exception: Min value must be greater than zero (0)");
        e = null;

        try {
        	workspace.report("jge:individual 2 3 0");      	
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Extension exception: Max value must be greater than zero (0)");
        e = null;
        
        try {
        	workspace.report("jge:individual 2 3 2");      	
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Extension exception: Min value must be less or equal to max value");
        e = null;
        
            
        // Dispose Workspace
        try {workspace.dispose();}
        catch(Exception ex) {ex.printStackTrace();}		
		
	}

}
