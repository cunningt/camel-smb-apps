package com.fuse;

import com.hierynomus.smbj.share.File;
import java.io.InputStream;
import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import com.hierynomus.smbj.SmbConfig;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple Camel route that reads files from /Volumes/src/test/write/bar
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class MySpringBootRouter extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(MySpringBootRouter.class);


    private void process(Exchange exchange) throws IOException {
        final File file = exchange.getMessage().getBody(File.class);
        try (InputStream inputStream = file.getInputStream()) {

            LOG.debug("Read exchange: {}, with contents: {}", file.getPath(),
                    new String(inputStream.readAllBytes()));
        }
    }

    @Override
    public void configure() {
        from("file:/Users/tcunning/test?noop=true")
            .log("${body}")
            .to("smb:{{camel.smb.host}}:{{camel.smb.port}}/src?userName=RAW({{camel.smb.username}})&password=RAW({{camel.smb.password}})&path=test/write/bar&fileExist=Append&autoCreate=true");
    }

}
