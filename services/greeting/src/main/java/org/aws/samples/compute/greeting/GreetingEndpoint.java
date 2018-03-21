package org.aws.samples.compute.greeting;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Arun Gupta
 */
@Path("greeting")
public class GreetingEndpoint {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {
        Subsegment subsegment = AWSXRay.beginSubsegment("get");
        String response = "Hello";
        AWSXRay.endSubsegment();

        return response;
    }
}
