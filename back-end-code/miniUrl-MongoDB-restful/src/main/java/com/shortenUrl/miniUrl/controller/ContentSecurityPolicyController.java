package com.shortenUrl.miniUrl.controller;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@RestController
@Slf4j
public class ContentSecurityPolicyController {
    // private static final Logger logger = LoggerFactory.getLogger(ContentSecurityPolicyController.class);

    @PostMapping("/report")
    public void report(HttpServletRequest request) throws IOException 
    {


        System.out.println("ContentSecurityPolicyController.report()..");
        if (log.isInfoEnabled()) 
        {

            String msg = IOUtils.toString(request.getInputStream());
            log.info("Report: {}", msg);
        }
    }
}