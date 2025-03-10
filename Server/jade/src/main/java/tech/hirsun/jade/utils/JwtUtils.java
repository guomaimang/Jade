package tech.hirsun.jade.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    // the key used to sign the JWT, must >= 256 bits
    @Setter
    private static String signKey = "NuwhbujHwsvJpwq2peJGkw23ejTmhqoqh2tydkei9izheoo9";
    @Setter
    private static Long expirePeriod = 86400000L; //

    /**
     * Generate a JWT token
     * @param claims What is stored in the JWT part 2 load payload
     * @return JWT token
     */
    public static String createJwt(Map<String, Object> claims) {
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expirePeriod))
                .signWith(SignatureAlgorithm.HS256, signKey)
                .compact();
        return jwt;
    }

    /**
     * Parse the JWT token
     * @param jwt JWT token
     * @return The payload of the JWT token
     */
    public static Map<String, Object> parseJwt(String jwt) {
        Map<String, Object> claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }


    public static void main(String[] args) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", null);
        claims.put("email", "sesdj@sdd");
        String jwt = JwtUtils.createJwt(claims);
        System.out.println(jwt);
        Map<String, Object> claim = parseJwt(jwt);
        System.out.println(claim.get("id"));
    }
}

