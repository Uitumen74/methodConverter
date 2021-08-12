package mn.mobicom.httpmethodconverter.config;

import mn.mobicom.httpmethodconverter.worker.Messages;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import mn.mobicom.cmn.logger.Log;
import mn.mobicom.httpmethodconverter.ex.ConverterException;
import mn.mobicom.httpmethodconverter.ex.ErrorCode;

/**
 *
 * @author uitumen.t
 */
public class ConfigController {

    private static final ConfigController instance = new ConfigController();

    private Properties properties;
    private List<String> isdnList = null;

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
            setTestIsdn("TEST_MODE");
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

    public boolean checkTestIsdn() {
        return !(isdnList == null || isdnList.isEmpty());
    }

    public void setTestIsdn(String testMode) {
        String strMode = properties.getProperty(testMode);
        String strIsdn = properties.getProperty("TEST_ISDN");
        switch (strMode) {
            case "1":
                isdnList = Arrays.asList(strIsdn.split(";"));
                break;
            case "0":
                isdnList = null;
                break;
            default:
                isdnList = null;
                break;
        }

    }

    public List<String> getTestIsdn() {
        return isdnList;
    }

    public String getString(String name) throws ConverterException {
        String strName = properties.getProperty(name);
        if (strName == null) {
            Log.create(String.format(Messages.configParamErr, name)).add("result", "FAILED").error();
            throw new ConverterException(ErrorCode.FAILED, String.format(Messages.configParamErr, name));
        }
        return strName;
    }
}
