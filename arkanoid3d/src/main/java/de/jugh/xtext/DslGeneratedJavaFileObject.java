package de.jugh.xtext;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * Used by the java compiler. Represents a Java Source defined by an {@link Dsl} instance.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
class DslGeneratedJavaFileObject extends SimpleJavaFileObject
{
  private final GeneratedSource generatorResult;

  public DslGeneratedJavaFileObject(GeneratedSource generatorResult)
  {
    super(URI.create(generatorResult.getSourceName()), Kind.SOURCE);
    this.generatorResult = generatorResult;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException
  {
    return generatorResult.getSource();
  }

  public GeneratedSource getGeneratorResult()
  {
    return generatorResult;
  }
}
