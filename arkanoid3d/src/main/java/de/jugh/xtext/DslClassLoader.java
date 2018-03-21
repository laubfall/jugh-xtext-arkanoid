package de.jugh.xtext;

import java.security.SecureClassLoader;

import org.apache.commons.lang3.StringUtils;

/**
 * Classloader to load classes that were created based on a dsl file.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
class DslClassLoader extends SecureClassLoader
{

  /**
   * Constructor.
   * 
   * @param parent the parent classloader.
   * @param ctx The context that was processed by {@link XTextStandaloneService#reloadUsmDsl(DslProcessCtx)}.
   *          Constructor checks the {@link GeneratedSource}s for existing bytecode and makes it accessible.
   */
  public DslClassLoader(ClassLoader parent, DslProcessCtx ctx)
  {
    super(parent);

    ctx.getDsls().forEach(usms -> {
      usms.getGeneratedSource().forEach(gs -> {
        String name = gs.getDslClassFile().getName();
        name = StringUtils.stripStart(name, "/");
        name = StringUtils.substringBefore(name, ".java");
        name = StringUtils.replace(name, "/", ".");
        defineClass(name,
            gs.getDslClassFile().byteCode(), 0, gs.getDslClassFile().byteCode().length);
      });
    });
  }

}
