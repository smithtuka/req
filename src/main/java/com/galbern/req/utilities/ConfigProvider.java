package com.galbern.req.utilities;

import lombok.Data;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@UtilityClass
@Data
@Component
public class ConfigProvider {

//    @Value("$(rms.retryable.delay.milliseconds:3)")
    private int maxAttempts;

//    @Value("$(rms.retryable.backoff:1000)")
    private int backoff;

    @Value("$(mail.dispatcher.service)")
    private String mailDispatcher;
    
}
