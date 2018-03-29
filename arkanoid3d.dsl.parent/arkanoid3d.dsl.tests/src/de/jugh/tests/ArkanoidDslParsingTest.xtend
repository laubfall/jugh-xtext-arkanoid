/*
 * generated by Xtext 2.12.0
 */
package de.jugh.tests

import com.google.inject.Inject
import de.jugh.arkanoidDsl.GameContentProvider
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.testing.util.ParseHelper
import org.eclipse.xtext.testing.validation.ValidationTestHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(XtextRunner)
@InjectWith(ArkanoidDslInjectorProvider)
class ArkanoidDslParsingTest {
	@Inject
	ParseHelper<GameContentProvider> parseHelper
	
	@Inject
	extension ValidationTestHelper
	
	@Test
	def void loadModel() {
		val result = parseHelper.parse('''
			import java.awt.Color;
			;
			
			B {
			fs
				val x = java.awt.Color.RED;
				fsdf
				val i = 0+1;
			}
		''')
		
		result.assertNoErrors
		Assert.assertNotNull(result)
		Assert.assertTrue(result.eResource.errors.isEmpty)
	}
}
