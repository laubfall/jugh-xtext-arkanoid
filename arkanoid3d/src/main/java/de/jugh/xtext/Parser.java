package de.jugh.xtext;

import org.eclipse.xtext.parser.IParseResult;

import de.jugh.parser.antlr.ArkanoidDslParser;

/**
 * Parses USM DSL Fragments and creates the ast.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
class Parser
{

  public IParseResult parse(Dsl dsl)
  {
    final XTextStandaloneService standaloneService = XTextStandaloneService.getInstance();

    final ArkanoidDslParser parser = standaloneService.instance(ArkanoidDslParser.class);
    final IParseResult parseResult = parser.doParse(dsl.getDsl());
    return parseResult;
  }
}
