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

import bangor.aiia.jge.bnf.BNFGrammar;
import bangor.aiia.jge.core.GEMapper;
import bangor.aiia.jge.population.Genotype;
import bangor.aiia.jge.population.InvalidPhenotypeException;


/**
 * The class <code>Phenotype</code> extends the 
 * <code>DefaultReporter</code>.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 19/03/08
 * @see 	ExtensionManager
 * @see 	BNFLoader
 * @see 	BNFPrinter
 * @see 	Binary
 * @since	jGE NetLogo 1.0
 */
public class Phenotype extends DefaultReporter {
	
	
	/**
	 * Returns Syntax which specifies the syntax that is acceptable for this reporter.
	 * Used by the compiler for type-checking. 
	 * 
	 * @return The Syntax for this reporter.
	 */
	public Syntax getSyntax() {       
        
        return Syntax.reporterSyntax(new int[] {Syntax.TYPE_STRING, Syntax.TYPE_STRING, Syntax.TYPE_NUMBER, Syntax.TYPE_NUMBER}, Syntax.TYPE_STRING);
                
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
    	String genome = args[0].getString();
    	String bnfref = args[1].getString();
        int codonSize = args[2].getIntValue();
        int maxWraps = args[3].getIntValue();
        
        // Check Arguments
        if (codonSize <= 0) throw new ExtensionException("Codonsize value must be greater than zero (0)");
        if (maxWraps < 0) throw new ExtensionException("MaxWraps value must be equal to or greater than zero (0)");
        
        // Get the BNF Grammar
		BNFGrammar bnf = BNFLoader.get(bnfref);
		if (bnf == null) throw new ExtensionException("There is no BNF Grammar in memory referenced by \"" + bnfref + "\"");
      
        // Create the GE Mapper 
        GEMapper mapper = new GEMapper(bnf, codonSize, maxWraps); 
        Genotype<String> genotype = new Genotype<String>(genome);
        bangor.aiia.jge.population.Phenotype<String> phenotype = null;
                
        // Create the Phenotype
        try {
        	phenotype = mapper.getPhenotype(genotype);
        	return phenotype.value();
        }
        catch(InvalidPhenotypeException ipe) {
            return "";
        }        
        
        
    }
	
	
}
