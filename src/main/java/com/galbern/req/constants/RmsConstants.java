package com.galbern.req.constants;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

@UtilityClass
public class RmsConstants {
    public static final String APP_NAME = "RMS";
    public static final String URL = "www.galbern.com";
    public static final String DEFAULT_EMAIL_DISPATCHER_SERVICE = "GCW_MAIL";
    @Value("aws.jdbc.url")
    public String jdbcUrl;
}
