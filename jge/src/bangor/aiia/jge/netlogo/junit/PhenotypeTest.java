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

import bangor.aiia.jge.netlogo.Phenotype;
import junit.framework.TestCase;

/**
 * The class <code>PhenotypeTest</code> is a JUnit Test Case
 * for the <code>bangor.aiia.jge.netlogo.Phenotype</code> class.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 19/03/08
 * @see 	Phenotype
 * @since	jGE NetLogo 1.0
 */
public class PhenotypeTest extends TestCase {
	
	private Phenotype phenotype;
	private Syntax syntax;

	public PhenotypeTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		phenotype = new Phenotype();
		syntax = phenotype.getSyntax();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.Phenotype.getSyntax()'
	 */
	public void testGetSyntax() {
		assertTrue(syntax.getRet() == Syntax.TYPE_STRING);
		assertTrue(syntax.getRight().length == 4);
		assertTrue(syntax.getRight()[0] == Syntax.TYPE_STRING);
		assertTrue(syntax.getRight()[1] == Syntax.TYPE_STRING);	
		assertTrue(syntax.getRight()[2] == Syntax.TYPE_NUMBER);
		assertTrue(syntax.getRight()[3] == Syntax.TYPE_NUMBER);
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.Phenotype.report(Argument[] args, Context context)'
	 */
	public void testReport() {

		Exception e = null;

		// Create and Open Workspace
		HeadlessWorkspace workspace = HeadlessWorkspace.newInstance();		
        try {workspace.open(AllTests.EmpltyModelPath);}            
        catch(Exception ex) {ex.printStackTrace();}    
        
        
        try {
        	
        	workspace.command("jge:load-bnf \"" + AllTests.BNFInteger + "\" \"bnf-int\"");
        	
        	assertEquals((String)workspace.report("jge:phenotype \"0\" \"bnf-int\" 2 0"), "");
        	assertEquals((String)workspace.report("jge:phenotype \"01011111\" \"bnf-int\" 4 0"), "5");
        	assertEquals((String)workspace.report("jge:phenotype \"01011110\" \"bnf-int\" 4 0"), "4");
        	assertEquals((String)workspace.report("jge:phenotype \"010010100011000111111110\" \"bnf-int\" 4 0"), "154");
        	assertEquals((String)workspace.report("jge:phenotype \"01001010001100011111\" \"bnf-int\" 4 0"), "");
        	assertEquals((String)workspace.report("jge:phenotype \"1100101000110001\" \"bnf-int\" 4 1"), "120");
        	
        	assertEquals((String)workspace.report("jge:phenotype \"001001000110\" \"bnf-int\" 2 0"), "012");
        	assertEquals((String)workspace.report("jge:phenotype \"001001000110\" \"bnf-int\" 1 0"), "001");
        	assertEquals((String)workspace.report("jge:phenotype \"11\" \"bnf-int\" 1 0"), "1");
        	       		
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNull(e);
        
        try {
        	workspace.report("jge:phenotype \"000000\" \"bnf-int\" 0 2");
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Extension exception: Codonsize value must be greater than zero (0)");
        e = null;
        
        try {
        	workspace.report("jge:phenotype \"000000\" \"bnf-int\" 1 -1");
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Extension exception: MaxWraps value must be equal to or greater than zero (0)");
        e = null;
        
        try {
        	workspace.report("jge:phenotype \"000000\" \"bnf-int-new\" 1 1");
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertEquals(e.getMessage(), "Extension exception: There is no BNF Grammar in memory referenced by \"bnf-int-new\"");
        e = null;
       
              
        // Dispose Workspace
        try {workspace.dispose();}
        catch(Exception ex) {ex.printStackTrace();} 	
		
		
		
	}

}
