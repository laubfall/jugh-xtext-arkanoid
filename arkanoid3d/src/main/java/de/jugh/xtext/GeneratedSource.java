package de.jugh.xtext;

/**
 * Represents source that was generated by the xtext generator out of an dsl.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
class GeneratedSource
{
	/**
	 * Some kind of filename e.g. MyClass.java.
	 */
	private final String sourceName;

	private final String source;

	private DslGeneratedClassFileObject dslClassFile;

	public GeneratedSource(final String sourceName, final String source) {
		this.source = source;
		this.sourceName = sourceName;
	}

	public String getSource()
	{
		return source;
	}

	public String getSourceName()
	{
		return sourceName;
	}

	public DslGeneratedClassFileObject getDslClassFile()
	{
		return dslClassFile;
	}

	public void setDslClassFile(DslGeneratedClassFileObject usmClassFile)
	{
		this.dslClassFile = usmClassFile;
	}

}