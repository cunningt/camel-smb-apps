package com.fuse;

import com.hierynomus.smbj.share.File;
import java.io.InputStream;
import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.hierynomus.smbj.SmbConfig;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class MySpringBootRouter extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(MySpringBootRouter.class);

    @Value("${camel.smb.username}")
    private String smbUsername;

    @Value("${camel.smb.password}")
    private String smbPassword;

    private void process(Exchange exchange) throws IOException {
        final File file = exchange.getMessage().getBody(File.class);
        try (InputStream inputStream = file.getInputStream()) {

            LOG.debug("Read exchange: {}, with contents: {}", file.getPath(),
                    new String(inputStream.readAllBytes()));
        }
    }

    @Override
    public void configure() {
        from("smb:192.168.1.20:445/src?username=RAW({{camel.smb.username}})&password=RAW({{camel.smb.password}})&path=test&recursive=false&disconnect=false")
            .to("stream:out");
    }

}
