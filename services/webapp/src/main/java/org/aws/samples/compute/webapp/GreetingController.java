package org.aws.samples.compute.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

@Path("/")
public class GreetingController {

    @Produces("text/plain")
    @GET
    public String greeting() {
        String greetingEndpoint = getEndpoint("GREETING");
        String nameEndpoint = getEndpoint("NAME");

        Client client = ClientBuilder.newClient();
        String greeting = client
                .target(greetingEndpoint)
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);

        String name = client
                .target(nameEndpoint)
                .request()
                .get(String.class);

        return greeting + " " + name;
    }

    private String getEndpoint(String type) {
        String endpoint = "http://"
                + System.getenv(type + "_SERVICE_HOST")
                + ":"
                + System.getenv(type + "_SERVICE_PORT")
                + System.getenv(type + "_SERVICE_PATH");

        System.out.println(type + " endpoint: " + endpoint);
        return endpoint;
    }

}
