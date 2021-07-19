package com.mobicom.httpmethodconverter.resource;

import com.mobicom.httpmethodconverter.config.ConfigController;
import com.mobicom.httpmethodconverter.worker.RequestEnums;
import com.mobicom.httpmethodconverter.worker.Worker;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
    
    @EJB
    Worker work;
    private String responseString = "ok";
    
    @Context
    private HttpServletRequest httpServletRequest;

//    @Path("test")
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response test() {
//        return Response.ok().build();
//    }
    @Path("recievereq")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response service(String json) {
        String result = "Record entered: " + json;
//        LoggerUtil.getLogger().info("recieved: ", json);
//        LoggerUtil.getLogger().info("response: ", result);
        // LOG.info(json);
        return Response.status(201).entity(result).build();
    }
    
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
    public Response recharge(@Context UriInfo ui) {
        try {
            
            MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
            List<String> ruleIds = queryParams.get((RequestEnums.ruleId).toString());
            Map<String, String> params = Worker.prepareParameters(queryParams);

            //RULEID-g shalgaj bna
            work.ruleIdChecker(ruleIds);
            
            work.requestSender(params);

            //        final String uuid = UUID.randomUUID().toString().replace("-", "");
        } catch (Exception e) {
            //todo log
            responseString = "Failed: " + e.getMessage();
        }
        //todo log
        return Response.ok(responseString).build();
    }
}
