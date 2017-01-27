package ru.skyeng.skyenglogin.network.authorization;

import android.util.Base64;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.spec.SecretKeySpec;
//import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import ru.skyeng.skyenglogin.network.interfaces.JWTGenerator;

import static io.jsonwebtoken.Jwts.parser;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 25/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class SEJWTGenerator implements JWTGenerator {

    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    @Override
    public String generate(String subject, String email, String password) {
        Key key = MacProvider.generateKey();
        HashMap<String, Object> claimMap = new HashMap<>();
        claimMap.put(KEY_EMAIL, email);
        claimMap.put(KEY_PASSWORD, password);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] apiKeySecretBytes = Arrays.toString(Base64.decode("se_test_secret", Base64.DEFAULT)).getBytes(); // = DatatypeConverter.parseBase64Binary("se_test_secret");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setIssuedAt(now)
                .setSubject(subject)
                .setClaims(claimMap)
                .signWith(SignatureAlgorithm.HS512, key)
                .signWith(signatureAlgorithm, signingKey)
                .compact();
    }

    public Claims decodeToken(String jwt) {
        return parser().setSigningKey(Arrays.toString(Base64.decode("se_test_secret", Base64.DEFAULT)).getBytes())
                .parseClaimsJws(jwt).getBody();
    }
}
