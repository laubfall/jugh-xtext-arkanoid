package de.jugh.tests

import com.google.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner
import org.eclipse.xtext.xbase.testing.CompilationTestHelper
import org.junit.runner.RunWith
import org.junit.Test

@RunWith(XtextRunner)
@InjectWith(ArkanoidDslInjectorProvider)
class ArkanoidDslGeneratorTest {
	@Inject extension CompilationTestHelper
	
	@Test
	def void simple() {
		'''
			RowCreator test {
				val l = new java.util.ArrayList();
								l.add(B {RED})
								return l;
			}
		'''.compile([
			println(singleGeneratedCode)
		])
	}
}