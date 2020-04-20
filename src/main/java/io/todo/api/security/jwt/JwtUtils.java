package io.todo.api.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {


    /**
     * Util method to generate a JWT token
     * @param username
     * @return
     */
    public static String generateToken(String username, JwtBean jwtBean) {
        Date iat = new Date();
        Date exp = new Date(System.currentTimeMillis() + jwtBean.getExpirationTimeInMillis());

        return Jwts.builder()
                    .setSubject(username)
                    .setIssuer(jwtBean.getIssuer())
                    .setIssuedAt(iat)
                    .setExpiration(exp)
                    .signWith(SignatureAlgorithm.HS512, jwtBean.getKey())
                    .compact();
    }
}
