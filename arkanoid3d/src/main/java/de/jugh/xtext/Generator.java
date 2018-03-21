package de.jugh.xtext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.generator.AbstractFileSystemAccess;
import org.eclipse.xtext.generator.GeneratorDelegate;
import org.eclipse.xtext.generator.IFilePostProcessor;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.StringInputStream;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

/**
 * Generates the code based on some dsl.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class Generator
{
  /**
   * Namespace of usm artifacts.
   */
  private static final String NS_ECORE = "Ark.ecore";

  /**
   * Generates the java source.
   * 
   * @param ast context that provides all the usm dsls.
   * @throws IOException thrown when loading the usm dsl failed.
   */
  public void generate(DslProcessCtx ast) throws IOException
  {
    final XtextResourceSet resourceSet = XTextStandaloneService.getInstance().instance(XtextResourceSet.class);

    final GeneratorDelegate generatorDelegate = XTextStandaloneService.getInstance().instance(GeneratorDelegate.class);

    final IResourceValidator validator = XTextStandaloneService.getInstance().instance(IResourceValidator.class);

    final InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();

    final Map<URI, Dsl> resUriToUsmDsl = new HashMap<>();

    // create the xtext resource, do the validation and cross reference resolution.
    for (Dsl ud : ast.getDsls()) {
      String key = NS_ECORE + ud.hashCode() + ".ark";
      final URI uri = URI.createFileURI(key);

      // The uri is required to resolve Artifacts in the usm namespace of the dsl.
      final XtextResource xtextResource = XTextStandaloneService.getInstance().instance(XtextResource.class);
      xtextResource.setURI(uri);
      final List<Issue> validate = validator.validate(xtextResource, CheckMode.ALL, CancelIndicator.NullImpl);
      xtextResource.load(new StringInputStream(ud.getDsl()), null);

      if (validate.isEmpty() == false) {
        throw new DslProcessingException("DSL validation failures occured");
      }

      resourceSet.getResources().add(xtextResource);
      resUriToUsmDsl.put(uri, ud);
    }

    // the code generation process
    new ArrayList<>(resourceSet.getResources()).forEach(resource -> {
      Dsl usmDsl = resUriToUsmDsl.get(resource.getURI());
      fsa.setPostProcessor(new FSGeneratorPostProcessor(usmDsl));
      generatorDelegate.doGenerate(resource, fsa);
    });

    // only logging
    fsa.getAllFiles().forEach((key, val) -> {
    });

  }

  class FSGeneratorPostProcessor implements IFilePostProcessor
  {
    private Dsl dsl;

    public FSGeneratorPostProcessor(Dsl dsl)
    {
      super();
      this.dsl = dsl;
    }

	@Override
	public CharSequence postProcess(org.eclipse.emf.common.util.URI fileURI, CharSequence content) {
		dsl.addSource(
		          new GeneratedSource(StringUtils.substringAfter(fileURI.toString(), AbstractFileSystemAccess.DEFAULT_OUTPUT),
		              content.toString()));
		      return content;
	}

  }
}
