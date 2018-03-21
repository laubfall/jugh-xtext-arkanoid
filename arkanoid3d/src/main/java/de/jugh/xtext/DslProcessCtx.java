package de.jugh.xtext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains all the information required by the process that parses dsls to executable artifacts.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class DslProcessCtx
{
  /**
   * All the usm dsls.
   */
  private final Set<Dsl> dsls;

  /**
   * Classloader that provides the classe required to compile the usm dsl fragments. Primarly used by {@link Compiler}.
   */
  private ClassLoader compileCL = getClass().getClassLoader();
  
  public DslProcessCtx(final Set<Dsl> usmDsls)
  {
    this.dsls = usmDsls;
  }
  
  public DslProcessCtx(final Set<Dsl> usmDsls, ClassLoader compileCL)
  {
    this(usmDsls);
    this.compileCL = compileCL;
  }

  /**
   * Factory method, an easy way to create the context object.
   * @param dsls dsl written by someone
   * @return see description.
   */
  public static final DslProcessCtx create(Dsl... dsls)
  {
    final HashSet<Dsl> hashSet = new HashSet<>();
    Arrays.stream(dsls).forEach(hashSet::add);
    return new DslProcessCtx(hashSet);
  }

  public Set<Dsl> getDsls()
  {
    return dsls;
  }

  public ClassLoader getCompileCL()
  {
    return compileCL;
  }

  public void setCompileCL(ClassLoader compileCL)
  {
    this.compileCL = compileCL;
  }
}
