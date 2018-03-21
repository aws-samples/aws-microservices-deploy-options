package org.aws.samples.compute.webapp;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/{id:([^/]+?)?}")
public class WebappController {

    private static final Logger logger = LoggerFactory.getLogger(WebappController.class);

    @Produces(MediaType.TEXT_PLAIN)
    @GET
    public String getMessage(@Context UriInfo uri, @PathParam("id") String id) {
        Subsegment subsegment = AWSXRay.beginSubsegment("get");

        String greetingEndpoint = getEndpoint("GREETING", uri.getRequestUri().getScheme(), null);
        logger.info("ID Query is: " + id);
        String pathQuery = (id.equals("")) ? null : ("/" + id);
        String nameEndpoint = getEndpoint("NAME", uri.getRequestUri().getScheme(), pathQuery);

        String greetingMessage = "";
        try {
            greetingMessage = Unirest
                    .get(greetingEndpoint)
                    .header("accept", "text/plain")
                    .asString()
                    .getBody();
            logger.info("Greeting is: " + greetingMessage);
        } catch (Exception e) {
            logger.error("Failed connecting Greeting API: " + e);
        }

        String nameMessage = "";
        try {
            nameMessage = Unirest
                    .get(nameEndpoint)
                    .header("accept", "text/plain")
                    .asString()
                    .getBody();
            logger.info("Name is: " + nameMessage);
        } catch (Exception e) {
            logger.error("Failed connecting Name API: " + e);
        }

        subsegment.end();
        return greetingMessage + " " + nameMessage;
    }

    private String getEndpoint(String type, String scheme, String pathQuery) {
        String host = System.getenv(type + "_SERVICE_HOST");
        if (null == host) {
            throw new RuntimeException(type + "_SERVICE_HOST environment variable not found");
        }

        String port = System.getenv(type + "_SERVICE_PORT");
        if (null == port) {
            throw new RuntimeException(type + "_SERVICE_PORT environment variable not found");
        }

        String path = System.getenv(type + "_SERVICE_PATH");
        if (null == path) {
            throw new RuntimeException(type + "_SERVICE_PATH environment variable not found");
        }
        if (null != pathQuery) {
            path = path + pathQuery;
        }

        /**
         * Note: Due to AWS Serverless Java Container assume all requests to API Gateway
         * are using HTTPS, so it hardcoded context URL to use "https". This assumption
         * doesn't work in SAM local. TODO: create an issue in AWS Serverless Java Container Github Repo
         */
        String schemeOverride = System.getenv(type + "_SERVICE_SCHEME");
        logger.info("scheme override is: " + schemeOverride);
        String endpoint;
        if (null == schemeOverride) {
            endpoint = scheme + "://" + host + ":" + port + path;
        } else {
            endpoint = schemeOverride + "://" + host + ":" + port + path;
        }

        logger.info(type + " endpoint: " + endpoint);
        return endpoint;
    }

}
