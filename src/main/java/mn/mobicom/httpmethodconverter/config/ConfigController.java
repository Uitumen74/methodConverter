package mn.mobicom.httpmethodconverter.config;

import mn.mobicom.httpmethodconverter.worker.Messages;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import mn.mobicom.cmn.logger.Log;

/**
 *
 * @author uitumen.t
 */
public class ConfigController {

    private static final ConfigController instance = new ConfigController();

    private Properties properties;

    public static ConfigController getInstance() {
        return instance;
    }

    public String reload() {
        String appConfig = System.getProperty("httpmethodconverter.config.path");

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(appConfig);
            properties = new Properties();
            properties.load(inputStream);
            Log.create("Config reloaded: {" + appConfig + "}").add("result", "SUCCESS").info();
        } catch (IOException ex) {
            Log.create("Config reload error: " + ex).add("result", "FAILED").error();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                Log.create("Config reload, inputStream close error").add("result", "FAILED").error();
            }
        }

        return appConfig;
    }

    public String getString(String name) {
        String strName = properties.getProperty(name);
        if (strName == null) {
            Log.create(Messages.configParamErr).add("result", "FAILED").error();
        }
        return strName;
    }
}
