package com.mobicom.httpmethodconverter.worker;

import com.mobicom.httpmethodconverter.config.ConfigController;
import com.mobicom.httpmethodconverter.models.DataRechargeRequest;
import com.mobicom.httpmethodconverter.models.DataSendRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.ejb.EJB;

/**
 *
 * @author uitumen.t
 */
public class Worker {

    @EJB
    ConfigController configController;

    private final DataSendRequest sendReq;

    public Worker(DataSendRequest sendReq) {
        this.sendReq = sendReq;
    }

    public void goy(DataRechargeRequest req) throws Exception {
        executePost("", "");
    }

    public void httpSender(DataSendRequest sendReq) {
        try {
            URL url1 = new URL(sendReq.getUrl());
            HttpURLConnection con = (HttpURLConnection) url1.openConnection();

            //Request setup
            con.setRequestMethod(sendReq.getMethod());
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("Content-Type", sendReq.getContentType());

            int status = con.getResponseCode();

            DataOutputStream wr = new DataOutputStream(
                    con.getOutputStream());
            wr.writeBytes("");
            wr.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL("https://tumee.requestcatcher.com/");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response  
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
