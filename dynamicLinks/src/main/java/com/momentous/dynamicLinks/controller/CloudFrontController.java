package com.momentous.dynamicLinks.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.momentous.dynamicLinks.api.CloudFrontApi;

@Controller
public class CloudFrontController {

private Logger log = LoggerFactory.getLogger(CloudFrontController.class);
    
    @Autowired
    private CloudFrontApi cloudFrontApi;
    
    @GetMapping("/cloudhealth")
    public String healthCheck() {
        return "cloudhealth";
    }
    
    @PostMapping("/generatecdnUrl")
    public String generateCdnUrl(@RequestParam("url") String s3Url) {
        log.info("generateCdnUrl : {}", s3Url);
        String cloudFrontUrl = cloudFrontApi.generateCloudFrontUrl(s3Url);
        log.info("cloudFrontUrl "+cloudFrontUrl);
        return "cloudhealth";
    }
}
