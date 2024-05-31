package com.example.patientskydashoard.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.nimbusds.jose.util.Base64URL;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.springframework.stereotype.Component;
@Component
public class DPoPJWTGenerator {

    

    public static String noPayloadGenerateDPoP(String privateKey, String publicKey, String url, String httpMethod) {
        return generateDPoP(privateKey, publicKey, url, httpMethod, null, null, null, null, null, null, null);
    }

    public static String generateDPoP(String privateKey, String publicKey, String url,
                                      String httpMethod, String requestBodyHash, List<String> requestHeaderKeyList,
                                      String requestHeaderHash, List<String> selectivePayloadKeyList, String selectivePayloadHash,
                                      List<String> formParamKeyList, String formParamHash) {

        String dpopToken = null;
        try {

            Security.addProvider(new BouncyCastleProvider());
            RSAPrivateKey rsaPrivateKey;
             privateKey="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";
             publicKey="MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAr2xTDvJGsdbMEOX89i+H bVhzfzLK1EzNZ0TPIqxH0rf9ELh0cBWCg9b19kXtmyCVlLccujzUVAzfG07H/voc ZWutcyTpc778O2j6jY6TrVeq00M/sVtOVwrg+tD4fC3hL5cIIUoLVpE98QR3jfPI i/bB+kIjcwEm09ZI+Sks5x/FIzMNKdu0hrVKbv8mEjqVRQuv+5C2hCzf5cgR8aJJ Xhy5lJoXjZwbOvJdnmG4CIzq0r9LFXGwA/rTZdxecF8JhaZdCuimQp/8l9/gfX96 nDUmqnrx33AWvQS5400fZYMwLn8gJtZEIlTEdWcNyNAdehz4Kw2S1RuMJr9fPVBm hbYDWre54kMIXrRu8eEOToX4Vvz2GVo/1JpvQVJE6vsey6jaaT0YbftN3dUtmZ01 evZKVLPyXfzRCibi1IHEUYMzAT42RsWNGdIP/aaNg93y8stjGFnU0KW9uct0w06A 2WkymCxsURuiGeOGHP/xllQboHNdQUvvPAIXS2/U4qUEPVrCYAFOiBtLegLDr27N Uo7txBj7pGHe59tyzGYf3PGc5GkoWlLwsTA8BlrC7XkQdf0RXZ7ZHXc2DR0qs6/x eTrYVlAC6z+06xksnArD0CxW9fdAXpVdGQmy/ajW2pzxD/5hi5ZZQTdtuX9VPdX/ h2bEMkhcixBzAM3qN1DRTyUCAwEAAQ==";
    	
            privateKey = privateKey.replaceAll("\\s+", "");
            publicKey = publicKey.replaceAll("\\s+", "");
            System.out.println(privateKey);
            byte[] decodedPv = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(decodedPv);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            rsaPrivateKey = (RSAPrivateKey) kf.generatePrivate(keySpecPv);

            RSAPublicKey rsaPublicKey;
           
            byte[] data = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            rsaPublicKey = (RSAPublicKey) kf.generatePublic(spec);


            Algorithm algorithm = Algorithm.RSA256(null, rsaPrivateKey);

            Map<String, Object> headerClaim = new HashMap<>();
            headerClaim.put("typ", "dpop+jwt");
            headerClaim.put("jwk", generateJWK(rsaPublicKey));

            JWTCreator.Builder builder = JWT.create();

            builder.withJWTId(UUID.randomUUID().toString())
                    .withHeader(headerClaim)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withClaim("htm", httpMethod)
                    .withClaim("htu", url);

            if (requestBodyHash != null)
                builder.withClaim("requestBodyHash", requestBodyHash);
            else if (selectivePayloadHash != null && selectivePayloadKeyList != null) {
                builder.withClaim("selectivePayloadHash", selectivePayloadHash);
                builder.withClaim("selectivePayloadKeyList", selectivePayloadKeyList);
            } else if (formParamHash != null && formParamKeyList != null) {
                builder.withClaim("formParamHash", formParamHash);
                builder.withClaim("formParamKeyList", formParamKeyList);
            }

            if (requestHeaderHash != null && requestHeaderKeyList != null) {
                builder.withClaim("requestHeaderKeyList", requestHeaderKeyList);
                builder.withClaim("requestHeaderHash", requestHeaderHash);
            }

            dpopToken = builder.sign(algorithm);

            System.out.println("token :: " + dpopToken);
            return dpopToken;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dpopToken;
    }

   

    private static Map<String, String> generateJWK(RSAPublicKey rsa) {


        Map<String, String> values = new HashMap<>();


        ////System.out.println(Base64URL.encode(rsa.getModulus())+" modulus");
        values.put("kty", rsa.getAlgorithm()); // getAlgorithm() returns kty not algorithm
        values.put("e", Base64.getUrlEncoder().encodeToString(rsa.getPublicExponent().toByteArray()));
        values.put("kid", UUID.randomUUID().toString());
        values.put("n", String.valueOf(Base64URL.encode(rsa.getModulus())));

        System.out.println(">>>>>>>>>>>>>>>>>>>>"+values);

        return values;
    }

}
