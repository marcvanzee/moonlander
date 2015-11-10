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
 
package bangor.aiia.jge.netlogo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;

import org.nlogo.api.*;

import bangor.aiia.jge.bnf.BNFGrammar;


/**
 * The class <code>BNFLoader</code> extends the 
 * <code>DefaultCommand</code>.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 19/03/08
 * @see 	ExtensionManager
 * @see 	BNFPrinter
 * @since	jGE NetLogo 1.0
 */
public class BNFLoader extends DefaultCommand {
	
	private static Hashtable<String, BNFGrammar> BNFMap = new Hashtable<String, BNFGrammar>();
	
	/**
	 * Returns Syntax which specifies the syntax that is acceptable for this command. 
	 * Used by the compiler for type-checking.
	 * 
	 * @return The Syntax for this command.
	 */
	public Syntax getSyntax() {       
        
        return Syntax.commandSyntax(new int[] {Syntax.TYPE_STRING, Syntax.TYPE_STRING});
                
    }

	
	/**
	 * Executes this command. 
	 * Called by NetLogo when this command is called in a running NetLogo model.
	 * 
	 * @param args The Arguments that were passed to the command in the NetLogo code.
	 * @param context The current Context allows access to NetLogo internal methods.
	 * @throws ExtensionException if an extension-related problem occurs.
	 * @throws LogoException if one of the evaluated arguments throws a LogoException.
	 */	
	public void perform(Argument[] args, Context context) throws ExtensionException, LogoException {
        		
		// Get the passed arguments using NetLogo type-safe helper methods 
		String file = args[0].getString();   
		String ref = args[1].getString();
						
        File bnfFile = new File(file);
        
        // If the path is relative to the model then create the absolute file path
        if (!bnfFile.isAbsolute()) {        	
    		String basedir = "";
            try {
            	basedir = context.attachModelDir(".");
            }
            catch (MalformedURLException e) {
            	System.out.println("Exception: " + e.getMessage());
            }
        	String filesep = System.getProperty("file.separator");
            String myfile = basedir + filesep + file;        	
            bnfFile = new File(myfile);
        }      
        
        BNFGrammar bnf = null;
        
        // Create BNF Grammar
        try {             
            bnf = new BNFGrammar(bnfFile);
            // Check if the BNF Grammar is valid
            bnf.toString();
        }
        catch(IOException ioe) {
            throw new ExtensionException(ioe.getMessage());
        }

        // Store in memory the BNF Grammar
        BNFMap.put(ref, bnf);	
                
    }
	
	/**
	 * Returns the BNF Grammar of the given reference. 
	 * If not a BNF Grammar with the given reference is found 
	 * then the method returns <code>null</code>.
	 * 
	 * @param reference The name of the requested BNF Grammar.
	 * @return The BNF Grammar with the given reference if exists, otherwise null.
	 */	
	public static BNFGrammar get(String reference) {
	
		return BNFMap.get(reference);
				
	}
	
	
}
