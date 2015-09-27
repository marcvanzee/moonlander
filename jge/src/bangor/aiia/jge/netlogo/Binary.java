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
  
package bangor.aiia.jge.netlogo;

import org.nlogo.api.*;


/**
 * The class <code>Binary</code> extends the 
 * <code>DefaultReporter</code>.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 29/03/08
 * @see 	ExtensionManager
 * @since	jGE NetLogo 2.0
 */
public class Binary extends DefaultReporter {
	
	
	/**
	 * Returns Syntax which specifies the syntax that is acceptable for this reporter.
	 * Used by the compiler for type-checking. 
	 * 
	 * @return The Syntax for this reporter.
	 */
	public Syntax getSyntax() {       
        
        return Syntax.reporterSyntax(new int[] {Syntax.TYPE_STRING}, Syntax.TYPE_BOOLEAN);
                
    }

	
	/**
	 * Executes this reporter. 
	 * Called by NetLogo when this reporter is called in a running NetLogo model.
	 * 
	 * @param args The Arguments that were included with the reporter in the NetLogo code.
	 * @param context The current Context allows access to NetLogo internal methods.
	 * @return The object to be reported.
	 * @throws ExtensionException if an extension-related problem occurs.
	 * @throws LogoException if one of the evaluated arguments throws a LogoException.
	 */	
	public Object report(Argument[] args, Context context) throws ExtensionException, LogoException {
	
		 // Get the passed argument using NetLogo type-safe helper methods	               
        String string = args[0].getString();
		
        if (string.length() == 0)
        	return false;
        
        for (int i = 0; i < string.length(); i++) {
        	if (string.charAt(i) != '0' && string.charAt(i) != '1')
        		return false;
        }
		
		return true;	
        
    }
	
	
}
