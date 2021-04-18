package com.galbern.req.constants;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;

@UtilityClass
public class RmsConstants {
    public static final String APP_NAME = "RMS";
    public static final String URL = "www.galbern.com";
    public static final String DEFAULT_EMAIL_DISPATCHER_SERVICE ="GCW_MAIL";
    @Value("aws.jdbc.url")
    public String jdbcUrl="jdbc:postgresql://ec2-35-171-57-132.compute-1.amazonaws.com:5432/d9n3s3inbo9qpn?user=ndniyttbkfzvad&password=ed5e4589b8ea410ae56eef4327cd508f4242014340d9e70d46ac3540b6b45153";
}
