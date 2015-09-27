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

import bangor.aiia.jge.netlogo.BNFLoader;



import junit.framework.TestCase;

/**
 * The class <code>BNFLoaderTest</code> is a JUnit Test Case
 * for the <code>bangor.aiia.jge.netlogo.BNFLoader</code> class.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 19/03/08
 * @see 	BNFLoader
 * @since	jGE NetLogo 1.0
 */
public class BNFLoaderTest extends TestCase {

	private BNFLoader bnf;
	private Syntax syntax;
	
	public BNFLoaderTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		bnf = new BNFLoader();
		syntax = bnf.getSyntax();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.BNFLoader.getSyntax()'
	 */
	public void testGetSyntax() {
		assertTrue(syntax.getRet() == Syntax.TYPE_VOID);
		assertTrue(syntax.getRight().length == 2);
		assertTrue(syntax.getRight()[0] == Syntax.TYPE_STRING);
		assertTrue(syntax.getRight()[1] == Syntax.TYPE_STRING);
	}

	/*
	 * Test method for 'bangor.aiia.jge.netlogo.BNFLoader.perform(Argument[] args, Context context)'
	 */
	public void testReport() {		
	
		Exception e = null;

		// Create and Open Workspace
		HeadlessWorkspace workspace = HeadlessWorkspace.newInstance() ;		
        try {workspace.open(AllTests.EmpltyModelPath);}            
        catch(Exception ex) {ex.printStackTrace();}    
        
        
        try {
        	workspace.command("jge:load-bnf \"" + AllTests.BNFInteger + "\" \"bnf-int\"");
        	workspace.command("jge:load-bnf \"" + AllTests.BNFNetLogo + "\" \"bnf-action\"");
        	
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-int\"")).contains("<Int>"));
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-int\"")).contains("<Digit>"));
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-int\"")).contains("<action>"));
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-int\"")).contains("<move>"));
        	        	
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-action\"")).contains("<action>"));
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-action\"")).contains("<move>"));
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-action\"")).contains("<Int>"));
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-action\"")).contains("<Digit>"));
        	
        	
        	workspace.command("jge:load-bnf \"" + AllTests.BNFInteger + "\" \"bnf-action\"");
        	
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-action\"")).contains("<Int>"));
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-action\"")).contains("<Digit>"));
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-action\"")).contains("<action>"));
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-action\"")).contains("<move>"));
        	
        	
        	workspace.command("jge:load-bnf \"" + AllTests.BNFInteger + "\" \"bnf-action\"");
        	workspace.command("jge:load-bnf \"" + AllTests.BNFNetLogo + "\" \"bnf-int\"");
        	
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-int\"")).contains("<Int>"));
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-int\"")).contains("<Digit>"));
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-int\"")).contains("<action>"));
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-int\"")).contains("<move>"));
        	        	
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-action\"")).contains("<action>"));
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-action\"")).contains("<move>"));
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-action\"")).contains("<Int>"));
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-action\"")).contains("<Digit>"));
        	
        	
        	workspace.command("jge:load-bnf \"" + AllTests.BNFNetLogo + "\" \"bnf-int\"");
        	workspace.command("jge:load-bnf \"" + AllTests.BNFNetLogo + "\" \"bnf-action\"");
        	
        	assertEquals(workspace.report("jge:bnf-grammar \"bnf-int\""), workspace.report("jge:bnf-grammar \"bnf-action\""));
        	        	
        	
        	workspace.command("jge:load-bnf \"jge/bnf/BNFInt.txt\" \"bnf-int-new\"");
        	
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-int-new\"")).contains("<Int>"));
        	assertTrue(((String)workspace.report("jge:bnf-grammar \"bnf-int-new\"")).contains("<Digit>"));
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-int-new\"")).contains("<action>"));
        	assertFalse(((String)workspace.report("jge:bnf-grammar \"bnf-int-new\"")).contains("<move>"));
        	
       		
        }
        catch (Exception ex) {
        	e = ex;        	
        }
        assertNull(e);
        
        try {
        	workspace.command("jge:load-bnf \"/bnf/BNFInt2.txt\" \"bnf-int\"");
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertTrue(e.getMessage().contains("The system cannot find the path specified"));        
        e = null;
        
        try {
        	workspace.command("jge:load-bnf \"" + AllTests.BNFInteger + ".txt\" \"bnf-int\"");
        }
        catch (Exception ex) {
        	e = ex;
        }
        assertNotNull(e);
        assertTrue(e.getMessage().contains("The system cannot find the file specified"));
        e = null;       
      
        
        // Dispose Workspace
        try {workspace.dispose();}
        catch(Exception ex) {ex.printStackTrace();} 		
		
	}

}
