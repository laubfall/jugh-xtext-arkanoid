package de.jugh.xtext;

/**
 * Service Manager. Provides extensibility to the xtext service.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class XTextStandaloneServiceManager
{
  /**
   * This manager.
   */
  private static XTextStandaloneServiceManager INSTANCE;

  /**
   * XTextStandaloneService.
   */
  private XTextStandaloneService xtextStandaloneService;
  
  static {
    INSTANCE = new XTextStandaloneServiceManager();
  }
  
  public static XTextStandaloneServiceManager get()
  {
    return INSTANCE;
  }

  public XTextStandaloneService getXtextStandaloneService()
  {
    if(xtextStandaloneService == null) {
      xtextStandaloneService = new XTextStandaloneService();
    }
    return xtextStandaloneService;
  }

  public void setXtextStandaloneService(XTextStandaloneService xtextStandaloneService)
  {
    this.xtextStandaloneService = xtextStandaloneService;
  }
}
