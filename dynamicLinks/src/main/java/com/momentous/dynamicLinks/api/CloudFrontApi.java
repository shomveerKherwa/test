package com.momentous.dynamicLinks.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Security;
import java.time.Instant;
import java.util.Date;
import org.jets3t.service.CloudFrontService;
import org.jets3t.service.CloudFrontServiceException;
import org.jets3t.service.utils.ServiceUtils;
import org.joda.time.LocalDateTime;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


@Service
public class CloudFrontApi {

    public String generateCloudFrontUrl (String s3Url) {
        addBouncyCastleSecurityProvider();
        String privateKeyFileName = "PrivateKey.der";
        String keyPairId = "APKAYLXPE2XUAR3LV6JW";
        String privateDistributionDomain = "d1j900xgsite7t.cloudfront.net";
        Resource resourceLocation = new ClassPathResource("/" + privateKeyFileName);
        byte[] privateKeyFileBytes = null;
        try {
            // privateKeyFileBytes = ServiceUtils.readInputStreamToBytes(
            //        new EncodedResource(resourceLocation).getInputStream());
            privateKeyFileBytes = ServiceUtils.readInputStreamToBytes(new
                    FileInputStream("C:\\Users\\T460\\GitRepos\\test\\dynamicLinks\\src\\main\\resources\\PrivateKey.der"));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String cloudFrontUrl = null;
        String privateUrl = String.format("%s://%s/%s", "https", privateDistributionDomain, s3Url);
        try {
            
            cloudFrontUrl = CloudFrontService.signUrlCanned(privateUrl, keyPairId, privateKeyFileBytes,LocalDateTime.now().plusMonths(3).toDate());
            System.out.println("signUrlCanned "+cloudFrontUrl);
            String policy = CloudFrontService.buildPolicyForSignedUrl(privateUrl, LocalDateTime.now().toDate(), null , null);
            cloudFrontUrl = CloudFrontService.signUrl(privateUrl, keyPairId, privateKeyFileBytes, policy);
            System.out.println("signUrl "+cloudFrontUrl);
        } catch (CloudFrontServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cloudFrontUrl;
    }
    
    private void addBouncyCastleSecurityProvider() {
        org.bouncycastle.jce.provider.BouncyCastleProvider bcProvider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
        Security.getProviders(); // this line seems to be useless, however it was in the original code snippet
        String name = bcProvider.getName();
        Security.removeProvider( name ); // remove old instance
        Security.addProvider( bcProvider );
    }
}
