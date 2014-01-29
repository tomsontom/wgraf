package com.trolltech.qt;

import com.trolltech.qt.internal.NativeLibraryManager;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public class Utilities
{
  public static final String VERSION_STRING;
  
  static
  {
    Properties props = new Properties();
    ClassLoader loader = Utilities.class.getClassLoader();
    if (loader == null) {
      throw new ExceptionInInitializerError("Could not get classloader!");
    }
    InputStream in = loader.getResourceAsStream("version.properties");
    if (in == null) {
      throw new ExceptionInInitializerError("version.properties not found!");
    }
    try
    {
      props.load(in);
    }
    catch (Exception ex)
    {
      throw new ExceptionInInitializerError("Cannot read properties!");
    }
    VERSION_STRING = props.getProperty("qtjambi.version");
    if (VERSION_STRING == null) {
      throw new ExceptionInInitializerError("qtjambi.version is not set!");
    }
  }
  
  public static enum OperatingSystem
  {
    Windows,  MacOSX,  Linux;
    
    private OperatingSystem() {}
  }
  
  public static enum Configuration
  {
    Release,  Debug;
    
    private Configuration() {}
  }
  
  public static OperatingSystem operatingSystem = decideOperatingSystem();
  public static Configuration configuration = decideConfiguration();
  @Deprecated
  public static boolean implicitLoading = false;
  public static String libSubPath = decideLibSubPath();
  @Deprecated
  public static boolean loadFromCache = matchProperty("com.trolltech.qt.load-from-cache", new String[] { "true" });
  
  public static boolean matchProperty(String name, String... substrings)
  {
    String value = System.getProperty(name);
    if (value == null) {
      return false;
    }
    if ((substrings == null) || (substrings.length == 0)) {
      return value != null;
    }
    for (String s : substrings) {
      if (value.contains(s)) {
        return true;
      }
    }
    return false;
  }
  
  public static void loadQtLibrary(String library)
  {
    loadQtLibrary(library, "4");
  }
  
  public static void loadQtLibrary(String library, String version)
  {
    NativeLibraryManager.loadQtLibrary(library, version);
  }
  
  public static void loadJambiLibrary(String library)
  {
    NativeLibraryManager.loadLibrary(library);
  }
  
  public static boolean loadLibrary(String lib)
  {
    try
    {
      NativeLibraryManager.loadLibrary(lib);
      return true;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public static File jambiTempDir()
  {
    return NativeLibraryManager.jambiTempDirBase("");
  }
  
  private static OperatingSystem decideOperatingSystem()
  {
    String osName = System.getProperty("os.name").toLowerCase();
    if (osName.startsWith("windows")) {
      return OperatingSystem.Windows;
    }
    if (osName.startsWith("mac os x")) {
      return OperatingSystem.MacOSX;
    }
    return OperatingSystem.Linux;
  }
  
  private static Configuration decideConfiguration()
  {
    if (System.getProperty("com.trolltech.qt.debug") != null) {
      return Configuration.Debug;
    }
    return Configuration.Release;
  }
  
  private static String decideLibSubPath()
  {
    return operatingSystem == OperatingSystem.Windows ? "bin" : "lib";
  }
  
  private static String stripLibraryName(String lib)
  {
    if (operatingSystem != OperatingSystem.Windows) {
      lib = lib.substring(3);
    }
    int dot = -1;
    switch (operatingSystem)
    {
    case Windows: 
      dot = lib.indexOf(".dll");
      break;
    case Linux: 
      dot = lib.indexOf(".so");
      break;
    case MacOSX: 
      dot = lib.indexOf(".");
    }
    return lib.substring(0, dot);
  }
  
  public static String unpackPlugins()
  {
    return null;
  }
  
  public static void loadSystemLibraries() {}
}
