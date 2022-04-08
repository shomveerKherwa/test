package com.momentous.dynamicLinks.api;

import java.io.InputStream;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class AmazonS3Api {

    public void fileUpload(InputStream input) {
        String accessKey = "AKIAYLXPE2XUKOP66OOR";
        String secretKey = "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";
        BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
        
        AmazonS3 client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .withRegion(Regions.AP_SOUTH_1).build();
        
        String bucket = "testbucketnamesom";
        
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, LocalDateTime.now().toString(), input, null);
        
        client.putObject(putObjectRequest);
        
    }
}
