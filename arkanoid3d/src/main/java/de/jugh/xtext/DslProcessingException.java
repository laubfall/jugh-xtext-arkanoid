package de.jugh.xtext;

/**
 * Exception to show that something went wrong during the dsl processing.
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class DslProcessingException extends RuntimeException
{
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1L;


  public DslProcessingException(String message)
  {
    super(message);
  }


  public DslProcessingException(String message, Throwable cause)
  {
    super(message, cause);
  }

}
