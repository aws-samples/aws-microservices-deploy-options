package org.aws.samples.compute.greeting;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorder;
import com.amazonaws.xray.entities.Segment;

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
//        AWSXRayRecorder xrayRecorder = AWSXRay.getGlobalRecorder();
//        Segment segment = xrayRecorder.beginSegment("greeting");
//        segment.putAnnotation("parentId", xrayRecorder.getTraceEntity().getId());
        String response = "Hello";
//        xrayRecorder.endSegment();

        return response;
    }
}
