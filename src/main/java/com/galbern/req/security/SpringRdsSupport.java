package com.galbern.req.security;


import com.galbern.req.constants.RmsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
//import org.springframework.cloud.aws.jdbc.config.annotation.RdsInstanceConfigurer;
//import org.springframework.cloud.aws.jdbc.datasource.TomcatJdbcDataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
//@Lazy
@Component
//@EnableRdsInstance(
//        dbInstanceIdentifier = "ec2-35-171-57-132.compute-1.amazonaws.com",
//        password = "ed5e4589b8ea410ae56eef4327cd508f4242014340d9e70d46ac3540b6b45153")
public class SpringRdsSupport {
    private static Logger LOGGER = LoggerFactory.getLogger(SpringRdsSupport.class);

//    @Bean
//    public RdsInstanceConfigurer instanceConfigurer() {
//        return () -> {
//            TomcatJdbcDataSourceFactory dataSourceFactory
//                    = new TomcatJdbcDataSourceFactory();
//            dataSourceFactory.setInitialSize(10);
//            dataSourceFactory.setValidationQuery("SELECT 1");
//            return dataSourceFactory;
//        };
//    }

    static {
        System.setProperty("RDS_HOSTNAME","ec2-35-171-57-132.compute-1.amazonaws.com");
    }

    public Connection getRemoteConnection() throws SQLException {
        LOGGER.info("getRemoteConnection....");
        if (System.getProperty("RDS_HOSTNAME") != null) {
            try {
                Class.forName("org.postgresql.Driver");
                LOGGER.trace("Getting remote connection with connection string from environment variables.");
                Connection con = DriverManager.getConnection(RmsConstants.jdbcUrl);
                LOGGER.info("Remote connection successful.");
                return con;
            } catch (ClassNotFoundException e) {
                LOGGER.error("ClassNotFoundException creating the connection");
                LOGGER.warn(e.toString());
            } catch (SQLException e) {
                LOGGER.error("SQLException creating the connection");
                LOGGER.warn(e.toString());
                throw e;
            }
        }
        return null;
    }

}
