package mn.mobicom.httpmethodconverter.worker;

import mn.mobicom.httpmethodconverter.config.ConfigController;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
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
import mn.mobicom.cmn.logger.Log;

/**
 *
 * @author uitumen.t
 */
@Stateless
public class Worker {

    public void requestSender(MultivaluedMap<String, String> queryParams) throws Exception {
        List<String> ruleIds = queryParams.get((RequestEnums.ruleId).toString());
        Map<String, String> requestParams = prepareParameters(queryParams);
        ruleIdChecker(ruleIds);
        String strDate = getDate();
        String jsonString = ConfigController.getInstance().getString((ConfigEnums.BODY + requestParams.get((RequestEnums.ruleId)
                .toString()))).replace("$date", strDate);

        for (Map.Entry<String, String> param : requestParams.entrySet()) {
            String key = "$" + param.getKey();
            if (key.equals("$isdn")) {
                String isdn = trimIsdn(param.getValue());
                jsonString = jsonString.replace(key, isdn);
            } else {
                jsonString = jsonString.replace(key, param.getValue());
            }
        }

        if (jsonString.contains("$")) {
            Log.create(Messages.queryParamErr).add("result", "FAILED").error();
            throw new Exception();
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
                Log.create(Messages.configMethodErr).add("result", "FAILED").error();
                throw new Exception();
        }
    }

    private void ruleIdChecker(List<String> ruleIds) throws Exception {
        if (ruleIds == null || ruleIds.size() > 1 || ruleIds.isEmpty()) {
            throw new Exception();
        }
        if (ConfigController.getInstance().getString(ConfigEnums.RULEID + ruleIds.get(0)).isEmpty()) {
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
            if (url == null || content == null || content.isEmpty() || url.isEmpty()) {
                Log.create(Messages.urlOrContentNullErr).add("result", "FAILED").error();
            }
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
            Log.create("Sending Post request")
                    .add("vendor-url", url)
                    .add("sending-data", content)
                    .add("response-code", reponseCode).info();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();

            Log.create("Response").add("body", response.toString()).info();
        } catch (IOException e) {
            Log.create(Messages.postMethodSendErr)
                    .add("vendor-url", url)
                    .add("sending-data", content)
                    .add("result", "FAILED").error();
            throw new Exception();
        }
    }

    private String trimIsdn(String isdn) {
        isdn = isdn.trim();
        if (isdn.length() > 8 && isdn.substring(0, 3).equals("976")) {
            isdn = isdn.substring(3, isdn.length());
        }
        return "976" + isdn;
    }

    private String getDate() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        String strDate = dateFormat.format(date);
        return strDate;
    }
}
