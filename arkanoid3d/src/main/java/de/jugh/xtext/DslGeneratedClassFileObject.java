package de.jugh.xtext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * Representation of a compiled dsl fragment.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
class DslGeneratedClassFileObject extends SimpleJavaFileObject
{
  /**
   * Code store.
   */
  private ByteArrayOutputStream baos = new ByteArrayOutputStream();

  /**
   * Constructor.
   * @param uri uri of the created class file.
   */
  public DslGeneratedClassFileObject(URI uri)
  {
    super(uri, Kind.CLASS);
  }

  @Override
  public OutputStream openOutputStream() throws IOException
  {
    return baos;
  }

  /**
   * Returns the code.
   * 
   * @return see description.
   */
  public final byte[] byteCode()
  {
    return baos.toByteArray();
  }
}
