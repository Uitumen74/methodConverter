package mn.mobicom.httpmethodconverter;

import mn.mobicom.httpmethodconverter.config.ConfigController;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

/**
 *
 * @author uitumen.t
 */
@WebListener
@ApplicationPath("rest")
public class RestActivator extends Application implements ServletContextListener {

    static private final String APP_NAME = "httpMethodConverter";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println(APP_NAME + ".Context initialized");
        initLogger();

        ConfigController.getInstance().reload();
    }

    private boolean initLogger() {
        String loggerConfigPath = System.getProperty("httpmethodconverter.logger.path");
        System.out.println(String.format("%s.Logger configuration: file=%s", APP_NAME, loggerConfigPath));

        try {
            Configurator.initialize(null, loggerConfigPath);

            System.out.println(APP_NAME + ".Logger initialized.");

            return true;
        } catch (Exception e) {
            System.out.println(APP_NAME + ".Logger initialization failed.");
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        LogManager.getLogger(RestActivator.class.getCanonicalName()).info("App stopped: " + APP_NAME);
        System.out.println(APP_NAME + ".Context destroyed");
    }

}
