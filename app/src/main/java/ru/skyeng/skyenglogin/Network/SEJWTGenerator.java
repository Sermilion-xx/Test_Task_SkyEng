package ru.skyeng.skyenglogin.Network;

import android.util.Base64;
import android.util.Pair;

import java.security.Key;
import java.util.HashMap;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import ru.skyeng.skyenglogin.Network.Interfaces.JWTGenerator;

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

    static final String KEY_EMAIL = "email";
    static final String KEY_PASSWORD = "password";

    @Override
    public String generate(String subject, String email, String password) {
        Key key = MacProvider.generateKey();
        HashMap<String, Object> claimMap = new HashMap<>();
        claimMap.put(KEY_EMAIL, email);
        claimMap.put(KEY_PASSWORD, password);

        return Jwts.builder()
                .setSubject(subject).setClaims(claimMap)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    @Override
    public String decodeToken(String token) {
        String[] parts = token.split(".");
        return decode(parts[1]);

    }

    private String decode(String part){
        return new String(Base64.decode(part, Base64.DEFAULT));
    }

}
