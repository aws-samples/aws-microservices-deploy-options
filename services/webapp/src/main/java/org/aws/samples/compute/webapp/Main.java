package org.aws.samples.compute.webapp;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class Main {
    public static void main(String[] args) throws Exception {
        Undertow server = Undertow.builder()
                .addHttpListener(
                        Integer.getInteger(System.getenv("port")),
                        System.getenv("host"))
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        String greeter = "http://"
                                + System.getenv("GREETER_SERVICE_HOST")
                                + ":"
                                + System.getenv("GREETER_SERVICE_PORT")
                                + "/"
                                + System.getenv("GREETER_SERVICE_PATH")
                                + "?greet="
                                + exchange.getQueryParameters().get("greet");
                        String name = "http://"
                                + System.getenv("NAME_SERVICE_HOST")
                                + ":"
                                + System.getenv("NAME_SERVICE_PORT")
                                + "/"
                                + System.getenv("NAME_SERVICE_PATH")
                                + "?id="
                                + exchange.getQueryParameters().get("id");
                        exchange.getResponseSender().send(greeter + " " + name);
                    }
                })
                .build();
        server.start();
    }
}
