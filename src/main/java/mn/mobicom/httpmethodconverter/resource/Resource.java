package mn.mobicom.httpmethodconverter.resource;

import mn.mobicom.httpmethodconverter.config.ConfigController;
import mn.mobicom.httpmethodconverter.worker.Worker;
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
import mn.mobicom.httpmethodconverter.ex.ConverterException;
import mn.mobicom.httpmethodconverter.ex.ResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

/**
 *
 * @author uitumen.t
 */
@Path("api")
public class Resource {

    @EJB
    Worker work;

    @Context
    private HttpServletRequest httpServletRequest;

    final String uuid = UUID.randomUUID().toString().replace("-", "");

    @Path("admin/config/reload/file")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response configFileReload(@Context UriInfo ui) throws Exception {
        return Response.ok(ConfigController.getInstance().reload()).build();
    }

    @Path("send/request")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResponseDto getMethodRequest(@Context UriInfo ui) throws ConverterException {
        ThreadContext.put("requestid", uuid);
        ThreadContext.put("ipaddress", httpServletRequest.getLocalAddr());
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
        ResponseDto response = work.requestSender(queryParams); //Request ilgeeh
        return response;
    }
}
