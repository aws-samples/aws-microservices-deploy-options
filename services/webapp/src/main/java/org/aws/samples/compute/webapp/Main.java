package org.aws.samples.compute.webapp;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class Main {
    public static void main(String[] args) throws Exception {
        Undertow server = Undertow.builder()
                .addHttpListener(
                        Integer.valueOf(System.getProperty("swarm.http.port")),
                        System.getProperty("swarm.http.host"))
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        String greeter = "http://"
                                + System.getProperty("GREETING_SERVICE_HOST")
                                + ":"
                                + System.getProperty("GREETING_SERVICE_PORT")
                                + System.getProperty("GREETING_SERVICE_PATH");
                        String name = "http://"
                                + System.getProperty("NAME_SERVICE_HOST")
                                + ":"
                                + System.getProperty("NAME_SERVICE_PORT")
                                + System.getProperty("NAME_SERVICE_PATH");
                        exchange.getResponseSender().send(greeter + " " + name);
                    }
                })
                .build();
        server.start();
    }
}
