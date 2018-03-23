package de.jugh.xtext;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.eclipse.xtext.parser.IParseResult;

import com.google.inject.Injector;

import de.jugh.ArkanoidDslStandaloneSetup;
import de.jugh.GameProperties;

/**
 * 
 * Service that provides the whole xtext functionality required to create the usm container and model classes out of an
 * usm dsl.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class XTextStandaloneService
{
	/**
	 * Key for local-settings.properties. Value is the path to the folder that stores the generated sources.
	 */
	private static final String USMDSL_DEV_SRC_PATH = "usmdsl.dev.src.path";

	/**
	 * Singleton instance.
	 */
	private static XTextStandaloneService INSTANCE;

	private static Logger LOG = Logger.getLogger(XTextStandaloneService.class);

	/**
	 * DSL Setup class.
	 */
	private ArkanoidDslStandaloneSetup usmStandaloneSetup = new ArkanoidDslStandaloneSetup();

	/**
	 * Guice DI.
	 */
	private Injector guiceInjector;

	/**
	 * The classloader with the acutal usm dsl classes.
	 */
	private DslClassLoader usmDslClassloader;

	/**
	 * Only accessible by the service manager or childs.
	 */
	protected XTextStandaloneService() {
		guiceInjector = usmStandaloneSetup.createInjectorAndDoEMFRegistration();
	}

	public static final XTextStandaloneService getInstance()
	{
		if (INSTANCE != null) {
			return INSTANCE;
		}

		init();
		return INSTANCE;
	}

	/**
	 * Reloads every usm dsl resource, creates the java artifacts and compiles them.
	 * 
	 * @param ctx Process context.
	 */
	public void reloadUsmDsl(DslProcessCtx ctx)
	{
		StopWatch sw = new StopWatch();
		sw.start();

		final Parser parser = new Parser();
		ctx.getDsls().forEach(usmDsl -> {
			final IParseResult ast = parser.parse(usmDsl);
			usmDsl.setAst(ast);

			if (ast.hasSyntaxErrors()) {
				ast.getSyntaxErrors().forEach(errorNode -> {
					LOG.warn("dsl with validation errors: " + errorNode.getSyntaxErrorMessage().getMessage());
				});
				throw new DslProcessingException("Dsl with validation errors");
			}
		});

		// after the parsing was successful its time to generate the source
		final Generator generator = new Generator();
		try {
			generator.generate(ctx);
		} catch (IOException e) {
			LOG.error("Unable to validate dsl and / or generate code", e);
			return;
		}

		 // the generated sources are attached to the UsmDsl object instances. Now
		 // compile them.
		final Compiler compiler = new Compiler();
		compiler.compile(ctx);

		// // persist the sources that were generated. Can be used for debugging purposes.
		storeSource(ctx);

		usmDslClassloader = new DslClassLoader(getClass().getClassLoader(), ctx);
	}

	/**
	 * Load an usm dsl class.
	 * 
	 * @param className class name to load (FQN)
	 * @return see description.
	 */
	public <T> Class<T> loadUsmDslClass(final String className)
	{
		try {
			return (Class<T>) usmDslClassloader.loadClass(className);
		} catch (ClassNotFoundException e) {
			// throw new LoggedRuntimeException(LogLevel.Error, CommonsLogCategory.UsmDsl,
			// "Not able to load usm dsl class",
			// new LogExceptionAttribute(e));
			throw new RuntimeException("unable to load dsl class", e);
		}
	}

	private void storeSource(DslProcessCtx ctx)
	{
		String genPath = GameProperties.get().getProperty("game.dsl.genpath");
		if (StringUtils.isBlank(genPath)) {
			return;
		}

		ctx.getDsls().forEach(usms -> {
			usms.getGeneratedSource().forEach(gs -> {
				try {
					FileUtils.write(new File(genPath, gs.getSourceName()), gs.getSource(), "UTF-8");
				} catch (IOException e) {
					LOG.error("failed to write generated sources", e);
				}
			});
		});
	}

	private static synchronized void init()
	{
		INSTANCE = new XTextStandaloneService();
	}

	/**
	 * Factory method that produce instances of classes that the guice di knows.
	 * 
	 * @param clazz class to initiate.
	 * @return see description.
	 */
	public <T> T instance(Class<T> clazz)
	{
		return guiceInjector.getInstance(clazz);
	}

}
