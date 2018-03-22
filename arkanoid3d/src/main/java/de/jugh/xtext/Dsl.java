package de.jugh.xtext;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.xtext.parser.IParseResult;

import de.jugh.arkanoidDsl.GameContentProvider;

/**
 * An USM DSL Fragment and its generated artifacts (ast, source).
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class Dsl
{
	/**
	 * Raw USM DSL.
	 */
	private final String dsl;

	/**
	 * Name of the dsl source file.
	 */
	private final String dslFileName;

	/**
	 * AST build upon the give usmDsl. Required to build the source.
	 */
	private IParseResult ast;

	/**
	 * For every given usm dsl multiple sources can be generated.
	 */
	private Set<GeneratedSource> sources = new HashSet<>();

	/**
	 * Constructor.
	 * 
	 * @param usmDsl an usm dsl.
	 */
	public Dsl(String usmDsl, String usmDslFileName) {
		super();
		this.dsl = usmDsl;
		this.dslFileName = usmDslFileName;
	}

	void addSource(GeneratedSource source)
	{
		sources.add(source);
	}

	public String getDslFileName()
	{
		return dslFileName;
	}

	String getDsl()
	{
		return dsl;
	}

	IParseResult getAst()
	{
		return ast;
	}

	Set<GeneratedSource> getGeneratedSource()
	{
		return sources;
	}

	/**
	 * Package protected, only used by the framework.
	 * 
	 * @param ast the ast.
	 */
	void setAst(IParseResult ast)
	{
		this.ast = ast;
	}

	public GameContentProvider getGameContentProvider()
	{
		return (GameContentProvider) ast.getRootASTElement();
	}
}
