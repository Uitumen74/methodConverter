package com.mobicom.httpmethodconverter.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author uitumen.t
 */
public class ConfigController {

    private static final Logger LOG = LogManager.getLogger(ConfigController.class.getCanonicalName());

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

            LOG.info("Config reloaded: {}", appConfig);
        } catch (IOException ex) {
            LOG.error("Config reload error", ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                LOG.error("Config reload, inputStream close error", ex);
            }
        }

        return appConfig;
    }

    public Date getDatetime(String name) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String value = properties.getProperty(name);
        return sdf.parse(value);
    }

    public int getInteger(String name, int _default) {
        String result = properties.getProperty(name, Integer.toString(_default));
        return Integer.parseInt(result);
    }

    public String getString(String name) {
        return properties.getProperty(name);
    }
}
