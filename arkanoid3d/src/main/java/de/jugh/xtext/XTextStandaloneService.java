package de.jugh.xtext;

import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.xtext.parser.IParseResult;

import com.google.inject.Injector;

import de.jugh.ArkanoidDslStandaloneSetup;

/**
 * 
 * Service that provides the whole xtext functionality required to create the
 * usm container and model classes out of an usm dsl.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class XTextStandaloneService {
	/**
	 * Key for local-settings.properties. Value is the path to the folder that
	 * stores the generated sources.
	 */
	private static final String USMDSL_DEV_SRC_PATH = "usmdsl.dev.src.path";

	/**
	 * Singleton instance.
	 */
	private static XTextStandaloneService INSTANCE;

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

	public static final XTextStandaloneService getInstance() {
		if (INSTANCE != null) {
			return INSTANCE;
		}

		init();
		return INSTANCE;
	}

	/**
	 * Reloads every usm dsl resource, creates the java artifacts and compiles them.
	 * 
	 * @param ctx
	 *            Process context.
	 */
	public void reloadUsmDsl(DslProcessCtx ctx) {
		StopWatch sw = new StopWatch();
		sw.start();

		final Parser parser = new Parser();
		ctx.getDsls().forEach(usmDsl -> {
			final IParseResult ast = parser.parse(usmDsl);
			usmDsl.setAst(ast);

			if (ast.hasSyntaxErrors()) {
				ast.getSyntaxErrors().forEach(errorNode -> {
					// GLog.error(CommonsLogCategory.UsmDsl,
					// "USM Node in error: " + errorNode.getSyntaxErrorMessage().getMessage());
				});
				throw new DslProcessingException("Usm with validation errors");
			}
		});

		// For solupharm demo purposes the code generation part is deactivated.
		
//		sw.split();
//		// GLog.note(CommonsLogCategory.UsmDsl, "Usm DSL parsing done in " +
//		// sw.getSplitTime());
//
//		// after the parsing was successful its time to generate the source
//		final Generator generator = new Generator();
//		try {
//			generator.generate(ctx);
//		} catch (IOException e) {
//			// GLog.error(CommonsLogCategory.UsmDsl, "Failed to generate usm artifacts", new
//			// LogExceptionAttribute(e));
//			throw new UsmDslProcessingException("Generating artifacts failed");
//		}
//
//		sw.split();
//		// GLog.note(CommonsLogCategory.UsmDsl, "Usm DSL source generation done in " +
//		// sw.getSplitTime());
//
//		// the generated sources are attached to the UsmDsl object instances. Now
//		// compile them.
//		final Compiler compiler = new Compiler();
//		compiler.compile(ctx);
//
//		sw.split();
//		// GLog.note(CommonsLogCategory.UsmDsl, "Usm DSL compile done in " +
//		// sw.getSplitTime());
//
//		// persist the sources that were generated. Can be used for debugging purposes.
//		storeSourceIfDevMode(ctx);
//
//		usmDslClassloader = new UsmDslClassLoader(getClass().getClassLoader(), ctx);
	}

	/**
	 * Load an usm dsl class.
	 * 
	 * @param className
	 *            class name to load (FQN)
	 * @return see description.
	 */
	public <T> Class<T> loadUsmDslClass(final String className) {
		try {
			return (Class<T>) usmDslClassloader.loadClass(className);
		} catch (ClassNotFoundException e) {
			// throw new LoggedRuntimeException(LogLevel.Error, CommonsLogCategory.UsmDsl,
			// "Not able to load usm dsl class",
			// new LogExceptionAttribute(e));
			throw new RuntimeException("unable to load dsl class", e);
		}
	}

	private void storeSourceIfDevMode(DslProcessCtx ctx) {
		// if (StaticDaoManager.get().isDEV() == false) {
		// return;
		// }

		// if (LocalSettings.get().containsKey(USMDSL_DEV_SRC_PATH) == false) {
		// GLog.note(CommonsLogCategory.UsmDsl,
		// "No usm src path defined, no sources are produced. Use key: " +
		// USMDSL_DEV_SRC_PATH);
		// return;
		// }

		// final String srcPath = LocalSettings.get().get(USMDSL_DEV_SRC_PATH);
		//
		// ctx.getUsmDsls().forEach(usms -> {
		// usms.getGeneratedSource().forEach(gs -> {
		// try {
		// FileUtils.write(new File(srcPath, gs.getSourceName()), gs.getSource());
		// GLog.note(CommonsLogCategory.UsmDsl, "Wrote usm source for dev mode: " +
		// gs.getSourceName());
		// } catch (IOException e) {
		// GLog.error(CommonsLogCategory.UsmDsl, "Error while writing usm source for dev
		// mode",
		// new LogExceptionAttribute(e));
		// }
		// });
		// });
	}

	private static synchronized void init() {
		INSTANCE = new XTextStandaloneService();
	}

	/**
	 * Factory method that produce instances of classes that the guice di knows.
	 * 
	 * @param clazz
	 *            class to initiate.
	 * @return see description.
	 */
	public <T> T instance(Class<T> clazz) {
		return guiceInjector.getInstance(clazz);
	}

	
}
