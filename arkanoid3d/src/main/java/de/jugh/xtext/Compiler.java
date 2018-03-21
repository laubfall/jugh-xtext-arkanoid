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

/**
 * Compiles the generated artifacts.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
class Compiler
{
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

    // TODO it is probably a good approach to create this filemanager once. Creating seems to be expensive.
//    final GenomeBCLJavaFileManager genomeBCLJavaFileManager = new GenomeBCLJavaFileManager(ctx.getCompileCL(), fm);
    final List<DslGeneratedJavaFileObject> compilationUnits = new ArrayList<>();

    // add the sources to compile
    for (Dsl usmDsl : ctx.getDsls()) {
      for (GeneratedSource generatedSource : usmDsl.getGeneratedSource()) {
        compilationUnits.add(new DslGeneratedJavaFileObject(generatedSource));
      }
    }

    final StringWriter sw = new StringWriter(); // TODO check if this writer is necessary.
    final CompilationTask task = compiler.getTask(sw, fm, diagnostics, null, null,
        compilationUnits);
    Boolean call = task.call();
    if (Boolean.FALSE.equals(call)) {

      StringWriter diagnosticWriter = new StringWriter();
      for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
        diagnosticWriter.append(diagnostic.toString()).append("\n");
      }

//      GLog.error(CommonsLogCategory.UsmDsl, "Failed to compile usm dsl",
//          new LogAttribute(GenomeAttributeType.Miscellaneous, diagnosticWriter.getBuffer().toString()));
      throw new DslProcessingException("Error during usm dsl compilation phase");
    }
    
//    GLog.note(CommonsLogCategory.UsmDsl, "Finished usm dsl artifact creation");
  }
}
