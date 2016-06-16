package controller;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import strategies.Strategy;

public class StrategyLoaderFromPackage implements StrategyLoader {

   /* (non-Javadoc)
   * @see controller.StrategyLoader#getStrategyArray()
   */
  @Override
  public Strategy[] getStrategyArray() {
    Strategy[] result = getStrategiesFromPackage(Strategy.class.getPackage(),"strategies.Strategy");
    return result;
  }

  /**
   * Loads strategy by class name
  * @param strategyClassName strategy class name
  * @return Strategy class instance
  */
  private Strategy loadStrategy(String strategyClassName) {
    Strategy strategy = null;
    ClassLoader classLoader = RoundRobinTournamentController.class.getClassLoader();
    try {
      Class<?> studentClass = classLoader.loadClass("strategies." + strategyClassName);
      Object studentInstance = studentClass.newInstance();
      strategy = (Strategy) studentInstance;
      return strategy;
    } catch (ClassNotFoundException exception) {
      System.exit(1);
    } catch (InstantiationException exception) {
      exception.printStackTrace();
    } catch (IllegalAccessException exception) {
      exception.printStackTrace();
    }
    return strategy;
  }
  
  /**
  * Gets strategies array from package
  * @param pkg path to package with strategies
  * @return strategies array
  */
  public Strategy[] getStrategiesFromPackage(Package pkg, String path) {
    String pkgname = pkg.getName();

    List<String> classes = new ArrayList<>();

    // Get a File object for the package
    File directory = null;
    String relPath = pkgname.replace('.', '/');

    URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);

    if (resource == null) {
      throw new RuntimeException("No resource for " + relPath);
    }

    try {
      directory = new File(resource.toURI());
    } catch (URISyntaxException exception) {
      throw new RuntimeException(pkgname 
      +  " (" + resource + ") does not appear to be a valid URL / URI." 
      + "  Strange, since we got it from the system...", exception);
    } catch (IllegalArgumentException exception) {
      directory = null;
    }

    if (directory != null && directory.exists()) {
      // Get the list of the files contained in the package
      String[] files = directory.list();
      for (int i = 0; i < files.length; i++) {

        // we are only interested in .class files
        if (files[i].endsWith(".class")) {

          // removes the .class extension
          String className = pkgname + '.' + files[i].substring(0, files[i].length() - 6);

          try {
            Class<?> clas = Class.forName(className);
            if (Arrays.asList(clas.getInterfaces()).contains(Class.forName(path))) {
              classes.add(Class.forName(className).getSimpleName());
            }
          } catch (ClassNotFoundException exception) {
            throw new RuntimeException("ClassNotFoundException loading " + className);
          }
        }
      }
    }

    Strategy[] strategies = new Strategy[classes.size()];

    for (int i = 0;i < classes.size();i++) {
      strategies[i] = loadStrategy(classes.get(i));
    }
    return strategies;
  }

}
