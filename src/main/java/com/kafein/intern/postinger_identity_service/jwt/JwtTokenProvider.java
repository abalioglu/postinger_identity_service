package com.kafein.intern.postinger_identity_service.jwt;

import com.kafein.intern.postinger_identity_service.model.User;
import com.kafein.intern.postinger_identity_service.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    private final Set<String> tokenBlacklist = new HashSet<>();
    private final UserService userService;
    private String secretKey = "XOMmbTa4keacmII06k7toYAJDnRpcgl3+v89wqPci9y1TKbNmO76U7ONYDhCuYio+Q/g2IMAX2eY4MQ0g/I3aQ==";

    private long validityInMilliseconds = 3600000; //1 saat

    public JwtTokenProvider(UserService userService) {
        this.userService = userService;
    }

    public String generateToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Map<String, Object> claims = new HashMap<>();
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        claims.put("id", userService.getUserByUsername(username).getId());
        claims.put("authorities", userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername(), now, validity);
    }

    private String createToken(Map<String, Object> claims, String subject, Date issuedAt, Date expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    public String refreshToken(String token) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);
        final Claims claims = extractAllClaims(token);
        final String username = claims.getSubject();
        final Map<String, Object> refreshedClaims = new HashMap<>(claims);
        refreshedClaims.put(Claims.ISSUED_AT, now);
        refreshedClaims.put(Claims.EXPIRATION, validity);

        return Jwts.builder()
                .setClaims(refreshedClaims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenBlacklisted(token));
    }
    public void invalidateToken(HttpServletRequest request, HttpServletResponse response) {
        // Add the token to the blacklist to invalidate it.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT_TOKEN")) {
                    tokenBlacklist.add(cookie.getValue());
                    break;
                }
            }
        }
        // Remove the token cookie from the response headers.
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        // Expire the token cookie immediately
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    public boolean isTokenAboutToExpire(String token) {
        final Date expirationDate = extractExpirationDate(token);
        final Date now = new Date();
        final long timeToExpiration = expirationDate.getTime() - now.getTime();
        return timeToExpiration < 5 * 60 * 1000;
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    public Long extractIdClaim(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        Long id = claims.get("id", Long.class);
        return id;
    }
}
