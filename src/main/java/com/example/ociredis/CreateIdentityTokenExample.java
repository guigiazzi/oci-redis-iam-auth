package com.example.ociredis;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SessionTokenAuthenticationDetailsProvider;
import com.oracle.bmc.auth.okeworkloadidentity.OkeWorkloadIdentityAuthenticationDetailsProvider;
import com.oracle.bmc.redis.RedisIdentityClient;
import com.oracle.bmc.redis.model.*;
import com.oracle.bmc.redis.requests.*;
import com.oracle.bmc.redis.responses.*;
import java.nio.charset.StandardCharsets;
import java.lang.System;
import java.security.Signature;
import java.util.Base64;
import java.security.PrivateKey;
import java.security.PublicKey;

// Instance
import com.oracle.bmc.auth.InstancePrincipalsAuthenticationDetailsProvider;
import com.oracle.bmc.identity.IdentityClient;


public class CreateIdentityTokenExample {
    public static void main(String[] args) throws Exception {
        SignToken signToken = new SignToken();
        // String publicKeyPath = "C:\\Users\\luand\\.oci\\public.pem";
        String publicKeyPath = "public_key.pem";
        // String privateKeyPath = "C:\\Users\\luand\\.oci\\private.pem";
        String privateKeyPath = "private_key.pem";
        // String configFilePath = "C:\\Users\\luand\\.oci\\config";

        // String publicKeyPath = "/home/opc/public.pem";
        // String privateKeyPath = "/home/opc/private.pem";
        // String configFilePath = "/home/opc/config";
        
        PrivateKey privateKey = signToken.loadPrivateKey(privateKeyPath);
        PublicKey publicKey = signToken.loadPublicKey(publicKeyPath);
        // String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String base64PublicKey = signToken.encodePublicKeyAsPem(publicKeyPath);
        String redisUserId = "ocid1.ocicacheuser.oc1.sa-saopaulo-1.amaaaaaa6b4wwwia4jndyrlpviqpmrpa6ptrg437pnv3uy6fwhthhz5n74nq";
        String redisClusterId = "ocid1.rediscluster.oc1.sa-saopaulo-1.amaaaaaa6b4wwwiaompblvigkm2wsjslyzw3izrce5stshengkqgf7ofmyzq";

        // final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configFilePath, "DEFAULT");
        // final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
        
        // final OkeWorkloadIdentityAuthenticationDetailsProvider provider = new OkeWorkloadIdentityAuthenticationDetailsProvider
        //         .OkeWorkloadIdentityAuthenticationDetailsProviderBuilder()
        //         .build();

        InstancePrincipalsAuthenticationDetailsProvider provider = InstancePrincipalsAuthenticationDetailsProvider.builder().build();

        IdentityClient identityClient = new IdentityClient(provider);

        RedisIdentityClient client = RedisIdentityClient.builder()
            .build(provider);

        CreateIdentityTokenDetails createIdentityTokenDetails = CreateIdentityTokenDetails.builder()
            .publicKey(base64PublicKey)
            .redisUser(redisUserId)
            .build();
        
        CreateIdentityTokenRequest createIdentityTokenRequest = CreateIdentityTokenRequest.builder()
            .createIdentityTokenDetails(createIdentityTokenDetails)
            .redisClusterId(redisClusterId)
            .build();

        CreateIdentityTokenResponse response = client.createIdentityToken(createIdentityTokenRequest);
        String rawToken = response.getIdentityTokenDetailsResponse().getIdentityToken();

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey); // private key of the user
        signature.update(rawToken.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = signature.sign();
        String encodedSignature = Base64.getUrlEncoder().encodeToString(signatureBytes);

        signature.initVerify(publicKey); //public key of the user
        signature.update(rawToken.getBytes(StandardCharsets.UTF_8));
        boolean isValid = signature.verify(Base64.getUrlDecoder().decode(encodedSignature));
                
        final String composite = encodedSignature + "|" + rawToken;
        final String finalToken = Base64.getEncoder().encodeToString(composite.getBytes(StandardCharsets.UTF_8));

        System.out.println("Raw Token: " + rawToken);
        System.out.println("Composite Token: " + composite);
        System.out.println("Signed Token: " + finalToken);
        System.out.println("Signature valid: " + isValid);

        RedisClient redisClient = new RedisClient("aaa6b4wwwiaompblvigkm2wsjslyzw3izrce5stshengkqgf7ofmyzq-p.redis.sa-saopaulo-1.oci.oraclecloud.com", 6379, "user-test-giazzi-iam", finalToken);

        redisClient.set("mykey", "myvalue");
        System.out.println("Value for 'mykey': " + redisClient.get("mykey"));
    }
}
