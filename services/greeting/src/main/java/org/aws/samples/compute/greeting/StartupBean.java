package org.aws.samples.compute.greeting;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.plugins.ECSPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartupBean {

    private static final Logger logger = LoggerFactory.getLogger(StartupBean.class);
    private static StartupBean thisInstance;

//    private StartupBean() {
//        logger.info("entry");
//        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder
//                .standard()
//                .withPlugin(new EC2Plugin()).withPlugin(new ECSPlugin());
//
//        AWSXRay.setGlobalRecorder(builder.build());
//        logger.info("exit");
//    }
//
//    public static final StartupBean getInstance() {
//        if (null == thisInstance) {
//            thisInstance = new StartupBean();
//        }
//
//        return thisInstance;
//    }
}
