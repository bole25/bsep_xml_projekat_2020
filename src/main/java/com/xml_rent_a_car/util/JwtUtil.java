package com.xml_rent_a_car.util;

import com.xml_rent_a_car.model.MyUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtUtil {
    private String SECRET_KEY = "aasdn1082jdasinmc981091j2";

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T>T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(MyUserDetails myUserDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", myUserDetails.getUser().getRole());
        return createToken(claims, myUserDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String user){
        return Jwts.builder().setClaims(claims).setSubject(user).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, MyUserDetails myUserDetails){
        String username = extractUsername(token);
        return (username.equals(myUserDetails.getUsername()) && !isTokenExpired(token));
    }

    public Collection<GrantedAuthority> getAuthorities(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        Collection authorities = Arrays.stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }
}
