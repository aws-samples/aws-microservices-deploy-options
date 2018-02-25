package org.aws.samples.compute.greeting;


import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.internal.testutils.Timer;
import com.amazonaws.serverless.proxy.jersey.JerseyLambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreetingHandler implements RequestStreamHandler {
    private final ResourceConfig jerseyApplication = new ResourceConfig()
                                                             .packages("org.aws.samples.compute.greeting")
                                                             .register(JacksonFeature.class);
    private final JerseyLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler
            = JerseyLambdaContainerHandler.getAwsProxyHandler(jerseyApplication);

    private static final Logger logger = LoggerFactory.getLogger(GreetingHandler.class);

    public GreetingHandler() {
        // we enable the timer for debugging. This SHOULD NOT be enabled in production.
        Timer.enable();
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {

        AwsProxyRequest request = LambdaContainerHandler.getObjectMapper().readValue(inputStream, AwsProxyRequest.class);

        logger.info(request.getPath());

        String path = request.getPath().replaceAll("/resources/", "/");
        request.setPath(path);
        logger.info(request.getPath());
        AwsProxyResponse resp = handler.proxy(request, context);

        LambdaContainerHandler.getObjectMapper().writeValue(outputStream, resp);

        System.err.println(LambdaContainerHandler.getObjectMapper().writeValueAsString(Timer.getTimers()));

        // just in case it wasn't closed by the mapper
        outputStream.close();
    }
}
