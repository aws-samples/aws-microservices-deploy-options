package org.aws.samples.compute.webapp;

import io.undertow.Undertow;
import io.undertow.util.Headers;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class Main {
    public static void main(String[] args) throws Exception {
        Undertow server = Undertow.builder()
                .addHttpListener(
                        Integer.valueOf(System.getProperty("swarm.http.port")),
                        System.getProperty("swarm.http.host"))
                .setHandler(exchange -> {
                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.TEXT_PLAIN);
                    String greetingEndpoint = getEndpoint("GREETING");
                    String nameEndpoint = getEndpoint("NAME");

                    Client client = ClientBuilder.newClient();
                    System.out.println("Calling greeting: " + greetingEndpoint);
                    String greeting = client
                            .target(greetingEndpoint)
                            .request(MediaType.TEXT_PLAIN)
                            .get(String.class);

                    System.out.println("Calling name: " + nameEndpoint);
                    String name = client
                            .target(nameEndpoint)
                            .request()
                            .get(String.class);

                    exchange
                            .getResponseSender()
                            .send(greeting + " " + name);
                })
                .build();
        server.start();
    }

    private static String getEndpoint(String type) {
        return "http://"
                + System.getProperty(type + "_SERVICE_HOST")
                + ":"
                + System.getProperty(type + "_SERVICE_PORT")
                + System.getProperty(type + "_SERVICE_PATH");
    }
}
