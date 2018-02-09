package org.aws.samples.compute.greeting;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Arun Gupta
 */
@Path("greeting")
@ApplicationScoped
public class GreetingEndpoint {

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public String get() {
        return "Hello";
    }
}
