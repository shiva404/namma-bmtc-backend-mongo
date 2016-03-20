package com.intuit;

import com.intuit.provider.JacksonJsonProvider;
import com.intuit.resource.LocationResource;
import com.intuit.resource.TestResource;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.RequestContextFilter;

public class MyApplication  extends ResourceConfig {
    public static final Logger LOGGER = LoggerFactory.getLogger(MyApplication.class);
    public MyApplication(){
        register(RequestContextFilter.class);
        register(LocationResource.class);
        register(TestResource.class);
        register(JacksonJsonProvider.class);
        register(new LoggingFilter());
    }
}
