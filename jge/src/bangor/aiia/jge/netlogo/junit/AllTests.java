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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The class <code>AllTests</code> is a JUnit Test Suite
 * which tests all the JUnit Test Cases of the jGE NetLogo extension.
 * 
 * @author 	Loukas Georgiou 
 * @version	1.0, 19/03/08
 * @since	jGE NetLogo 1.0
 */
public class AllTests {
	
	public static String NetLogoInstallationFolder = "C:/AIProjects/NetLogo";
	public static String EmpltyModelPath = "C:/AIProjects/NetLogo/extensions/EmptyModel.nlogo";
	public static String BNFInteger = "C:/AIProjects/NetLogo/extensions/jge/bnf/BNFInt.txt";
	public static String BNFNetLogo = "C:/AIProjects/NetLogo/extensions/jge/bnf/BNFNetLogoDemo.txt";
	

	public static Test suite() {
		
		TestSuite suite = new TestSuite("Test for jGE NetLogo extension");
		
		//$JUnit-BEGIN$		
		suite.addTestSuite(IndividualTest.class);
		suite.addTestSuite(BNFPrinterTest.class);
		suite.addTestSuite(PhenotypeTest.class);
		suite.addTestSuite(BNFLoaderTest.class);
		suite.addTestSuite(MutationTest.class);
		suite.addTestSuite(PopulationTest.class);
		suite.addTestSuite(CrossoverTest.class);
		suite.addTestSuite(CrossoverFixedLengthTest.class);
		suite.addTestSuite(VersionTest.class);
		suite.addTestSuite(BinaryTest.class);
		//$JUnit-END$
		
		return suite;
		
	}

}
