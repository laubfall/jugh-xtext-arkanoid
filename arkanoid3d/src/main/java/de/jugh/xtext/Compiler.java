package de.jugh.xtext;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.log4j.Logger;

/**
 * Compiles the generated artifacts.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
class Compiler
{
	private static Logger LOG = Logger.getLogger(Compiler.class);
	
	/**
	 * Compile every source that is attached to an {@link Dsl} instance.
	 * 
	 * @param ctx processing context. Mandatory.
	 */
	public final void compile(DslProcessCtx ctx)
	{

		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		final StandardJavaFileManager fm = compiler.getStandardFileManager(diagnostics, null, Charset.forName("UTF-8"));

		final DslCompilerFileManager dslCompilerFileManager = new DslCompilerFileManager(fm);
		final List<DslGeneratedJavaFileObject> compilationUnits = new ArrayList<>();

		// add the sources to compile
		for (Dsl usmDsl : ctx.getDsls()) {
			for (GeneratedSource generatedSource : usmDsl.getGeneratedSource()) {
				final DslGeneratedJavaFileObject dslGeneratedJavaFileObject = new DslGeneratedJavaFileObject(generatedSource);
				compilationUnits.add(dslGeneratedJavaFileObject);
			}
		}

		final StringWriter sw = new StringWriter();
		ArrayList<String> options = new ArrayList<>();
		options.add("-g");
		final CompilationTask task = compiler.getTask(sw, dslCompilerFileManager, diagnostics, options, null, compilationUnits);
		Boolean call = task.call();
		
		if (Boolean.FALSE.equals(call)) {
			StringWriter diagnosticWriter = new StringWriter();
			for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
				diagnosticWriter.append(diagnostic.toString()).append("\n");
			}

			LOG.error("Failed to compile dsl generated java code: " + diagnosticWriter.getBuffer().toString());
			
			throw new DslProcessingException("Error during usm dsl compilation phase");
		}

		LOG.info("Finished compiling dsl generated java code");
	}
}
