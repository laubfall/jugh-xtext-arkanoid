package de.jugh.xtext;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

/**
 * This file manager transfers the generated classes to the {@link DslGeneratedClassFileObject}.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class DslCompilerFileManager implements JavaFileManager
{
	private JavaFileManager stdFileManager;

	public DslCompilerFileManager(JavaFileManager stdFileManager) {
		super();
		this.stdFileManager = stdFileManager;
	}

	@Override
	public int isSupportedOption(String option)
	{
		return stdFileManager.isSupportedOption(option);
	}

	@Override
	public ClassLoader getClassLoader(Location location)
	{
		return stdFileManager.getClassLoader(location);
	}

	@Override
	public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse)
			throws IOException
	{
		return stdFileManager.list(location, packageName, kinds, recurse);
	}

	@Override
	public String inferBinaryName(Location location, JavaFileObject file)
	{
		return stdFileManager.inferBinaryName(location, file);
	}

	@Override
	public boolean isSameFile(FileObject a, FileObject b)
	{
		return stdFileManager.isSameFile(a, b);
	}

	@Override
	public boolean handleOption(String current, Iterator<String> remaining)
	{
		return stdFileManager.handleOption(current, remaining);
	}

	@Override
	public boolean hasLocation(Location location)
	{
		return stdFileManager.hasLocation(location);
	}

	@Override
	public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) throws IOException
	{
		return stdFileManager.getJavaFileForInput(location, className, kind);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling)
			throws IOException
	{
		final DslGeneratedJavaFileObject ujfo = (DslGeneratedJavaFileObject) sibling;
		final DslGeneratedClassFileObject usmClassFileObject = new DslGeneratedClassFileObject(sibling.toUri());
		ujfo.getGeneratorResult().setDslClassFile(usmClassFileObject);
		return usmClassFileObject;
	}

	@Override
	public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException
	{
		return getFileForInput(location, packageName, relativeName);
	}

	@Override
	public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling)
			throws IOException
	{
		return stdFileManager.getFileForOutput(location, packageName, relativeName, sibling);
	}

	@Override
	public void flush() throws IOException
	{
		stdFileManager.flush();
	}

	@Override
	public void close() throws IOException
	{
		stdFileManager.close();
	}

}
