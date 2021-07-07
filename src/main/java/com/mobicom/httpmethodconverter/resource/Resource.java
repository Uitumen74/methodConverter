package com.mobicom.httpmethodconverter.resource;

import com.mobicom.httpmethodconverter.config.ConfigController;
import com.mobicom.httpmethodconverter.models.DataRechargeRequest;
import com.mobicom.httpmethodconverter.worker.ConfigEnums;
import com.mobicom.httpmethodconverter.worker.RequestEnums;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author uitumen.t
 */
@Path("api")
public class Resource {

    @Context
    private HttpServletRequest httpServletRequest;

    @Path("admin/config/reload/file")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response configFileReload(@Context UriInfo ui) throws Exception {
        return Response.ok(ConfigController.getInstance().reload()).build();
    }

    @Path("receive")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response receiveGET(@Context UriInfo ui) throws Exception {
        String responseString = "ok";

        try {
            Enumeration parameters = httpServletRequest.getParameterNames();
            while (parameters.hasMoreElements()) {
                Object pname = parameters.nextElement();
                System.out.println("name=" + pname + ", class=" + pname.getClass().getCanonicalName());
            }

        } catch (Exception e) {
            responseString = "Failed: " + e.getMessage();
        }

        return Response.ok(responseString).build();
    }

    @Path("recharge")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response recharge(@Context UriInfo ui) throws Exception {
//        Log.create("Test log")
//                .add("isdn", isdn)
//                .add("price", price)W
//                .add("bagts", bagts)
//                .info();
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
        List<String> ruleIds = queryParams.get((RequestEnums.ruleId).toString());

        //RULEID-g shalgaj bna
        if (ruleIds.size() > 1 || ruleIds.isEmpty()) {
            //todo log ruleId 1-ees olon bna, esvel hooson
            throw new Exception();
        }
        if (ConfigController.getInstance().getString(ConfigEnums.RULEID + ruleIds.get(0)).isEmpty()) {
            //todo log config dotor baihgu bna
            throw new Exception();
        }

//        reqParams = prepareParameters(queryParams);
        //        MultivaluedMap<String, String> pathParams = ui.getPathParameters();
        //        final String uuid = UUID.randomUUID().toString().replace("-", "");
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
//        String strDate = dateFormat.format(date);
        DataRechargeRequest req = new DataRechargeRequest();
//        req.setIsdn(isdn);
//        req.setPrice(price);
//        req.setRuleId(ruleId);
//        req.setBagts(bagts);
//        req.setData(data);

        Map<String, String> params = prepareParameters(queryParams);

        String jsonString = ConfigController.getInstance().getString((ConfigEnums.BODY).toString());

        for (Map.Entry<String, String> param : params.entrySet()) {
            String key = "$" + param.getKey();
            jsonString = jsonString.replace(key, param.getValue());
        }

//        JSONObject json = new JSONObject(jsonString);
        try {
//            DataSendRequest sendReq = new DataSendRequest(configController.getString("URL" + ruleId), configController.getString("METHOD" + ruleId), configController.getString("CONTENTTYPE" + ruleId));
//            new Worker(sendReq).goy(req);
            System.out.println("");
        } catch (Exception E) {
            //todo log
            throw new Exception();
        }
        return Response.ok().build();
    }

    private Map<String, String> prepareParameters(MultivaluedMap<String, String> queryParameters) {

        Map<String, String> parameters = new HashMap<String, String>();

        for (String str : queryParameters.keySet()) {
            parameters.put(str, queryParameters.getFirst(str));
        }
        return parameters;
    }
}
