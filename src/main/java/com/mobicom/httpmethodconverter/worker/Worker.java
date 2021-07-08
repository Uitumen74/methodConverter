package com.mobicom.httpmethodconverter.worker;

import com.mobicom.httpmethodconverter.config.ConfigController;
import com.mobicom.httpmethodconverter.models.DataSendRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public void requestSender(Map<String, String> requestParams) throws Exception {
        try {
            String jsonString = ConfigController.getInstance().getString((ConfigEnums.BODY + requestParams.get((RequestEnums.ruleId).toString())));

            for (Map.Entry<String, String> param : requestParams.entrySet()) {
                String key = "$" + param.getKey();
                jsonString = jsonString.replace(key, param.getValue());
            }
            
//            {
//  "param1": "1",
//  "param2": "2",
//  "param3": "3"
//}
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject json = new JSONObject(jsonString);
            String n = executePost(jsonString);
            System.out.println(n);
        } catch (Exception e) {
            System.out.println("Aldaa bol : " + e.getMessage());
            e.printStackTrace();
        }
    }
//
//    private void httpSender(String json) {
//        try {
//            URL url = new URL("https://tumee.requestcatcher.com/");
//        } catch (Exception ex) {
//            System.out.println("ex aldaa : " + ex);
//        }
//    }

    private String executePost(String urlParameters) {
        try {
            //Create connection
            URL url = new URL("https://z-wallet.candy.mn:443/rest/dummy");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String jsonInputString = urlParameters;
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
