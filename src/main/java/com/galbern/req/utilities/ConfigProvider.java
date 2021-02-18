package com.galbern.req.utilities;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

@UtilityClass
public class ConfigProvider {

    @Value("$(rms.retryable.delay.milliseconds)")
    private int delay;

    @Value("$(rms.retryable.backoff)")
    private int backoff;
    
}
