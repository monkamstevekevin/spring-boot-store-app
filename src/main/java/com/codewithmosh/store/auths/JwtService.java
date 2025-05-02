package com.codewithmosh.store.auths;

import com.codewithmosh.store.auths.Jwt;
import com.codewithmosh.store.auths.JwtConfig;
import com.codewithmosh.store.users.User;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
  private  final JwtConfig jwtConfig;

    public com.codewithmosh.store.auths.Jwt generateAccessToken(User user) {
    // 5 minutes
        return generateToken(user, jwtConfig.getAccessTokenExpiration() );
    }
    public com.codewithmosh.store.auths.Jwt generateRefreshToken(User user) {
       // 7 days
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    private com.codewithmosh.store.auths.Jwt generateToken(User user, long tokenExpirationTime) {
        var claims =  Jwts.claims()
                .subject(user.getId().toString())
                .add("role", user.getRole().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpirationTime*1000))
                .build();

return new com.codewithmosh.store.auths.Jwt(claims, jwtConfig.getSecretKey());

    }




    private Claims getClaims(String token) {
        return  Jwts.parser()
                  .verifyWith(jwtConfig.getSecretKey())
                  .build()
                  .parseSignedClaims(token)
                  .getPayload();
    }

    public com.codewithmosh.store.auths.Jwt parse(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException e) {
            return null;
        }
    }
}
