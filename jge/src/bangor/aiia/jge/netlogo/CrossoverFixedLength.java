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

import org.nlogo.api.*;


/**
 * The class <code>Crossover-Fixed-Length</code> extends the 
 * <code>DefaultReporter</code>.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 19/07/08
 * @see 	ExtensionManager
 * @see 	Crossover
 * @see 	Mutation
 * @see 	Binary
 * @since	jGE NetLogo 2.0
 */
public class CrossoverFixedLength extends DefaultReporter {
	
	
	/**
	 * Returns Syntax which specifies the syntax that is acceptable for this reporter.
	 * Used by the compiler for type-checking. 
	 * 
	 * @return The Syntax for this reporter.
	 */
	public Syntax getSyntax() {       
        
        return Syntax.reporterSyntax(new int[] {Syntax.TYPE_STRING, Syntax.TYPE_STRING, Syntax.TYPE_NUMBER}, Syntax.TYPE_LIST);
                
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
        
        // Get the passed arguments using NetLogo type-safe helper methods	               
        StringBuilder offspringA = new StringBuilder(args[0].getString());
        StringBuilder offspringB = new StringBuilder(args[1].getString());
        double prop = args[2].getDoubleValue();        
        
        // Check Arguments
        if (offspringA.length() == 0) throw new ExtensionException("ParentA must be a non-empty binary string");
        if (offspringB.length() == 0) throw new ExtensionException("ParentB must be a non-empty binary string");
        if (offspringA.length() != offspringB.length()) throw new ExtensionException("ParentA and ParentB must have the same length");
        
        // Perform the crossover operation
        bangor.aiia.jge.evolution.Crossover.standardOnePoint(offspringA, offspringB, prop);
        
        // Create a NetLogo List object to hold the results
        LogoList list = new LogoList();
        list.add(0, offspringA.toString());
        list.add(1, offspringB.toString());            

        return list;	
        
    }
	
	
}
