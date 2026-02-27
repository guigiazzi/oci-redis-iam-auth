package com.example.ociredis;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class SignToken {
    public String readKeyFile(String path) throws IOException {
        String content = Files.readString(Paths.get(path));
        String key = content
            .replaceAll("-----(BEGIN|END)([^-]+)-----", "")
            .replaceAll("\\s+", "")
            .replaceAll("OCI_API_KEY", "");
        
        return key;
    }

    public PrivateKey loadPrivateKey(String path) throws Exception {
        String key = readKeyFile(path);
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public PublicKey loadPublicKey(String path) throws Exception {
        String key = readKeyFile(path);
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public String encodePublicKeyAsPem(String path) throws IOException {
        String pemContent = Files.readString(Paths.get(path));
        return Base64.getEncoder().encodeToString(pemContent.getBytes(StandardCharsets.UTF_8));
    }

}
