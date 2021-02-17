package com.galbern.req.sandbox;


import io.micrometer.core.instrument.util.StringEscapeUtils;

public class EscapeXml {
    public static void main(String[] args) {
        System.out.println(" xml " + StringEscapeUtils.escapeJson(""));
    }
}
