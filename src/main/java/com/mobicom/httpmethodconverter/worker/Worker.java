package com.mobicom.httpmethodconverter.worker;

import com.mobicom.httpmethodconverter.config.ConfigController;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author uitumen.t
 */
@Stateless
public class Worker {

    private static final Logger LOG = LogManager.getLogger(Worker.class.getCanonicalName());

    public void requestSender(MultivaluedMap<String, String> queryParams) throws Exception {
        List<String> ruleIds = queryParams.get((RequestEnums.ruleId).toString());
        Map<String, String> requestParams = prepareParameters(queryParams);
        try {
            ruleIdChecker(ruleIds);
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
            String strDate = dateFormat.format(date);
            String jsonString = ConfigController.getInstance().getString((ConfigEnums.BODY + requestParams.get((RequestEnums.ruleId).toString())));
            jsonString = jsonString.replace("$date", strDate);

            for (Map.Entry<String, String> param : requestParams.entrySet()) {
                String key = "$" + param.getKey();
                if (key.equals("$isdn")) {
                    String isdn = trimIsdn(param.getValue());
                    jsonString = jsonString.replace(key, isdn);
                } else {
                    jsonString = jsonString.replace(key, param.getValue());
                }
            }
            String method = ConfigController.getInstance().getString((ConfigEnums.METHOD + requestParams.get((RequestEnums.ruleId).toString())));
            String url = ConfigController.getInstance().getString((ConfigEnums.URL + requestParams.get((RequestEnums.ruleId).toString())));
            switch (method) {
                case "GET":
                    break;
                case "POST":
                    sendPostRequest(jsonString, url);
                    break;
                default:
                    LOG.error(Messages.configMethodErr);
                    throw new Exception();
            }
        } catch (Exception e) {
            LOG.error(Messages.sendRequestErr);
            throw new Exception();
        }
    }

    private void ruleIdChecker(List<String> ruleIds) throws Exception {
        if (ruleIds.size() > 1 || ruleIds.isEmpty()) {
            LOG.error(Messages.ruleIdSizeErr);
            throw new Exception();
        }
        if (ConfigController.getInstance().getString(ConfigEnums.RULEID + ruleIds.get(0)).isEmpty()) {
            LOG.error(Messages.ruleIdConfigErr);
            throw new Exception();
        }
    }

    static public Map<String, String> prepareParameters(MultivaluedMap<String, String> queryParameters) {

        Map<String, String> parameters = new HashMap<>();

        for (String str : queryParameters.keySet()) {
            parameters.put(str, queryParameters.getFirst(str));
        }
        return parameters;
    }

    private void sendPostRequest(String content, String url) throws Exception {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //Setting Basic Post request
            con.setRequestMethod("POST");
            con.setRequestProperty("Accept-Language", "application/json; utf-8");
            con.setRequestProperty("Content-Type", "application/json");

            //Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(content);
            wr.flush();
            wr.close();

            //Response
            int reponseCode = con.getResponseCode();
            LOG.info("Sending Post request to URL : " + url);
            LOG.info("Post Data : " + content);
            LOG.info("Response Code : " + reponseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();

            LOG.info("Response : " + response.toString());
        } catch (Exception e) {
            LOG.error(Messages.postMethodSendErr);
            throw new Exception();
        }
    }

    private String trimIsdn(String isdn) {
        isdn = isdn.trim();
        if (isdn.length() > 8 && isdn.substring(0, 3).equals("976")) {
            return isdn.substring(3, isdn.length());
        }
        return isdn;
    }
}
