package org.aws.samples.compute.greeting;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.plugins.ECSPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

public class StartupBean {

    private static final Logger logger = LoggerFactory.getLogger(StartupBean.class);

    public StartupBean() {
        logger.info("StartupBean.setup.entry");
        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder
                .standard()
                .withPlugin(new EC2Plugin()).withPlugin(new ECSPlugin());

        AWSXRay.setGlobalRecorder(builder.build());
        logger.info("StartupBean.setup.exit");
    }
}
