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

import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.PrimitiveManager;

/**
 * The class <code>Extension Manager</code> extends the 
 * <code>DefaultClassManager</code> and tells NetLogo which primitives 
 * are part of the jGE NetLogo extension.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 19/03/08
 * @since	jGE NetLogo 1.0
 */
public class ExtensionManager extends DefaultClassManager {

	
	/**
	 * Loads the primitives in the jGE NetLogo extension. 
	 * This method is called once per model compilation.
	 * 
	 * @param primitiveManager The manager to transport the primitives to NetLogo.
	 */
    public void load(PrimitiveManager primitiveManager) {
        
        primitiveManager.addPrimitive("load-bnf", new BNFLoader());
        primitiveManager.addPrimitive("bnf-grammar", new BNFPrinter());
        primitiveManager.addPrimitive("phenotype", new Phenotype());
        primitiveManager.addPrimitive("crossover", new Crossover());
        primitiveManager.addPrimitive("crossover-fixed-length", new CrossoverFixedLength());
        primitiveManager.addPrimitive("mutation", new Mutation());
        primitiveManager.addPrimitive("individual", new Individual());
        primitiveManager.addPrimitive("population", new Population());
        primitiveManager.addPrimitive("version", new Version());
        primitiveManager.addPrimitive("is-binary?", new Binary());
        
    }   
    
    
}
