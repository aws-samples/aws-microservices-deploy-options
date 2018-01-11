package org.aws.samples.compute.greeting;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import static org.aws.samples.compute.greeting.MyApplication.APP_ROOT;

/**
 * @author arungupta
 */
@ApplicationPath(APP_ROOT)
public class MyApplication extends Application {

  public static final String APP_ROOT = "/resources";
    
}
