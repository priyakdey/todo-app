package io.todo.api.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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

    /**
     * Method is to verify is the email token is valid
     * @param token
     * @param verificationToken
     * @param jwtBean
     * @return
     */
    public static boolean validateToken(String token, String verificationToken, JwtBean jwtBean) {
        Objects.requireNonNull(token);
        if (!Objects.equals(token, verificationToken))
            return false;

        // If tokens are equal, it is a valid token
        // We check the expiration to check if the token is still valid in context of expiration
        Claims claims = Jwts.parser()
                            .setSigningKey(jwtBean.getKey())
                            .parseClaimsJws(token)
                            .getBody();

//        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.getDefault());

        Date expiration = claims.getExpiration();
        return expiration.after(new Date());
    }
}
