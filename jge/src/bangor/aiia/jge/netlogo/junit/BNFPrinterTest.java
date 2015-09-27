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

import bangor.aiia.jge.netlogo.BNFPrinter;
import junit.framework.TestCase;

/**
 * The class <code>BNFPrinterTest</code> is a JUnit Test Case
 * for the <code>bangor.aiia.jge.netlogo.BNFPrinter</code> class.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 19/03/08
 * @see 	BNFPrinter
 * @since	jGE NetLogo 1.0
 */
public class BNFPrinterTest extends TestCase {

	private BNFPrinter bnf;
	private Syntax syntax;
	
	public BNFPrinterTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		bnf = new BNFPrinter();
		syntax = bnf.getSyntax();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.BNFPrinter.getSyntax()'
	 */
	public void testGetSyntax() {
		assertTrue(syntax.getRet() == Syntax.TYPE_STRING);
		assertTrue(syntax.getRight().length == 1);
		assertTrue(syntax.getRight()[0] == Syntax.TYPE_STRING);
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.BNFPrinter.report(Argument[] args, Context context)'
	 */
	public void testReport() {

		Exception e = null;

		// Create and Open Workspace
		HeadlessWorkspace workspace = HeadlessWorkspace.newInstance();		
        try {workspace.open(AllTests.EmpltyModelPath);}            
        catch(Exception ex) {ex.printStackTrace();}    
        
        
        try {
        	workspace.command("jge:load-bnf \"" + AllTests.BNFInteger + "\" \"bnf-int\"");
        	
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-int\"")).contains("<Int>"));
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-int\"")).contains("<Digit>"));
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-int\"")).contains("<action>"));
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-int\"")).contains("<move>"));        	        	
        	       		
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNull(e);
        
        try {
        	workspace.report("jge:bnf-grammar \"bnf-action\"");
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertTrue(e.getMessage().contains("There is no BNF Grammar in memory referenced by "));
        e = null;
       
              
        // Dispose Workspace
        try {workspace.dispose();}
        catch(Exception ex) {ex.printStackTrace();} 	
		
		
		
	}

}
