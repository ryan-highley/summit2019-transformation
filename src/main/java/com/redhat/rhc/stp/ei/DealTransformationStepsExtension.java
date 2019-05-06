package com.redhat.rhc.stp.ei;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.syndesis.extension.api.annotations.ConfigurationProperty;
import io.syndesis.extension.api.annotations.Action;

@Action(
    id = "my-step",
    name = "My Logging Step",
    description = "A simple logging step"
)
public class DealTransformationStepsExtension {
    private static final Logger LOGGER = LoggerFactory.getLogger(DealTransformationStepsExtension.class);

    @ConfigurationProperty(
        name = "trace",
        displayName = "Trace",
        description = "Log the body as TRACE level, default INFO")
    private boolean trace;

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    public boolean isTrace() {
        return this.trace;
    }

    @Handler
    public void log(@Body Object body){
        if(trace) {
            LOGGER.trace("Body is: {}",body);
        } else {
            LOGGER.info("Body is: {}",body);
        }
    }
}
