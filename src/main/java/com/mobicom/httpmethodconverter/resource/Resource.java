package com.mobicom.httpmethodconverter.resource;

import com.mobicom.httpmethodconverter.config.ConfigController;
import com.mobicom.httpmethodconverter.worker.Worker;
import java.util.UUID;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

/**
 *
 * @author uitumen.t
 */
@Path("api")
public class Resource {

    private static final Logger LOG = LogManager.getLogger(Resource.class.getCanonicalName());

    @EJB
    Worker work;

    @Context
    private HttpServletRequest httpServletRequest;

    private String responseString = "ok";

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

//    @Path("receive")
//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response receiveGET(@Context UriInfo ui) throws Exception {
//
//        try {
//            Enumeration parameters = httpServletRequest.getParameterNames();
//            while (parameters.hasMoreElements()) {
//                Object pname = parameters.nextElement();
//                System.out.println("name=" + pname + ", class=" + pname.getClass().getCanonicalName());
//            }
//
//        } catch (Exception e) {
//            responseString = "Failed: " + e.getMessage();
//        }
//
//        return Response.ok(responseString).build();
//    }
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getMethodRequest(@Context UriInfo ui) {
        try {
            final String uuid = UUID.randomUUID().toString().replace("-", "");
            ThreadContext.put("requestid", uuid);
            ThreadContext.put("ipaddress", httpServletRequest.getLocalAddr());
            MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
            //Request ilgeeh
            work.requestSender(queryParams);
        } catch (Exception e) {
            LOG.error("Execution failed", e);
            responseString = "Failed: " + e.getMessage();
        }
        return Response.ok(responseString).build();
    }
}
