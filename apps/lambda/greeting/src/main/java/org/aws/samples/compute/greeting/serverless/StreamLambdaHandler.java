package org.aws.samples.compute.greeting.serverless;

import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.jersey.JerseyLambdaContainerHandler;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;


public class StreamLambdaHandler implements RequestStreamHandler {
    private static final ResourceConfig jerseyApplication = new ResourceConfig().packages("org.aws.samples.compute.greeting");
    private static final JerseyLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler
            = JerseyLambdaContainerHandler.getAwsProxyHandler(jerseyApplication);


    public void handleRequest(java.io.InputStream inputStream, java.io.OutputStream outputStream, Context context)
            throws IOException {
        AwsProxyRequest request = LambdaContainerHandler.getObjectMapper().readValue(inputStream, AwsProxyRequest.class);

        AwsProxyResponse resp = handler.proxy(request, context);

        LambdaContainerHandler.getObjectMapper().writeValue(outputStream, resp);

        // just in case it wasn't closed by the mapper
        outputStream.close();
    }
}
