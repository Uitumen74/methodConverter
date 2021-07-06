package com.mobicom.httpmethodconverter.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 *
 * @author uitumen.t
 */
@Singleton
@Startup
@Path("config")
public class ConfigController {

    private static final Logger LOG = LogManager.getLogger(ConfigController.class.getCanonicalName());

    private Properties properties;

    @PostConstruct
    public void setup() {
        this.reload();
    }

    @PreDestroy
    public void clear() {
        if (properties != null) {
            properties.clear();
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String reload() {
        String appConfig = System.getProperty("httpmethodconverter.config.path");
        String logConfig = System.getProperty("httpmethodconverter.logger.path");
//        String logConfig = System.getProperty("nice.config.path") + "/application/nice-sample/nice-sample-log4j2.xml";

        Configurator.initialize(null, logConfig);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(appConfig);
            properties = new Properties();
            properties.load(inputStream);
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

        LOG.info("Config reloaded: {}", appConfig);
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

    public String getString(String name) throws Exception {
        try {
            return properties.getProperty(name);
        } catch (Exception E) {
            //todo log config dotor bhgu bn
            throw new Exception();
        }
    }
}
