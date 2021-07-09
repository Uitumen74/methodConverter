package com.mobicom.httpmethodconverter.worker;

import com.mobicom.httpmethodconverter.config.ConfigController;
import com.mobicom.httpmethodconverter.models.DataSendRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import org.json.JSONObject;

/**
 *
 * @author uitumen.t
 */
@Stateless
public class Worker {

    private String ruleId;

    private DataSendRequest sendReq;
//
//    public Worker(DataSendRequest sendReq) {
//        this.sendReq = sendReq;
//    }

    public void ruleIdChecker(List<String> ruleIds) throws Exception {
        if (ruleIds.size() > 1 || ruleIds.isEmpty()) {
            throw new Exception();
        }
        if (ConfigController.getInstance().getString(ConfigEnums.RULEID + ruleIds.get(0)).isEmpty()) {
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

    public void requestSender(Map<String, String> requestParams) {
        try {
            trimIsdn(requestParams.get(RequestEnums.idsn));
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
            String strDate = dateFormat.format(date);

            String jsonString = ConfigController.getInstance().getString((ConfigEnums.BODY + requestParams.get((RequestEnums.ruleId).toString())));

            jsonString = jsonString.replace("$date", strDate);

            for (Map.Entry<String, String> param : requestParams.entrySet()) {
                String key = "$" + param.getKey();
                jsonString = jsonString.replace(key, param.getValue());
            }
            sendingPostRequest(jsonString);
//            System.out.println(n);
        } catch (Exception e) {
            System.out.println("Aldaa bol : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendingPostRequest(String content) {
        String url = "http://127.0.0.1:8080/httpMethodConverter/rest/api/recievereq";
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
//            LoggerUtil.getLogger().info("Sending Post request to URL : " + url);
//            LoggerUtil.getLogger().info("Post Data : " + content);
//            LoggerUtil.getLogger().info("Response Code : " + reponseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();

//            LoggerUtil.getLogger().info("Response : " + response.toString());
        } catch (Exception e) {
//            LoggerUtil.getLogger().error("Exception on sendingPostRequest : " + e);
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
