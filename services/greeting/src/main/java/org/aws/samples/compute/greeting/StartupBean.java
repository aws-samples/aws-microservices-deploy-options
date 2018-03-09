package org.aws.samples.compute.greeting;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.plugins.ECSPlugin;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

@Singleton
public class StartupBean {

    @PostConstruct
    public void setup() {
        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder
                .standard()
                .withPlugin(new EC2Plugin()).withPlugin(new ECSPlugin());

        AWSXRay.setGlobalRecorder(builder.build());
    }
}
